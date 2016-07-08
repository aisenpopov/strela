package ru.strela.util.localization;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses i18n dictionary with includes and fills Dictionary object
 *
 * <dictionary xmlns="http://schemas.webalta.com/core/2005/01/i18n-dictionary" xmlns:xi="http://www.w3.org/2001/XInclude">
 *    <xi:include href="content/dictionary/other.xml"/>
 *    <item text="January">
 *        <localized locale="ru" >Январь</localized>
 *        <localized locale="ru" case="short">Янв</localized>
 *    </item>
 * </dictionary>
 */

class DictionaryContentHandler extends DefaultHandler {
    private final String locale;
    private final Dictionary dictionary;

    private String itemText=null;
    private String versionLocale=null;
    private String versionCase;
    private final StringBuffer versionBody=new StringBuffer();

    public DictionaryContentHandler(String locale, Dictionary dictionary) {
        this.locale=locale;
        this.dictionary=dictionary;
    }

    public void startElement(String uri, String localName, String qname, Attributes attrs) throws SAXException {
        if(itemText==null && localName.equals("dictionary")) {
        } else if(itemText==null && localName.equals("include")) {
            String name=attrs.getValue("href");
            DictionaryReader.fillDictionary(dictionary,name,locale);
        } else if(itemText==null && localName.equals("item")) {
            itemText=attrs.getValue("text");
        } else if(itemText==null) {
            throw new SAXException("Top level node must be 'include' or 'item', not '"+localName +"'");
        } else if(versionLocale==null && localName.equals("localized")) {
            versionLocale=attrs.getValue("locale");
            versionCase=attrs.getValue("case");
            versionBody.setLength(0);
        } else if(versionLocale==null) {
            throw new SAXException("'item' must have 'localized' as children, not '"+localName +"'");
        } else {
            throw new SAXException("Cannot have subnodes inside <localized> tag");
        }
    }

    public void endElement(String uri, String localName, String qname) throws SAXException {
        if(versionLocale!=null) {
            String text=versionBody.toString();
            if(locale.equals(versionLocale))
                dictionary.put(itemText,versionCase,text);
            versionLocale=null;
        } else if(itemText!=null) {
            itemText=null;
        }
    }

    public void characters(char[] buffer, int pos, int len) throws SAXException {
        if(versionLocale!=null) versionBody.append(buffer,pos,len);
    }
}
