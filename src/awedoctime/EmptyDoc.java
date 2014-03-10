package awedoctime;
import java.util.HashMap;
import java.util.ArrayList;

public class EmptyDoc implements Document{

    private HashMap<String, String> content;
    private HashMap<String, ArrayList<String>> structure;
    private ArrayList<String> body;
    
    /**
     * Initialize an empty Doc
     */
    public EmptyDoc(){
        content = new HashMap<String, String>();
        structure = new HashMap<String, ArrayList<String>>();
        body = new ArrayList<String>();
    }
    
    private boolean checkRep(){
        boolean contentEmpty, structureEmpty, bodyEmpty;
        contentEmpty = content.keySet().size() == 0;
        structureEmpty = structure.keySet().size() == 0;
        bodyEmpty = body.size() == 0;
        return contentEmpty && structureEmpty && bodyEmpty;
        
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
        return "";
    }

    @Override
    public String getNestedSections(String ID, int indent) {
        return "";
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
        return 0;
    }

    @Override
    public int wordCountNested(String id) {
        return 0; // since no nesting
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
        String laTex = "\\documentclass{article}\\begin{document}\\end{document}";
        return laTex;
    }
    @Override
    public String nestedLatexSections(String nestedID, int nested){
        return "";
    }

    @Override
    public String toMarkdown() throws ConversionException {
        return "";
    }

    @Override
    public String getNestedMarkDownSections(String id, int hashtags) {
        return "";
    }


    @Override
    public String toMarkdownBullets() {
        return "";
    }

    @Override
    public String getNestedMarkdownBulletSections(String id, int tabs) {
        return "";
    }



}
