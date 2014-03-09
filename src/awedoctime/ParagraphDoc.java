package awedoctime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

public class ParagraphDoc implements Document{

    private HashMap<String, String> content = new HashMap<String, String>();
    private HashMap<String, ArrayList<String>> structure = new HashMap<String, ArrayList<String>>();
    private ArrayList<String> body = new ArrayList<String>();
    
    /**
     * Initialize a new paragraph Document
     * 
     * @param paragraphContent
     */
    ParagraphDoc(String paragraphContent){
        java.util.Date date= new java.util.Date();
        String ID = new String(CreateID.P());
        
        body.add(ID);
        content.put(ID, paragraphContent);
    }
    
    public HashMap<String, String> getContentMap(){
        return new HashMap<String, String>(content);
    }
    
    public HashMap<String, ArrayList<String>> getStructureMap(){
        return new HashMap<String, ArrayList<String>>(structure);
    }
    
    public ArrayList<String> getBodyArray(){
        return new ArrayList<String>(body);
    }
    
    /**
     * Returns a concise String representation of the document.
     */
    @Override public String toString(){
        return "Paragraph: " + content.get(body.get(0));
    }
    
    
    @Override
    public Document append(Document other) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int bodyWordCount() {
        int count = 0;
        for(String id: content.keySet()){
            if(id.charAt(0) == 'P'){
                for(String word: Helper.getListOfWords(content.get(id))) count++;
            }
        }
        return count;
    }
    
    @Override
    public Document tableOfContents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toLaTeX() throws ConversionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toMarkdown() throws ConversionException {
        // TODO Auto-generated method stub
        return null;
    }

}
