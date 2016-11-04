package dom4j.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlFactory {
	
	private SAXReader reader;
	
	private XmlFactory(){
		reader = new SAXReader();
	}
	
	private static XmlFactory xf;
	
	public static XmlFactory getInstance(){
		if(xf==null){
			xf = new XmlFactory();
		}
		return xf;
	}
	
	public Document read(String fileName)throws MalformedURLException, DocumentException{
		return reader.read(new File(fileName));
	}
	
	public static void write(Document doc, String fileName)throws IOException{
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("GBK");
		XMLWriter writer = null;
		try{
			writer= new XMLWriter(new FileOutputStream(fileName), format);
			writer.write(doc);
		}catch(IOException e){
			throw e;
		}finally{
			if(writer!=null){
				try{writer.close();}catch(IOException e){}
			}
		}
	}

}
