package com.report.casio.timer;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WheelTimer {
    private static final int MAX_BUCKET_SIZE = 1024 * 1024;
    private static final Map<TimerTask, Integer> taskDurationMap = new ConcurrentHashMap<>(20);

    private final int size;
    // 槽与槽的间隔
    private final int tickDuration;
    // 时间单位（缺少周、月、年）
    private final TimeUnit timeUnit;
    private final WheelBucket[] wheelBuckets;
    private final AtomicInteger index = new AtomicInteger(0);

    private ScheduledFuture<?> scheduledFuture;

    public WheelTimer(int size, int tickDuration, TimeUnit timeUnit) {
        this.size = size;
        this.tickDuration = tickDuration;
        this.timeUnit = timeUnit;
        this.wheelBuckets = createWheel(size);
    }

    private WheelBucket[] createWheel(int tickDuration) {
        if (tickDuration < 0 || tickDuration > MAX_BUCKET_SIZE) {
            throw new IllegalArgumentException(
                    "ticksPerWheel must be greater than 0 and less than " + MAX_BUCKET_SIZE);
        }
        int normalSize = 1;
        while (normalSize < tickDuration) {
            normalSize = normalSize << 1;
        }
        WheelBucket[] result = new WheelBucket[normalSize];
        for (int i = 0; i < result.length; i++) {
            result[i] = new WheelBucket(this);
        }
        return result;
    }

    public void addTimeTask(TimerTask timerTask, int duration) {
        WheelBucket wheelBucket = this.wheelBuckets[duration % size];
        int round = duration / size;
        WheelTimeout timeout = new WheelTimeout(timerTask, round);
        wheelBucket.addTimeout(timeout);
        taskDurationMap.put(timerTask, duration);
    }

    public void start() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        Runnable runnable = () -> {
            int i = index.getAndIncrement();
            WheelBucket wheelBucket = wheelBuckets[i % size];
            wheelBucket.execute(i % size * tickDuration);
        };
        this.scheduledFuture = service.scheduleAtFixedRate(runnable, 0, tickDuration, timeUnit);
    }

    public void close() {
        if (this.scheduledFuture != null && !this.scheduledFuture.isDone()) {
            this.scheduledFuture.cancel(true);
        }
    }

    /**
     * 时间轮的槽
     */
    private static final class WheelBucket {
        private final ExecutorService service = Executors.newFixedThreadPool(10);
        private WheelTimeout head;
        private WheelTimeout tail;
        private final WheelTimer wheelTimer;

        public WheelBucket(WheelTimer wheelTimer) {
            this.wheelTimer = wheelTimer;
        }

        public void addTimeout(WheelTimeout timeout) {
            if (head == null) {
                head = tail = timeout;
            } else {
                tail.next = timeout;
                timeout.prev = tail;
                tail = timeout;
            }
        }

        public void remove(WheelTimeout timeout) {
            WheelTimeout next = timeout.next;
            if (timeout.prev != null) {
                timeout.prev.next = next;
            }
            if (next != null) {
                next.prev = timeout.prev;
            }
            if (timeout == head) {
                head = next;
            }
            if (timeout == tail) {
                tail = timeout.prev;
            }
            // for JVM GC
            timeout.prev = null;
            timeout.next = null;
            timeout.timerTask = null;
        }

        public void execute(int i) {
            WheelTimeout timeout = head;
            Set<WheelTimeout> removeTimeoutSet = new HashSet<>();
            Map<WheelTimeout, WheelBucket> addTimeoutBucketMap = new HashMap<>();
            while (timeout != null) {
                int round = timeout.decrRound();
                WheelTimeout nextTimeout = timeout.next;
                if (round == -1) {
                    final TimerTask timerTask = timeout.timerTask;
                    service.execute(timerTask::start);
                    int duration = taskDurationMap.get(timerTask);
                    int nextRuntime = duration + i;
                    WheelBucket wheelBucket = wheelTimer.wheelBuckets[nextRuntime % wheelTimer.size];
                    int nextRound = duration / wheelTimer.size;
                    WheelTimeout nextRunTimeout = new WheelTimeout(timerTask, nextRound);
                    removeTimeoutSet.add(timeout);
                    addTimeoutBucketMap.put(nextRunTimeout, wheelBucket);
                }
                timeout = nextTimeout;
                if (timeout == head) break;
            }
            removeTimeoutSet.forEach(this::remove);
            addTimeoutBucketMap.keySet().forEach(key -> addTimeoutBucketMap.get(key).addTimeout(key));
        }

    }

    private static final class WheelTimeout {
        private TimerTask timerTask;
        private WheelTimeout prev;
        private WheelTimeout next;
        private final AtomicInteger rounds;

        public WheelTimeout(TimerTask timerTask, int rounds) {
            this.timerTask = timerTask;
            this.prev = null;
            this.next = null;
            this.rounds = new AtomicInteger(rounds);
        }

        public int decrRound() {
            return rounds.decrementAndGet();
        }

    }

}