package awedoctime;

import static awedoctime.AwesomeDocumentTime.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import awedoctime.Document.ConversionException;

/**
 * 
 * Document ADT
 * 
 * For this recursive data type, the Paragraph acts as our base cause because it just contains a string.
 * The Section makes this data type recursive because it can contain a paragraph, another section, or nothing (empty document).  
 * A document can be empty if it does not contain a section or a paragraph.
 * Appended document returns a document after adding the content of one document to another.
 * 
 * The variants (constructors) for a document are: Empty, Paragraph, Section, and Appended
 * 
 * Recursive Data Type Definition:
 *      Document = Empty + Paragraph(String) + Section (String, Document) + AppendedDocs(Document, Document)
 * 
 * --------------------------------------------------------------------------
 * 
 * Tests for Document.
 * 
 * You SHOULD create additional test classes to unit-test variants of Document.
 * You MAY strengthen the specs of Document and test those specs.
 */
public class DocumentTest {
    
    Document emptyA = empty();
    Document emptyB = empty();
    Document paragraphA = paragraph("I'm a paragraph!");
    Document paragraphB = paragraph("I'm a paragraph!");
    Document paragraphC = paragraph("I'm a different paragraph!");
    Document sectionA = section("I'm a section!", paragraphA);
    Document sectionB = section("I'm a section!", paragraphB);
    Document sectionC = section("I'm a different section!", paragraphA);
    Document sectionSectionA = section("I'm a section of sections!", sectionA);
    Document sectionSectionB = section("I'm a section of sections!", sectionB);
    Document sectionSectionC = section("I'm a different section of sections!", sectionA);
    Document appendedParagraphsA = append(paragraphA, paragraphB);
    Document appendedParagraphsB = append(paragraphB, paragraphA);
    Document appendedParagraphsC = append(paragraphA, paragraphC);
    Document appendedSectionsAB = append(sectionSectionA, sectionSectionB);
    Document appendedSectionsBA = append(sectionSectionB, sectionSectionA);
    Document appendedSectionsC = append(sectionSectionA, sectionSectionC);

    /**
     * Tests for toString method
     * (A) Creating an Empty Document
     * (B) Creating a Paragraph Document with a/an...
     *      (B1) Empty String (B2) Non-empty string
     * (C) Creating a Section Document with a/an...
     *      (C1) Empty Document (C2) Paragraph Document (C3) Section Document (C4) Appended Document
     * (D) Creating an Appended Document with a/an...
     *      (D1) Empty Document (D2) Paragraph Document (D3) Section Document (D4) Appended Document
     */
    
    // Tests (A) Creating an Empty Document
    @Test public void testToStringEmptyDoc(){
        Document emptyDoc = empty();
        String output = emptyDoc.toString();
        assertEquals(output, "");
    }

    // Creating a Paragraph Document with an (B1) empty string
    @Test public void testToStringParagraphDocEmptyString(){
        Document paragraphDoc = paragraph("");
        String output = paragraphDoc.toString();
        assertEquals(output, "");
    }
    
    // Creating a Paragraph Document with a (B2) None-empty string
    @Test public void testToStringParagraphDoc(){
        Document paragraphDoc = paragraph("I'm a new paragraph!!");
        String output = paragraphDoc.toString();
        assertEquals(output, "I'm a new paragraph!!");
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
    
    
    // Creating an Appended Document with (D1) Empty Document (D4) Appended Documents
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
    
    // Creating an Appended Document with (D2) Paragraph Document
    @Test public void testToStringAppendParagraphs(){
        Document p1 = paragraph("Paragraph 1");
        Document p2 = paragraph("Paragraph 2");
        Document finalDoc = append(p1, p2);
        
        String output = new String(finalDoc.toString());
        String expectedAnswer = "Paragraph 1\nParagraph 2\n";
        assertEquals(expectedAnswer,output);
    }
    
  // Creating an Appended Document with (D2) Paragraph Document (D3) Section Document
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
     * Tests for the equals method
     * (A) Empty Documents
     * (B) Paragraph Documents with... 
     *      (B1) same content (B2) different content
     * (C) Section Documents
     *      (C1) same content (C2) different content
     * (D) Section of a Section Documents
     *      (D1) same content (D2) different content
     * (E) Appended Documents of...
     *      (E1) Just paragraphs with same content 
     *      (E2) Just paragraphs with different content
     *      (E3) Sections and Paragraphs with same content
     *      (E4) Sections and Paragraphs with different content
     *      (E5) Other appended documents
     * (F) Different documents
     *      (F1) Empty and Paragraph
     *      (F2) Empty and Section
     *      (F3) Empty and Appended
     *      (F4) Paragraph and Section
     *      (F5) Paragraph and Appended
     *      (F6) Section and Appended
     */
    
    // (A) Empty Documents
    @Test public void testEqualsEmptyDoc() {
        assertEquals(emptyA, emptyB);
    }
    
    // Paragraph Documents with (B1) same content
    @Test public void testEqualsParagraphSame() {
        assertEquals(paragraphA, paragraphB);
    }    
    
    // Paragraph Documents with (B2) different content
    @Test public void testEqualsParagraphDifferent() {
        assertTrue(!paragraphA.equals(paragraphC));
    }    

    // Section Documents with (C1) same content
    @Test public void testEqualsSectionSame() {
        assertTrue(sectionA.equals(sectionB));
    }  

    // Section Documents with (C2) different content
    @Test public void testEqualsSectionDifferent() {
        assertTrue(!sectionA.equals(sectionC));
    }
    
    // Section of a Section of Documents with (D1) same content
    @Test public void testEqualsSectionSectionSame() {
        assertTrue(sectionSectionA.equals(sectionSectionB));
    }  

    // Section of a Section of Documents with (D2) different content
    @Test public void testEqualsSectionSectionDifferent() {
        assertTrue(!sectionSectionA.equals(sectionSectionC));
    }
    
    // Appended documents of (E1) Just paragraphs with same content 
    @Test public void testEqualsAppendedParagraphsSame() {
        assertTrue(appendedParagraphsA.equals(appendedParagraphsB));
    }
    
    // Appended documents of (E2) Just paragraphs with different content
    @Test public void testEqualsAppendedParagraphsDifferent() {
        assertTrue(!appendedParagraphsA.equals(appendedParagraphsC));
    }
    
    // Appended documents of (E3) Sections and Paragraphs with same content
    @Test public void testEqualsAppendedSectionsSame() {
        assertTrue(appendedSectionsAB.equals(appendedSectionsAB));
    }
    
    // Appended documents of (E4) Sections and Paragraphs with different content
    @Test public void testEqualsAppendedSectionsDifferent() {
        assertTrue(!appendedSectionsAB.equals(appendedSectionsC));
    }
    
    //Different documents (F1) Empty and Paragraph
    @Test public void testEqualsEmptyAndParagraph() {
        assertTrue(!emptyA.equals(paragraphA));
    }

    //Different documents (F2) Empty and Section
    @Test public void testEqualsEmptyAndSection() {
        assertTrue(!emptyA.equals(sectionA));
    }
    
  //Different documents (F3) Empty and Appended
    @Test public void testEqualsEmptyAndAppended() {
        assertTrue(!emptyA.equals(appendedParagraphsA));
    }
    
    //Different documents (F4) Paragraph and Section
    @Test public void testEqualsParagraphAndSection() {
        assertTrue(!paragraphA.equals(sectionA));
    }

    //Different documents (F5) Paragraph and Appended
    @Test public void testEqualsParagraphAndAppended() {
        assertTrue(!paragraphA.equals(appendedParagraphsA));
    }

    //Different documents (F6) Section and Appended
    @Test public void testEqualsSectionAndAppended() {
        assertTrue(!sectionA.equals(appendedParagraphsA));
    }
    
    /**
     * Tests for the hashcode method
     * (A) Empty Documents
     * (B) Paragraph Documents with... 
     *      (B1) same content (B2) different content
     * (C) Section Documents
     *      (C1) same content (C2) different content
     * (D) Section of a Section Documents
     *      (D1) same content (D2) different content
     * (E) Appended Documents of...
     *      (E1) Just paragraphs with same content 
     *      (E2) Just paragraphs with different content
     *      (E3) Sections and Paragraphs with same content
     *      (E4) Sections and Paragraphs with different content
     *      (E5) Other appended documents
     * (F) Different documents
     *      (F1) Empty and Paragraph
     *      (F2) Empty and Section
     *      (F3) Empty and Appended
     *      (F4) Paragraph and Section
     *      (F5) Paragraph and Appended
     *      (F6) Section and Appended
     */
    
    // (A) Empty Documents
    @Test public void testHashCodeEmptyDoc() {
        assertEquals(emptyA.hashCode(), emptyB.hashCode());
    }
    
    // Paragraph Documents with (B1) same content
    @Test public void testHashCodeParagraphSame() {
        assertEquals(paragraphA.hashCode(), paragraphB.hashCode());
    }    
    
    // Paragraph Documents with (B2) different content
    @Test public void testHashCodeParagraphDifferent() {
        assertTrue(paragraphA.hashCode() != paragraphC.hashCode());
    }    

    // Section Documents with (C1) same content
    @Test public void testHashCodeSectionSame() {
        assertEquals(sectionA.hashCode(), sectionB.hashCode());
    }  

    // Section Documents with (C2) different content
    @Test public void testHashCodeSectionDifferent() {
        assertTrue(sectionA.hashCode() != sectionC.hashCode());
    }
    
    // Section of a Section of Documents with (D1) same content
    @Test public void testHashCodeSectionSectionSame() {
        assertEquals(sectionSectionA.hashCode(), sectionSectionB.hashCode());
    }  

    // Section of a Section of Documents with (D2) different content
    @Test public void testHashCodeSectionSectionDifferent() {
        assertTrue(sectionSectionA.hashCode() != sectionSectionC.hashCode());
    }
    
    // Appended documents of (E1) Just paragraphs with same content 
    @Test public void testHashCodeAppendedParagraphsSame() {
        assertEquals(appendedParagraphsA.hashCode(), appendedParagraphsB.hashCode());
    }
    
    // Appended documents of (E2) Just paragraphs with different content
    @Test public void testHashCodeAppendedParagraphsDifferent() {
        assertTrue(appendedParagraphsA.hashCode() != appendedParagraphsC.hashCode());
    }
    
    // Appended documents of (E3) Sections and Paragraphs with same content
    @Test public void testHashCodeAppendedSectionsSame() {
        assertEquals(appendedSectionsAB.hashCode(), appendedSectionsAB.hashCode());
    }
    
    // Appended documents of (E4) Sections and Paragraphs with different content
    @Test public void testHashCodeAppendedSectionsDifferent() {
        assertTrue(appendedSectionsAB.hashCode() != appendedSectionsC.hashCode());
    }
    
    //Different documents (F1) Empty and Paragraph
    @Test public void testHashCodeEmptyAndParagraph() {
        assertTrue(emptyA.hashCode() != paragraphA.hashCode());
    }

    //Different documents (F2) Empty and Section
    @Test public void testHashCodeEmptyAndSection() {
        assertTrue(emptyA.hashCode() != sectionA.hashCode());
    }
    
  //Different documents (F3) Empty and Appended
    @Test public void testHashCodeEmptyAndAppended() {
        assertTrue(emptyA.hashCode() != appendedParagraphsA.hashCode());
    }
    
    //Different documents (F4) Paragraph and Section
    @Test public void testHashCodeParagraphAndSection() {
        assertTrue(paragraphA.hashCode() != sectionA.hashCode());
    }

    //Different documents (F5) Paragraph and Appended
    @Test public void testHashCodeParagraphAndAppended() {
        assertTrue(paragraphA.hashCode() != appendedParagraphsA.hashCode());
    }

    //Different documents (F6) Section and Appended
    @Test public void testHashCodeSectionAndAppended() {
        assertTrue(sectionA.hashCode() != appendedParagraphsA.hashCode());
    }    
    
    /**
     * Tests for bodyWordCount method
     * (A) Count words in Empty Document
     * (B) Count words in a Paragraph Document
     * (C) Count words in a Section Document made with a/an...
     *      (C1) Empty Document (C2) Paragraph Document (C3) Section Document (C4) Appended Document
     * (D) Count words in an Appended Document made with a/an...
     *      (D1) Empty Document (D2) Paragraph Document (D3) Section Document (D4) Appended Document
     */
    
    // (A) Count words in Empty Document
    @Test public void testBodyWordCountEmpty() {
        Document doc = empty();
        assertEquals(0, doc.bodyWordCount());
    }
    
    // (B) Count words in a Paragraph Document
    @Test public void testBodyWordCountParagraph() {
        Document doc = paragraph("Hello, world!");
        assertEquals(2, doc.bodyWordCount());
    }
    
    // Count words in a Section Document made with an (C1) Empty Document
    @Test public void testBodyWordCountSectionEmpty() {
        Document doc = section("Section One", empty());
        assertEquals(0, doc.bodyWordCount());
    }
    
    // Count words in a Section Document made with a (C2) Paragraph Document and an (C4) Appended Docuemnt
    @Test public void testBodyWordCountSectionParagraphsAppended() {
        Document paragraphs = paragraph("Hello, world!").append(paragraph("Goodbye."));
        Document doc = section("Section One", paragraphs);
        assertEquals(3, doc.bodyWordCount());
    }
    
    // Count words in a Section Document made with a (C3) Section Document
    @Test public void testBodyWordCountSectionParagraphs() {
        Document paragraphs = paragraph("Hello, world!").append(paragraph("Goodbye."));
        Document doc = section("Section One", section("Subsection", paragraphs));
        assertEquals(3, doc.bodyWordCount());
    }
    
    // Count words in an Appended Document made with an (D1) Empty Document
    @Test public void testBodyWordCountAppendedEmpty() {
        Document AppendedEmptyDocs = append(empty(),append(empty(), empty()));
        assertEquals(0, AppendedEmptyDocs.bodyWordCount());
    }
    
    // Count words in an Appended Document made with a (D2) Paragraph Document and a (D3) Section Document
    @Test public void testBodyWordCountAppendedSectionsParagraphs() {
        Document paragraphs = append(empty(),paragraph("Hello, world!").append(paragraph("Goodbye.")));
        Document s1 = section("Section One", paragraphs);
        Document doc = append(s1, s1);
        
        assertEquals(6, doc.bodyWordCount());
    }
    
    // Count words in an Appended Document made with a (D2) Paragraph Document and a (D3) Section Document (D4) Appended Documents
    @Test public void testBodyWordCountAppendedAppendedDocuments() {
        Document paragraphs = append(empty(),paragraph("Hello, world!").append(paragraph("Goodbye.")));
        Document s1 = section("Section One", paragraphs);
        Document appended = append(s1, s1);
        Document doc = append(section("Title", paragraphs), section("contents",appended));
        
        assertEquals(9, doc.bodyWordCount());
    }
    
    /**
     * Tests for tableOfContents method for a document containing a/an...
     * (A) Empty Document
     * (B) Paragraph Document
     * (C) Section Document with a/an...
     *      (C1) Empty Document (C2) Paragraph Document (C3) Section Document (C4) Appended Document
     * (D) Appended Document with a/an...
     *      (D1) Empty Document (D2) Paragraph Document (D3) Section Document (D4) Appended Document
     */
    
    //Empty Document (A)
    @Test public void testTableOfContentsEmptyDoc(){
        String expectedAnswer = "";
        assertEquals(expectedAnswer, empty().tableOfContents().toString());
    }
    
    //Paragraph Document (B)
    @Test public void testTableOfContentsParagraphDoc(){
        String expectedAnswer = "";
        assertEquals(expectedAnswer, paragraph("I'm a paragrph").tableOfContents().toString());
    }    
    
    // (C1)
    @Test public void testTableOfContentsSectionOfEmpty(){
        String expectedAnswer = "1. Section (0 paragraphs)\n";
        Document doc = section("Section", empty());
        assertEquals(expectedAnswer, doc.tableOfContents().toString());
    }   
    
    //(C4)
    @Test public void testTableOfContentsSectionOfAppended(){
        String expectedAnswer = "1. Section (2 paragraphs)\n";
        Document doc = section("Section", append(paragraph("p1"), paragraph("p2")));
        assertEquals(expectedAnswer, doc.tableOfContents().toString());
    } 
    
    //(D1)
    @Test public void testTableOfContentsAppendedOfEmpty(){
        String expectedAnswer = "";
        Document doc = append(paragraph("p1"), empty());
        assertEquals(expectedAnswer, doc.tableOfContents().toString());
    } 
    
    //(D3)
    @Test public void testTableOfContentsAppendedOfSections(){
        String expectedAnswer = "1. Section1 (0 paragraphs)\n2. Section2 (1 paragraphs)\n";
        Document doc = append(section("Section1", empty()), section("Section2", paragraph("hellooo")));
        assertEquals(expectedAnswer, doc.tableOfContents().toString());
    } 
    
    // Appended Document with (B), (C2), (C3), (D2), (D4)
    @Test public void testTableOfContentsAppendedWithManyThings() {
        Document paragraphs = append(empty(),paragraph("Hello, world!").append(paragraph("Goodbye.")));
        Document s1 = section("Section One", paragraphs);
        Document s2 = section("Section two", paragraphs);
        Document Title = section("Title", section("HighSection",append(s1, s2)));
        Document doc = append(Title, section("Second High Section", (append(s1,s2))));
        String expectedAnswer = "1. Title (0 paragraphs)\n"
                + "    1.1. HighSection (0 paragraphs)\n"
                + "        1.1.1. Section One (2 paragraphs)\n"
                + "        1.1.2. Section two (2 paragraphs)\n"
                + "2. Second High Section (0 paragraphs)\n"
                + "    2.1. Section One (2 paragraphs)\n"
                + "    2.2. Section two (2 paragraphs)\n";

        assertEquals(expectedAnswer, doc.tableOfContents().toString());
    }
    
    /**
     * Tests for laTex method for a document containing a/an...
     * (A) Empty Document
     * (B) Paragraph Document
     * (C) Section Document with a/an...
     *      (C1) Empty Document (C2) Paragraph Document (C3) Section Document (C4) Appended Document
     * (D) Appended Document with a/an...
     *      (D1) Empty Document (D2) Paragraph Document (D3) Section Document (D4) Appended Document
     * (E) Too many subsections for laTex to handle - expected an exception thrown
     * @throws ConversionException 
     */
    
    // (A)
    @Test public void testToLatexEmptyDoc() throws ConversionException{
        String expectedAns = "\\documentclass{article}\\begin{document}\\end{document}";
        assertEquals(expectedAns, empty().toLaTeX());
    }
    
    // (B)
    @Test public void testToLatexParagraphDoc() throws ConversionException{
        String expectedAns = "\\documentclass{article}\\begin{document}I'm a paragraph\\end{document}";
        assertEquals(expectedAns, paragraph("I'm a paragraph").toLaTeX());
    }    
    
    // (C1)
    @Test public void testToLatexSectionOfEmpty() throws ConversionException{
        String expectedAns = "\\documentclass{article}\\begin{document}\\section{I'm a section}\\end{document}";
        Document doc = section("I'm a section", empty());
        assertEquals(expectedAns, doc.toLaTeX());
    }    
    // (C2)
    @Test public void testToLatexSectionSectionOfParagraph() throws ConversionException{
        String expectedAns = "\\documentclass{article}\\begin{document}\\section{I'm a section}I'm a paragraph\\end{document}";
        Document doc = section("I'm a section", paragraph("I'm a paragraph"));
        assertEquals(expectedAns, doc.toLaTeX());
    } 

    
    // (C3)
    @Test public void testToLatexSection() throws ConversionException{
        String expectedAns = "\\documentclass{article}\\begin{document}\\section{I'm a section}\\subsection{I'm a subsection}I'm a paragraph\\end{document}";
        Document doc = section("I'm a section", section("I'm a subsection", paragraph("I'm a paragraph")));
        System.out.println(doc.toLaTeX());
        assertEquals(expectedAns, doc.toLaTeX());
    } 
    
//    @Test public void testToLatexSection() throws ConversionException{
//        String expectedAns = "";
//        Document doc = 
//        System.out.println(doc.toLaTeX());
//        assertEquals(expectedAns, doc.toLaTeX());
//    } 
    
}
