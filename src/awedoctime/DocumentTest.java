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
        assertEquals(3, doc.bodyWordCount());
    }
    
    // TODO write additional tests for Document
    
    @Test public void testToStringEmptyDoc(){
        Document emptyDoc = empty();
        String output = emptyDoc.toString();
        assertEquals(output, "Empty");
    }

    @Test public void testToStringParagraphDoc(){
        Document paragraphDoc = paragraph("I'm a new paragraph!!");
        String output = paragraphDoc.toString();
        assertEquals(output, "Paragraph: I'm a new paragraph!!");
    }
    
    @Test public void testToStringSectionDoc(){
        Document p1 = paragraph("Paragraph 1");
        Document s1 = section("Section 1", p1);
        Document s2 = section("Section 2", s1);
        Document finalDoc = section("Section 3", s2);
        String output = new String(finalDoc.toString());
        System.out.println(output);
        String expectedAnswer = "Section 3\n    Section 2\n        Section 1\n            Paragraph 1";
        System.out.println(expectedAnswer);
        //assertTrue(expectedAnswer.equals(output));
    }
    
}
