package com.bgi.edims.model;

public class EmailConfig {

	private String host;
	private String port;
	private String email;
	private String password;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "EmailConfig [host=" + host + ", port=" + port + ", email=" + email + ", password=" + password + "]";
	}
	
}
