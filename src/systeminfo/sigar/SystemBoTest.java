package systeminfo.sigar;

import java.util.List;

public class SystemBoTest {
	
	public static final long M = 1024*1024;
	
	public static final int PERCENT = 100;
	
	public static final int SLEEP_TIME = 1000;
	
	
	public static void main(String[] args) { 
        try { 
            system();
        } catch (Exception e1) { 
            e1.printStackTrace(); 
        } 
    } 
	
	private static void system() throws Exception{
		SystemDto system = SystemBo.system();
		BaseSystemDto bdto = system.getBaseSystemDto();
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
        System.out.println("----------------------------------"); 
        MemoryDto mdto = system.getMemoryDto();
        System.out.println("内存总量:    " + mdto.getMemTotal() + "MB"); 
        System.out.println("当前内存使用量:    " + mdto.getMemUsed() + "MB"); 
        System.out.println("当前内存剩余量:    " + mdto.getMemFree() + "MB"); 
        System.out.println("当前内存使用率:    " + mdto.getMemUsePercent()  + "%");
        System.out.println("虚拟内存总量:    " + mdto.getSwapTotal() + "MB"); 
        System.out.println("虚拟内存使用量:    " + mdto.getSwapUsed()+ "MB"); 
        System.out.println("虚拟内存剩余量:    " + mdto.getSwapFree() + "MB"); 
        System.out.println("虚拟内存使用率:    " + mdto.getSwapUsePercent()  + "%");
        System.out.println("----------------------------------"); 
        List<CpuDto> cpus = system.getCpuDtos();
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
    	System.out.println("----------------------------------"); 
		List<DiskDto> disks = system.getDiskDtos();
		for(DiskDto disk : disks){
    		System.out.println(disk.getDevName() + "总大小:    " + disk.getTotal() + "GB"); 
            System.out.println(disk.getDevName() + "可用大小:    " + disk.getAvail() + "GB"); 
            System.out.println(disk.getDevName() + "已经使用量:    " + disk.getUsed() + "GB"); 
            System.out.println(disk.getDevName() + "资源的利用率:    " + disk.getUsePercent() + "%"); 
            System.out.println(disk.getDevName() + "已读入:    " + disk.getDiskReads() + "KB"); 
            System.out.println(disk.getDevName() + "已写入:    " + disk.getDiskWrites() + "KB");
    	}
		System.out.println("----------------------------------"); 
		List<NetDto> nets = system.getNetDtos();
    	for(NetDto net : nets){
    		System.out.println(net.getName() + "IP地址:    " + net.getIp());// IP地址 
    		System.out.println(net.getName() + "网卡MAC地址:" + net.getMac());// 网卡MAC地址  
            System.out.println(net.getName() + "网卡描述信息:" + net.getDescription());// 网卡描述信息 
            System.out.println(net.getName() + "接收的总包裹数:" + net.getRxPackets());// 接收的总包裹数 
            System.out.println(net.getName() + "发送的总包裹数:" + net.getTxPackets());// 发送的总包裹数 
            System.out.println(net.getName() + "接收到的总字节数:" + net.getRxBytes()/SystemBo.K+"KB");// 接收到的总字节数 
            System.out.println(net.getName() + "发送的总字节数:" + net.getTxBytes()/SystemBo.K+"KB");// 发送的总字节数 
            System.out.println(net.getName() + "接收到的错误包数:" + net.getRxErrors());// 接收到的错误包数 
            System.out.println(net.getName() + "发送数据包时的错误数:" + net.getTxErrors());// 发送数据包时的错误数 
            System.out.println(net.getName() + "接收时丢弃的包数:" + net.getRxDropped());// 接收时丢弃的包数 
            System.out.println(net.getName() + "发送时丢弃的包数:" + net.getTxDropped());// 发送时丢弃的包数 
    	}
	}
     
}
