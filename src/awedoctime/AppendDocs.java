package awedoctime;

import java.util.ArrayList;
import java.util.HashMap;

import awedoctime.Document.ConversionException;

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
        if(body.size() >= 1){
            String lastBodyContent = body.get(body.size()-1);
            if(lastBodyContent.charAt(0) == 'S'){ //last element in doc1's body is a Section
                for(String bodyContent: doc2.getBodyArray()){
                    if(bodyContent.charAt(0) == 'S'){
                        body.add(bodyContent);
                    }else{ //it's a paragraph, so find the last section to insert it in
                        String lastID = lastBodyContent;
                        ArrayList<String> currentList = structure.get(lastID);
                        while(lastID.charAt(0) == 'S'){
                            if (currentList.isEmpty()) {
                                structure.get(lastID).add(bodyContent); //
                                lastID = "P"; //to break the loop
                              }else{
                                  if(currentList.get(currentList.size()-1).charAt(0) == 'P'){
                                      structure.get(lastID).add(bodyContent);
                                      lastID = "P"; //to break the loop
                                  }else{
                                      lastID = currentList.get(currentList.size()-1);
                                      currentList = structure.get(lastID);
                                  }
                              }
                        }
                    }
                }
            }else{//last element in doc1's body is a Paragraph
                for(String bodyContent: doc2.getBodyArray()){
                    body.add(bodyContent);
                }
            }
        }else{//doc1's body is empty
            for(String bodyContent: doc2.getBodyArray()){
                body.add(bodyContent);
            }
        }
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
     * Note: for my representation, I visually show indentations to to see the nested sections
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
    
    /**
     * A helper function for toString to recursively iterate through the strucutre hashMap
     * @param ID the id of the element (that can be found in 'content')
     * @param indent the current number of indents
     * @return a string representation of the specific section
     */
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
        for(String id: body){
            if(id.charAt(0) == 'P'){
                for(String word: Helper.getListOfWords(content.get(id))){
                    if(word.length()> 0)count++;  
                }
            }else{
                count += wordCountNested(id);
            }
        }
        return count;
    }

    /**
     * Helper function for bodyWordCount.  Helps get the word count of nested sections
     * within other sections. 
     * @return the word count in a specified section
     */
    private int wordCountNested(String id) {
        int count = 0;
        for (String nestedID: structure.get(id)){
            if(nestedID.charAt(0) == 'P'){
                for(String word: Helper.getListOfWords(content.get(nestedID))){
                    if(word.length()> 0)count++;  
                }
            }else{
                count+= wordCountNested(nestedID);
            }
        }        return count;
    }
        
    /**
     * returns the Table of Contents of this document in a format as indicated in the pset assignment 
     */
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
                newSection = new AppendDocs(new ParagraphDoc(header), nestedHeaders(id, arraySectionNum));
                sectionNum++;
            }
            output = new AppendDocs(output, newSection);
        }
        return output;
    }
    
    /**
     * A helper function for tableOfContents
     * @param id, the id of the element (that can be found in 'content')
     * @param nested, an array of integers containing the order for the numbering of the sections (e.g. 1.2.1)
     *                associated with the element of the corresponding id
     * @return a document containing the content for the tableOfContents
     */
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
                newSection = new AppendDocs(new ParagraphDoc(header), nestedHeaders(ID, newNested));
                sectionNum++;
            }
            output = new AppendDocs(output, newSection);
        }
        return output;
    }
    
    /**
     * returns a string containing the content of this document in formatted for LaTeX
     */
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

    /**
     * A helper function for toLaTex() to recursively iterate through all the sections
     * @param nestedID the id of the element (that can be found in 'content')
     * @param nested, the count of the current nested layer associated with the element of the given ID
     * @return a string containing the syntax for laTeX
     */
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
    
    /**
     * returns a string containing the content of this document in formatted for Markdown
     */
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

    /**
     * A helper function for toMarkdown() to recursively iterate through all the sections
     * @param id the id of the element (that can be found in 'content')
     * @param hashtags, the necessary number of hashtags needed for this nested layer associated with the element of the given ID
     * @return a string containing the syntax for laTeX
     */
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

    /**
     * For problem 5:
     * 
     * returns a string containing the content of this document in formatted for Markdown using bullets
     */
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

    /**
     * A helper function for toMarkdown() to recursively iterate through all the sections
     * @param id, the id of the element (that can be found in 'content')
     * @param tabs, the count of the current nested layer associated with the element of the given ID
     * @return a string containing the syntax for MarkDown
     */
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
