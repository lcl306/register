package systeminfo.sigar;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

public class SystemBo {
	
	public static final long M = 1024*1024;
	
	public static final int PERCENT = 100;
	
	public static final int SLEEP_TIME = 1000;
	
	public static BaseSystemDto property() throws UnknownHostException { 
        Runtime r = Runtime.getRuntime(); 
        Properties props = System.getProperties(); 
        InetAddress addr; 
        addr = InetAddress.getLocalHost(); 
        String ip = addr.getHostAddress(); 
        Map<String, String> map = System.getenv(); 
        //String userName = map.get("USERNAME");// 获取用户名 
        String computerName = map.get("COMPUTERNAME");// 获取计算机名 
        String userDomain = map.get("USERDOMAIN");// 获取计算机域名 
        long javaMaxMemory = r.maxMemory() / M;
        // 已使用内存
        long javaUsedMemory = (r.totalMemory() - r.freeMemory()) / M;
        // 未使用内存
        long javaUnusedMemory = javaMaxMemory - javaUsedMemory;
        //System.out.println("用户名:    " + userName);
        BaseSystemDto bdto = new BaseSystemDto();
        bdto.setComputerName(computerName);
        bdto.setUserDomain(userDomain);
        bdto.setIp(ip);
        bdto.setHostname(addr.getHostName());
        bdto.setOsname(props.getProperty("os.name"));
        bdto.setJavaMaxMemory(javaMaxMemory);
        bdto.setJavaUnusedMemory(javaUnusedMemory);
        bdto.setJavaMemusedPercent(PERCENT-javaUnusedMemory*PERCENT/javaMaxMemory);
        bdto.setJavaProcessorNum(r.availableProcessors());
        bdto.setJavaVersion(props.getProperty("java.version"));
        //System.out.println("Java的运行环境供应商：    " + props.getProperty("java.vendor")); 
        //System.out.println("Java供应商的URL：    " + props.getProperty("java.vendor.url")); 
        //System.out.println("Java的安装路径：    " + props.getProperty("java.home")); 
        //System.out.println("Java的虚拟机规范版本：    " + props.getProperty("java.vm.specification.version")); 
        //System.out.println("Java的虚拟机规范供应商：    " + props.getProperty("java.vm.specification.vendor")); 
        //System.out.println("Java的虚拟机规范名称：    " + props.getProperty("java.vm.specification.name")); 
        //System.out.println("Java的虚拟机实现版本：    " + props.getProperty("java.vm.version")); 
        //System.out.println("Java的虚拟机实现供应商：    " + props.getProperty("java.vm.vendor")); 
        //System.out.println("Java的虚拟机实现名称：    " + props.getProperty("java.vm.name")); 
        //System.out.println("Java运行时环境规范版本：    " + props.getProperty("java.specification.version")); 
        //System.out.println("Java运行时环境规范供应商：    " + props.getProperty("java.specification.vender")); 
        //System.out.println("Java运行时环境规范名称：    " + props.getProperty("java.specification.name")); 
        //System.out.println("Java的类格式版本号：    " + props.getProperty("java.class.version")); 
        //System.out.println("Java的类路径：    " + props.getProperty("java.class.path")); 
        //System.out.println("加载库时搜索的路径列表：    " + props.getProperty("java.library.path")); 
        //System.out.println("默认的临时文件路径：    " + props.getProperty("java.io.tmpdir")); 
        //System.out.println("一个或多个扩展目录的路径：    " + props.getProperty("java.ext.dirs")); 
        //System.out.println("操作系统的构架：    " + props.getProperty("os.arch")); 
        //System.out.println("操作系统的版本：    " + props.getProperty("os.version")); 
        //System.out.println("文件分隔符：    " + props.getProperty("file.separator")); 
        //System.out.println("路径分隔符：    " + props.getProperty("path.separator")); 
        //System.out.println("行分隔符：    " + props.getProperty("line.separator")); 
        //System.out.println("用户的账户名称：    " + props.getProperty("user.name")); 
        //System.out.println("用户的主目录：    " + props.getProperty("user.home")); 
        //System.out.println("用户的当前工作目录：    " + props.getProperty("user.dir")); 
        return bdto;
    } 
	
	public static MemoryDto memory() throws SigarException { 
        Sigar sigar = new Sigar(); 
        Mem mem = sigar.getMem(); 
        MemoryDto mdto = new MemoryDto();
        mdto.setMemTotal(mem.getTotal() / M );
        mdto.setMemFree(mem.getFree() / M );
        mdto.setMemUsed(mem.getUsed() / M );
        mdto.setMemUsePercent(mem.getUsed()*PERCENT/mem.getTotal());
        Swap swap = sigar.getSwap(); 
        mdto.setSwapTotal(swap.getTotal() / M);
        mdto.setSwapUsed(swap.getUsed() / M);
        mdto.setSwapFree(swap.getFree() / M);
        mdto.setSwapUsePercent(swap.getUsed()*PERCENT/swap.getTotal());
        return mdto;
    }
	
	public static List<CpuDto> cpu() throws SigarException { 
		List<CpuDto> cdtos = new ArrayList<CpuDto>();
        Sigar sigar = new Sigar(); 
        CpuInfo infos[] = sigar.getCpuInfoList(); 
        CpuPerc cpuList[] = null; 
        cpuList = sigar.getCpuPercList(); 
        for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用 
            CpuInfo info = infos[i];  
            CpuDto cdto = new CpuDto();
            cdto.setModel(info.getModel());
            cdto.setUserUse(CpuPerc.format(cpuList[i].getUser()));
            cdto.setSysUse(CpuPerc.format(cpuList[i].getSys()));
            cdto.setWait(CpuPerc.format(cpuList[i].getWait()));
            cdto.setNice(CpuPerc.format(cpuList[i].getNice()));
            cdto.setIdle(CpuPerc.format(cpuList[i].getIdle()));
            cdto.setCombined(CpuPerc.format(cpuList[i].getCombined()));
            //System.out.println("CPU的总量MHz:    " + info.getMhz());// CPU的总量MHz 
            //System.out.println("CPU生产商:    " + info.getVendor());// 获得CPU的卖主，如：Intel  
            //System.out.println("CPU缓存数量:    " + info.getCacheSize());// 缓冲存储器数量 
            cdtos.add(cdto);
        } 
        return cdtos;
    } 

}
