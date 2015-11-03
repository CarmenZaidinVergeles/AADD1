package com.example.carmen.aadd1;

import android.os.Environment;

import java.io.File;

/**
 * Created by Carmen on 26/10/2015.
 */
public class Archivos {
    private static final int MINIMO = 10;

    public static boolean isLegible() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isModificable(File f) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isRecomendable(File f){
        long libre= f.getFreeSpace();
        long total = f.getTotalSpace();
        if((libre/total)*100>Archivos.MINIMO){
            return true;
        }
        return false;
    }
}

