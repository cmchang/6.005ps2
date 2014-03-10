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
    public ParagraphDoc(String paragraphContent){
        java.util.Date date= new java.util.Date();
        String ID = new String(CreateID.P());
        
        body.add(ID);
        content.put(ID, paragraphContent);
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
    
    /**
     * Returns a concise String representation of the document.
     */
    @Override public String toString(){
        return "" + content.get(body.get(0));
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    @Override
    public Document append(Document other) {
        Document newDoc = new AppendDocs(this, other);
        return newDoc;
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
        return new EmptyDoc();
    }

    @Override
    public Document nestedHeaders(String id, ArrayList<Integer> nested) {
        return new EmptyDoc();
    }
    
    @Override
    public String toLaTeX() throws ConversionException {
        String laTex = "\\documentclass{article}\\begin{document}\\paragraph{" + Helper.specialLatexCharacters(content.get(body.get(0))) + "}\\end{document}";
        return laTex;
    }

    @Override
    public String nestedLatexSections(String nestedID, int nested){
        return "";
    }
    
    @Override
    public String toMarkdown() throws ConversionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int wordCountNested(String id) {
        return 0; //since no nesting
    }

}
