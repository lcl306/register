package cindy.http.message;

public class HttpResponse extends HttpMessage {
	
	private int statusCode;
	
	private String reasonPhrase;

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(getVersion()).append(" ").append(statusCode).append(" ")
			  .append(reasonPhrase).append("\r\n");
		buffer.append(super.toString());
		return buffer.toString();
	}

}
