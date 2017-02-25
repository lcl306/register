package redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Person implements Serializable{

	private static final long serialVersionUID = -7084202997160519681L;

	private long id;
	
	private String name;
	
	private int age;
	
	private double salary;
	
	private List<Address> addrs = new ArrayList<Address>();
	
	private List<Integer> cards = new ArrayList<Integer>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public List<Address> getAddrs() {
		return addrs;
	}

	public void setAddrs(List<Address> addrs) {
		this.addrs = addrs;
	}

	public List<Integer> getCards() {
		return cards;
	}

	public void setCards(List<Integer> cards) {
		this.cards = cards;
	}
	
	public static Person getInstance(long id){
		Person p = new Person();
		p.setId(id);
		p.setAge((int)(10*id));
		p.setName("name"+id);
		p.setSalary(12.12*id);
		p.getCards().add((int)(100*id));
		p.getCards().add((int)(101*id));
		p.getCards().add((int)(102*id));
		Address addr = new Address("上海", "文明路街道"+id, (int)(10*id));
		Address addr2 = new Address("上海", "文明路街道"+id, (int)(20*id));
		p.getAddrs().add(addr);
		p.getAddrs().add(addr2);
		return p;
	}

}

class Address implements Serializable{

	private static final long serialVersionUID = -5199285367599776208L;

	private String city;
	
	private String street;
	
	private int code;
	
	public Address(String city, String street, int code){
		this.city = city;
		this.street = street;
		this.code = code;
	}
	
	public Address(){
		
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
