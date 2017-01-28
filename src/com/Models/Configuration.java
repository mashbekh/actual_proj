package com.Models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuration")
public class Configuration {
	
	private Long user_id;
	private String key_name;
	private String value;
	private String nodeId;
	
	public Configuration(Long user_id, String key_name, String value , String nodeId) {
		super();
		this.user_id = user_id;
		this.key_name = key_name;
		this.value = value;
		this.nodeId= nodeId;
	}
	
	@Override
	public String toString() {
		return "Configuration [user_id=" + user_id + ", key_name=" + key_name + ", value=" + value + ", nodeId="
				+ nodeId + "]";
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Configuration() {
		super();
	}

	@Id
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	
	public String getKey_name() {
		return key_name;
	}

	public void setKey_name(String key_name) {
		this.key_name = key_name;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	

}
