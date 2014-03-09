package awedoctime;
import java.util.HashMap;
import java.util.ArrayList;
public class Clone {

    public static HashMap<String, String> content(HashMap<String,String> content){
        HashMap<String, String> copy = new HashMap<String, String>();
        for(String ID: content.keySet()){
            copy.put(ID, content.get(ID));
        }
        return copy;
    }
    
    public static HashMap<String, ArrayList<String>> structure(HashMap<String, ArrayList<String>> structure){
        HashMap<String, ArrayList<String>> copy = new HashMap<String, ArrayList<String>>();
        
        for(String ID: structure.keySet()){
            ArrayList<String> nestedArr = new ArrayList<String>();
            for(String nestedID: structure.get(ID)){
                nestedArr.add(nestedID);
            }
            copy.put(ID, nestedArr);
        }
        return copy;
        
    }
    
    public static ArrayList<String> body(ArrayList<String> body){
        ArrayList copy = new ArrayList<String>();
        for(String ID: body){
            copy.add(ID);
        }
        return copy;
    }
}
