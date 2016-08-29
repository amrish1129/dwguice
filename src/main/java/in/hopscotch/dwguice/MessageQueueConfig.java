package in.hopscotch.dwguice;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageQueueConfig {
	@JsonProperty
    @NotNull
    private String brokerUrl;

    @JsonProperty
    private String brokerUsername;

    @JsonProperty
    private String brokerPassword;

    @JsonProperty
    private long healthCheckMillisecondsToWait = 2000; // 2 seconds

    @JsonProperty
    private int shutdownWaitInSeconds = 20;

    @JsonProperty
    private int timeToLiveInSeconds = -1; // Default no TTL. Jackson does not support java.util.Optional yet.
    
    @JsonProperty
	public String getBrokerUrl() {
		return brokerUrl;
	}
    
    @JsonProperty
	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

    @JsonProperty
	public String getBrokerUsername() {
		return brokerUsername;
	}

    @JsonProperty
	public void setBrokerUsername(String brokerUsername) {
		this.brokerUsername = brokerUsername;
	}

    @JsonProperty
	public String getBrokerPassword() {
		return brokerPassword;
	}

    @JsonProperty
	public void setBrokerPassword(String brokerPassword) {
		this.brokerPassword = brokerPassword;
	}

    @JsonProperty
	public long getHealthCheckMillisecondsToWait() {
		return healthCheckMillisecondsToWait;
	}

    @JsonProperty
	public void setHealthCheckMillisecondsToWait(long healthCheckMillisecondsToWait) {
		this.healthCheckMillisecondsToWait = healthCheckMillisecondsToWait;
	}

    @JsonProperty
	public int getShutdownWaitInSeconds() {
		return shutdownWaitInSeconds;
	}

    @JsonProperty
	public void setShutdownWaitInSeconds(int shutdownWaitInSeconds) {
		this.shutdownWaitInSeconds = shutdownWaitInSeconds;
	}

    @JsonProperty
	public int getTimeToLiveInSeconds() {
		return timeToLiveInSeconds;
	}

    @JsonProperty
	public void setTimeToLiveInSeconds(int timeToLiveInSeconds) {
		this.timeToLiveInSeconds = timeToLiveInSeconds;
	}

    @JsonProperty
	public MessageQueuePoolConfig getPool() {
		return pool;
	}
    
    @JsonProperty
	public void setPool(MessageQueuePoolConfig pool) {
		this.pool = pool;
	}

	@JsonProperty
    @Valid
    private MessageQueuePoolConfig pool;

    @Override
    public String toString() {
        return "ActiveMQConfig{" +
                "brokerUrl='" + brokerUrl + '\'' +
                ", healthCheckMillisecondsToWait=" + healthCheckMillisecondsToWait +
                ", shutdownWaitInSeconds=" + shutdownWaitInSeconds +
                ", timeToLiveInSeconds=" + timeToLiveInSeconds +
                ", brokerUsername=" + brokerUsername +
                ", brokerPassword=" + brokerPassword +
                ", pool=" + pool +
                '}';
    }
	
    /*
	public MessageQueueClient build(Environment env) {
		MessageQueueClient client = new MessageQueueClient(getHost(), getPort());
		env.lifecycle().manage(new Managed() {

			@Override
			public void start() throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void stop() throws Exception {
				// TODO Auto-generated method stub
				client.close();
			}
			
		});
		
		return client;
	}*/
	
}
