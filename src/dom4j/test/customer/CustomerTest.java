package dom4j.test.customer;

import java.util.Iterator;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.dom4j.Document;
import org.dom4j.Element;

import dom4j.customer.Customer;
import dom4j.customer.SaxCustomer;
import dom4j.customer.WriteCustomer;
import dom4j.util.SaxXmlUtil;
import dom4j.util.XmlFactory;


public class CustomerTest extends TestCase {
	
	public static String dir = "C:/work/eclipse_workspace/register/src/dom4j/customer/";

	public static String path = "";
	
	public static String writePath = "";
	
	public static String updatePath = "";
	
	public SaxCustomer sc = null;
	
	public WriteCustomer wc = null;
	
	public Element root = null;
	
	public Document doc = null;
	
	public Document rwDoc = null;
	
	public Document udDoc = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		//path = System.getProperty("PATH", "D:/workspace/LocalRexass/src/dom4j/customer/customer.xml");
		//writePath = System.getProperty("WRITE_PATH", "D:/workspace/LocalRexass/src/dom4j/customer/customer-write.xml");
		//updatePath = System.getProperty("UPDATE_PATH", "D:/workspace/LocalRexass/src/dom4j/customer/customer-update.xml");
		sc = new SaxCustomer();
		wc = new WriteCustomer();
		doc = XmlFactory.getInstance().read(path);
		//rwDoc = XmlFactory.getInstance().read(writePath);
		root = SaxXmlUtil.getRootElement(doc);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private void testList(List customers)throws Exception{
		if(customers==null || customers.isEmpty()){
			fail("no customer gotten!");
		}
		for(Iterator it = customers.iterator(); it.hasNext();){
			Customer custGet = (Customer)it.next();
			String custId = custGet.getCustId();
			if(custId==null || !custId.equals("001") && !custId.equals("002")){
				fail("customer id is wrong! ");
			}
			int custNum = custGet.getPerson().getCustomerNumber();
			if(custNum!=123456789 && custNum != 123456780){
				fail("customer number is wrong! ");
			}
			assertEquals(custGet.getPerson().getFirstName(), "崔兵");
			assertEquals(custGet.getPerson().getLastName(), "Smith");
			assertEquals(custGet.getStreet(), "12345 Happy Lane");
			assertEquals(custGet.getCity(), "Plunk");
			assertEquals(custGet.getState(), "WA");
			assertEquals(custGet.getZip(), new Integer(98059));
			assertEquals(custGet.getPhone(), "888.555.1234");
		}
	}
	
	public void testGetCustomers()throws Exception{
		List<Customer> customers = sc.getCustomers(path);
		testList(customers);
	}
	
	public void testGetElementsByName()throws Exception{
		List elements = sc.getElementsByName(path);
		assertEquals(elements.size(), 2);
	}
	
	public void testFillCusts()throws Exception{
		sc.fillCusts(path);
		testList(sc.custs);
	}
	
	public void testGetCustNums()throws Exception{
		List<Integer> custNums = sc.getCustNums(doc);
		if(custNums==null || custNums.isEmpty()){
			fail("customer number is not gotten! ");
		}else{
			int idx = 0;
			for(Integer num : custNums){
				if(idx==0) assertEquals(num, new Integer(123456789));
				if(idx==1) assertEquals(num, new Integer(123456780));
				idx++;
			}
		}
	}
	
	public void testWriteCustXML()throws Exception{
		List<Customer> customers = sc.getCustomers(path);
		WriteCustomer.writeCustXML(customers, writePath);
		List<Customer> customersWrite = sc.getCustomers(writePath);
		testList(customersWrite);
	}
	
	public void testCustomerXML()throws Exception{
		wc.updateCustXML(path, updatePath);
		udDoc = XmlFactory.getInstance().read(updatePath);
		System.out.println(udDoc.asXML());
	}
	
	public static Test suite(){
		TestSuite testSuite = new TestSuite(CustomerTest.class);
		
		// load only once
		TestSetup wrapper = new TestSetup(testSuite){
			public void setUp()throws Exception{
				super.setUp();
				path = System.getProperty("PATH", dir+"customer.xml");
				writePath = System.getProperty("WRITE_PATH", dir+"customer-write.xml");
				updatePath = System.getProperty("UPDATE_PATH", dir+"customer-update.xml");
			}
		};
		return wrapper;
	}

}
