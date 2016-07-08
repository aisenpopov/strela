package ru.strela.util.localization;

import org.apache.commons.lang.StringUtils;

import java.io.PrintWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Writer having special methods for printing XML tags.
 */
public class XmlWriter extends PrintWriter {
    private final boolean writeOffset;

    /**
     * Format ued to print floating point numbers throughout the system.
     */
    public static final DecimalFormat doubleFormat = new DecimalFormat("#.##############################",new DecimalFormatSymbols(Locale.US));
    public static final DecimalFormat s_DF = new DecimalFormat("####################.######",new DecimalFormatSymbols(Locale.US));

    public static String formatDouble(double d) {
        if(Double.isNaN(d)) return "NaN";
        return XmlWriter.doubleFormat.format(d);
    }


    public static String formatObject(Object p_obj) {
        if(p_obj == null) {
            return "";
        } else if(p_obj instanceof Double) {
            double d = (Double)p_obj;
            if(Double.isNaN(d)) return "NaN";
            return XmlWriter.s_DF.format(d);
        }
        return p_obj.toString();
    }

    protected int o_offset=0;

    public XmlWriter(Writer p_writer) { this(p_writer, true); }
    public XmlWriter(Writer p_writer, boolean writeOffset) {
        super(p_writer, true);
        this.writeOffset = writeOffset;
    }

    public static String getEncodedChar(char p_char) {
        if(p_char == '&') {
            return "&amp;";
        } else if (p_char == '>') {
            return "&gt;";
        } else if (p_char == '<') {
            return "&lt;";
        } else if (p_char == '"') {
            return "&quot;";
        } else if (p_char < ' ' && p_char != '\n' && p_char != '\r' && p_char != '\t') {
            return " ";
        }
        return null;
    }

    protected void writeOffset() {
        if (writeOffset) {
            for (int i = 0; i <o_offset; i++) {
                print("    ");
            }
        }
    }

    private void writeln(char c) {
        if (writeOffset) println(c);
        else print(c);
    }

    private void writeln(String s) {
        if (writeOffset) println(s);
        else print(s);
    }

    public int getOffset() {
        return o_offset;
    }

    protected void setOffset(int p_offset) {
        o_offset = p_offset;
    }

    /**
     * Opens XML tag.
     * @param p_name Tag name to open
     */
    public void openTag(String p_name) {
        writeOffset();
        o_offset++;

        write('<');
        write(p_name);
        writeln('>');
    }

    public void openTagBracket(String p_name) {
        writeOffset();
        o_offset++;

        write('<');
        write(p_name);
    }

    public void closeTagBracket(boolean p_isEmptyTag) {
        if(p_isEmptyTag) {
            o_offset--;
            writeln("/>");
        } else {
            writeln('>');
        }
    }

    public void printTagAttribute(String p_atrName, Object p_atrValue) {
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
    }

    public void printTagAttribute(String p_atrName, double p_atrValue) {
        write(' ');
        write(p_atrName);
        write("=\"");
        printNumeric(XmlWriter.formatDouble(p_atrValue));
        write("\" ");
    }

    public void printTagAttribute(String p_atrName, boolean p_atrValue) {
        write(' ');
        write(p_atrName);
        write("=\"");
        print(p_atrValue?"true":"false");
        write("\" ");
    }

    // work around "-0" problem by discarding '-' sign if it is not followed by any nonzero
    // digits. It should handle "-0,000.00" type of situations.
    private void printNumeric(String p_formattedNumber) {
        if(p_formattedNumber.charAt(0)=='-') {
            boolean hasNonZeroDigits=false;
            for(int i=1;i<p_formattedNumber.length();i++) {
                char ch=p_formattedNumber.charAt(i);
                hasNonZeroDigits |=(ch>='1' && ch<='9');
            }
            if(!hasNonZeroDigits) p_formattedNumber=p_formattedNumber.substring(1);
        }
        print(p_formattedNumber);
    }

    /**
     * Opens XML tag with attribute. Child tags will be written with the same offset as this tag.
     */
    public void openTagInline(String p_name,String p_atrName,Object p_atrValue) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject( p_atrValue ));
        print("\">");
    }

    public void openTagInline(String p_name,String[] p_attrs,Object[] p_values) {
        writeOffset();
        write('<');
        write(p_name);

        for (int i = 0; i < p_attrs.length; i++) {
            write(' ');
            write(p_attrs[i]);
            write("=\"");
            printEncoded(XmlWriter.formatObject( p_values[i] ));
            print("\"");
        }

        print(">");
    }

    /**
     * Opens XML tag with attribute.
     */
    public void openTag(String p_name,String p_atrName,Object p_atrValue) {
        writeOffset();
        o_offset++;

        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        writeln("\">");
    }

    /**
     * Opens XML tag with 2 attributes. Child tags will be written with the same offset as this tag.
     */
    public void openTagInline(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        print("\">");
    }
    
    /**
     * Opens XML tag with 2 attributes. Child tags will be written with the same offset as this tag.
     */
    public void openTagInline(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2,String p_atrName3,Object p_atrValue3) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        write("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue3));
        print("\">");
    }

    /**
     * Opens XML tag with 2 attributes.
     */
    public void openTag(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2) {
        writeOffset();
        o_offset++;

        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        writeln("\">");
    }

    /**
     * Opens XML tag with 3 attributes.
     */
    public void openTag(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2,String p_atrName3,Object p_atrValue3) {
        writeOffset();
        o_offset++;

        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue3));
        writeln("\">");
    }

    /**
     * Opens XML tag with 4 attributes.
     */
    public void openTag(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2,
                        String p_atrName3,Object p_atrValue3,String p_atrName4,Object p_atrValue4) {
        writeOffset();
        o_offset++;

        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue3));
        write("\" ");
        write(p_atrName4);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue4));
        writeln("\">");
    }

    /**
     * Opens XML tag with 5 attributes.
     */
    public void openTag(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2,
                        String p_atrName3,Object p_atrValue3,String p_atrName4,Object p_atrValue4,
                        String p_atrName5,Object p_atrValue5) {
        writeOffset();
        o_offset++;

        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue3));
        write("\" ");
        write(p_atrName4);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue4));
        write("\" ");
        write(p_atrName5);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue5));
        writeln("\">");
    }

    /**
     * Opens XML tag with 7 attributes.
     */
    public void openTag(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2,
                        String p_atrName3,Object p_atrValue3,String p_atrName4,Object p_atrValue4,
                        String p_atrName5,Object p_atrValue5,String p_atrName6,Object p_atrValue6,
                        String p_atrName7,Object p_atrValue7) {
        writeOffset();
        o_offset++;

        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue3));
        write("\" ");
        write(p_atrName4);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue4));
        write("\" ");
        write(p_atrName5);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue5));
        write("\" ");
        write(p_atrName6);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue6));
        write("\" ");
        write(p_atrName7);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue7));
        writeln("\">");

    }

    /**
     * Opens XML tag with 8 attributes.
     */
    public void openTag(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2,
                        String p_atrName3,Object p_atrValue3,String p_atrName4,Object p_atrValue4,
                        String p_atrName5,Object p_atrValue5,String p_atrName6,Object p_atrValue6,
                        String p_atrName7,Object p_atrValue7, String p_atrName8,Object p_atrValue8) {
        writeOffset();
        o_offset++;

        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue3));
        write("\" ");
        write(p_atrName4);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue4));
        write("\" ");
        write(p_atrName5);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue5));
        write("\" ");
        write(p_atrName6);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue6));
        write("\" ");
        write(p_atrName7);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue7));
        write("\" ");
        write(p_atrName8);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue8));
        writeln("\">");

    }

    /**
     * Opens XML tag with 9 attributes.
     */
    public void openTag(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2,
                        String p_atrName3,Object p_atrValue3,String p_atrName4,Object p_atrValue4,
                        String p_atrName5,Object p_atrValue5,String p_atrName6,Object p_atrValue6,
                        String p_atrName7,Object p_atrValue7, String p_atrName8,Object p_atrValue8,
                        String p_atrName9,Object p_atrValue9) {
        writeOffset();
        o_offset++;

        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue3));
        write("\" ");
        write(p_atrName4);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue4));
        write("\" ");
        write(p_atrName5);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue5));
        write("\" ");
        write(p_atrName6);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue6));
        write("\" ");
        write(p_atrName7);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue7));
        write("\" ");
        write(p_atrName8);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue8));
        write("\" ");
        write(p_atrName9);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue9));
        writeln("\">");

    }

    /**
     * Closes XML tag. Surrouding context tags will have the same indent as contents of this tag.
     * @param p_name Name of the tag to close
     */
    public void closeTagInline(String p_name) {
        write("</");
        write(p_name);
        writeln('>');
    }

    /**
     * Closes XML tag.
     * @param p_name Name of the tag to close
     */
    public void closeTag(String p_name) {
        o_offset--;
        writeOffset();
        write("</");
        write(p_name);
        writeln('>');
    }

    /**
     * Prints an XML tag. Tag contents is encoded according to XML rules
     * @param p_name Name of the tag to print
     * @param p_contents Contents of the tag to print
     */
    public void printTag(String p_name,String p_contents) {
        writeOffset();
        write('<'); // we do not use openTag() to avoid CR
        write(p_name);
        write('>');
        printEncoded(p_contents);
        write("</");
        write(p_name);
        writeln('>');
    }

    /**
     * Prints an XML tag. Tag contents is encoded according to XML rules
     * @param p_name Name of the tag to print
     */
    public void printTag(String p_name) {
        writeOffset();
        writeln("<"+p_name+"/>"); // we do not use openTag() to avoid CR
    }

    /**
     * Prints an XML tag.
     * @param p_name Name of the tag to print
     * @param p_contents Contents of the tag to print
     */
    public void printTag(String p_name,Object p_contents) {
        writeOffset();
        write('<'); // we do not use openTag() to avoid CR
        write(p_name);
        write('>');
        printEncoded(XmlWriter.formatObject(p_contents));
        write("</");
        write(p_name);
        writeln('>');
    }

    /**
     * Prints an XML tag.
     * @param p_name Name of the tag to print
     * @param p_contents Contents of the tag to print
     */
    public void printTag(String p_name,double p_contents) {
        writeOffset();
        write('<'); // we do not use openTag() to avoid CR
        write(p_name);
        write('>');
        print(XmlWriter.doubleFormat.format(p_contents));
        write("</");
        write(p_name);
        writeln('>');
    }
    public void printTag(String p_name,double p_contents, Object...attrs) {
        writeOffset();
        write('<'); // we do not use openTag() to avoid CR
        write(p_name);
        for (int i=0; i<attrs.length; i+=2){
        	write(' ');
        	write(attrs[i].toString());
        	write("=\"");
        	write(attrs[i+1].toString());
        	write('"');
        }
        write('>');
        print(XmlWriter.doubleFormat.format(p_contents));
        write("</");
        write(p_name);
        writeln('>');
    }

    /**
     * Prints an XML tag.
     * @param p_name Name of the tag to print
     * @param p_contents Contents of the tag to print
     */
    public void printTag(String p_name,boolean p_contents) {
        printTag(p_name,p_contents?"true":"false");
    }

    /**
     * Prints an empty XML tag with attribute.
     */
    public void printEmptyTag(String p_name,String p_atrName,Object p_atrValue) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        writeln("\"/>");
    }

    /**
     * Prints an empty XML tag with attribute.
     */
    public void printEmptyTag(String p_name,String p_atrName,double p_atrValue) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        print(p_atrValue);
        writeln("\"/>");
    }

    /**
     * Prints an empty XML tag with 2 attributes.
     */
    public void printEmptyTag(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        writeln("\"/>");
    }

    /**
     * Prints an empty XML tag with 3 attributes.
     */
    public void printEmptyTag(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2,String p_atrName3,Object p_atrValue3) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue3));
        writeln("\"/>");
    }

    /**
     * Prints an empty XML tag with 4 attributes.
     */
    public void printEmptyTag(String p_name,String p_atrName,Object p_atrValue,String p_atrName2,Object p_atrValue2,String p_atrName3,Object p_atrValue3,String p_atrName4,Object p_atrValue4) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue3));
        print("\" ");
        write(p_atrName4);
        write("=\"");
        printEncoded(XmlWriter.formatObject(p_atrValue4));
        writeln("\"/>");
    }

    public void printEmptyTag(String p_name, String p_atrName, Object p_atrValue, String p_atrName2, Object p_atrValue2, String p_atrName3, Object p_atrValue3, String p_atrName4, Object p_atrValue4, String p_atrName5, Object p_atrValue5) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(formatObject(p_atrValue3));
        print("\" ");
        write(p_atrName4);
        write("=\"");
        printEncoded(formatObject(p_atrValue4));
        print("\" ");
        write(p_atrName5);
        write("=\"");
        printEncoded(formatObject(p_atrValue5));
        writeln("\"/>");
    }

    /**
     * Prints an empty XML tag with 6 attributes.
     */
    public void printEmptyTag(String p_name,
                              String p_atrName, Object p_atrValue,
                              String p_atrName2, Object p_atrValue2,
                              String p_atrName3, Object p_atrValue3,
                              String p_atrName4, Object p_atrValue4,
                              String p_atrName5, Object p_atrValue5,
                              String p_atrName6, Object p_atrValue6) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(formatObject(p_atrValue3));
        print("\" ");
        write(p_atrName4);
        write("=\"");
        printEncoded(formatObject(p_atrValue4));
        print("\" ");
        write(p_atrName5);
        write("=\"");
        printEncoded(formatObject(p_atrValue5));
        print("\" ");
        write(p_atrName6);
        write("=\"");
        printEncoded(formatObject(p_atrValue6));
        writeln("\"/>");
    }

    public void printEmptyTag(String p_name,
                              String p_atrName, Object p_atrValue,
                              String p_atrName2, Object p_atrValue2,
                              String p_atrName3, Object p_atrValue3,
                              String p_atrName4, Object p_atrValue4,
                              String p_atrName5, Object p_atrValue5,
                              String p_atrName6, Object p_atrValue6,
                              String p_atrName7, Object p_atrValue7) {
        writeOffset();
        write('<');
        write(p_name);
        write(' ');
        write(p_atrName);
        write("=\"");
        printEncoded(formatObject(p_atrValue));
        write("\" ");
        write(p_atrName2);
        write("=\"");
        printEncoded(formatObject(p_atrValue2));
        print("\" ");
        write(p_atrName3);
        write("=\"");
        printEncoded(formatObject(p_atrValue3));
        print("\" ");
        write(p_atrName4);
        write("=\"");
        printEncoded(formatObject(p_atrValue4));
        print("\" ");
        write(p_atrName5);
        write("=\"");
        printEncoded(formatObject(p_atrValue5));
        print("\" ");

        write(p_atrName6);
        write("=\"");
        printEncoded(formatObject(p_atrValue6));
        print("\" ");

        write(p_atrName7);
        write("=\"");
        printEncoded(formatObject(p_atrValue7));
        writeln("\"/>");
    }

    /**
     * Prints an empty XML tag with arbitrary number of attributes.
     * @param p_name Name of the tag to print
     * @param p_names Array of attribute names
     * @param p_values Array of attribute values
     */
    public void printEmptyTag(String p_name, String[] p_names, Object[] p_values) {
        if (p_names == null || p_values == null || p_names.length != p_values.length) {
            throw new IllegalArgumentException("Invalid arguments in printEmptyTag(String, String[], Object[]) call");
        }

        // Print tag name
        writeOffset();
        write('<');
        write(p_name);

        // Print attributes
        for (int i = 0; i < p_names.length; i++) {
            write(' ');
            write(p_names[i]);
            write("=\"");
            printEncoded(XmlWriter.formatObject(p_values[i]));
            write("\"");
        }

        // Terminate tag
        writeln("/>");
    }

    /**
     * Write a string with chars like <>& escaped according to XML rules.
     * @param str String to encode
     */
    public void printEncoded(String str) {
        if(str ==null) return;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String encoded = getEncodedChar(ch);
            if (encoded == null) {
                buf.append(ch);
            } else {
                buf.append(encoded);
            }
        }
        print(buf);
    }

    public static CharSequence encode(String str) {
        if(str ==null) return "";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String encoded = getEncodedChar(ch);
            if (encoded == null) {
                buf.append(ch);
            } else {
                buf.append(encoded);
            }
        }
        return buf;
    }

    public static void printEncoded(String str, PrintWriter writer) {
        if(str ==null) return;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String encoded = getEncodedChar(ch);
            if (encoded == null) {
                writer.append(ch);
            } else {
                writer.append(encoded);
            }
        }
    }

    
    
    
    public void openJsHash() {
        openJsHash(null);
    }
    
    public void openJsHash(String name) {
        if(!StringUtils.isEmpty(name))
            print("\"" + name + "\": {\n");
        else
            print("{\n");
    }
    
    public void closeJsHash() {
        print("}");
    }
    
    public void printJsPair(String key, Object value) {
        printJsPair(key, value, true);
    }
    
    public void printJsPair(String key, Object value, boolean withComa) {
        printJsPair(key, value, withComa, true);
    }

    public void printJsPair(String key, Object value, boolean withComa, boolean withoutQoutes) {
        print("\"" + key + "\": " + (withoutQoutes ? "\"" : "") + toJsString(value) + (withoutQoutes ? "\"" : "") + (withComa ? ",\n" : "\n"));
    }

    private final static String INVALID =   // see http://www.w3.org/TR/xml11/#charsets
        "\\x00-\\x08\\x0B-\\x0C\\x0E-\\x1F\\x7F-\\x84\\x86-\\x9F";
    private final static String BACKSLASH = "\\\\";
    private final static String SLASH_R = "\r";
    private final static String SLASH_N = "\n";
    private final static String QUOTE = "\"";
    private final static String PARAGRAPH = "\u2029";
    private final static Pattern pattern = Pattern.compile("[" + SLASH_R + SLASH_N + QUOTE + BACKSLASH + PARAGRAPH + INVALID + "]");
    
    public static String toJsString(Object o) {
            
        if(o == null)
            return "";
        Matcher m = pattern.matcher(o.toString());
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String g = m.group();
            if(QUOTE.equals(g)) m.appendReplacement(sb, BACKSLASH + "\"");
            else if("\\".equals(g)) m.appendReplacement(sb,  BACKSLASH + BACKSLASH);
            else if(SLASH_N.equals(g) || PARAGRAPH.equals(g)) m.appendReplacement(sb, BACKSLASH + "n");
            // else if(SLASH_R.equals(g)) m.appendReplacement(sb, "");
            else m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }
    

    public static void printEncodedBody(String str, PrintWriter writer) {
        if(str ==null) return;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String encoded = ch=='\"' ? null:ch==0xa0?"&#xA0;":getEncodedChar(ch);
            if (encoded == null) {
                writer.append(ch);
            } else {
                writer.append(encoded);
            }
        }
    }
    
    public void openCDATA(){
    	write("<![CDATA[");
    }
    
    public void closeCDATA(){
    	write("]]>");
    }

	public void writePreamble() {
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	}
}
