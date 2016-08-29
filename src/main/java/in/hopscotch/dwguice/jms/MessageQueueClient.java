package in.hopscotch.dwguice.jms;

public class MessageQueueClient {
	
	private String host;
	private int port;
	
	public MessageQueueClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public void close() {
		
	}
	
}
