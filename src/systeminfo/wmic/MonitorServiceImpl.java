package systeminfo.wmic;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.management.OperatingSystemMXBean;


public class MonitorServiceImpl implements IMonitorService {

	
	//可以设置长些，防止读到运行此次系统检查时的cpu占用率，就不准了   
    private static final int CPUTIME = 5000;   
  
    private static final int PERCENT = 100;   
  
    private static final int FAULTLENGTH = 10;   
  
    /** *//**  
     * 获得当前的监控对象.  
     * @return 返回构造好的监控对象  
     * @throws Exception  
     * @author amg     * Creation date: 2008-4-25 - 上午10:45:08  
     */  
    @Override
    public MonitorInfoBean getMonitorInfoBean() throws Exception {  
    	MonitorInfoBean infoBean = new MonitorInfoBean(); 
        int mb = 1024*1024;   
        // java最大可使用内存   
        long javaMaxMemory = Runtime.getRuntime().maxMemory() / mb;
        // 已使用内存
        long javaUsedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / mb;
        // 未使用内存
        long javaUnusedMemory = javaMaxMemory - javaUsedMemory;
        
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 操作系统   
        String osName = System.getProperty("os.name");   
        // 总的物理内存   
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / mb;   
        // 剩余的物理内存   
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / mb;   
        // 已使用的物理内存   
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / mb;   
  
        // 获得线程总数   
        ThreadGroup parentThread;   
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread.getParent() != null; 
        		parentThread = parentThread.getParent());   
        int totalThread = parentThread.activeCount();   
  
        double cpuRatio = 0;   
        if (osName.toLowerCase().startsWith("windows")) {   
            cpuRatio = this.getCpuRatioForWindows(infoBean.getProcessInfos());   
        }     
        infoBean.setFreeMemory(javaUnusedMemory);   
        infoBean.setFreePhysicalMemorySize(freePhysicalMemorySize);   
        infoBean.setMaxMemory(javaMaxMemory);   
        infoBean.setOsName(osName);   
        infoBean.setJavaUsedMemory(javaUsedMemory);   
        infoBean.setTotalMemorySize(totalMemorySize);   
        infoBean.setTotalThread(totalThread);   
        infoBean.setUsedMemory(usedMemory);   
        infoBean.setCpuRatio(cpuRatio);   
        return infoBean;   
    }   
  
    /** *//**  
     * 获得CPU使用率.  
     * @return 返回cpu使用率  
     * @author amg     * Creation date: 2008-4-25 - 下午06:05:11  
     */  
    private double getCpuRatioForWindows(List<ProcessInfo> processInfos) {  
        try {   
            String procCmd = System.getenv("windir")   
                    + "//system32//wbem//wmic.exe process get Caption,CommandLine,"  
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount,WorkingSetSize,PeakWorkingSetSize";   
            // 取进程信息   
            Map<String,ProcessInfo> processInfosPre = new HashMap<String, ProcessInfo>();
            Map<String,ProcessInfo> processInfosCur = new HashMap<String, ProcessInfo>();
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd), processInfosPre);   
            Thread.sleep(CPUTIME);   
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd), processInfosCur);   
            if (c0 != null && c1 != null) {   
                long idletime = c1[0] - c0[0];   
                long busytime = c1[1] - c0[1];
                for(String key : processInfosCur.keySet()){
                	ProcessInfo cur = processInfosCur.get(key);
                	ProcessInfo pre = processInfosPre.get(key);
                	if(pre!=null){
                		cur.setCpuRatio((cur.getKernelModeTime()+cur.getUserModeTime()-pre.getKernelModeTime()-pre.getUserModeTime())*PERCENT/(busytime+idletime));
                		//cur.setWorkingSetSize(cur.getWorkingSetSize()-pre.getWorkingSetSize());
                		processInfos.add(cur);
                	}
                }
                return Double.valueOf( PERCENT * (busytime) / (busytime + idletime)).doubleValue();   
            } else {   
                return 0.0;   
            }   
        } catch (Exception ex) {   
            ex.printStackTrace();   
            return 0.0;   
        }   
    }   
  
    /** *//**  
     * 读取CPU信息.  
     * @param proc  
     * @return  
     * @author amg     * Creation date: 2008-4-25 - 下午06:10:14  
     */  
    private long[] readCpu(final Process proc, Map<String,ProcessInfo> processInfos) {   
        long[] retn = new long[2]; 
        long idletime = 0;   
        long kneltime = 0;   
        long usertime = 0;
        String caption = "";
        try {   
            proc.getOutputStream().close();   
            InputStreamReader ir = new InputStreamReader(proc.getInputStream(),Bytes.CODE_NAME);   
            LineNumberReader input = new LineNumberReader(ir);   
            String line = input.readLine();   
            if (line == null || line.length() < FAULTLENGTH) {   
                return null;   
            }   
            int capidx = line.indexOf("Caption");   
            int cmdidx = line.indexOf("CommandLine");   
            int rocidx = line.indexOf("ReadOperationCount");   
            int umtidx = line.indexOf("UserModeTime");   
            int kmtidx = line.indexOf("KernelModeTime");   
            int wocidx = line.indexOf("WriteOperationCount"); 
            int pwsidx = line.indexOf("PeakWorkingSetSize");
            int wssidx = line.lastIndexOf("WorkingSetSize");
               
            while ((line = input.readLine()) != null) {   
                if (line.length() < wocidx) {   
                    continue;   
                }   
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,PeakWorkingSetSize,ReadOperationCount,ThreadCount,UserModeTime,WorkingSetSize,WriteOperationCount 
                caption = Bytes.substring(line, capidx, cmdidx - 1).trim();   
                String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();   
                if (cmd.indexOf("wmic.exe") >= 0) {   
                    continue;   
                }     
                if (caption.equals("System Idle Process") || caption.equals("System")) {   
                    idletime += Long.valueOf(Bytes.substring(line, kmtidx, pwsidx - 1).trim()).longValue();   
                    idletime += Long.valueOf(Bytes.substring(line, umtidx, wssidx - 1).trim()).longValue();   
                }else{
                	long ktime = Long.valueOf(Bytes.substring(line, kmtidx, pwsidx - 1).trim()).longValue();
                	long utime = Long.valueOf(Bytes.substring(line, umtidx, wssidx - 1).trim()).longValue();
                	long pMemory = Long.valueOf(Bytes.substring(line, pwsidx, rocidx - 1).trim()).longValue();
                	long cMemory = Long.valueOf(Bytes.substring(line, wssidx, wocidx - 1).trim()).longValue();
                	kneltime += ktime;
                    usertime += utime;
                    ProcessInfo pi = new ProcessInfo();
                    pi.setCaption(caption);
                    pi.setCommandLine(cmd);
                    pi.setKernelModeTime(ktime);
                    pi.setUserModeTime(utime);
                    pi.setPeakWorkingSetSize(pMemory);
                    pi.setWorkingSetSize(cMemory/1024);
                    processInfos.put(caption+cmd,pi);
                }
            }   
            retn[0] = idletime;   
            retn[1] = kneltime + usertime;   
            return retn;   
        } catch (Exception ex) {   
            ex.printStackTrace();
            System.out.println(caption);
        } finally {   
            try {   
                proc.getInputStream().close();   
            } catch (Exception e) {   
                e.printStackTrace();   
            }   
        }   
        return null;   
    }   
       
    /** *//**  
     * 测试方法.  
     * @param args  
     * @throws Exception  
     * @author amg     * Creation date: 2008-4-30 - 下午04:47:29  
     */  
    public static void main(String[] args) throws Exception {   
        IMonitorService service = new MonitorServiceImpl();   
        MonitorInfoBean monitorInfo = service.getMonitorInfoBean();   
        System.out.println("cpu占有率=" + monitorInfo.getCpuRatio());   
           
        System.out.println("App已使用内存=" + monitorInfo.getJavaUsedMemory()+"MB");   
        System.out.println("App剩余内存=" + monitorInfo.getFreeMemory()+"MB");   
        System.out.println("App最大可使用内存=" + monitorInfo.getMaxMemory()+"MB");   
           
        System.out.println("操作系统=" + monitorInfo.getOsName());   
        System.out.println("总物理内存=" + monitorInfo.getTotalMemorySize() + "MB");   
        System.out.println("剩余物理内存=" + monitorInfo.getFreeMemory() + "MB");   
        System.out.println("已使用物理内存=" + monitorInfo.getUsedMemory() + "MB");   
        System.out.println("线程总数=" + monitorInfo.getTotalThread() + "KB");   
        for(ProcessInfo pi : monitorInfo.getProcessInfos()){
        	System.out.println("进程=" + pi.getCaption());
        	System.out.println("命令=" +pi.getCommandLine());
        	System.out.println("cpu占用率="+pi.getCpuRatio()+"%");
        	System.out.println("最大内存=" + pi.getPeakWorkingSetSize()+"KB");
        	System.out.println("当前内存=" + pi.getWorkingSetSize()+"KB");
        }
    }   

}
