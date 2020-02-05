package com.kellton.userservice.exception;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Errors {

	private int status;
	private String code;
	private ErrorSource source;
	private String value;
	private String message;
	
	
	public Errors(int status, String code, ErrorSource source, String string, String message) {
		super();
		this.status = status;
		this.code = code;
		this.source = source;
		this.value = string;
		this.message = message;
	}
	
	



	public Errors(int status, String code, ErrorSource source, String value, String message, Object data) {
		super();
		this.status = status;
		this.code = code;
		this.source = source;
		this.value = value;
		this.message = message;
		this.data = data;
	}



	private Object data;
	
}
