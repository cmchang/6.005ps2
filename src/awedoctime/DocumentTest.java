package awedoctime;

import static awedoctime.AwesomeDocumentTime.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * Document ADT
 * 
 * For this recursive data type, the Paragraph acts as our base cause because it just contains a string.
 * The Section makes this data type recursive because it can contain a paragraph, another section, or nothing (empty document).  
 * A document can be empty if it does not contain a section or a paragraph.
 * 
 * The variants (constructors) for a document are: Empty, Paragraph, and Section
 * 
 * Recursive Data Type Definition:
 *      Document = Empty + Paragraph(String) + Section (String, Document)
 * 
 * --------------------------------------------------------------------------
 * 
 * Tests for Document.
 * 
 * You SHOULD create additional test classes to unit-test variants of Document.
 * You MAY strengthen the specs of Document and test those specs.
 */
public class DocumentTest {
    /**
     * Tests for toString method
     * (A) Creating an Empty Document
     * (B) Creating a Paragraph Document
     * (C) Creating a Section Document with a/an...
     *      (C1) Empty Document (C2) Paragraph Document (C3) Section Document (C4) Appended Document
     * (D) Creating an Appended Document with a/an...
     *      (D1) Empty Document (D2) Paragraph Document (D3) Section Document (D4) Appended Document
     */
    
    // Tests (A) Creating an Empty Document
    @Test public void testToStringEmptyDoc(){
        Document emptyDoc = empty();
        String output = emptyDoc.toString();
        assertEquals(output, "Empty");
    }
    // (B) Creating a Paragraph Document
    @Test public void testToStringParagraphDoc(){
        Document paragraphDoc = paragraph("I'm a new paragraph!!");
        String output = paragraphDoc.toString();
        assertEquals(output, "Paragraph: I'm a new paragraph!!");
    }
    
    // Creating a Section Document with (C1) An Empty Document
    @Test public void testToStringSectionDocWithEmpty(){
        Document empty = empty();
        Document section = section("Section 1", empty);
        
        String output = new String(section.toString());
        String expectedAnswer = "Section 1\n";
        assertEquals(expectedAnswer,output);
    }
    
    // Creating a Section Document with (C2) Paragraph Document (C3) Section Document
    @Test public void testToStringSectionDocWithParagraphsAndSections(){
        Document p1 = paragraph("Paragraph 1");
        Document s1 = section("Section 1", p1);
        Document s2 = section("Section 2", s1);
        Document finalDoc = section("Section 3", s2);
        
        String output = new String(finalDoc.toString());
        String expectedAnswer = "Section 3\n    Section 2\n        Section 1\n            Paragraph 1\n";
        assertEquals(expectedAnswer,output);
    }
    
    // Creating a Section Document with (C4) Append Documents
    @Test public void testToStringSectionDocWithAppendedDocuments(){
        Document p1 = paragraph("Paragraph 1");
        Document p2 = paragraph ("Paragraph 2");
        Document twoParagraphs = append(p1, p2);
        Document s1 = section("Section 1", twoParagraphs);
        
        String output = new String(s1.toString());
        String expectedAnswer = "Section 1\n    Paragraph 1\n    Paragraph 2\n";
        assertEquals(expectedAnswer,output);
    }
    
    
    //Creating an Appended Document with (D1) Empty Document (D4) Appended Documents
    @Test public void testToStringAppendEmptyAndOtherAppendedDocs(){
        Document empty = empty();
        Document p1 = paragraph("Paragraph 1");
        Document p2 = paragraph("Paragraph 2");
        Document twoPs = append(p1, p2);
        Document s1 = section("Section 1", twoPs);
        Document finalDoc = append(append(append(p1, s1), empty), s1);
        
        String output = new String(finalDoc.toString());
        String expectedAnswer = "Paragraph 1\nSection 1\n    Paragraph 1\n    Paragraph 2\nSection 1\n    Paragraph 1\n    Paragraph 2\n";
        assertEquals(expectedAnswer,output);
    }  
    
    //Creating an Appended Document with (D2) Paragraph Document
    @Test public void testToStringAppendParagraphs(){
        Document p1 = paragraph("Paragraph 1");
        Document p2 = paragraph("Paragraph 2");
        Document finalDoc = append(p1, p2);
        
        String output = new String(finalDoc.toString());
        String expectedAnswer = "Paragraph 1\nParagraph 2\n";
        assertEquals(expectedAnswer,output);
    }
    
  //Creating an Appended Document with (D2) Paragraph Document (D3) Section Document
    @Test public void testToStringAppendParagraphsAndSections(){
        Document p1 = paragraph("Paragraph 1");
        Document p2 = paragraph("Paragraph 2");
        Document twoPs = append(p1, p2);
        Document s1 = section("Section 1", twoPs);
        Document finalDoc = append(p1, s1);
        
        String output = new String(finalDoc.toString());
        String expectedAnswer = "Paragraph 1\nSection 1\n    Paragraph 1\n    Paragraph 2\n";
        assertEquals(expectedAnswer,output);
    }
    
    
    /**
     * Tests for bodyWordCount method
     */
    
    @Test public void testBodyWordCountEmpty() {
        Document doc = empty();
        assertEquals(0, doc.bodyWordCount());
    }
    
    @Test public void testBodyWordCountParagraph() {
        Document doc = paragraph("Hello, world!");
        assertEquals(2, doc.bodyWordCount());
    }
    
    @Test public void testBodyWordCountSectionParagraphs() {
        Document paragraphs = paragraph("Hello, world!").append(paragraph("Goodbye."));
        Document doc = section("Section One", paragraphs);
//        System.out.println(doc);
        assertEquals(3, doc.bodyWordCount());
    }
}
