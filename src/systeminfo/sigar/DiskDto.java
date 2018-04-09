package systeminfo.sigar;

import java.io.Serializable;

public class DiskDto implements Serializable{

	private static final long serialVersionUID = -8830310014225744599L;
	
	//盘符
	private String devName;
	//总容量，单位GB
	private long total;
	//可用量，单位GB
	private long avail;
	//已用量，单位GB
	private long used;
	//使用率
	private double usePercent;
	//已读入，单位KB
	private long diskReads;
	//已写入，单位KB
	private long diskWrites;
	
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getAvail() {
		return avail;
	}
	public void setAvail(long avail) {
		this.avail = avail;
	}
	public long getUsed() {
		return used;
	}
	public void setUsed(long used) {
		this.used = used;
	}
	public double getUsePercent() {
		return usePercent;
	}
	public void setUsePercent(double usePercent) {
		this.usePercent = usePercent;
	}
	public long getDiskReads() {
		return diskReads;
	}
	public void setDiskReads(long diskReads) {
		this.diskReads = diskReads;
	}
	public long getDiskWrites() {
		return diskWrites;
	}
	public void setDiskWrites(long diskWrites) {
		this.diskWrites = diskWrites;
	}
	
	

}
