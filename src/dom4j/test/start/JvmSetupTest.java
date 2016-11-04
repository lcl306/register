package dom4j.test.start;

import junit.textui.TestRunner;

public class JvmSetupTest {

	public static void tearDownOnce(){
		System.out.println("Teardown");
	}
	
	public static void setUpOnce(){
		System.out.println("Setup");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		setUpOnce();
		
		TestRunner testRunner = new TestRunner(){
			public void terminate(){
				tearDownOnce();
				//super.terminate();
			}
		};
		testRunner.start(new String[]{"dom4j.test.customer.CustomerTest"});
		
		/*TestRunner testRunner = new TestRunner(){
			protected TestResult start(String args[]) throws Exception{
				while(true){
					doRun(getTest(""), true);
				}
			}
		};*/
		

	}

}
