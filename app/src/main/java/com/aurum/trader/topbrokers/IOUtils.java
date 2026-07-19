package com.aurum.trader.topbrokers;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IOUtils {
    static final String TAG = IOUtils.class.getName();

    public static String toText(InputStreamReader inputStreamReader) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                } else {
                    String sb2 = sb.toString();
                    close(inputStreamReader);
                    return sb2;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            close(inputStreamReader);
            return "";
        } catch (Throwable th) {
            close(inputStreamReader);
            throw th;
        }
    }


    public static void close(InputStreamReader inputStreamReader) {
        if (inputStreamReader != null) {
            try {
                inputStreamReader.close();
            } catch (Exception unused) {
            }
        }
    }

    public static void close(AsyncTask asyncTask) {
        if (asyncTask != null) {
            try {
                asyncTask.cancel(true);
            } catch (Exception unused) {
            }
        }
    }

}
