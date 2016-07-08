package ru.strela.util.localization;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import ru.strela.util.SystemException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class DictionaryReader {
    private static long lastChangeTimestamp=System.currentTimeMillis();
    private static final Map<String,Dictionary> dictionary = new HashMap<String, Dictionary>();

	private static final String DICT_NAME = "dict.xml";
	
    static Dictionary getDictionary(String name, String locale) {
        String key = name + ":" + locale;
        
        if(!dictionary.containsKey(key)) {
            Dictionary d=new Dictionary();
            fillDictionary(d,name,locale);
            dictionary.put(key, d);
        }

        return dictionary.get(key);
    }

    public static long lastChangeTimestamp() {
        return lastChangeTimestamp;
    }

    public static String localize(String dictionary, String text, String locale ) {
        return localize(dictionary,	locale, text, null);
    }
    
    public static String localize(String dictionary, String locale, String text, String textCase) {
        return getDictionary(dictionary, locale).localize(text, textCase);
    }

    static void fillDictionary(Dictionary dictionary, String name, String locale) {
        try {
            XMLReader reader= XMLReaderFactory.createXMLReader();
            reader.setContentHandler(new DictionaryContentHandler(locale,dictionary));

            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            InputSource in = new InputSource(stream);
            in.setSystemId("resource://" + name);
            reader.parse(in);
            in.getByteStream().close();
        } catch(SAXException ex) {
            throw new SystemException(ex);
        } catch(IOException ex) {
            throw new SystemException(ex);
        }
    }

}
