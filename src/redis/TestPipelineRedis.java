package redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.base.JedisUtil;
import redis.base.JedisUtil.Callback;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class TestPipelineRedis {
	
	public static void main(String[] args){
		JedisUtil.connect(new Callback(){
			public void exec(Jedis tx){
				System.out.println("--------------exec string ----------------");
				execString(tx);
				System.out.println("--------------exec hash ----------------");
				execHash(tx);
				System.out.println("--------------exec list ----------------");
				execList(tx);
				System.out.println("--------------exec set ----------------");
				execSet(tx);
			}
		});
	}
	
	public static void execString(Jedis jedis){
		Pipeline pl = jedis.pipelined();
		pl.set("p1", "javaPerson");
		pl.append("p1", "-spring");
		Response<String> r = pl.get("p1");
		pl.del("p1");
		pl.sync(); //从管道中获得
		System.out.println(r.get());
	}
	
	public static void execHash(Jedis jedis){
		Pipeline pl = jedis.pipelined();
		Map<String, String> map = new HashMap<>();
		map.put("name", "person");
		map.put("age", "18");
		pl.hmset("p2", map);
		Response<Map<String,String>> rp = pl.hgetAll("p2");
		Response<String> rps =pl.hget("p2", "name");
		Map<Integer, Person> iMap = new HashMap<>();
		iMap.put(1, Person.getInstance(1));
		iMap.put(2, Person.getInstance(2));
		pl.del("pintMap");
		JedisUtil.hmset(pl, "pintMap", iMap);
		pl.del("pnvlMap");
		Map<Number, Person> nMap = null;
		JedisUtil.hmset(pl, "pnvlMap", nMap);
		Response<Map<byte[],byte[]>> rpi = pl.hgetAll(JedisUtil.getKeyByte("pintMap"));
		Response<Map<byte[],byte[]>> rpn = pl.hgetAll(JedisUtil.getKeyByte("pnvlMap"));
		pl.sync();
		System.out.println(rp.get());
		System.out.println(rps.get());
		Map<Number, Person> rMap = JedisUtil.change(jedis, "pintMap", rpi.get());
		System.out.println(rMap);
		Map<Number, Person> rnMap = JedisUtil.change(jedis, "pnvlMap", rpn.get());
		System.out.println(rnMap);
	}
	
	public static void execList(Jedis jedis){
		Pipeline pl = jedis.pipelined();
		pl.del("plist");
		System.out.println(pl.lpush("plist", "redis"));
		System.out.println(pl.lpush("plist", "mongodb"));
		System.out.println(pl.lpush("plist", "sqlite"));
		jedis.del("pzlist");
		System.out.println(pl.rpush("pzlist", "redis"));
		System.out.println(pl.rpush("pzlist", "mongodb"));
		System.out.println(pl.rpush("pzlist", "sqlite"));
		Response<List<String>> pa = pl.lrange("plist", 0, 10);
		Response<List<String>> pz = pl.lrange("zlist", 0, 10);
		Response<List<String>> pa2 = pl.lrange("plist", 0, 10);
		pl.del("pobjList");
		List<Person> plist = new ArrayList<>();
		plist.add(Person.getInstance(1));
		plist.add(Person.getInstance(2));
		plist.add(Person.getInstance(3));
		JedisUtil.rpush(pl, "pobjList", plist);
		Response<List<byte[]>> po = pl.lrange(JedisUtil.getKeyByte("pobjList"), 0, 10);
		pl.sync();
		System.out.println(pa.get());
		System.out.println(pz.get());
		System.out.println(pa2.get());
		List<Person> ra = JedisUtil.change(po.get());
		System.out.println(ra);
	}
	
	public static void execSet(Jedis jedis){
		Pipeline pl = jedis.pipelined();
		pl.del("pset");
		pl.sadd("pset", "redis");
		pl.sadd("pset", "mongodb");
		pl.sadd("pset", "sqlite");
		//Response<Set<String>> ps = pl.smembers("pset");
		Response<Long> psl = pl.scard("pset");
		pl.srem("pset", "mongodb");
		//Response<Set<String>> ps2 = pl.smembers("pset");
		Set<Person> pset = new HashSet<>();
		pset.add(Person.getInstance(1));
		pset.add(Person.getInstance(2));
		pset.add(Person.getInstance(3));
		jedis.del("pobjSet");
		JedisUtil.sadd(pl, "pobjSet", pset);
		Response<Set<byte[]>> psr = pl.smembers(JedisUtil.getKeyByte("pobjSet"));
		Response<Long> pslr = pl.scard(JedisUtil.getKeyByte("pobjSet"));
		Response<Set<byte[]>> psr2 = pl.smembers(JedisUtil.getKeyByte("pobjSet2"));
		pl.sync();
		//System.out.println(ps.get());
		System.out.println(psl.get());
		//System.out.println(ps2.get());
		System.out.println(JedisUtil.change(psr.get()));
		System.out.println(pslr.get());
		Set<Person> prs = JedisUtil.change(psr2.get());
		System.out.println(prs);
	}

}
