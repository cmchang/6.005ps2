package awedoctime;

import java.util.ArrayList;
import java.util.HashMap;

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
        String ID = new String(CreateID.P());
        
        body.add(ID);
        content.put(ID, paragraphContent);
    }
    
    /**
     * Checks the representation to make sure it is still valid
     * @return a boolean indicating if the rep is still valid
     */
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
    
    /**
     * returns a copy of the contents contained in the content hashMap
     */
    public HashMap<String, String> getContentMap(){
        return new HashMap<String, String>(content);
    }
    
    /**
     * returns a copy of the contents contained in the structure hashMap
     */
    public HashMap<String, ArrayList<String>> getStructureMap(){
        return new HashMap<String, ArrayList<String>>(structure);
    }
    
    /**
     * returns a copy of the contents contained in the body array
     */
    public ArrayList<String> getBodyArray(){
        return new ArrayList<String>(body);
    }
    
    /**
     * Returns a concise String representation of the document.
     */
    @Override public String toString(){
        return "" + content.get(body.get(0));
    }

    /**
     * Returns a hash code of this particular document
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * returns a boolean indicating if the two documents have equivalent content
     */
    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    /**
     * returns a new Document containing the 'other' document's content appended to this document
     */
    @Override
    public Document append(Document other) {
        Document newDoc = new AppendDocs(this, other);
        return newDoc;
    }

    /**
     * returns a count of all the words in paragraphs of this Document
     */
    @Override
    public int bodyWordCount() {
        int count = 0;
        for(String id: content.keySet()){
            if(id.charAt(0) == 'P'){
                for(String word: Helper.getListOfWords(content.get(id))){
                    if(word.length()> 0)count++;  
                }
            }
        }
        return count;
    }
    
    /**
     * returns the Table of Contents of this document in a format as indicated in the pset assignment 
     */
    @Override
    public Document tableOfContents() {
        return new EmptyDoc();
    }

    /**
     * returns a string containing the content of this document in formatted for LaTeX
     */
    @Override
    public String toLaTeX() throws ConversionException {
        String laTex = "\\documentclass{article}\\begin{document}\\paragraph{" + Helper.specialLatexCharacters(content.get(body.get(0))) + "}\\end{document}";
        return laTex;
    }

    /**
     * returns a string containing the content of this document in formatted for Markdown
     */
    @Override
    public String toMarkdown() throws ConversionException {
        return Helper.markdownCharEscape(content.get(body.get(0)));
    }
    
    /**
     * For problem 5:
     * 
     * returns a string containing the content of this document in formatted for Markdown using bullets
     */
    @Override
    public String toMarkdownBullets() {
        return "+    " + Helper.markdownCharEscape(content.get(body.get(0)));
    }

}
