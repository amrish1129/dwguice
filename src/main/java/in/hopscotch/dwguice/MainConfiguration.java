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
	private MessageQueueConfig messageQueueFactory = new MessageQueueConfig();
	
	@Valid
	@NotNull
	@JsonProperty
	private DataSourceFactory dataSourceFactory = new DataSourceFactory();
	
	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	@JsonProperty("messageQueue")
	public MessageQueueConfig getMessageQueueFactory() {
		return messageQueueFactory;
	}

	@JsonProperty("messageQueue")
	public void setMessageQueueFactory(MessageQueueConfig messageQueueFactory) {
		this.messageQueueFactory = messageQueueFactory;
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
