package awedoctime;

import java.util.ArrayList;
import java.util.HashMap;

public class AppendDocs implements Document {


    private HashMap<String, String> content = new HashMap<String, String>();
    private HashMap<String, ArrayList<String>> structure = new HashMap<String, ArrayList<String>>();
    private ArrayList<String> body = new ArrayList<String>();
    
    /**
     * Initialize two documents appended together
     */
    public AppendDocs(Document doc1, Document doc2){
        //copy doc1 document information
        content = doc1.getContentMap();
        structure = doc1.getStructureMap(); 
        body = doc1.getBodyArray();
        
        //combine document information
        content.putAll(doc2.getContentMap());
        structure.putAll(doc2.getStructureMap());
        for(String bodyContent: doc2.getBodyArray()){
            body.add(bodyContent);
        }
    }
    
    private boolean checkRep(){
        boolean structureMatchesContent = true;
        for(String key: structure.keySet()){
            if(!content.containsKey(key)) structureMatchesContent = false; 
            for(String key2: structure.get(key)){
                if(!content.containsKey(key2)) structureMatchesContent = false; 
            }
        }
        
        boolean bodyNonZero = body.size() >= 0;
        boolean bodyMatchesContent = true;
        for(String key: body){
            if(!content.containsKey(key)) bodyMatchesContent = false; 
        }
        return structureMatchesContent && bodyMatchesContent && bodyNonZero;
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
    
    @Override
    public Document append(Document other) {
        Document newDoc = new AppendDocs(this, other);
        return newDoc;
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
