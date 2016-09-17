package zookeeper.util;

import java.util.Arrays;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;

public abstract class Lock extends BaseWatcher {

	protected String myNode;
	
	public Lock(String host, String root) {
		super(host, root);
	}
	
	public void init(){
		try {
			//保证了/lock_节点创建的顺序性
			myNode = this.create("/task_", new byte[0], CreateMode.EPHEMERAL_SEQUENTIAL);
			getLock();
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 任务完成后，需要释放锁，否则getLock和waitForLock会相互调用致系统堆栈溢出
	 * */
	public void release(){
		try {
			this.delete("/"+myNode);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void getLock()throws InterruptedException, KeeperException{
		List<String> list = this.getAllChildren(false);
		if(!list.isEmpty()){
			String[] nodes = list.toArray(new String[]{});
			Arrays.sort(nodes);
			//如果第一个node和自己的node相同，执行业务，否则等待锁
			System.out.println(Thread.currentThread().getName()+"_"+list.size()+"_"+nodes[0]+"_"+myNode);
			if(nodes[0].equals(myNode)){
				doAction();
			}else{
				waitForLock(nodes[0]);
			}
		}
	}
	
	void waitForLock(String first)throws InterruptedException, KeeperException{
		//如果第一个节点没删除，则加锁；如果被删除，则通知其他被锁线程getLock，其它被锁线程getLock时，发现第一个是自己则执行业务，如果不是自己则再次加锁
		if(this.exist("/"+first, true)!=null){
			synchronized(mutex){
				mutex.wait();
			}
		}
		//一旦notifyAll后，所有被锁Thread都会去执行getLock方法
		getLock();
	}
	
	@Override
	public void process(WatchedEvent event){
		//如果是删除node，则释放锁
		//操作delete，exist观察，事件EventType.NodeDeleted
		if(event.getPath().startsWith(this.root+"/") && event.getType()==Event.EventType.NodeDeleted){
			super.process(event);
		}
	}
	
	public abstract void doAction();

}
