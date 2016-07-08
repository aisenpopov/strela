package ru.strela.util.localization;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Parses <i18n:text> tags and i18n:attr attributes and writes a localized version of resource.
 *
 * @author Andrey Subbotin
 */
class LocalizingHandler extends DefaultHandler {
	public static final String NAMESPACE_URI = "http://www.webalta.com/i18n";
	
    private final String locale;
    private final PrintWriter writer;

    private int tagDepth=0;
    private int namespaceDepth=0;
    private String[] namespaces=new String[20];  // up to 20 levels of i18n:dictionary declarations
    private int[] namespaceTag=new int[20];  // up to 20 levels of i18n:dictionary declarations

    private boolean isInTextTag=false;
    private String i18nCase;
    private final StringBuffer versionBody=new StringBuffer();
    private final List<String> localizableAttrs =new ArrayList<String>(10);

    public LocalizingHandler(String locale, PrintWriter writer) {
        this.locale=locale;
        this.writer=writer;
    }

    /**
     * if this in i18n:text element, enter special mode that does not allow subelements
     * and localizes text contents.
     *
     * for other nodes, check for i18n:attr attribute that contains space-separated names of
     * localizable attributes, localize those attributes and write others verbatim. Example:
     *   <img i18n:attr="alt title" src="hello.gif" alt="Hello!" title="Greeting"/>
     * is converted to
     *   <img src="hello.gif" alt="Privet" title="Pozdravlenie"/>
     *
     */
    public void startElement(String uri, String localName, String qname, Attributes attrs) throws SAXException {
        if(isInTextTag)
            throw new SAXException("'i18n:text' cannot have subelements, but it contains tag "+qname);

        if(localName.equals("text")) {
            isInTextTag=true;
            i18nCase=attrs.getValue("case");
        } else {
            if("html".equals(qname))
                writer.write("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' 'http://www.w3.org/tr/xhtml1/dtd/xhtml1-strict.dtd'>\n");

            String catalogue=attrs.getValue(NAMESPACE_URI,"catalogue");
            if(catalogue!=null) {
                if(namespaceDepth>=namespaces.length)
                    throw new SAXException("Too many i18n:catalogue levels");
                namespaceDepth++;
                namespaces[namespaceDepth]=catalogue;
                namespaceTag[namespaceDepth]=tagDepth;
            }

            localizableAttrs.clear();
            String attlist=attrs.getValue(NAMESPACE_URI,"attr");
            if(attlist!=null) {
                for(StringTokenizer tokenizer=new StringTokenizer(attlist," "); tokenizer.hasMoreTokens(); ) {
                    String a=tokenizer.nextToken();
                    localizableAttrs.add(a);
                }
            }

            writer.append('<');
            writeText(qname);
            if("html".equals(qname))
                writeAttribute("xmlns","http://www.w3.org/1999/xhtml");
            for(int i=0; i<attrs.getLength(); i++) {
                String name=attrs.getLocalName(i);
                String ns=attrs.getQName(i);
                if(localizableAttrs.contains(name)) {
                    String localized=localize(attrs.getValue(i),null);
                    writeAttribute(ns,localized);
                } else if(!NAMESPACE_URI.equals(attrs.getURI(i))) {
                    writeAttribute(ns,attrs.getValue(i));
                }
            }
            if(isEmptyTag(localName)) {
                writer.append("/>");
            } else {
                writer.append('>');
            }
        }
        tagDepth++;
    }

    public void endElement(String uri, String localName, String qname) throws SAXException {
        if(isInTextTag) {
            String text=versionBody.toString();
            String localized=localize(text,i18nCase);
            writeBodyText(localized);
            isInTextTag=false;
            versionBody.setLength(0);
        } else if(!isEmptyTag(localName)) {
            writer.append("</");
            writeText(qname);
            writer.append('>');
        }
        if(namespaceDepth>=0 && namespaceTag[namespaceDepth]==tagDepth) namespaceDepth--;
        tagDepth--;
    }

    public void characters(char[] buffer, int pos, int len) throws SAXException {
        if(isInTextTag) {
            versionBody.append(buffer,pos,len);
        } else {
            writeBodyText(new String(buffer,pos,len));
        }
    }

    private String localize(String value, String valueCase) throws SAXException {
        if(namespaceDepth<0)
            throw new SAXException("i18n:text or i18n:attr must be preceded by i18n:catalogue");
        return DictionaryReader.localize(namespaces[namespaceDepth],locale,value,valueCase);
    }

    private void writeAttribute(String name, String value) {
        writer.append(' ');
        writeText(name);
        writer.append("=\"");
        writeText(value);
        writer.append("\"");
    }

    private void writeText(String value) {
        XmlWriter.printEncoded(value,writer);
    }

    private void writeBodyText(String value) {
        XmlWriter.printEncodedBody(value,writer);
    }

    private boolean isEmptyTag(String tag) {
        return "img".equals(tag) || "br".equals(tag) || "input".equals(tag);
    }

}
