package zookeeper.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;

public abstract class Election extends BaseWatcher {
	
	static Integer mutex2 = -1;
	
	protected boolean isLeader = false;
	
	public Election(String host, String root) {
		super(host, root);
	}
	
	public void findLeader(String node)throws InterruptedException, KeeperException, UnknownHostException{
		String leader = null;
		byte[] host = null;
		try {
			host = this.getData("/"+node, true); //如果node节点不存在是无法watch的, watch node_setData
			this.exist("/"+node, true); // watch node_delete
		} catch (KeeperException e) {
			if(!(e instanceof KeeperException.NoNodeException)) throw e;
			else{
				System.out.println(Thread.currentThread().getName()+": 没有"+node+"节点，准备创建");
			}
		}
		if(host==null){
				try {
					host = InetAddress.getLocalHost().getHostAddress().getBytes(BaseWatcher.CODING);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					//如果节点已经创建，其它节点再次创建时，会抛异常，leader为null
					leader = this.createExistError("/"+node, null, CreateMode.EPHEMERAL);
					this.setData("/"+node, host);
					isLeader = true;
				} catch (KeeperException e) {
					if(!(e instanceof KeeperException.NodeExistsException)) throw e;
					else{
						System.out.println(Thread.currentThread().getName()+": "+node+"节点已存在");
						this.getData("/"+node, true); //如果第一个getData无法watch，使用本getData进行watch, watch node_setData
						this.exist("/"+node, true); // watch node_delete
					}
				}
		}
		if(leader!=null) {
			leading();
		}
		//host是leader的，不是自己的
		host = this.getData("/"+node, false);
		if(host!=null){
			following(host);
		}else if(leader==null){
			synchronized(mutex){
				mutex.wait();
			}
			//host是leader的，不是自己的
			following(this.getData("/"+node, false));
		}
		//如果leader宕机了，其创建的节点被删除，其他follower竞争leader，新follower following新leader
		if(leader==null){
			synchronized(mutex2){
				mutex2.wait();
			}
			findLeader(node);
		}
		
	}
	
	@Override
	public void process(WatchedEvent event){
		//操作setData，getData观察，事件EventType.NodeDataChanged
		if(event.getPath().startsWith(this.root+"/") && event.getType()==Event.EventType.NodeDataChanged){
			super.process(event);
		}
		if(event.getPath().startsWith(this.root+"/") && event.getType()==Event.EventType.NodeDeleted){
			super.process(event, mutex2);
		}
	}
	
	public abstract void following(byte[] host);
	
	public abstract void leading();

}
