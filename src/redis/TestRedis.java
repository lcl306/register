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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;

public class TestRedis {
	
	public static void main(String[] args){
		JedisUtil.connect(new Callback(){
			public void exec(Jedis jedis){
				System.out.println(jedis.ping());
				System.out.println(jedis.keys("*"));
				System.out.println("--------------exec string ----------------");
				execString(jedis);
				System.out.println("--------------exec hash ----------------");
				execHash(jedis);
				System.out.println("--------------exec obj ----------------");
				execObj(jedis);
				System.out.println("--------------exec list ----------------");
				execList(jedis);
				System.out.println("--------------exec set ----------------");
				execSet(jedis);
			}
		});
	}
	
	public static void execSet(Jedis jedis){
		jedis.del("aset");
		jedis.sadd("aset", "redis");
		jedis.sadd("aset", "mongodb");
		jedis.sadd("aset", "sqlite");
		System.out.println(jedis.smembers("aset"));
		System.out.println(jedis.scard("aset"));
		System.out.println(jedis.srem("aset", "mongodb"));
		System.out.println(jedis.smembers("aset"));
		List<String> r=jedis.sscan("aset", "0", new ScanParams().match("*s*")).getResult();
		System.out.println(r);
		Set<Person> pset = new HashSet<>();
		pset.add(Person.getInstance(1));
		pset.add(Person.getInstance(2));
		pset.add(Person.getInstance(3));
		jedis.del("objSet");
		System.out.println(JedisUtil.sadd(jedis, "objSet", pset));
		Set<Person> rset = JedisUtil.smembers(jedis, "objSet");
		System.out.println(rset);
		System.out.println(JedisUtil.scard(jedis, "objSet"));
		Set<Person> rset2 = JedisUtil.smembers(jedis, "objSet2");
		System.out.println(rset2);
		System.out.println(JedisUtil.scard(jedis, "objSet2"));
	}
	
	public static void execList(Jedis jedis){
		jedis.del("alist");
		System.out.println(jedis.lpush("alist", "redis"));
		System.out.println(jedis.lpush("alist", "mongodb"));
		System.out.println(jedis.lpush("alist", "sqlite"));
		jedis.del("zlist");
		System.out.println(jedis.rpush("zlist", "redis"));
		System.out.println(jedis.rpush("zlist", "mongodb"));
		System.out.println(jedis.rpush("zlist", "sqlite"));
		System.out.println(jedis.lrange("alist", 0, jedis.llen("alist")));
		System.out.println(jedis.lrange("zlist", 0, jedis.llen("zlist")));
		System.out.println(jedis.lrem("alist", 1, jedis.lindex("alist", 2)));
		System.out.println(jedis.lrange("alist", 0, jedis.llen("alist")));
		jedis.del("objList");
		List<Person> plist = new ArrayList<>();
		plist.add(Person.getInstance(1));
		plist.add(Person.getInstance(2));
		plist.add(Person.getInstance(3));
		JedisUtil.rpush(jedis, "objList", plist);
		List<Person> rplist = JedisUtil.lrange(jedis, "objList");
		System.out.println(rplist);
		Person p = JedisUtil.lindex(jedis, "objList", 0);
		System.out.println(p);
		p = JedisUtil.lindex(jedis, "objList", 5);
		System.out.println(p);
		p = JedisUtil.lindex(jedis, "objList2", 1);
		System.out.println(p);
	}
	
	public static void execString(Jedis jedis){
		System.out.println(jedis.set("1", "javaPerson"));
		System.out.println(jedis.get("1"));
		jedis.append("1", "-spring");
		System.out.println(jedis.mget("1", "11"));
		System.out.println(jedis.del("1"));
		System.out.println(jedis.keys("*"));
	}
	
	public static void execHash(Jedis jedis){
		Map<String, String> map = new HashMap<>();
		map.put("name", "person");
		map.put("age", "18");
		jedis.hmset("2", map);
		System.out.println(jedis.hgetAll("2"));
		System.out.println(jedis.hexists("2", "name"));
		System.out.println(jedis.hget("2", "name"));
		System.out.println(jedis.hdel("2", "age"));
		System.out.println(jedis.hgetAll("2"));
		Map<Integer, Person> iMap = new HashMap<>();
		iMap.put(1, Person.getInstance(1));
		iMap.put(2, Person.getInstance(2));
		jedis.del("intMap");
		JedisUtil.hmset(jedis, "intMap", iMap);
		Map<Number, Person> rMap = JedisUtil.hgetall(jedis, "intMap");
		System.out.println(rMap);
		Person p = JedisUtil.hget(jedis, "intMap", 1);
		System.out.println(p);
		Map<String, Person> sMap = new TreeMap<>();
		sMap.put("a", Person.getInstance(1));
		sMap.put("b", Person.getInstance(2));
		jedis.del("strMap");
		JedisUtil.hmset(jedis, "strMap", sMap);
		Map<Number, Person> rsMap = JedisUtil.hgetall(jedis, "strMap");
		System.out.println(rsMap);
		JedisUtil.hdel(jedis, "strMap", "b");
		Person ps = JedisUtil.hget(jedis, "strMap", "b");
		System.out.println(ps);
		jedis.del("nvlMap");
		Map<Number, Person> nMap = null;
		JedisUtil.hmset(jedis, "nvlMap", nMap);
		Map<Number, Person> nsMap = JedisUtil.hgetall(jedis, "nvlMap");
		System.out.println(nsMap);
		Person pn = JedisUtil.hget(jedis, "nvlMap", "b");
		System.out.println(pn);
	}
	
	public static void execObj(Jedis jedis){
		JedisUtil.setObj(jedis, "obj1", Person.getInstance(1));
		Person p = JedisUtil.getObj(jedis, "obj1");
		System.out.println(p);
		
		List<Person> pList = new ArrayList<>();
		pList.add(Person.getInstance(2));
		pList.add(Person.getInstance(3));
		pList.add(Person.getInstance(4));
		JedisUtil.setObj(jedis, "obj2", pList);
		List<Person> rList = JedisUtil.getObj(jedis, "obj2");
		System.out.println(rList);
	}

}
