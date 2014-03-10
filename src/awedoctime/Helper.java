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
    
    /**
     * This helper method returns a string of text in a form that is compatible with LaTex,
     * 
     * @param text
     * @return
     */
    public static String specialLatexCharacters(String text){
        String specialCharacters = "#$%&~_^{}\\"; // not testing for backslashes yet
        char[] textChar = text.toCharArray();

        //find all the locations of the special characters IN ORDER
        ArrayList<Integer> specialCharLoc = new ArrayList<Integer>();
        for(int i = 0; i< textChar.length; i++){
            String currentChar = Character.toString(textChar[i]);
            if(specialCharacters.contains(currentChar)){
             specialCharLoc.add(i);   
            }
        }
        if(specialCharLoc.size() == 0) return text;//no special character
        
        //replace special characters with LaTex compatible syntax
        String output = "";
        if(specialCharLoc.size() >= 0){
            output += text.substring(0, specialCharLoc.get(0));
            output += insertLatexSyntax(text.charAt(specialCharLoc.get(0)));
        }
        for(int i = 1; i < specialCharLoc.size(); i++){
            output += text.substring(specialCharLoc.get(i-1)+1, specialCharLoc.get(i));
            output += insertLatexSyntax(text.charAt(specialCharLoc.get(i)));
        }
        
        //check the last section of the text
        String lastSection = text.substring(specialCharLoc.get(specialCharLoc.size()-1));
        if(lastSection.length() > 1 && !specialCharacters.contains(lastSection)){
            if(specialCharacters.contains(Character.toString(lastSection.charAt(0)))){ 
                output+= text.substring(specialCharLoc.get(specialCharLoc.size()-1)+1);
            }else{
                output+= text.substring(specialCharLoc.get(specialCharLoc.size()-1));
            }
        }
        return output;
    }
    
    public static String insertLatexSyntax(char symbol){
        String symbolStr = Character.toString(symbol);
        String simpleSymbols = "#$%&_{}"; //not ~^
        String output = new String();
        
        if(simpleSymbols.contains(symbolStr)){
            output = "\\" + symbolStr;
        }else if(symbol == '~'){
            output = "\\~{}";
        }else if(symbol == '^'){
            output = "\\^{}";
        }else if(symbol == '\\'){
            output = "\\textbackslash";
        }
        return output;
    }
}
