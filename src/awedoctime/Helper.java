package awedoctime;

import java.util.ArrayList;
import java.util.HashMap;

public class Helper {

    /**
     * Given a string, return a list containing all of the words in the string
     * @param paragraph
     *          A string of one or more words
     * @return
     */
    public static ArrayList<String> getListOfWords(String paragraph){
        ArrayList<String> ListOfWords = new ArrayList<String>();

        String currentText = new String(paragraph);
        while(currentText.contains(" ")){
            int index = currentText.indexOf(" ");
            ListOfWords.add(currentText.substring(0, index));
            currentText = currentText.substring(index+1);
        }
        ListOfWords.add(currentText);
        
        return ListOfWords;
    }
}
