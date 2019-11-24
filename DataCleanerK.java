import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


public class DataCleanerK{
        ////////////////////////////////////////////////////////////////////////////////////
        ///                                 Variables                                    ///
        ////////////////////////////////////////////////////////////////////////////////////
        
        public static int[] monthDays = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
        ////////////////////////////////////////////////////////////////////////////////////
        ///                                    Main                                      ///
        ////////////////////////////////////////////////////////////////////////////////////
        public static void main(String[] args) throws IOException{
                welcome();
                
                
                String path = existingFile("csv", 'r');
                String[][] fileData = readCsvToArray(path);
                
                System.out.println("Clustering by Zone...");
                System.out.println("0%");
                ArrayList <String[][]> clusteredData = clusterize(fileData, 0);
                System.out.println("100%");
                System.out.println("Successfully clustered by Zone.");
                
                System.out.println("Clustering by Date...");
                System.out.println("0%");
                ArrayList <ArrayList<String[][]>> fullyClusteredData = new ArrayList<ArrayList<String[][]>>();
                for (int h = 0; h < clusteredData.size(); h++){
                        fullyClusteredData.add(clusterize(clusteredData.get(h), 1));
                        System.out.println((((h+1) * 100) / clusteredData.size()) + "%");
                }
                System.out.println("Successfully clustered by Date.");
                
                System.out.println("Sorting by Date.");
                System.out.println("0%");
                for (int h = 0; h < fullyClusteredData.size(); h++){
                        for (int i = 0; i < fullyClusteredData.get(h).size(); i++){
                                fullyClusteredData.set(h, quickSort(fullyClusteredData.get(h)));
                                System.out.println((((h) * 100) / fullyClusteredData.size()) + "%" + " ...Sorting Cluster " + h + ": " + (((i+1) * 100) / fullyClusteredData.get(h).size()) + "%");
                        }
                        System.out.println((((h+1) * 100) / fullyClusteredData.size()) + "%");
                }
                
                write(existingFile("csv", 'w'), fullyClusteredData);
        }

        ////////////////////////////////////////////////////////////////////////////////////
        ///                                   Methods                                    ///
        ////////////////////////////////////////////////////////////////////////////////////
        
        //////////////////////////////////
        //          GUI Stuff           //
        //////////////////////////////////
        
        public static void welcome(){
                
                //welcomes the user
                //getss nothing
                //returns nothing
                //does say hello!
                
                System.out.println("Welcome to the DataCleaner 2.0");
                System.out.println("This file will clean your data if the csv format looks like below. Otherwise You will run into error and the program will crash.");
                System.out.println("Zone, Date, Month, Depth, ParmName, Units, Value, Mdl");
                System.out.println("To proceed press ENTER");
                System.out.println();
                Scanner prompt = new Scanner(System.in);
                prompt.nextLine();
                
        }
        
        //////////////////////////////////
        //       Clustering Stuff       //
        //////////////////////////////////
        
        public static String[] addString(String[] array, String arg){
                //Adds a String to a String Array
                //formal: array: the String Array, arg: the String Argument to add
                //returns: ret = array + arg
                String[] ret = new String[array.length + 1];
                for (int h = 0; h < array.length; h++){
                        ret[h] = array[h];
                }
                ret[array.length] = arg;
                return ret;
        }
        public static int[] addInt(int[] array, int arg){
                //Adds an int to an int Array
                //formal: array: the int Array, arg: the int Argument to add
                //returns: ret = array + arg
                int[] ret = new int[array.length + 1];
                for (int h = 0; h < array.length; h++){
                        ret[h] = array[h];
                }
                ret[array.length] = arg;
                return ret;
        }
        
        public static ArrayList<String[][]> clusterize(String[][] array, int index){
                //Getss a 2d Array, gets an index, makes an arrayList of 2d Arrays where the 2d Array is the Splitted lines which their index'th index are the same.
                //formal: array: the original 2d array, index: the filter of clustering
                //retirns: arrayClustered: has all of the String 2d Arrays which their arguments have the same index'th index
                ArrayList<String[][]> arrayClustered = new ArrayList<String[][]>();
                String[] allZones = new String[0];
                int[] allCounts = new int[0];
                for (String[] h: array){
                        boolean alreadySeen = false;
                        for (int i = 0; i < allZones.length; i++){
                                if (allZones[i].equals(h[index])){
                                        alreadySeen = true;
                                        allCounts[i]++;
                                        break;
                                }
                        }
                        if (!alreadySeen){
                                allZones = addString(allZones, h[index]);
                                allCounts = addInt(allCounts, 1);
                        }
                }
                for (int h = 0; h < allCounts.length;  h++){
                        arrayClustered.add(new String[allCounts[h]][5]);
                        int counter = 0;
                        for (int i = 0; i < array.length; i++){
                                if (array[i][index].equals(allZones[h])){
                                        arrayClustered.get(h)[counter] = array[i];
                                        counter++;
                                }
                        }
                }
                return arrayClustered;
        }
        
        //////////////////////////////////
        //      Date sorting Stuff      //
        //////////////////////////////////
        
        public static ArrayList<String[][]> insertionSort(ArrayList<String[][]> a)  { 
                //Do insertion sort by the format of date
                //formal: a: an arraylist where arraylist.get(i)[0][1] is the representive date for all of the date-clustered Arraylist.
                //returns: a after a changed.
                for (int i = a.size() - 1; i != 0; i--){
                        for (int j = 0; j < i; j++){
                                if (dateToValue(dateToArray(a.get(j)[0][1])) > dateToValue(dateToArray(a.get(i)[0][1]))){
                                        for (int k = i; k > j; k--){
                                                String[][] temp = a.get(k);
                                                a.set(k, a.get(k-1));
                                                a.set(k-1, temp);
                                        }
                                }
                        }
                }      
                return a;
        }
        public static ArrayList<String[][]> quickSort(ArrayList<String[][]> arr){
                //does quick sort by the format of date, helped by insertionSort.
                //formal: arraylist which is clustered by date
                //returns: arraylist sorted by date
                if (arr.size() == 0){
                        return arr;
                }
                if (arr.size() <= 100){
                        arr = insertionSort(arr);
                        return arr;
                }
                ArrayList<String[][]> ret = new ArrayList<String[][]>();
                ArrayList<String[][]> l = new ArrayList<String[][]>();
                ArrayList<String[][]> h = new ArrayList<String[][]>();
                ArrayList<String[][]> m = new ArrayList<String[][]>();
                m.add(arr.get(0));
                for (int i = 1; i < arr.size(); i++){
                        if (dateToValue(dateToArray(arr.get(i)[0][1])) < dateToValue(dateToArray(m.get(0)[0][1]))){
                                l.add(arr.get(i));
                        }
                        else{
                                h.add(arr.get(i));
                        }
                }
                ret.addAll(quickSort(l));
                ret.addAll(m);
                ret.addAll(quickSort(h));
                
                return ret;
        }
                                        
        //////////////////////////////////
        // Reading and validation stuff //
        //////////////////////////////////
        public static String[][] readCsvToArray(String path) throws IOException{
                //change csv file into 2d array and ignore some unwanted columns
                //formal: a validated path helped by existingFile
                //returns: a 2d array, filtered by ignored columns.
                File csvFile = new File(path);
                Scanner csvFileScanner = new Scanner(csvFile);
                int csvFileLineCounter = 0;
                
                while (csvFileScanner.hasNextLine()){
                        csvFileScanner.nextLine();
                        csvFileLineCounter++;
                }
                
                String[][] csvFileArray = new String[csvFileLineCounter - 1][5]; 
                String csvFileLine;
                String[] csvFileLineSplitted;
                csvFileScanner = new Scanner(csvFile);
                csvFileLineCounter --;
                csvFileScanner.nextLine();
                
                while (csvFileScanner.hasNextLine()){
                        csvFileLine = csvFileScanner.nextLine();
                        csvFileLineSplitted = csvFileLine.split(",");
                        
                        csvFileArray[csvFileArray.length - csvFileLineCounter][0] = csvFileLineSplitted[0];
                        csvFileArray[csvFileArray.length - csvFileLineCounter][1] = csvFileLineSplitted[1];
                        csvFileArray[csvFileArray.length - csvFileLineCounter][2] = csvFileLineSplitted[2];
                        csvFileArray[csvFileArray.length - csvFileLineCounter][3] = csvFileLineSplitted[4];
                        csvFileArray[csvFileArray.length - csvFileLineCounter][4] = csvFileLineSplitted[6];
                        
                        csvFileLineCounter --;
                        
                }
                
                return csvFileArray;
        }
        
        public static String existingFile(String fileExtension, char activity) throws IOException{
                
                /*
                 * Return a valid path to read from/write to
                 * 
                 * Input: 
                 * The required extension for the file like csv or txt or java
                 * The action, is either 'r' (read) or 'w' (write)
                 * 
                 * Output:
                 * The first valid path eligible for doing the action
                 * 
                 * */
                
                String path = "";
                File file;
                Scanner pathInput = new Scanner(System.in);
                
                boolean gotAnswer = false;
                boolean correctExtension;
                
                if (activity == 'r'){
                        System.out.println("Give the path of the file that you wish to read from: ");
                }
                else{
                        System.out.println("Give the path of the file that you wish to write in: ");
                }     
                        
                while (!gotAnswer){
                        
                        
                        path = pathInput.nextLine();
                        
                        correctExtension = true;
                        for (int h = 0; h < fileExtension.length(); h++){
                                if (path.charAt(path.length() - h - 1) != fileExtension.charAt(fileExtension.length() - h - 1)){
                                        correctExtension = false;
                                        System.out.println("Wrong extension file, " + fileExtension + " file expected.");
                                        break;
                                }
                        }
                        
                        if (correctExtension){
                                file = new File(path);
                                
                                if (activity == 'r' && file.exists()){
                                        System.out.println("Path validated successfully.");
                                        gotAnswer = true;
                                        break;
                                }
                                
                                
                                else if (activity == 'w' && file.exists()){
                                        
                                        System.out.println("Exists. Overwrite? (y/n)");
                                        System.out.println(file.getPath());
                                        String overwrite;
                                        overwrite = validInput(new String[]{"Y", "N", "y", "n"});
                                        if (overwrite.equals("y") || overwrite.equals("Y")){
                                                file.createNewFile();
                                                gotAnswer = true;
                                                System.out.println("File validated and overwrote successfully.");
                                                break;
                                                        
                                        }
                                }
                                
                                
                                else if (activity == 'w' && !file.exists()){
                                        
                                        try{
                                                file.createNewFile();
                                                gotAnswer = true;
                                                System.out.println("File validated and created successfully.");
                                                break; 
                                        }
                                        
                                        catch (Exception e){
                                                System.out.println("Wrong path, retry;");
                                        }
                                }
                                
                                else{
                                        System.out.println("File doesn't exist, retry;");
                                }
                        }
                }
                return path;
        }
        public static String validInput(String[] allowedInputs){ // used for getting Y, N, y, n from user.
                String notSure;
                String valid = "";
                Scanner input = new Scanner(System.in);
                
                //Takes several inputs until a valid one comes, based on the valid input list it got as an argument;
                
                
                while (valid == ""){ 
                        notSure = input.nextLine();
                        
                        //cchecks if input is in the valid domain;
                        
                        for (String h : allowedInputs){
                                try{
                                        if (h.equals(notSure)){
                                                valid = h;
                                                break;
                                        }
                                }
                                catch(Exception e){
                                        System.out.println("");
                                }
                        }
                        
                        
                        //no need to type invalid if the input works!
                        
                        if (valid != ""){
                                break;
                        }
                        
                        System.out.println("Invalid, Retry!");
                }
                
                
                
                
                //returns the first valid input entered.
                
                return valid;
        }
        
        //////////////////////////////////
        //    Date processing Stuff     //
        //////////////////////////////////
        

        
        public static String[] dateToArray(String date){
                //changes the date to a String Array
                //formal: date by form: xx-xx-xx
                //returns: {xx, xx, xx}
                String[] dateArray = date.split("-");
                
                return dateArray;
        }
        
        public static int sumToIndex(int[] array, int index){
                //adds up the month value helped by the public variable monthDays
                //gets: array: monthDays, index: is actually the dateArray[1] - 1;
                //returns: month's value.
                int sum = 0;
                
                for (int h = 0; h < index + 1; h++){
                        sum += array[h];
                }
                
                return sum;
        }
        
        public static String[] changeDateFormat(String[] date){
                //changes from [dd, mm, yy] to [dd, mm, yyyy]
                //gets: date: date array [dd, mm, yy]
                //returns: date array [dd, mm, yyyy]
                if ((date[0] + date[1] + date[2]).length() == 8){
                        return date;
                }
                String[] newDate = new String[3];
                if (Integer.parseInt(date[2]) > 50){
                        newDate[0] = "19" + date[2];
                }
                else{
                        newDate[0] = "20" + date[2];
                }
                newDate[1] = date[1];
                newDate[2] = date[0];
                
                return newDate;
        }
        
        public static int dateToValue(String[] date){
                //gives the rational value of date helped by the other date methods
                ///gets: [dd, mm, yy]
                //returns: dateValue
                
                int dateValue = 0;
                date = changeDateFormat(date);
                
                dateValue += (Integer.parseInt(date[0]) - 1980) * 365;
                dateValue += sumToIndex(monthDays, Integer.parseInt(date[1]) - 1);
                dateValue += Integer.parseInt(date[2]);
                
                return dateValue;
        }
        
        public static void write(String path, ArrayList<ArrayList<String[][]>> fullyCookedLines) throws IOException{
                
                //writes in a file, the given ArrayList
                //gets: path of the file, the ArrayList *path must include the file  name
                //returns: nothing
                //does: make a file in the given path.
                
                File file = new File(path);
                file.createNewFile();
                PrintWriter printer = new PrintWriter(path);
                
                for (int h = 0; h < fullyCookedLines.size(); h++){
                        for (int i = 0; i < fullyCookedLines.get(h).size(); i++){
                                for (String[] j: fullyCookedLines.get(h).get(i)){
                                        for (String k: j){
                                                printer.print(k + ",");
                                        }
                                        printer.println();
                                }
                        }
                }
                printer.close();
        }
}
