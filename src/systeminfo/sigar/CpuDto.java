package systeminfo.sigar;

import java.io.Serializable;

public class CpuDto implements Serializable{
	
	private static final long serialVersionUID = 6121828083510427424L;
	//获得CPU的类别，如：Celeron
	private String model;
	// 用户使用率 
	private String userUse;
	// 系统使用率
	private String sysUse;
	// 当前等待率 
	private String wait;
	// 当前错误率
	private String nice;
	// 当前空闲率
	private String idle;
	// 总的使用率
	private String combined;
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getUserUse() {
		return userUse;
	}
	public void setUserUse(String userUse) {
		this.userUse = userUse;
	}
	public String getSysUse() {
		return sysUse;
	}
	public void setSysUse(String sysUse) {
		this.sysUse = sysUse;
	}
	public String getWait() {
		return wait;
	}
	public void setWait(String wait) {
		this.wait = wait;
	}
	public String getNice() {
		return nice;
	}
	public void setNice(String nice) {
		this.nice = nice;
	}
	public String getIdle() {
		return idle;
	}
	public void setIdle(String idle) {
		this.idle = idle;
	}
	public String getCombined() {
		return combined;
	}
	public void setCombined(String combined) {
		this.combined = combined;
	}
	

	
}
