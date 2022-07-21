package br.com.jdo2.poc.envixo.view;

import lombok.Data;

@Data
public class ErrorView {
	
	public ErrorView() {}
	
	public ErrorView(String message) {
		this.message = message;
	}
	
	private String message;

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
