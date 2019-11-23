//The main class
//Start doing the functions and utilities

// WE ARE GOING TO CRUSH THIS

//////////////////////////////////////////////
//                 PART 2                   //
//////////////////////////////////////////////

//Check if there is repeated parameters
//If the row which is being processed is duplicate of another examination in the same subject, room and date, make an average.
import java.util.ArrayList;
 
class Repetes{
  public static void main (String [] args){
    String [] [] array = {
      {"804", "2007-01-02", "1", "Alkalinity", "38.6"},
      {"804", "2007-01-02", "1", "Alkalinity", "39.6"},
      {"804", "2007-01-02", "1",  "PH", "36.6"},
      {"804", "2007-01-02", "1", "Conductivity", "40.8"},
      {"804", "2008-10-02", "10",  "Alkalinity", "39.5"},
      {"817", "04-10-99", "10",  "Alkalinity",  "38.4"},
      {"826", "04-10-99", "10", "PH",  "38.2"}
    } ;
    repCheck(array);
  }
  public static void repCheck(String [] [] ogArray){
    
    int counter = 1; // Counts how many lines are repeted
    ArrayList <String [] [] > finalArray = new ArrayList<String [][]> (1); // Array List to save the final array after repetitions are checked
    ArrayList <Integer> tempr = new ArrayList<Integer>(1); // Saves which lines are repeted
    for (int i = 0; i < ogArray.length; i++){
      for (int j = 0; j < ogArray.length; j++){ // To loop through all the lines 
        if (ogArray[i][1] == ogArray[j][1] && ogArray[i][3] == ogArray[j][3]){ // If the paramname and the date are the same
          counter +=1; // adds to counter
          tempr.add(j); // Adds the repeted row number to the array, so that we can keep track of lines
          System.out.println(j + "th is being repeted");
        }
      }
      tempr.add(i);   
      // Add average of repeted elements
      int sum = 0;
      for (int j = 0; j < counter; j++){
        sum += Integer.parseInt(ogArray[tempr.get(j)][4]); // sum
        System.out.println(sum);
      }
      // add to array list
      // This is where I am having trouble
      //finalArray.add(Integer.toString(sum / counter)) ;
      counter = 1; // Reset counter, so that other repeted lines can also be checked
    } 
    // Add all not-repeted rows , by skipping over all the rows highlighted in tempr array
  }
}
