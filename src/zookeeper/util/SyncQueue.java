package zookeeper.util;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;

public abstract class SyncQueue<T> extends BaseWatcher {

	protected int size = -1;
	
	protected String start = "";
	
	public SyncQueue(String host, String root, int size, String start) {
		super(host, root);
		this.size = size;
		this.start = start;
	}
	
	public T submit(String node) throws KeeperException, InterruptedException{
		prepare();
		this.create("/"+node, new byte[0], CreateMode.EPHEMERAL_SEQUENTIAL);
		this.exist("/"+start, true); // watch node的create，事件EventType.NodeCreated
		if(this.getAllChildren(false).size()==size){
			this.create("/"+start, null, CreateMode.EPHEMERAL);
			this.setData("/"+start, new byte[0]);
			return get(size);
		}else{
			synchronized(mutex){
				mutex.wait();
			}
			return get(this.getAllChildren(false).size());
		}
	}
	
	@Override
	public void process(WatchedEvent event){
		if(event.getPath().startsWith(this.root+"/"+start) && event.getType()==Event.EventType.NodeCreated){
			super.process(event);
		}
	}
	
	public abstract T get(int size);
	
	public abstract void prepare();

}
