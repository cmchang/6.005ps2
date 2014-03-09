package awedoctime;

import java.util.ArrayList;
import java.util.HashMap;

public class SectionDoc implements Document {

    private HashMap<String, String> content = new HashMap<String, String>();
    private HashMap<String, ArrayList<String>> structure = new HashMap<String, ArrayList<String>>();
    private ArrayList<String> body = new ArrayList<String>();
    
    /**
     * Initialize a new section Document 
     * @param header
     * @param doc
     */
    public SectionDoc(String header, Document doc){
        String ID = CreateID.S();

        content = doc.getContentMap();
        content.put(ID, header);
        
        structure = doc.getStructureMap();
        structure.put(ID, doc.getBodyArray());            
     
        body.add(ID);
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
    
    private boolean checkRep(){
        return true;
    }
    
    /**
     * Returns a concise String representation of the document.
     */
    @Override public String toString(){
        String endLine = "\n";
        String output = "";
        for(String id: body){
            output += content.get(id) + endLine;
            output += (id.charAt(0) == 'S') ? getNestedSections(id, 1): "" ; //if a section, get the nested sections
        }
        
        return output;
    }
    
    private String getNestedSections(String ID, int indent){
        String tab = "    ";
        String endLine = "\n";
        String output = "";
        for(String id: structure.get(ID)){
            for(int x = 0; x< indent; x++) output+= tab; //adds right number of tabs for indentation
            output += content.get(id) + endLine;
            output += (id.charAt(0) == 'S')? getNestedSections(id, indent+1): ""; //if a section, get the nested sections
        }
        
        return output;
    }
    
    @Override
    public Document append(Document other) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int bodyWordCount() {
        // TODO Auto-generated method stub
        return 0;
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
