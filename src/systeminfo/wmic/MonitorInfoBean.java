package systeminfo.wmic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MonitorInfoBean implements Serializable{
	
	private static final long serialVersionUID = -8860410410526834385L;

	/** *//** java已使用内存. */  
    private long javaUsedMemory;   
       
    /** *//** java剩余内存. */  
    private long freeMemory;   
       
    /** *//** java最大可使用内存. */  
    private long maxMemory;   
       
    /** *//** 操作系统. */  
    private String osName;   
       
    /** *//** 总的物理内存. */  
    private long totalMemorySize;   
       
    /** *//** 剩余的物理内存. */  
    private long freePhysicalMemorySize;   
       
    /** *//** 已使用的物理内存. */  
    private long usedMemory;   
       
    /** *//** 线程总数. */  
    private int totalThread;   
       
    /** *//** cpu使用率. */  
    private double cpuRatio;   
    
    private List<ProcessInfo> processInfos = new ArrayList<ProcessInfo>();
  
    public long getFreeMemory() {   
        return freeMemory;   
    }   
  
    public void setFreeMemory(long freeMemory) {   
        this.freeMemory = freeMemory;   
    }   
  
    public long getFreePhysicalMemorySize() {   
        return freePhysicalMemorySize;   
    }   
  
    public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {   
        this.freePhysicalMemorySize = freePhysicalMemorySize;   
    }   
  
    public long getMaxMemory() {   
        return maxMemory;   
    }   
  
    public void setMaxMemory(long maxMemory) {   
        this.maxMemory = maxMemory;   
    }   
  
    public String getOsName() {   
        return osName;   
    }   
  
    public void setOsName(String osName) {   
        this.osName = osName;   
    }   
     
    public long getJavaUsedMemory() {
		return javaUsedMemory;
	}

	public void setJavaUsedMemory(long javaUsedMemory) {
		this.javaUsedMemory = javaUsedMemory;
	}

	public long getTotalMemorySize() {   
        return totalMemorySize;   
    }   
  
    public void setTotalMemorySize(long totalMemorySize) {   
        this.totalMemorySize = totalMemorySize;   
    }   
  
    public int getTotalThread() {   
        return totalThread;   
    }   
  
    public void setTotalThread(int totalThread) {   
        this.totalThread = totalThread;   
    }   
  
    public long getUsedMemory() {   
        return usedMemory;   
    }   
  
    public void setUsedMemory(long usedMemory) {   
        this.usedMemory = usedMemory;   
    }   
  
    public double getCpuRatio() {   
        return cpuRatio;   
    }   
  
    public void setCpuRatio(double cpuRatio) {   
        this.cpuRatio = cpuRatio;   
    }

	public List<ProcessInfo> getProcessInfos() {
		return processInfos;
	}

	public void setProcessInfos(List<ProcessInfo> processInfos) {
		this.processInfos = processInfos;
	}   

}
