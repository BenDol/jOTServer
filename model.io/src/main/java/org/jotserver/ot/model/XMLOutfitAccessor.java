package org.jotserver.ot.model;

import org.jotserver.io.TextInput;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class XMLOutfitAccessor extends DefaultHandler implements OutfitAccessor {
	
	private Map<Integer, OutfitType> outfits;
	
	public XMLOutfitAccessor() {
		outfits = new HashMap<Integer, OutfitType>();
	}
	
	public void loadFromXML(String file) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		loadFromStream(in);
		in.close();
	}
	
	public void loadFromStream(InputStream in) throws IOException {
		try {
			SAXParser p = SAXParserFactory.newInstance().newSAXParser();
			p.parse(in, this);
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}

	public void startElement(String uri, String localName, String elementName,
			Attributes attributes) throws SAXException {
		
		if(elementName.equalsIgnoreCase("outfit")) {
			String name = attributes.getValue("", "name");
			int looktype = Integer.parseInt(attributes.getValue("", "looktype"));
			boolean enabled = TextInput.getBoolean(attributes.getValue("", "enabled"));
			
			int outfitType = TextInput.getInt(attributes.getValue("", "type"), 0);
			
			OutfitType.Type type = OutfitType.Type.values()[outfitType];
			
			outfits.put(looktype, new OutfitType(name, type, looktype, enabled));
		}
		
	}

	public Collection<OutfitType> getOutfits() {
		return Collections.unmodifiableCollection(outfits.values());
	}
	
	public OutfitType getOutfit(int look) {
		return outfits.get(look);
	}
}
