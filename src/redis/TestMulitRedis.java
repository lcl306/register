package redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import redis.base.JedisUtil;
import redis.base.JedisUtil.Callback;
import redis.base.JedisUtil.MultiCallback;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestMulitRedis {
	
	public static void main(String[] args){
		JedisUtil.connect(new MultiCallback(){
			public void exec(Transaction tx){
				System.out.println("--------------exec string ----------------");
				execString(tx);
				System.out.println("--------------exec hash ----------------");
				execHash(tx);
				System.out.println("--------------exec obj ----------------");
				execObj(tx);
				System.out.println("--------------exec list ----------------");
				execList(tx);
				System.out.println("--------------exec set ----------------");
				execSet(tx);
				System.out.println("--------------exec hyperlog ----------------");
				hyperlog(tx);
			}
		});
		
		JedisUtil.connect(new Callback(){
			public void exec(Jedis jedis){
				System.out.println("--------------exec get ----------------");
				get(jedis);
			}
		});
	}
	
	public static void execString(Transaction tx){
		System.out.println(tx.set("t1", "javaPerson"));
		System.out.println(tx.get("t1"));
		tx.append("t1", "-spring");
		System.out.println(tx.mget("t1", "11"));
		System.out.println(tx.del("t1"));
		System.out.println(tx.keys("*"));
	}
	
	public static void hyperlog(Transaction tx){
		tx.pfadd("pflist", "apple", "cherry", "durian", "mongo", "cherry");
		System.out.println(tx.pfcount("tpflist"));
		tx.pfadd("tpflist2", "apple", "cherry", "banana");
		tx.pfmerge("pfmlist&2", "pflist2", "pflist");
		System.out.println(tx.pfcount("pfmlist&2"));
	}
	
	public static void execSet(Transaction tx){
		tx.del("taset");
		tx.sadd("taset", "redis");
		tx.sadd("taset", "mongodb");
		tx.sadd("taset", "sqlite");
		System.out.println(tx.smembers("taset"));
		System.out.println(tx.scard("taset"));
		System.out.println(tx.srem("taset", "mongodb"));
		System.out.println(tx.smembers("taset"));
		Set<Person> pset = new HashSet<>();
		pset.add(Person.getInstance(1));
		pset.add(Person.getInstance(2));
		pset.add(Person.getInstance(3));
		tx.del("tobjSet");
		System.out.println(JedisUtil.sadd(tx, "tobjSet", pset));
		
	}
	
	public static void execList(Transaction tx){
		tx.del("talist");
		System.out.println(tx.lpush("talist", "redis"));
		System.out.println(tx.lpush("talist", "mongodb"));
		System.out.println(tx.lpush("talist", "sqlite"));
		tx.del("tzlist");
		System.out.println(tx.rpush("tzlist", "redis"));
		System.out.println(tx.rpush("tzlist", "mongodb"));
		System.out.println(tx.rpush("tzlist", "sqlite"));
		tx.del("tobjList");
		List<Person> plist = new ArrayList<>();
		plist.add(Person.getInstance(1));
		plist.add(Person.getInstance(2));
		plist.add(Person.getInstance(3));
		JedisUtil.rpush(tx, "tobjList", plist);
	}
	
	public static void execHash(Transaction tx){
		Map<String, String> map = new HashMap<>();
		map.put("name", "person");
		map.put("age", "18");
		tx.hmset("t2", map);
		System.out.println(tx.hgetAll("t2"));
		System.out.println(tx.hexists("t2", "name"));
		System.out.println(tx.hget("t2", "name"));
		System.out.println(tx.hdel("t2", "age"));
		System.out.println(tx.hgetAll("t2"));
		Map<Integer, Person> iMap = new HashMap<>();
		iMap.put(1, Person.getInstance(1));
		iMap.put(2, Person.getInstance(2));
		tx.del("tintMap");
		JedisUtil.hmset(tx, "tintMap", iMap);
		Map<String, Person> sMap = new TreeMap<>();
		sMap.put("a", Person.getInstance(1));
		sMap.put("b", Person.getInstance(2));
		tx.del("tstrMap");
		JedisUtil.hmset(tx, "tstrMap", sMap);
		JedisUtil.hdel(tx, "tstrMap", "b");
		tx.del("tnvlMap");
		Map<Number, Person> nMap = null;
		JedisUtil.hmset(tx, "tnvlMap", nMap);
	}
	
	public static void execObj(Transaction tx){
		JedisUtil.setObj(tx, "tobj1", Person.getInstance(1));
		List<Person> pList = new ArrayList<>();
		pList.add(Person.getInstance(2));
		pList.add(Person.getInstance(3));
		pList.add(Person.getInstance(4));
		JedisUtil.setObj(tx, "tobj2", pList);
	}
	
	public static void get(Jedis jedis){
		Set<Person> rset = JedisUtil.smembers(jedis, "tobjSet");
		System.out.println(rset);
		List<Person> rplist = JedisUtil.lrange(jedis, "tobjList");
		System.out.println(rplist);
		Map<Number, Person> rMap = JedisUtil.hgetall(jedis, "tintMap");
		System.out.println(rMap);
		Person p = JedisUtil.hget(jedis, "tintMap", 1);
		System.out.println(p);
		Map<Number, Person> rsMap = JedisUtil.hgetall(jedis, "tstrMap");
		System.out.println(rsMap);
		Person ps = JedisUtil.hget(jedis, "tstrMap", "b");
		System.out.println(ps);
		Map<Number, Person> nsMap = JedisUtil.hgetall(jedis, "tnvlMap");
		System.out.println(nsMap);
		Person pn = JedisUtil.hget(jedis, "tnvlMap", "b");
		System.out.println(pn);
		Person p2 = JedisUtil.getObj(jedis, "tobj1");
		System.out.println(p2);
		List<Person> rList = JedisUtil.getObj(jedis, "tobj2");
		System.out.println(rList);
	}

}
