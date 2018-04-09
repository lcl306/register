package systeminfo.sigar;

import java.io.Serializable;

public class BaseSystemDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//计算机名
	private String computerName;
	
	//计算机域名
	private String userDomain;
	
	//本地ip地址
	private String ip;
	
	//本地主机名
	private String hostname;
	
	//操作系统名
	private String osname;
	
	//JVM可以使用的总内存 MB
	private long javaMaxMemory;
	
	//JVM可以使用的剩余内存 MB
	private long javaUnusedMemory;
	
	//JVM内存使用率
	private double javaMemusedPercent;
	
	//JVM可以使用的处理器个数
	private int javaProcessorNum;
	
	//Java的运行环境版本
	private String javaVersion;

	public String getComputerName() {
		return computerName;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public String getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(String userDomain) {
		this.userDomain = userDomain;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public long getJavaMaxMemory() {
		return javaMaxMemory;
	}

	public void setJavaMaxMemory(long javaMaxMemory) {
		this.javaMaxMemory = javaMaxMemory;
	}

	public long getJavaUnusedMemory() {
		return javaUnusedMemory;
	}

	public void setJavaUnusedMemory(long javaUnusedMemory) {
		this.javaUnusedMemory = javaUnusedMemory;
	}

	public double getJavaMemusedPercent() {
		return javaMemusedPercent;
	}

	public void setJavaMemusedPercent(double javaMemusedPercent) {
		this.javaMemusedPercent = javaMemusedPercent;
	}

	public int getJavaProcessorNum() {
		return javaProcessorNum;
	}

	public void setJavaProcessorNum(int javaProcessorNum) {
		this.javaProcessorNum = javaProcessorNum;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getOsname() {
		return osname;
	}

	public void setOsname(String osname) {
		this.osname = osname;
	}
	
	

}
