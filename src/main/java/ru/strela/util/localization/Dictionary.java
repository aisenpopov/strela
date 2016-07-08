package ru.strela.util.localization;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Describes the mapping from key strings to localized strings in a
 * particular locale.
 * <p/>
 * For each key, we hold a default localized string, and possibly
 * other string for special cases such as "genitive" or "short". When
 * requesting localization, you can supply the case.
 */

public class Dictionary {
	/**
	 * Map from (key, case) pairs to localized Strings.
	 */
	private final Map<KeyCase, String> translations = new HashMap<KeyCase, String>();

	void put(String key, String keyCase, String value) {
		translations.put(new KeyCase(key, keyCase), value); //resolve(value));
	}

	public String localize(CharSequence key, String keyCase) {
		String s = key.toString().trim();
		String localized = translations.get(new KeyCase(s, keyCase));
		if (localized == null && keyCase != null) localized = translations.get(new KeyCase(s, null));
		return (localized != null) ? localized : s;
	}

	private static class KeyCase {
		private final String key;
		private final String keyCase;

		private KeyCase(String key, String keyCase) {
			this.key = key;
			this.keyCase = keyCase;
		}

		public int hashCode() {
			return key.hashCode() + (keyCase != null ? keyCase.hashCode() : 0);
		}

		public boolean equals(Object other) {
			if (!(other instanceof KeyCase))
				return false;

			KeyCase o = (KeyCase) other;
			return key.equals(o.key) && ((keyCase != null && keyCase.equals(o.keyCase)) || (keyCase == null && o.keyCase == null));
		}

		public String toString() {
			return key + '(' + keyCase + ')';
		}
	}

	public Dictionary() {}

	public Dictionary(String name, String locale) {
		init(name, locale);
	}

	public void init(String name, String locale) {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new DictionaryContentHandler(locale, this));
			InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
			InputSource in = new InputSource(stream);
			in.setSystemId("resource://" + name);
			reader.parse(in);
			in.getByteStream().close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
