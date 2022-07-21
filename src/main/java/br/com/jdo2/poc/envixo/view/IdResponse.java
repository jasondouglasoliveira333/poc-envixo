package br.com.jdo2.poc.envixo.view;

import lombok.Data;
import lombok.RequiredArgsConstructor;

//@Data
//@RequiredArgsConstructor
public class IdResponse {
	private Integer id;
	
	public IdResponse() {}
	
	public IdResponse(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
}
