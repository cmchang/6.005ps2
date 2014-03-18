package awedoctime;
import java.util.HashMap;
import java.util.ArrayList;

public class EmptyDoc implements Document{

    private HashMap<String, String> content;
    private HashMap<String, ArrayList<String>> structure;
    private ArrayList<String> body;
    
    /**
     * Initializes an empty Doc
     */
    public EmptyDoc(){
        content = new HashMap<String, String>();
        structure = new HashMap<String, ArrayList<String>>();
        body = new ArrayList<String>();
    }
    
    /**
     * Checks the representation to make sure it is still valid
     * @return a boolean indicating if the rep is still valid
     */
    private boolean checkRep(){
        boolean contentEmpty, structureEmpty, bodyEmpty;
        contentEmpty = content.keySet().size() == 0;
        structureEmpty = structure.keySet().size() == 0;
        bodyEmpty = body.size() == 0;
        return contentEmpty && structureEmpty && bodyEmpty;
        
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
        return "";
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
     * returns a count of all the words in this Document
     */
    @Override
    public int bodyWordCount() {
        return 0;
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
        String laTex = "\\documentclass{article}\\begin{document}\\end{document}";
        return laTex;
    }

    /**
     * returns a string containing the content of this document in formatted for Markdown
     */
    @Override
    public String toMarkdown() throws ConversionException {
        return "";
    }

    /**
     * For problem 5:
     * 
     * returns a string containing the content of this document in formatted for Markdown using bullets
     */
    @Override
    public String toMarkdownBullets() {
        return "";
    }

}
