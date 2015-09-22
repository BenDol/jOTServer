package org.jotserver.ot.model.item;

import org.apache.log4j.Logger;
import org.jotserver.ot.model.player.InventorySlot;
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

import static org.jotserver.ot.model.item.ItemAttribute.*;

public class XMLItemTypeLoader extends DefaultHandler {
    private static Logger logger = Logger.getLogger(XMLItemTypeLoader.class);

    private ItemTypeAccessor types;

    private ItemType current;
    private boolean nested;
    private int off;

    public XMLItemTypeLoader(ItemTypeAccessor types) {
        this.types = types;
        this.current = null;
        this.nested = false;
        this.off = 0;
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

    public void startDocument() throws SAXException {
        current = null;
    }

    public void startElement(String uri, String localName, String name, Attributes attr) {

        if(current == null) {
            if(name.equalsIgnoreCase("item")) {
                int id = Integer.parseInt(attr.getValue("", "id"));

                if(id > 20000 && id < 20100){
                    id = id - 20000;

                    ItemType iType = new ItemType();
                    iType.serverId = id;
                    types.setItemType(iType.getId(), iType);
                }
                current = types.getItemType(id);

                String itemName = attr.getValue("", "name");
                if(itemName != null) {
                    current.name = itemName;
                }

                String article = attr.getValue("", "article");
                if(article != null) {
                    current.article = article;
                }

                String plural = attr.getValue("", "plural");
                if(plural != null) {
                    current.pluralName = plural;
                }
            }
        } else if(!nested) {
            parseAttribute(attr.getValue("", "key"), attr.getValue("", "value"));
        } else {
            off++;
        }
    }

    private void parseAttribute(String key, String value) {
        nested = true;
        key = key.toLowerCase();

        if(key.equals("slottype")) {
            if(value.equalsIgnoreCase("body")) {
                value = "armor";
            }
            if(value.equalsIgnoreCase("two-handed")) {
                // TODO: Implement two-handed
            } else {
                try {
                    current.addSlot(InventorySlot.valueOf(value.toUpperCase()));
                } catch(IllegalArgumentException e) {
                    logger.warn("Illegal slot type: " + value + ".");
                }
            }
        } else if(key.equals("attack")) {
            current.attack = Integer.valueOf(value);
        } else if(key.equals("defense")) {
            current.defense = Integer.valueOf(value);
        } else if(key.equals("armor")) {
            current.armor = Integer.valueOf(value);
        } else if(key.equals("containersize")) {
            current.containerSize = Integer.valueOf(value);
        } else if(key.equals("clientid")) {
            current.clientId = Integer.valueOf(value);
        } else if(key.equals("article")) {
            current.article = value;
        } else if(key.equals("plural")) {
            current.pluralName = value;
        } else if(key.equals("name")) {
            current.name = value;
        } else if(key.equals("lightcolor")) {
            current.lightColor = Integer.valueOf(value);
        } else if(key.equals("lightlevel")) {
            current.lightLevel = Integer.valueOf(value);
        } else if(key.equals("alwaysontop")) {
            current.setAttribute(ALWAYSONTOP, Integer.valueOf(value) != 0);
        } else if(key.equals("alwaysontoporder")) {
            current.alwaysOnTopOrder = Integer.valueOf(value);
        } else if(key.equals("floorchangenorth")) {
            current.setAttribute(FLOORCHANGENORTH, Integer.valueOf(value) != 0);
        } else if(key.equals("floorchangesouth")) {
            current.setAttribute(FLOORCHANGESOUTH, Integer.valueOf(value) != 0);
        } else if(key.equals("floorchangeeast")) {
            current.setAttribute(FLOORCHANGEEAST, Integer.valueOf(value) != 0);
        } else if(key.equals("floorchangewest")) {
            current.setAttribute(FLOORCHANGEWEST, Integer.valueOf(value) != 0);
        } else if(key.equals("basespeed")) {
            current.baseSpeed = Integer.valueOf(value);
        } else if(key.equals("blocksolid")) {
            current.setAttribute(BLOCKSOLID, Integer.valueOf(value) != 0);
        } else if(key.equals("blockprojectile")) {
            current.setAttribute(BLOCKPROJECTILE, Integer.valueOf(value) != 0);
        } else if(key.equals("blockpathfind")) {
            current.setAttribute(BLOCKPATHFIND, Integer.valueOf(value) != 0);
        } else if(key.equals("pickupable")) {
            current.setAttribute(PICKUPABLE, Integer.valueOf(value) != 0);
        } else if(key.equals("stackable")) {
            current.setAttribute(STACKABLE, Integer.valueOf(value) != 0);
        } else if(key.equals("moveable")) {
            current.setAttribute(MOVEABLE, Integer.valueOf(value) != 0);
        } else if(key.equals("hasheight")) {
            current.setAttribute(HASHEIGHT, Integer.valueOf(value) != 0);
        } else if(key.equals("weapontype")) {
            WeaponType type = WeaponType.valueOf(value.toUpperCase());
            current.weaponType = type;
        }
    }

    public void endElement(String uri, String localName, String name) {
        if(current != null) {
            if(off > 0) {
                off--;
            } else if(nested) {
                nested = false;
            } else     if(name.equalsIgnoreCase("item")) {
                if(current.getPluralName() == null || current.getPluralName().isEmpty()) {
                    if(current.getName() != null && !current.getName().isEmpty()) {
                        current.pluralName = current.getName() + "s";
                    }
                }
                current = null;
            }
        }
    }
}