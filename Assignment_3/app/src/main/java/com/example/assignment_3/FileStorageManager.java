package com.example.assignment_3;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileStorageManager {
    static String fileName = "Result.txt";

    FileOutputStream fos;
    FileInputStream fis;

    public void writeResultToFile(Activity context, int correctAnswers, int attemptQuestions){
        try{
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            String resultString = String.valueOf(correctAnswers) + "@" + String.valueOf(attemptQuestions) + "@";
            fos.write(resultString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Integer> readAllValuesFromFile(Activity context){
        ArrayList<Integer> list = new ArrayList<>(0);
        StringBuffer stringBuffer = new StringBuffer();

        try {
            File file = new File(context.getFilesDir(), fileName);
            if(!file.exists()){
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                String temp = 0 + "@" + 0 + "@";
                fos.write(temp.getBytes());
                fos.close();
            }
            fis = context.openFileInput(fileName);

            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

            int read = 0;
            while ((read = inputStreamReader.read()) != -1){
                stringBuffer.append((char)read);
            }

            // read the value from the stringBuffer and convert and store it to Integer ArrayList
            {
                int index = 0;
                for (int i = 0; i < stringBuffer.toString().toCharArray().length; i++) {
                    if (stringBuffer.toString().toCharArray()[i] == '@') {
                        String firstValue = stringBuffer.toString().substring(index, i);
                        list.add(Integer.parseInt(firstValue));
                        index = i + 1;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
