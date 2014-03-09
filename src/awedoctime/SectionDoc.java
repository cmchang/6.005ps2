package awedoctime;

import java.util.ArrayList;
import java.util.HashMap;

public class SectionDoc implements Document {

    //Rep Invariants
    private HashMap<String, String> content;
    private HashMap<String, ArrayList<String>> structure;
    private ArrayList<String> body;
    
    /**
     * Initialize a new section Document 
     * @param header
     * @param doc
     */
    SectionDoc(String header, Document doc){
        String ID = CreateID.S();

        content = doc.getContentMap();
        content.put(ID, header);
        
        structure = doc.getStructureMap();
        structure.put(ID, doc.getBodyArray());
     
        body.add(ID);
    }
    
    public HashMap<String, String> getContentMap(){
        HashMap<String, String> copy = Clone.content(content);
        return copy;
    }
    
    public HashMap<String, ArrayList<String>> getStructureMap(){
        HashMap<String, ArrayList<String>> copy = Clone.structure(structure);
        return copy;
    }
    
    public ArrayList<String> getBodyArray(){
        ArrayList<String> copy = Clone.body(body);
        return copy;
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
