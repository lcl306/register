package file;

public class SystemAns {
	
	/**返回已使用的内存，单位M
	 * $JAVA_HOME/bin下的jvisualvm.exe，可以查看运行的java线程的内存使用情况，
	 * 并一直动态记录该线程的内存使用，直至该线程停止，jvisualvm才会停止记录并静止显示
	 * */
	public static long getUsedMem(){
		Runtime run = Runtime.getRuntime();
		return (run.totalMemory() - run.freeMemory())/(1024*1024);
	}
	
	public static void main(String[] args){
		String str = "";
		for(int i=0; i<100000; ++i){
		    str += i;
		}
		System.out.println(getUsedMem()+"M");
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
