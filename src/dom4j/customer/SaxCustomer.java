package dom4j.customer;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;

import dom4j.util.SaxXmlUtil;
import dom4j.util.XmlFactory;

public class SaxCustomer extends VisitorSupport{
	
	public List<Customer> custs = new ArrayList<Customer>();
	
	public List<Customer> getCustomers(String fileName)throws MalformedURLException, DocumentException{
		List<Customer> customers = new ArrayList<Customer>();
		Document doc = XmlFactory.getInstance().read(fileName);
		Element root = SaxXmlUtil.getRootElement(doc);
		
		for(Iterator it = root.elementIterator(); it.hasNext();){
			Element element = (Element)it.next();
			if (element.getName().equals("customer")) {
				Customer cust = new Customer();
				Attribute cust_id = element.attribute("cust-id");
				if(cust_id!=null){
					cust.setCustId(cust_id.getValue());
				}
				for(Iterator itCust = element.elementIterator(); itCust.hasNext();){
					Element custEle = (Element)itCust.next();
					if (custEle.getName().equals("person")) {
						Person person = new Person();
						for (Iterator iter = custEle.elementIterator(); iter.hasNext();) {
							Element innerEle = (Element) iter.next();
							if (innerEle.getName().equals("cust-num")) {
								try {
									person.setCustomerNumber(Integer.parseInt(innerEle.getText().trim()));
								} catch (Exception e) {
									e.printStackTrace();
									throw new DocumentException(e.getMessage());
								}
							}
							if (innerEle.getName().equals("first-name")) {
								person.setFirstName(innerEle.getText());
							}
							if (innerEle.getName().equals("last-name")) {
								person.setLastName(innerEle.getText());
							}
						}
						cust.setPerson(person);
					}
					if (custEle.getName().equals("street")) {
						cust.setStreet(custEle.getText());
					}
					if (custEle.getName().equals("city")) {
						cust.setCity(custEle.getText());
					}
					if (custEle.getName().equals("state")) {
						cust.setState(custEle.getText());
					}
					if (custEle.getName().equals("zip")) {
						try {
							cust.setZip(Integer.parseInt(custEle.getText().trim()));
						} catch (Exception e) {
							e.printStackTrace();
							throw new DocumentException(e.getMessage());
						}
					}
					if (custEle.getName().equals("phone")) {
						cust.setPhone(custEle.getText());
					}
				}
				customers.add(cust);
			}
		}
		
		return customers;
	}
	
	public List<Integer> getCustNums(Document doc){
		List custNumEles = doc.selectNodes("/cust-root/customer/person/cust-num");
		List<Integer> custNums = new ArrayList<Integer>();
		for(Iterator it = custNumEles.iterator(); it.hasNext();){
			Element custNumEle = (Element)it.next();
			int custNum = Integer.parseInt(custNumEle.getText().trim());
			custNums.add(custNum);
		}
		return custNums;
	}
	
	public List getElementsByName(String fileName)throws MalformedURLException, DocumentException{
		Document doc = XmlFactory.getInstance().read(fileName);
		Element root = SaxXmlUtil.getRootElement(doc);
		List<Element> elements = new ArrayList<Element>(); 
		return SaxXmlUtil.getElementsByName(elements, root, "customer");
	}
	
	public void fillCusts(String fileName)throws MalformedURLException, DocumentException{
		Document doc = XmlFactory.getInstance().read(fileName);
		Element root = SaxXmlUtil.getRootElement(doc);
		root.accept(this);
	}
	
	public void visit(Element element){
		//String tagName = element.getName();
		if (element.getName().equals("customer")) {
			Customer cust = new Customer();
			Attribute cust_id = element.attribute("cust-id");
			if(cust_id!=null){
				cust.setCustId(cust_id.getValue());
			}
			custs.add(cust);
		}else if(!element.getName().equals("cust-root")){
			Customer cust = custs.get(custs.size()-1);
			
			if(element.getName().equals("person")){
				Person person = new Person();
				cust.setPerson(person);
			}else if(element.getName().equals("cust-num")){
				Person person = cust.getPerson();
				try {
					person.setCustomerNumber(Integer.parseInt(element.getText().trim()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(element.getName().equals("first-name")){
				Person person = cust.getPerson();
				person.setFirstName(element.getText());
			}else if(element.getName().equals("last-name")){
				Person person = cust.getPerson();
				person.setLastName(element.getText());
			}else if (element.getName().equals("street")) {
				cust.setStreet(element.getText());
			}else if (element.getName().equals("city")) {
				cust.setCity(element.getText());
			}else if (element.getName().equals("state")) {
				cust.setState(element.getText());
			}else if (element.getName().equals("zip")) {
				try {
					cust.setZip(Integer.parseInt(element.getText().trim()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (element.getName().equals("phone")) {
				cust.setPhone(element.getText());
			}
		}
	}
	
	
	
	
	

}
