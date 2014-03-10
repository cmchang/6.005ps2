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
        String endLine = "\n";
        String output = "";
        for(String id: body){
            output += content.get(id) + endLine;
            output += (id.charAt(0) == 'S') ? getNestedSections(id, 1): "" ; //if a section, get the nested sections
        }
        
        return output;
    }
    
    public String getNestedSections(String ID, int indent){
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
        for(String id: body){
            if(id.charAt(0) == 'P'){
                for(String word: Helper.getListOfWords(content.get(id))) count++;
            }else{
                count += wordCountNested(id);
            }
        }
        return count;
    }

    @Override
    public int wordCountNested(String id) {
        int count = 0;
        for (String nestedID: structure.get(id)){
            if(nestedID.charAt(0) == 'P'){
                for(String word: Helper.getListOfWords(content.get(nestedID))) count++;
            }else{
                count+= wordCountNested(nestedID);
            }
        }        return count;
    }
     
    @Override
    public Document tableOfContents() {
        Document output = new EmptyDoc();
        
        int sectionNum = 1;
        for(String id:body){
            String header = "";
            Document newSection = new EmptyDoc();
            if(id.charAt(0) == 'S'){
                header+= sectionNum + ". " + content.get(id);
                
                //get number of paragraphs
                int paragraphCount = 0;
                for(String nestedID: structure.get(id)){
                    paragraphCount += nestedID.charAt(0) == 'P'? 1: 0;
                }
                header+= " (" + paragraphCount + " paragraphs)";
                
                ArrayList<Integer> arraySectionNum = new ArrayList<Integer>();
                arraySectionNum.add(sectionNum);
                newSection = new SectionDoc(header, nestedHeaders(id, arraySectionNum));
                sectionNum++;
            }
            output = new AppendDocs(output, newSection);
        }
        return output;
    }
    
    private Document nestedHeaders(String id, ArrayList<Integer> nested){
        Document output = new EmptyDoc();
        int sectionNum = 1;
        for(String ID: structure.get(id)){
            String header = "";
            Document newSection = new EmptyDoc();
            if(ID.charAt(0) == 'S'){
                for(int num: nested) header+= num + ".";
                header+= sectionNum + ". " + content.get(ID);
                
              //get number of paragraphs
                int paragraphCount = 0;
                for(String nestedID: structure.get(ID)){
                    paragraphCount += nestedID.charAt(0) == 'P'? 1: 0;
                }
                header+= " (" + paragraphCount + " paragraphs)";
                
                ArrayList<Integer> newNested = new ArrayList<Integer>(nested);
                newNested.add(sectionNum);
                newSection = new SectionDoc(header, nestedHeaders(ID, newNested));
                sectionNum++;
            }
            output = new AppendDocs(output, newSection);
        }
        return output;
    }
    
    @Override
    public String toLaTeX() throws ConversionException {
        String laTex = "\\documentclass{article}\\begin{document}";
        for(String id: body){
            if(id.charAt(0) == 'S'){
                laTex += "\\section{" + Helper.specialLatexCharacters(content.get(id)) + "}";
                String nestedLatex = nestedLatexSections(id, 1);
                if(nestedLatex.equals("throw exception")){
                    throw new ConversionException("Too many nested sections for LaTex Syntax");
                }else{
                    laTex += nestedLatex;
                }
                
            }else{ // content.charAt(0) == 'P'
                laTex += "\\paragraph{" + Helper.specialLatexCharacters(content.get(id)) + "}";
            }
        }
        laTex += "\\end{document}";
        return laTex;
    }

    private String nestedLatexSections(String nestedID, int nested){
        String laTex = "";
        String latexSubsection = "";
        
        //Added latex syntax
        for(String id: structure.get(nestedID)){
            if(id.charAt(0) == 'S'){
                //Figure out subsection depth
                if(nested == 1){
                    latexSubsection = "\\subsection{";
                }else if(nested == 2){
                    latexSubsection = "\\subsubsection{";
                }else{
                    //nested value is >2, latex can't handle more subsections --> throw error
                    return "throw exception";
                }
                
                laTex += latexSubsection +  Helper.specialLatexCharacters(content.get(id)) + "}";
                String nestedLatex = nestedLatexSections(id, nested+1);
                if(nestedLatex.equals("throw exception")){
                    laTex = "throw exception";
                }else{
                    laTex += nestedLatex;
                }
            }else{// content.charAt(0) == 'P'
                laTex += "\\paragraph{"+ Helper.specialLatexCharacters(content.get(id)) + "}";
            }
        }
        return laTex;
    }
    
    @Override
    public String toMarkdown() throws ConversionException {
        String endLine = "\n";
        String output = "";
        for(String id: body){
            if(id.charAt(0) == 'P'){
                output += Helper.markdownCharEscape(content.get(id)) + endLine;                
            }else{
                output += "#" + Helper.markdownCharEscape(content.get(id)) + endLine; 
            }
            
            if(id.charAt(0) == 'S'){  //if a section, get the nested sections
                String nestedStr = getNestedMarkDownSections(id, 2);
                if(nestedStr.equals("throw exception")){
                    throw new ConversionException("Too many nested sections for LaTex Syntax");
                }else{
                    output +=  nestedStr;
                }
            }
        }
        return output;
    }

    private String getNestedMarkDownSections(String id, int hashtags) {
        String endLine = "\n";
        String output = "";
        for(String nestedID: structure.get(id)){
            if(nestedID.charAt(0)=='S'){
                if(hashtags > 6){
                    return "throw exception";
                }
                for(int x = 0; x< hashtags; x++) output+= "#"; //adds right number of hashtags for indentation
            }
            output += Helper.markdownCharEscape(content.get(nestedID)) + endLine;
            if(nestedID.charAt(0) == 'S'){  //if a section, get the nested sections
                String nestedStr = getNestedMarkDownSections(nestedID, hashtags+1);
                if(nestedStr.equals("throw exception")){
                    return "throw exception";
                }else{
                    output +=  nestedStr;
                }
            }
        }
        return output;
    }

    @Override
    public String toMarkdownBullets() {
        String endLine = "\n";
        String bullet = "+    ";
        String output = "";
        for(String id: body){
            output += bullet + Helper.markdownCharEscape(content.get(id)) + endLine;                
            if(id.charAt(0) == 'S'){  //if a section, get the nested sections
                output +=  getNestedMarkdownBulletSections(id, 2);
            }
        }
        return output;
    }

    private String getNestedMarkdownBulletSections(String id, int tabs) {
        String endLine = "\n";
        String tab = "    ";
        String bullet = "+    ";
        String output = "";
        
        for(String nestedID: structure.get(id)){
            if(nestedID.charAt(0)=='S'){
                for(int x = 0; x< tabs; x++) output+= tab; //adds right number of tabs for indentation
            }
            output += bullet + Helper.markdownCharEscape(content.get(nestedID)) + endLine;
            if(nestedID.charAt(0) == 'S'){  //if a section, get the nested sections
                output += getNestedMarkdownBulletSections(nestedID, tabs+1);
            }
        }
        return output;
    }
}
