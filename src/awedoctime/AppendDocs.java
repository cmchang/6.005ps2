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
        if(body.size() >= 1){
            String lastBodyContent = body.get(body.size()-1);
            if(lastBodyContent.charAt(0) == 'S'){ //last element in doc1's body is a Section
                
                for(String bodyContent: doc2.getBodyArray()){
                    if(bodyContent.charAt(0) == 'S'){
                        body.add(bodyContent);
                    }else{ 
                        structure.get(lastBodyContent).add(bodyContent);
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toMarkdown() throws ConversionException {
        // TODO Auto-generated method stub
        return null;
    }

}
