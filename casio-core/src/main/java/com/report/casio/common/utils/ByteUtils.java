package com.report.casio.common.utils;

import java.io.*;

public class ByteUtils {
    private ByteUtils() {
    }

    public static byte[] objectToBytes(Object obj) throws IOException {
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream sOut = new ObjectOutputStream(out)
        ) {
            sOut.writeObject(obj);
            sOut.flush();
            return out.toByteArray();
        }
    }

    public static Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        try (
                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                ObjectInputStream sIn = new ObjectInputStream(in)
        ) {
            return sIn.readObject();
        }
    }

}
