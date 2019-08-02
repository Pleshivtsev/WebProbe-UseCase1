package utils;

import webprobe.utils.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CSVProcessor {

    //Возвращает массив строк из файла
    public static ArrayList<String> readCSV(String filePath){

        BufferedReader br;
        String line;
        ArrayList<String> stringCollection = new ArrayList<String>();

        filePath = filePath.replaceAll("%20", " ");

        try {
            br = new BufferedReader(new FileReader(filePath));

            while ( (line = br.readLine()) != null){
                stringCollection.add(line.trim());
            }

        } catch (FileNotFoundException e) {
            Assert.pageAssert("File \"" + filePath + "\" not found!");
        }
        catch (IOException e) {
            e.printStackTrace();
            Assert.pageAssert("Something wrong while read data from csv file");
        }

        return stringCollection;
    }

    //Запись массива строк в файл
    public static void writeToFile(ArrayList<String> stringList, String fileName ){
        File file;
        String content;

        try{
            file = new File(fileName);

            // if file doesnt exists, then create it
            if (!file.exists())     file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (String string:stringList){
                content = string + "\r\n";
                bw.write(content);
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            Assert.pageAssert("Something wrong while writing data ti file");
        }
    }//public static void writeToFile

    public static String getStringAndDeleteFromFile(String fileName){
        ArrayList<String> strings = readCSV(fileName);
        if (strings.size() < 1) return "";

        String item = strings.get(0);
        strings.remove(0);
        writeToFile(strings, fileName);
        return item;

    }

    public static ArrayList<String> getNStringAndDeleteFromFile(String fileName, int elementNums){
        ArrayList<String> fileStrings = readCSV(fileName);
        ArrayList<String> returnStrings = new ArrayList<>();

        if (fileStrings.size() < 1) return returnStrings;

        for (int i = 0; i < elementNums; i++){
            if (fileStrings.size() < 1) break;
            returnStrings.add(fileStrings.get(0));
            fileStrings.remove(0);
        }

        writeToFile(fileStrings, fileName);
        return returnStrings;
    }

    public static List<String> toUpperCase(ArrayList<String> stringList){
        ArrayList<String> stringCollection = new ArrayList<String>();
        for (String string:stringList)
            stringCollection.add(string.toUpperCase());

        return stringCollection;
    }

    public static String getRandomFromList(ArrayList<String> stringList){
        Random rnd = new Random();
        int i = rnd.nextInt(stringList.size());
        return stringList.get(i);
    }


}