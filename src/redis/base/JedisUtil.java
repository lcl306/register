package redis.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import util.BytesUtil;

public class JedisUtil {
	
	public static String host = "localhost";
	
	public static int port = 6379;
	
	public static String password = "123456";
	
	protected static final String JEDIS_CODE = "utf-8";
	
	private static final String JEDIS_MAP_TYPE ="JEDIS:::MAP:::TYPE";
	
	private static final String JEDIS_MAP_KEYTYPE ="JEDIS:::MAP:::KEYTYPE";
	
	public static interface Callback{
		
		void exec(Jedis jedis);
	}
	
	public static void connect(Callback callback){
		Jedis jedis = new Jedis(host, port);
		jedis.auth(password);
		callback.exec(jedis);
		jedis.close();
	} 
	
	public static String setObj(Jedis jedis, String key, Object obj){
		return jedis.set(getKeyByte(key), bytes(obj));
	}
	
	public static <T> T getObj(Jedis jedis, String key){
		return object(jedis.get(getKeyByte(key)));
	}
	
	public static <T> Long rpush(Jedis jedis, String key, List<T> list){
		long num = 0;
		if(list!=null && !list.isEmpty()){
			for(T t : list){
				num += jedis.rpush(getKeyByte(key), bytes(t));
			}
		}
		return num;
	}
	
	public static <T> Long lpush(Jedis jedis, String key, List<T> list){
		long num = 0;
		if(list!=null && !list.isEmpty()){
			for(T t : list){
				num += jedis.lpush(getKeyByte(key), bytes(t));
			}
		}
		return num;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> lrange(Jedis jedis, String key){
		List<byte[]> l = jedis.lrange(getKeyByte(key), 0, jedis.llen(getKeyByte(key)));
		List<T> list = null;
		if(l!=null){
			list = new ArrayList<>();
			for(byte[] b : l){
				list.add((T)object(b));
			}
		}
		return list;
	}
	
	public static <T> T lindex(Jedis jedis, String key, long index){
		return object(jedis.lindex(getKeyByte(key), index));
	}
	
	/**
	 * redis是按照hash储存map的，treeMap放入，得到的也是HashMap
	 * Map的key必须为String、Long、Integer、Short、Byte
	 * */
	public static <K,V> String hmset(Jedis jedis, String key, Map<K,V> map){
		Map<byte[], byte[]> m = new HashMap<>();
		String keyType = null;
		if(map!=null){
			for(K k : map.keySet()){
				if(keyType==null) keyType=k.getClass().getName();
				m.put(getKeyByte(k), bytes(map.get(k)));
			}
			m.put(getKeyByte(JEDIS_MAP_TYPE), bytes(map.getClass().getName()));
			m.put(getKeyByte(JEDIS_MAP_KEYTYPE), bytes(keyType));
			return jedis.hmset(getKeyByte(key), m);
		}
		return null;
	}
	
	/**
	 * 返回Map<Serializable, Object>, Serializable只可以是String、Long、Integer、Short、Byte
	 * */
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K,V> hgetall(Jedis jedis, String key){
		Map<byte[], byte[]> m = jedis.hgetAll(getKeyByte(key));
		Map<K, V> map = null;
		if(m!=null && !m.isEmpty()){
			try {
				map = (Map<K, V>) Class.forName((String)hget(jedis, key, JEDIS_MAP_TYPE)).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			String keyType = hget(jedis, key, JEDIS_MAP_KEYTYPE);
			for(byte[] k : m.keySet()){
				Serializable rk = keyType.indexOf("String")!=-1?getByteStr(k):BytesUtil.byteToNum(k);
				if(rk!=null && !rk.equals(JEDIS_MAP_TYPE) && !rk.equals(JEDIS_MAP_KEYTYPE))
					map.put((K)rk, (V)object(m.get(k)));
			}
		}
		return map;
	}
	
	public static <K,V> V hget(Jedis jedis, String key, K propKey){
		return object(jedis.hget(getKeyByte(key), getKeyByte(propKey)));
	}
	
	public static <K> Long hdel(Jedis jedis, String key, K propKey){
		return jedis.hdel(getKeyByte(key), getKeyByte(propKey));
	}
	
	public static <T> Long sadd(Jedis jedis, String key, Set<T> set){
		Long num = 0l;
		if(set!=null && !set.isEmpty()){
			for(T t : set){
				num +=jedis.sadd(getKeyByte(key), bytes(t));
			}
		}
		return num;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> HashSet<T> smembers(Jedis jedis, String key){
		Set<byte[]> s = jedis.smembers(getKeyByte(key));
		HashSet<T> set = null;
		if(s!=null){
			set = new HashSet<>();
			for(byte[] b : s){
				set.add((T)object(b));
			}
		}
		return set;
	}
	
	public static Long scard(Jedis jedis, String key){
		return jedis.scard(getKeyByte(key));
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	
	public static <K> byte[] getKeyByte(K key){
		if(key instanceof String)
			try {
				return ((String)key).getBytes(JEDIS_CODE);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		if(key instanceof Number)
			return BytesUtil.numToByte((Number)key);
		return null;
	}
	
	public static String getByteStr(byte[] b){
		if(b!=null){
			try {
				return new String(b, JEDIS_CODE);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
     * 序列化
     */
    protected static byte[] bytes(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        if(object!=null){
	        try {
	            baos = new ByteArrayOutputStream();
	            oos = new ObjectOutputStream(baos);
	            oos.writeObject(object);
	            bytes = baos.toByteArray();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }finally{
	        	try {
					oos.close();
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
        }
        return bytes;
    }

    /**
     * 反序列化
     */
    @SuppressWarnings("unchecked")
	protected static <T> T object(byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        T t = null;
        if(bytes!=null){
        	try {
                bais = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bais);
                t = (T)ois.readObject();
            } catch (Exception e) {
            	e.printStackTrace();
            }finally{
            	try {
    				ois.close();
    				bais.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
            	
            }
        }
        return t;
    }

}
