package zookeeper.util;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;

public class FIFOQueue extends BaseWatcher {

	public FIFOQueue(String host, String root) {
		super(host, root);
	}
	
	public String produce(String node, byte[] data) throws KeeperException, InterruptedException{
		return super.create("/"+node, data, CreateMode.PERSISTENT_SEQUENTIAL);
	}
	
	public byte[] consume(boolean wait) throws InterruptedException, KeeperException{
		synchronized(mutex){  //必须加锁，否则第一个线程消费途中，第二个线程过来，可能重复消费
			List<String> nodes = this.getAllChildren(true);
			if(!nodes.isEmpty()){
				String minNode = getMin(nodes);
				System.out.println("consume node: "+minNode);
				byte[] data = this.getData("/"+minNode, false);
				this.delete("/"+minNode);
				return data;
			}else if(wait){
				mutex.wait();
				return consume(wait);
			}
		}
		return null;
	}
	
	protected String getMin(List<String> nodes){
		String  min = nodes.get(0);
		for(String n : nodes){
			if(n.compareTo(min)<0){
				min = n;
			}
		}
		return min;
	}
	
	@Override
	public void process(WatchedEvent event){
		// path是/root，不是/root/
		if(event.getPath().startsWith(this.root) && event.getType()==Event.EventType.NodeChildrenChanged){
			super.process(event);
		}
	}

}
