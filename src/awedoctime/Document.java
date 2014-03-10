package awedoctime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An immutable structured document.
 * 
 * <p>
 * Documents consist of paragraphs and sections. The top-level of a document
 * may have any number of paragraphs or sections. Paragraphs must always belong
 * to the section whose heading most closely precedes them in the document.
 * </p>
 * 
 * This is a required ADT interface.
 * You MUST NOT weaken the provided specifications.
 * You may strengthen the provided specifications, and you may also add
 * additional methods, but you must not expose your rep when you write your
 * strengthen specs or add new specs.
 */
public interface Document {
    
    /**
     * Returns a document which has the contents of this followed by the
     * contents of other. Top-level sections in other become top-level sections
     * in the new document.
     * @param other document to append
     * @return concatenation of this and other
     */
    public Document append(Document other);
    
    /**
     * Returns the number of words in the paragraphs of this document. Words
     * are delimited by spaces and by the beginnings and ends of paragraphs.
     * @return body word count
     */
    public int bodyWordCount();
    /**
     * Helper function for bodyWordCount.  Helps get the word count of nested sections
     * within other sections. 
     * @return the word count in a specified section
     */
    public int wordCountNested(String id);
    
    /**
     * Returns a document containing one paragraph for every section heading in
     * this document. Each paragraph contains:
     * <br> - the section number, written as a sequence of parent section
     *        numbers separated by periods, ending with the position of this
     *        section under its parent (or under the top level, if none)
     * <br> - the section heading
     * <br> - the number of paragraphs in this section, written as
     *        "1 paragraph", or "N paragraphs" for N != 1
     * <br> For an example, see the problem set handout.
     * @return table of contents
     */
    public Document tableOfContents();
    public Document nestedHeaders(String id, ArrayList<Integer> nested);
    /**
     * Returns a LaTeX representation of the document that:
     * <br> - contains a preamble with document class "article" and no other
     *        options or packages
     * <br> - contains all the section headings and paragraphs of the document
     *        using appropriate LaTeX syntax and character escaping
     * <br> For an example, see the problem set handout.
     * @return LaTeX conversion
     * @throws ConversionException if the document cannot be converted
     */
    public String toLaTeX() throws ConversionException;
    public String nestedLatexSections(String nestedID, int nested);
    
    /**
     * Returns a Markdown representation of the document that:
     * <br> - contains all the section headings and paragraphs of the document
     *        using appropriate Markdown syntax and character escaping
     * <br> For an example, see the problem set handout.
     * @return Markdown conversion
     * @throws ConversionException if the document cannot be converted
     */
    public String toMarkdown() throws ConversionException;
    public String getNestedMarkDownSections(String id, int hashtags);
    
    /**
     * Returns a concise String representation of the document.
     */
    @Override public String toString();
    public String getNestedSections(String ID, int indent);
    
    /**
     * Indicates that a document conversion could not be completed.
     */
    public static class ConversionException extends Exception {
        
        private static final long serialVersionUID = 1L;
        
        /**
         * Constructs a ConversionException with a detail message.
         * @param message description of the error
         */
        public ConversionException(String message) {
            super(message);
        }
        
        /**
         * Constructs a ConversionException with a detail message and cause.
         * @param message description of the error
         * @param cause exception that caused this error
         */
        public ConversionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Gets the document's content HashMap
     * @return a new HashMap that is identical (equivalent keys and values) to the content HashMap
     */
    public HashMap<String, String> getContentMap();
    
    /**
     * Gets the document's structure HashMap
     * @return a new HashMap that is identical (equivalent keys and values) to the structure HashMap
     */
    public HashMap<String, ArrayList<String>> getStructureMap();
    
    /**
     * Gets the document's body ArrayList
     * @return a new ArrayList that is identical (equivalent strings) to the body ArrayList
     */
    public ArrayList<String> getBodyArray();
    
    public String toMarkdownBullets();
    public String getNestedMarkdownBulletSections(String id, int tabs);
}
