package minas.mina.filter.myprotocol;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;

public class ProgrammeDto {
	
	private Logger logger = Logger.getLogger(ProgrammeDto.class);
	
	private int dayIndex;
	
	private int beginTime;
	
	private int totalTime;
	
	private int status;
	
	private String url;
	
	private String programmeName;
	
	public int getLen(Charset charset){
		int len = 0;
		try{
			if(programmeName!=null && !"".equals(programmeName)){
				len += programmeName.getBytes(charset).length;
			}
			if(url!=null && !"".equals(url)){
				len += url.getBytes(charset).length;
			}
		}catch(Exception e){
			logger.error("节目信息转换为字节码错误...",e);
		}
		return len;
	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}

	public int getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProgrammeName() {
		return programmeName;
	}

	public void setProgrammeName(String programmeName) {
		this.programmeName = programmeName;
	}

}
