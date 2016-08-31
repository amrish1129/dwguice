package in.hopscotch.dwguice;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

/**
 * We need to parse config.yml. It is a simple class, with properties named
 * after our configuration settings along with their getter methods. This class
 * will be used as configuration proxy.
 * 
 * @author amrish
 *
 */
public class MainConfiguration extends Configuration {
	@NotEmpty // annotation will assure that application will not start if  appName value will be not defined.
	@JsonProperty
	private String appName;

	@JsonProperty
	private String message;
	
	@JsonProperty
	private String messageRepetitions;
	
	@Valid
	@NotNull
	private MessageQueueConfig messageQueueConfig = new MessageQueueConfig();
	
	@Valid
	@NotNull
	@JsonProperty("database")
	private DataSourceFactory dataSourceFactory = new DataSourceFactory();
	
	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}
	@JsonProperty("database")
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}

	@JsonProperty("messageQueue")
	public MessageQueueConfig getMessageQueueConfig() {
		return messageQueueConfig;
	}

	@JsonProperty("messageQueue")
	public void setMessageQueueConfig(MessageQueueConfig messageQueueConfig) {
		this.messageQueueConfig = messageQueueConfig;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageRepetitions() {
		return messageRepetitions;
	}

	public void setMessageRepetitions(String messageRepetitions) {
		this.messageRepetitions = messageRepetitions;
	}

}
