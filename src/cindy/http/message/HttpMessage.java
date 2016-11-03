package cindy.http.message;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpMessage {

	private static final byte[] EMPTY_CONTENT = new byte[0];
	
	private final Map params = new LinkedHashMap();
	
	private String version = "HTTP/1.0";
	
	private byte[] content = EMPTY_CONTENT;

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		if(content==null) content = EMPTY_CONTENT;
		this.content = content;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map getParams() {
		return Collections.unmodifiableMap(params);
	}
	
	public final String getParam(String key){
		return key == null ? null : (String)params.get(key);
	}
	
	public final void setParam(String key, String value){
		if(key!=null){
			if(value==null) params.remove(key);
			else params.put(key, value);
		}
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		for(Iterator it = params.entrySet().iterator(); it.hasNext();){
			Entry entry = (Entry)it.next();
			buffer.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
		}
		buffer.append("\r\n");
		return buffer.toString();
	}
	
}
