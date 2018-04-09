package systeminfo.sigar;

import java.io.Serializable;

public class MemoryDto implements Serializable{

	private static final long serialVersionUID = 6029830300351314528L;
	
	// 当前内存总量 MB
	private double memTotal;
	// 当前内存使用量 MB
	private double memUsed;
	// 当前内存剩余量 MB
	private double memFree;
	// 内存使用率
	private double memUsePercent;
	// 交换区总量 
	private double swapTotal;
	// 当前交换区使用量 
	private double swapUsed;
	// 当前交换区剩余量
	private double swapFree;
	// 交换区使用率
	private double swapUsePercent;
	
	public double getMemTotal() {
		return memTotal;
	}
	public void setMemTotal(double memTotal) {
		this.memTotal = memTotal;
	}
	public double getMemUsed() {
		return memUsed;
	}
	public void setMemUsed(double memUsed) {
		this.memUsed = memUsed;
	}
	public double getMemFree() {
		return memFree;
	}
	public void setMemFree(double memFree) {
		this.memFree = memFree;
	}
	public double getMemUsePercent() {
		return memUsePercent;
	}
	public void setMemUsePercent(double memUsePercent) {
		this.memUsePercent = memUsePercent;
	}
	public double getSwapTotal() {
		return swapTotal;
	}
	public void setSwapTotal(double swapTotal) {
		this.swapTotal = swapTotal;
	}
	public double getSwapUsed() {
		return swapUsed;
	}
	public void setSwapUsed(double swapUsed) {
		this.swapUsed = swapUsed;
	}
	public double getSwapFree() {
		return swapFree;
	}
	public void setSwapFree(double swapFree) {
		this.swapFree = swapFree;
	}
	public double getSwapUsePercent() {
		return swapUsePercent;
	}
	public void setSwapUsePercent(double swapUsePercent) {
		this.swapUsePercent = swapUsePercent;
	}
	
	
}
