package zookeeper.test;

import java.util.List;

import org.apache.zookeeper.CreateMode;


public class TestConnect {
	
	static String host = "127.0.0.1:2181";
	
	public static void testZookeeper()throws Exception{
		TestWatcher w = new TestWatcher(host);
		byte[] data = new byte[]{1,2,3};
		w.create("/data_", data, CreateMode.EPHEMERAL_SEQUENTIAL);
		w.create("/data1", data, CreateMode.PERSISTENT);
		List<String> nodes = w.getAllChildren(true);
		for(String n : nodes){
			if(w.exist("/"+n, true)!=null){
				System.out.println(n+":" +w.getData("/"+n, true).length);
				w.setData("/"+n, new byte[]{1,2,3,4,56});
			}
		}
		w.delete("/data1");
		w.close();
	}
	
	public static void main(String[] args)throws Exception{
		testZookeeper();
	}

}
