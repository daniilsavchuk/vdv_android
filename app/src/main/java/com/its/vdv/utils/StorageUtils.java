package com.its.vdv.utils;

import android.content.Context;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class StorageUtils {
    public static <T> T readData(Context context, String fileName) throws IOException {
        InputStream is = null;

        try {
            createFileIfNotExists(context, fileName);

            is = context.openFileInput(fileName);

            try {
                ObjectInputStream ois = new ObjectInputStream(is);

                return (T) ois.readObject();
            } catch (EOFException e) {
                return null;
            }
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static <T> void writeData(Context context, String fileName, T o) throws IOException {
        OutputStream os = null;

        try {
            createFileIfNotExists(context, fileName);

            os = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(os);

            oos.writeObject(o);
            oos.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    private static void createFileIfNotExists(Context context, String fileName) throws IOException {
        File file = new File(context.getFilesDir() + "/" + fileName);

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException();
            }
        }
    }
}