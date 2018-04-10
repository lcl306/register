package systeminfo.sigar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SystemDto implements Serializable{

	private static final long serialVersionUID = 3000661766752810930L;
	
	private BaseSystemDto baseSystemDto;
	
	private MemoryDto memoryDto;
	
	private List<CpuDto> cpuDtos = new ArrayList<CpuDto>();
	
	private List<DiskDto> diskDtos = new ArrayList<DiskDto>();
	
	private List<NetDto> netDtos = new ArrayList<NetDto>();

	public BaseSystemDto getBaseSystemDto() {
		return baseSystemDto;
	}

	public void setBaseSystemDto(BaseSystemDto baseSystemDto) {
		this.baseSystemDto = baseSystemDto;
	}

	public MemoryDto getMemoryDto() {
		return memoryDto;
	}

	public void setMemoryDto(MemoryDto memoryDto) {
		this.memoryDto = memoryDto;
	}

	public List<CpuDto> getCpuDtos() {
		return cpuDtos;
	}

	public void setCpuDtos(List<CpuDto> cpuDtos) {
		this.cpuDtos = cpuDtos;
	}

	public List<DiskDto> getDiskDtos() {
		return diskDtos;
	}

	public void setDiskDtos(List<DiskDto> diskDtos) {
		this.diskDtos = diskDtos;
	}

	public List<NetDto> getNetDtos() {
		return netDtos;
	}

	public void setNetDtos(List<NetDto> netDtos) {
		this.netDtos = netDtos;
	}

}
