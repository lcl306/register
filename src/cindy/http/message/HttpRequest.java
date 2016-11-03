package cindy.http.message;

public class HttpRequest extends HttpMessage {
	
	private String method;
	private String uri;
	
	public String getRequestMethod() {
		return method;
	}
	public void setRequestMethod(String method) {
		this.method = method;
	}
	public String getRequestUri() {
		return uri;
	}
	public void setRequestUri(String uri) {
		this.uri = uri;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(method).append(" ");
		buffer.append(uri).append(" ");
		buffer.append(getVersion()).append("\r\n");
		buffer.append(super.toString());
		return buffer.toString();
	}

}
