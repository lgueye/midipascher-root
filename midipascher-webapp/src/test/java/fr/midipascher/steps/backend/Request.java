package fr.midipascher.steps.backend;

import java.util.Locale;

import javax.ws.rs.core.MediaType;

/**
 * User: louis.gueye@gmail.com
 */
public class Request {
	private String	uri;
	private Object	body;
	private String	requestedLanguage	= Locale.ENGLISH.getLanguage();
	private String	requestedType		= MediaType.APPLICATION_JSON;
	private String	type				= MediaType.APPLICATION_JSON;

	public String getRequestedType() {
		return this.requestedType;
	}

	public String getRequestedLanguage() {
		return this.requestedLanguage;
	}

	public String getUri() {
		return this.uri;
	}

	public String getType() {
		return this.type;
	}

	public Object getBody() {
		return this.body;
	}

	public void setRequestedLanguage(String requestedLanguage) {
		this.requestedLanguage = requestedLanguage;
	}

	public void setRequestedType(String requestedType) {
		this.requestedType = requestedType;
	}

	public void setType(String type) {

		this.type = type;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
