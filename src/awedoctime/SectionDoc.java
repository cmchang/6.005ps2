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
    
    public Document nestedHeaders(String id, ArrayList<Integer> nested){
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
        String laTex = "";
        for(String id: body){
            if(id.charAt(0) == 'S'){
                laTex += "\\documentclass{article}\\begin{document}\\section{" + content.get(id) + "}";
                String nestedLatex = nestedLatexSections(id, 1);
                if(nestedLatex.equals("throw exception")){
                    throw new ConversionException("Too many nested sections for LaTex Syntax");
                }else{
                    laTex += nestedLatex;
                }
                
            }else{ // content.charAt(0) == 'P'
                laTex += content.get(id);
            }
        }
        laTex += "\\end{document}";
        return laTex;
    }

    @Override
    public String nestedLatexSections(String nestedID, int nested){
        String laTex = "";
        String latexSubsection = "";
        
        //Figure out subsection depth
        if(nested == 1){
            latexSubsection = "\\subsection{'";
        }else if(nested == 2){
            latexSubsection = "\\subsubsection{'";
        }else{
            //nested value is >2, latex can't handle more subsections --> throw error
            return "throw exception";
        }
        
        //Added latex syntax
        for(String id: structure.get(nestedID)){
            if(id.charAt(0) == 'S'){
                laTex += latexSubsection +  content.get(id) + "'}";
                String nestedLatex = nestedLatexSections(id, nested+1);
                if(nestedLatex.equals("throw exception")){
                    laTex = "throw exception";
                }else{
                    laTex += nestedLatex;
                }
            }else{// content.charAt(0) == 'P'
                laTex += content.get(id);
            }
        }
        return laTex;
    }
    
    @Override
    public String toMarkdown() throws ConversionException {
        // TODO Auto-generated method stub
        return null;
    }

}
