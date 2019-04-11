package systeminfo.wmic;

import java.io.Serializable;

public class ProcessInfo implements Serializable{

	private static final long serialVersionUID = 405408690867722781L;
	
	private String caption;
	
	private String commandLine;
	
	//应用使用时间
	private long userModeTime;
	
	//windows使用时间
	private long kernelModeTime;
	
	//最大内存使用，单位KB
	private long peakWorkingSetSize; 
	
	//当前内存使用，单位KB
	private long workingSetSize;
	
	private double cpuRatio;
	
	private double memoryRatio;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCommandLine() {
		return commandLine;
	}

	public void setCommandLine(String commandLine) {
		this.commandLine = commandLine;
	}

	public long getUserModeTime() {
		return userModeTime;
	}

	public void setUserModeTime(long userModeTime) {
		this.userModeTime = userModeTime;
	}

	public long getKernelModeTime() {
		return kernelModeTime;
	}

	public void setKernelModeTime(long kernelModeTime) {
		this.kernelModeTime = kernelModeTime;
	}

	public double getCpuRatio() {
		return cpuRatio;
	}

	public void setCpuRatio(double cpuRatio) {
		this.cpuRatio = cpuRatio;
	}

	public double getMemoryRatio() {
		return memoryRatio;
	}

	public void setMemoryRatio(double memoryRatio) {
		this.memoryRatio = memoryRatio;
	}

	public long getPeakWorkingSetSize() {
		return peakWorkingSetSize;
	}

	public void setPeakWorkingSetSize(long peakWorkingSetSize) {
		this.peakWorkingSetSize = peakWorkingSetSize;
	}

	public long getWorkingSetSize() {
		return workingSetSize;
	}

	public void setWorkingSetSize(long workingSetSize) {
		this.workingSetSize = workingSetSize;
	}
	
	
}
