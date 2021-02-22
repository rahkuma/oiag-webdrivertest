package com.tibco.automation.oiag.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtil {
	
	private String filepath;
	private String parentNode;
	private String[] names;
	
	public XMLUtil(String filepath, String parentNode, String... names) {
		this.filepath = filepath;
		this.parentNode = parentNode;
		this.names = names;
	}
	
	private Document read() {
        File inputXML = new File( filepath );
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read( inputXML );
        }
        catch ( DocumentException e ) {
            e.printStackTrace();
        }
        return document;
	}
	
	private HashMap<Integer, List<String>> getProperties() {
		HashMap<Integer, List<String>> properties = new HashMap<>();
		Document CSDoc = this.read();
		Element root = CSDoc.getRootElement();
		List<?> nodes = root.elements(parentNode);
		int index = 1;
		for (Object node : nodes) {
			List<String> list = new ArrayList<>();
			for (String name : names) {
				list.add(((Element) node).elementText(name));
			}
			properties.put(index, list);
			index ++;
		}
		return properties;
	}
	
	public boolean query(String... values) {
		HashMap<Integer, List<String>> map = this.getProperties();
		for (List<String> list : map.values()) {
			if (Arrays.asList(values).stream().filter(list:: contains).count() == values.length)
				return true;
		}
		return false;
	}
}
