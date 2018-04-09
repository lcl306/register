package systeminfo.sigar;

import java.net.UnknownHostException;
import java.util.List;

import org.hyperic.sigar.SigarException;

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
            // 文件系统信息 
            List<DiskDto> disks1=file(); 
            System.out.println("----------------------------------"); 
            // 网络信息 
            List<NetDto> nets1=net(); 
            System.out.println("----------------------------------"); 
            Thread.sleep(1000);
            // 文件系统信息 
            List<DiskDto> disks2=file(); 
            System.out.println("----------------------------------"); 
            // 网络信息 
            List<NetDto>nets2=net(); 
            System.out.println("----------------------------------"); 
            for(int i=0; i<disks1.size(); i++){
            	DiskDto disk1 = disks1.get(i);
            	DiskDto disk2 = disks2.get(i);
            	System.out.println(disk1.getDevName() + "已读入:    " + (disk2.getDiskReads()-disk1.getDiskReads()) + "KB"); 
                System.out.println(disk1.getDevName() + "已写入:    " + (disk2.getDiskWrites()-disk1.getDiskWrites()) + "KB"); 
            }
            for(int i=0; i<nets1.size(); i++){
            	NetDto net1 = nets1.get(i);
            	NetDto net2 = nets2.get(i);
            	System.out.println(net1.getName() + "接收的总包裹数:" + (net2.getRxPackets()-net1.getRxPackets()));// 接收的总包裹数 
                System.out.println(net1.getName() + "发送的总包裹数:" + (net2.getTxPackets()-net1.getTxPackets()));// 发送的总包裹数 
                System.out.println(net1.getName() + "接收到的总字节数:" + (net2.getRxBytes()-net1.getRxBytes())/1024+"KB");// 接收到的总字节数 
                System.out.println(net1.getName() + "发送的总字节数:" + (net2.getTxBytes()-net1.getTxBytes())/1024+"KB");// 发送的总字节数 
                System.out.println(net1.getName() + "接收到的错误包数:" + (net2.getRxErrors()-net1.getRxErrors()));// 接收到的错误包数 
                System.out.println(net1.getName() + "发送数据包时的错误数:" + (net2.getTxErrors()-net1.getTxErrors()));// 发送数据包时的错误数 
                System.out.println(net1.getName() + "接收时丢弃的包数:" + (net2.getRxDropped()-net1.getRxDropped()));// 接收时丢弃的包数 
                System.out.println(net1.getName() + "发送时丢弃的包数:" + (net2.getTxDropped()-net1.getTxDropped()));// 发送时丢弃的包数 
            }
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

    private static List<DiskDto> file() throws Exception { 
    	List<DiskDto> ddtos = SystemBo.disk();
    	for(DiskDto disk : ddtos){
    		System.out.println(disk.getDevName() + "总大小:    " + disk.getTotal() + "GB"); 
            System.out.println(disk.getDevName() + "可用大小:    " + disk.getAvail() + "GB"); 
            System.out.println(disk.getDevName() + "已经使用量:    " + disk.getUsed() + "GB"); 
            System.out.println(disk.getDevName() + "资源的利用率:    " + disk.getUsePercent() + "%"); 
    	}
    	return ddtos;
    } 

    private static List<NetDto> net() throws Exception { 
    	List<NetDto> ndtos = SystemBo.net();
    	for(NetDto net : ndtos){
    		System.out.println(net.getName() + "IP地址:    " + net.getIp());// IP地址 
    		System.out.println(net.getName() + "网卡MAC地址:" + net.getMac());// 网卡MAC地址  
            System.out.println(net.getName() + "网卡描述信息:" + net.getDescription());// 网卡描述信息 
    	}
    	return ndtos;
    }
     
}
