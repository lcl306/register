package zookeeper.util;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class BaseWatcher implements Watcher {
	
	public static final String CODING = "UTF-8";
	
	protected static ZooKeeper zk = null;
	
	protected int SESSION_TIMEOUT = 6000;
	
	protected static Integer mutex = -1;
	
	public String root;
	
	public BaseWatcher(String host, String root){
		if(zk==null){
			try {
				// 注册了this，所以创建连接时调用了process
				zk = new ZooKeeper(host, SESSION_TIMEOUT, this);
				if(zk.exists(root, false)==null)
					zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} catch (IOException | InterruptedException | KeeperException e) {
				zk = null;
				e.printStackTrace();
			}
		}
		this.root = root;
	}

	/**
	 * 如果BaseWatcher被继承，则继承的子类响应，并不是BaseWatcher响应
	 * 如果getData和exist的watch都为true，process只会调用一次
	 * */
	@Override
	public void process(WatchedEvent event) {
		this.process(event, mutex);
	}
	
	protected void process(WatchedEvent event, Integer mutex) {
		System.out.println(this.getClass().getName()+"响应"+event.getPath()+"的事件："+event.getType().name()+": 连接状态="+event.getState().name());
		synchronized(mutex){
			mutex.notifyAll();
		}
	}
	
	/**
	 * 添加node
	 * @CreateMode：CreateMode.EPHEMERAL:临时节点（session结束或过期消失），CreateMode.PERSISTENT:持久化，CreateMode.EPHEMERAL_SEQUENTIAL：节点名后自动添加编号，编号按顺序且唯一
	 * */
	public String create(String path, byte[] data, CreateMode createMode)throws KeeperException, InterruptedException{
		if(exist(path, false)==null){
			String p = zk.create(root+path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
			path = p.substring(root.length()+1);
		}
		return path;
	}
	
	public String createExistError(String path, byte[] data, CreateMode createMode)throws KeeperException, InterruptedException{
		String p = zk.create(root+path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
		return p.substring(root.length()+1);
	}
	
	public List<String> getAllChildren(boolean watch)throws InterruptedException, KeeperException{
		return zk.getChildren(root, watch);
	}
	
	/**
	 * 拿到所有节点，watch=true表示调用者（BaseWatcher或其子类）监控这个节点的删除，及子节点的添加和删除
	 * */
	public List<String> getChildren(String path, boolean watch)throws InterruptedException, KeeperException{
		return zk.getChildren(root+path, watch);
	}
	
	/**
	 * 判断节点是否存在，watch=true表示调用者（BaseWatcher或其子类）监控这个节点的删除，及setData
	 * 如果setData事件被监听，则delete事件不会监听
	 * */
	public Stat exist(String path, boolean watch)throws InterruptedException,KeeperException{
		Stat s = null;
		if(zk!=null){
			s = zk.exists(root+path, watch);
		}
		return s;
	}
	
	/**
	 * 节点赋值
	 * @version：版本号必须和node的版本号一致
	 * 最大值不能超过1MB
	 * */
	public Stat setData(String path, byte[] data)throws InterruptedException, KeeperException{
		Stat s = exist(path, false);
		if(s!=null) return zk.setData(root+path, data, s.getVersion());
		return null;
	}
	
	/**
	 * 获得节点数据，watch=true表示调用者（BaseWatcher或其子类）监控这个节点的删除，及setData
	 * 如果setData事件被监听，则delete事件不会监听
	 * */
	public byte[] getData(String path, boolean watch)throws InterruptedException, KeeperException{
		return zk.getData(root+path, watch, null);
	}
	
	/**
	 * 删除节点
	 * */
	public void delete(String path)throws KeeperException, InterruptedException{
		Stat s = exist(path, false);
		if(s!=null){
			zk.delete(root+path, s.getVersion());
		} 
	}
	
	/**
	 * zookeeper异常关闭，可能临时节点没有释放
	 * */
	public void clear(){
		try {
			for(String n : this.getAllChildren(false)){
				this.delete("/"+n);
			}
		} catch (InterruptedException | KeeperException e) {
			e.printStackTrace();
		}
	}
	
	public void close()throws InterruptedException{
		if(zk!=null) zk.close();
	}

}
