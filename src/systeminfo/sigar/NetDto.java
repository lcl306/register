package systeminfo.sigar;

import java.io.Serializable;

public class NetDto implements Serializable{

	private static final long serialVersionUID = 6132277673794630789L;
	
	private String name;
	
	private String ip;
	
	private String mask;
	
	private String mac;
	
	private String description;
	
	// 接收的总包裹数
	private long rxPackets;
	// 发送的总包裹数 
	private long txPackets;
	// 接收到的总字节数，单位B
	private long rxBytes;
	// 发送的总字节数，单位B
	private long txBytes;
	// 接收到的错误包数
	private long rxErrors;
	// 发送数据包时的错误数 
	private long txErrors;
	// 接收时丢弃的包数
	private long rxDropped;
	// 发送时丢弃的包数
	private long txDropped;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMask() {
		return mask;
	}
	public void setMask(String mask) {
		this.mask = mask;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getRxPackets() {
		return rxPackets;
	}
	public void setRxPackets(long rxPackets) {
		this.rxPackets = rxPackets;
	}
	public long getTxPackets() {
		return txPackets;
	}
	public void setTxPackets(long txPackets) {
		this.txPackets = txPackets;
	}
	public long getRxBytes() {
		return rxBytes;
	}
	public void setRxBytes(long rxBytes) {
		this.rxBytes = rxBytes;
	}
	public long getTxBytes() {
		return txBytes;
	}
	public void setTxBytes(long txBytes) {
		this.txBytes = txBytes;
	}
	public long getRxErrors() {
		return rxErrors;
	}
	public void setRxErrors(long rxErrors) {
		this.rxErrors = rxErrors;
	}
	public long getTxErrors() {
		return txErrors;
	}
	public void setTxErrors(long txErrors) {
		this.txErrors = txErrors;
	}
	public long getRxDropped() {
		return rxDropped;
	}
	public void setRxDropped(long rxDropped) {
		this.rxDropped = rxDropped;
	}
	public long getTxDropped() {
		return txDropped;
	}
	public void setTxDropped(long txDropped) {
		this.txDropped = txDropped;
	}
	
	

}
