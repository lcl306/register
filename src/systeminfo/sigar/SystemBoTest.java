package systeminfo.sigar;

import java.net.UnknownHostException;
import java.util.List;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Who;

public class SystemBoTest {
	
	public static final long M = 1024*1024;
	
	public static final int PERCENT = 100;
	
	public static final int SLEEP_TIME = 1000;
	
	
	public static void main(String[] args) { 
        try { 
            // System信息，从jvm获取 
            property(); 
            System.out.println("----------------------------------"); 
            // cpu信息 
            cpu(); 
            System.out.println("----------------------------------"); 
            // 内存信息 
            memory(); 
            System.out.println("----------------------------------"); 
            // 操作系统信息 
            /*os(); 
            System.out.println("----------------------------------"); */
            // 用户信息 
            /*who(); 
            System.out.println("----------------------------------");*/ 
            // 文件系统信息 
            file(); 
            System.out.println("----------------------------------"); 
            // 网络信息 
            net(); 
            System.out.println("----------------------------------"); 
        } catch (Exception e1) { 
            e1.printStackTrace(); 
        } 
    } 

    private static void property() throws UnknownHostException { 
        BaseSystemDto bdto = SystemBo.property();
        System.out.println("计算机名:    " + bdto.getComputerName()); 
        System.out.println("计算机域名:    " + bdto.getUserDomain()); 
        System.out.println("本地ip地址:    " + bdto.getIp()); 
        System.out.println("本地主机名:    " + bdto.getHostname()); 
        System.out.println("操作系统的名称：    " + bdto.getOsname()); 
        System.out.println("JVM可以使用的总内存:    " + bdto.getJavaMaxMemory()+"MB"); 
        System.out.println("JVM可以使用的剩余内存:    " + bdto.getJavaUnusedMemory()+"MB"); 
        System.out.println("JVM内存使用率:    " + bdto.getJavaMemusedPercent()+"%"); 
        System.out.println("JVM可以使用的处理器个数:    " + bdto.getJavaProcessorNum()); 
        System.out.println("Java的运行环境版本：    " + bdto.getJavaVersion()); 
    } 

    private static void memory() throws SigarException { 
        MemoryDto mdto = SystemBo.memory();
        System.out.println("内存总量:    " + mdto.getMemTotal() + "MB"); 
        System.out.println("当前内存使用量:    " + mdto.getMemUsed() + "MB"); 
        System.out.println("当前内存剩余量:    " + mdto.getMemFree() + "MB"); 
        System.out.println("当前内存使用率:    " + mdto.getMemUsePercent()  + "%");
        System.out.println("虚拟内存总量:    " + mdto.getSwapTotal() + "MB"); 
        System.out.println("虚拟内存使用量:    " + mdto.getSwapUsed()+ "MB"); 
        System.out.println("虚拟内存剩余量:    " + mdto.getSwapFree() + "MB"); 
        System.out.println("虚拟内存使用率:    " + mdto.getSwapUsePercent()  + "%");
    } 

    private static void cpu() throws SigarException { 
    	List<CpuDto> cpus = SystemBo.cpu();
    	int i =0;
    	for(CpuDto cpu : cpus){
    		System.out.println("第" + (i + 1) + "块CPU信息"); 
    		System.out.println("CPU类别:    " + cpu.getModel());
    		System.out.println("CPU用户使用率:    " + cpu.getUserUse());// 用户使用率 
            System.out.println("CPU系统使用率:    " + cpu.getSysUse());// 系统使用率 
            System.out.println("CPU当前等待率:    " + cpu.getWait());// 当前等待率 
            System.out.println("CPU当前错误率:    " + cpu.getNice());// 
            System.out.println("CPU当前空闲率:    " + cpu.getIdle());// 当前空闲率 
            System.out.println("CPU总的使用率:    " + cpu.getCombined());// 总的使用率
    	}
    } 

    private static void file() throws Exception { 
        Sigar sigar = new Sigar(); 
        FileSystem fslist[] = sigar.getFileSystemList(); 
        for (int i = 0; i < fslist.length; i++) { 
            System.out.println("分区的盘符名称" + i); 
            FileSystem fs = fslist[i]; 
            // 分区的盘符名称 
            //System.out.println("盘符名称:    " + fs.getDevName()); 
            // 分区的盘符名称 
            //System.out.println("盘符路径:    " + fs.getDirName()); 
            //System.out.println("盘符标志:    " + fs.getFlags());// 
            // 文件系统类型，比如 FAT32、NTFS 
            //System.out.println("盘符类型:    " + fs.getSysTypeName()); 
            // 文件系统类型名，比如本地硬盘、光驱、网络文件系统等 
            //System.out.println("盘符类型名:    " + fs.getTypeName()); 
            // 文件系统类型 
            //System.out.println("盘符文件系统类型:    " + fs.getType()); 
            FileSystemUsage usage = null; 
            usage = sigar.getFileSystemUsage(fs.getDirName()); 
            switch (fs.getType()) { 
            case 0: // TYPE_UNKNOWN ：未知 
                break; 
            case 1: // TYPE_NONE 
                break; 
            case 2: // TYPE_LOCAL_DISK : 本地硬盘 
                // 文件系统总大小 
                System.out.println(fs.getDevName() + "总大小:    " + usage.getTotal()/M + "GB"); 
                // 文件系统剩余大小 
                //System.out.println(fs.getDevName() + "剩余大小:    " + usage.getFree()/MB + "GB"); 
                // 文件系统可用大小 
                System.out.println(fs.getDevName() + "可用大小:    " + usage.getAvail()/M + "GB"); 
                // 文件系统已经使用量 
                System.out.println(fs.getDevName() + "已经使用量:    " + usage.getUsed()/M + "GB"); 
                double usePercent = usage.getUsePercent() * PERCENT; 
                // 文件系统资源的利用率 
                System.out.println(fs.getDevName() + "资源的利用率:    " + usePercent + "%"); 
                break; 
            case 3:// TYPE_NETWORK ：网络 
                break; 
            case 4:// TYPE_RAM_DISK ：闪存 
                break; 
            case 5:// TYPE_CDROM ：光驱 
                break; 
            case 6:// TYPE_SWAP ：页面交换 
                break; 
            } 
            long reads = usage.getDiskReads();
            long writes = usage.getDiskWrites();
            Thread.sleep(SLEEP_TIME);
            System.out.println(fs.getDevName() + "读出：    " + (usage.getDiskReads()-reads)+"KB"); 
            System.out.println(fs.getDevName() + "写入：    " + (usage.getDiskWrites()-writes)+"KB"); 
        } 
        return; 
    } 

    private static void net() throws Exception { 
        Sigar sigar = new Sigar(); 
        String ifNames[] = sigar.getNetInterfaceList(); 
        for (int i = 0; i < ifNames.length; i++) { 
        	String name = ifNames[i];
        	NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name); 
        	if(!ifconfig.getAddress().equals("0.0.0.0") && !ifconfig.getAddress().equals("127.0.0.1")){
        		if ((ifconfig.getFlags() & 1L) <= 0L) { 
                    System.out.println("!IFF_UP...skipping getNetInterfaceStat"); 
                    continue; 
                } 
                if (NetFlags.LOOPBACK_ADDRESS.equals(ifconfig.getAddress()) || (ifconfig.getFlags() & NetFlags.IFF_LOOPBACK) != 0 
                        || NetFlags.NULL_HWADDR.equals(ifconfig.getHwaddr())) { 
                    continue; 
                } 
        		//System.out.println("网络设备名:    " + name);// 网络设备名 
                System.out.println("IP地址:    " + ifconfig.getAddress());// IP地址 
                //System.out.println("子网掩码:    " + ifconfig.getNetmask());// 子网掩码
                //System.out.println(ifconfig.getName() + "网关广播地址:" + ifconfig.getBroadcast());// 网关广播地址 
                System.out.println(ifconfig.getName() + "网卡MAC地址:" + ifconfig.getHwaddr());// 网卡MAC地址  
                System.out.println(ifconfig.getName() + "网卡描述信息:" + ifconfig.getDescription());// 网卡描述信息 
                //System.out.println(ifconfig.getName() + "网卡类型" + ifconfig.getType());// 
                
                NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name); 
                System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets()/M+"M");// 接收的总包裹数 
                System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets()/M+"M");// 发送的总包裹数 
                System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes()/M+"MB");// 接收到的总字节数 
                System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes()/M+"MB");// 发送的总字节数 
                System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors()/M+"M");// 接收到的错误包数 
                System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors()/M+"M");// 发送数据包时的错误数 
                System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped()/M+"M");// 接收时丢弃的包数 
                System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped()/M+"M");// 发送时丢弃的包数 
        	}
        } 
    }
    
    private static void os() { 
        OperatingSystem OS = OperatingSystem.getInstance(); 
        // 操作系统内核类型如： 386、486、586等x86 
        System.out.println("操作系统:    " + OS.getArch()); 
        System.out.println("操作系统CpuEndian():    " + OS.getCpuEndian());// 
        System.out.println("操作系统DataModel():    " + OS.getDataModel());// 
        // 系统描述 
        System.out.println("操作系统的描述:    " + OS.getDescription()); 
        // 操作系统类型 
        // System.out.println("OS.getName():    " + OS.getName()); 
        // System.out.println("OS.getPatchLevel():    " + OS.getPatchLevel());// 
        // 操作系统的卖主 
        System.out.println("操作系统的卖主:    " + OS.getVendor()); 
        // 卖主名称 
        System.out.println("操作系统的卖主名:    " + OS.getVendorCodeName()); 
        // 操作系统名称 
        System.out.println("操作系统名称:    " + OS.getVendorName()); 
        // 操作系统卖主类型 
        System.out.println("操作系统卖主类型:    " + OS.getVendorVersion()); 
        // 操作系统的版本号 
        System.out.println("操作系统的版本号:    " + OS.getVersion()); 
    } 

    private static void who() throws SigarException { 
        Sigar sigar = new Sigar(); 
        Who who[] = sigar.getWhoList(); 
        if (who != null && who.length > 0) { 
            for (int i = 0; i < who.length; i++) { 
                // System.out.println("当前系统进程表中的用户名" + String.valueOf(i)); 
                Who _who = who[i]; 
                System.out.println("用户控制台:    " + _who.getDevice()); 
                System.out.println("用户host:    " + _who.getHost()); 
                // System.out.println("getTime():    " + _who.getTime()); 
                // 当前系统进程表中的用户名 
                System.out.println("当前系统进程表中的用户名:    " + _who.getUser()); 
            } 
        } 
    } 
}
