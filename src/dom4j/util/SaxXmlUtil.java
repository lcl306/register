package dom4j.util;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

public class SaxXmlUtil {
	
	public static Element getRootElement(Document doc){
		return doc.getRootElement();
	}
	
	public static List<Element> getElementsByName(List<Element> elements, Element top, String name){
		if(top.getName().equals(name)){
			elements.add(top);
		}
		for(Iterator it = top.elementIterator(); it.hasNext();){
			Element subEle = (Element)it.next();
			getElementsByName(elements, subEle, name);
		}
		return elements;
	}

}
