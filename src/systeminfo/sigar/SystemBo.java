package systeminfo.sigar;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Who;

public class SystemBo {
	
	public static final long K = 1024;
	
	public static final long M = K*K;
	
	public static final int PERCENT = 100;
	
	/**单位秒*/
	public static int SLEEP_TIME = 1;
	
	
	public static SystemDto system() throws Exception{
		SystemDto sDto = new SystemDto();
		List<DiskDto> disks1=disk(); 
		List<NetDto> nets1=net(); 
        Thread.sleep(SLEEP_TIME*1000);
        List<DiskDto> disks2=disk(); 
        List<NetDto>nets2=net(); 
        for(int i=0; i<disks1.size(); i++){
        	DiskDto disk1 = disks1.get(i);
        	DiskDto disk2 = disks2.get(i);
        	disk2.setDiskReads(disk2.getDiskReads()-disk1.getDiskReads());
        	disk2.setDiskWrites(disk2.getDiskWrites()-disk1.getDiskWrites());
        }
        for(int i=0; i<nets1.size(); i++){
        	NetDto net1 = nets1.get(i);
        	NetDto net2 = nets2.get(i);
        	net2.setRxPackets((net2.getRxPackets()-net1.getRxPackets())/SLEEP_TIME);
        	net2.setTxPackets((net2.getTxPackets()-net1.getTxPackets())/SLEEP_TIME);
        	net2.setRxBytes((net2.getRxBytes()-net1.getRxBytes())/SLEEP_TIME);
        	net2.setTxBytes((net2.getTxBytes()-net1.getTxBytes())/SLEEP_TIME);
        	net2.setRxErrors((net2.getRxErrors()-net1.getRxErrors())/SLEEP_TIME);
        	net2.setTxErrors((net2.getTxErrors()-net1.getTxErrors())/SLEEP_TIME);
        	net2.setRxDropped((net2.getRxDropped()-net1.getRxDropped())/SLEEP_TIME);
        	net2.setTxDropped((net2.getTxDropped()-net1.getTxDropped())/SLEEP_TIME);
        }
        sDto.setBaseSystemDto(property()); 
        sDto.setMemoryDto(memory());
        sDto.setCpuDtos(cpu());
        sDto.setDiskDtos(disks2);
        sDto.setNetDtos(nets2);
        return sDto;
	}
	
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
	
	public static List<DiskDto> disk() throws Exception { 
		List<DiskDto> diskDtos = new ArrayList<DiskDto>();
        Sigar sigar = new Sigar(); 
        FileSystem fslist[] = sigar.getFileSystemList(); 
        for (int i = 0; i < fslist.length; i++) { 
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
            try{
            	usage = sigar.getFileSystemUsage(fs.getDirName()); 
            }catch (SigarException e){
            	//System.out.println("读到光驱了");
            }
            switch (fs.getType()) { 
            case 0: // TYPE_UNKNOWN ：未知 
                break; 
            case 1: // TYPE_NONE 
                break; 
            case 2: // TYPE_LOCAL_DISK : 本地硬盘 
            	DiskDto ddto = new DiskDto();
            	ddto.setDevName(fs.getDevName());
            	ddto.setTotal(usage.getTotal()/M);
            	ddto.setUsed(usage.getUsed()/M);
            	ddto.setAvail(usage.getAvail()/M);
            	ddto.setUsePercent(usage.getUsePercent() * PERCENT);
            	ddto.setDiskReads(usage.getDiskReads());
            	ddto.setDiskWrites(usage.getDiskWrites());
                // 文件系统剩余大小 
                //System.out.println(fs.getDevName() + "剩余大小:    " + usage.getFree()/MB + "GB"); 
            	diskDtos.add(ddto);
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
        } 
        return diskDtos; 
    } 
	
	public static List<NetDto> net() throws Exception { 
		List<NetDto> netDtos = new ArrayList<NetDto>();
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
                //System.out.println(ifconfig.getName() + "网关广播地址:" + ifconfig.getBroadcast());// 网关广播地址
                //System.out.println(ifconfig.getName() + "网卡类型" + ifconfig.getType());// 
                NetDto ndto = new NetDto();
                ndto.setName(name);
                ndto.setIp(ifconfig.getAddress());
                ndto.setMask(ifconfig.getNetmask());
                ndto.setMac(ifconfig.getHwaddr());
                ndto.setDescription(ifconfig.getDescription());
               
                NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
                ndto.setRxPackets(ifstat.getRxPackets());
                ndto.setTxPackets(ifstat.getTxPackets());
                ndto.setRxBytes(ifstat.getRxBytes());
                ndto.setTxBytes(ifstat.getTxBytes());
                ndto.setRxErrors(ifstat.getRxErrors());
                ndto.setTxErrors(ifstat.getTxErrors());
                ndto.setRxDropped(ifstat.getRxDropped());
                ndto.setTxDropped(ifstat.getTxDropped());
                netDtos.add(ndto);
        	}
        } 
        return netDtos;
    }
    
    public static void os() { 
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

    public static void who() throws SigarException { 
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
