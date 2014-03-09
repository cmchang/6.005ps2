package awedoctime;
import java.util.HashMap;
import java.util.ArrayList;

public class EmptyDoc implements Document{

    private HashMap<String, String> content;
    private HashMap<String, ArrayList<String>> structure;
    private ArrayList<String> body;
    
    
    /**
     * Initialize an empty Doc
     * 
     */
    public EmptyDoc(){
        content = new HashMap<String, String>();
        structure = new HashMap<String, ArrayList<String>>();
        body = new ArrayList<String>();
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
