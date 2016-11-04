package dom4j.customer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dom4j.util.XmlFactory;

//dom4j.jar jaxen.jar
public class WriteCustomer {
	
	public static void writeCustXML(List customers, String fileName)throws IOException{
		XmlFactory.write(writeCust(customers), fileName);
	}
	
	public static Document writeCust(List customers){
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("cust-root");
		for(Iterator it = customers.iterator(); it.hasNext();){
			Customer cust = (Customer)it.next();
			Element customer = root.addElement("customer");
			if(cust.getCustId()!=null) customer.addAttribute("cust-id", cust.getCustId());
			customer.addComment("这里是注释");
			if(cust.getPerson()!=null){
				Element person = customer.addElement("person");
				String custNum = cust.getPerson().getCustomerNumber()!=0?cust.getPerson().getCustomerNumber()+"":"";
				person.addElement("cust-num").addText(custNum);

				String firstName = cust.getPerson().getFirstName()!=null?cust.getPerson().getFirstName():"";
				person.addElement("first-name").addText(firstName);
				
				String lastName = cust.getPerson().getLastName()!=null?cust.getPerson().getLastName():"";
				person.addElement("last-name").addText(lastName);
			}
			String street = cust.getStreet()!=null?cust.getStreet():"";
			customer.addElement("street").addText(street);
			
			String city = cust.getCity()!=null?cust.getCity():"";
			customer.addElement("city").addText(city);
			
			String state = cust.getState()!=null?cust.getState():"";
			customer.addElement("state").addText(state);
			
			String zip = cust.getZip()!=null?cust.getZip()+"":"";
			customer.addElement("zip").addText(zip);
			
			String phone = cust.getPhone()!=null?cust.getPhone():"";
			customer.addElement("phone").addText(phone);
		}
		return doc;
	}
	
	public void updateCustXML(String srcFileName, String targetFileName)
		throws MalformedURLException, DocumentException, IOException{
		Document doc = XmlFactory.getInstance().read(srcFileName);
		List custIds = doc.selectNodes("/cust-root/customer/@cust-id");
		for(Iterator it = custIds.iterator(); it.hasNext();){
			Attribute ab = (Attribute)it.next();
			int custId = Integer.parseInt(ab.getValue().trim());
			ab.setValue(custId+10+"");
		}
		List persons = doc.selectNodes("/cust-root/customer/person");
		for(Iterator it = persons.iterator(); it.hasNext();){
			Element person = (Element)it.next();
			person.addElement("date").setText("2007-09-30");
			person.addAttribute("sex", "Male");
		}
		List lastNames = doc.selectNodes("/cust-root/customer/person/last-name");
		for(Iterator it = lastNames.iterator(); it.hasNext();){
			Element lastName = (Element)it.next();
			Element parent = lastName.getParent();
			parent.remove(lastName);
		}
		XmlFactory.write(doc, targetFileName);
	}

}
