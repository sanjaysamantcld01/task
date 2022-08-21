package com.soenergy.task.beans;

import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"identifier","name","description"})
public class Beer implements Comparable<Beer>{
	
	@JsonProperty("beer_identifier")
	@JsonAlias("id")
	private long identifier;
	
	@JsonProperty("beer_name")
	@JsonAlias("name")
	@NotBlank
	private String name;
	
	@JsonProperty("beer_description")
	@JsonAlias("description")
	@NotBlank
	private String description;
	
	public Beer(long identifier, String name, String description) {
		super();
		this.identifier = identifier;
		this.name = name;
		this.description = description;
	}
	
	public Beer() {
		super();
		
	}

	public long getIdentifier() {
		return identifier;
	}
	public void setIdentifier(long identifier) {
		this.identifier = identifier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "Beer [identifier=" + identifier + ", name=" + name + ", description=" + description + "]";
	}


	@Override
	public int compareTo(Beer beer) {
		if(this.getIdentifier()>beer.getIdentifier())
			return 1;
		else if(this.getIdentifier()==beer.getIdentifier())
			return 0;
		else return -1;
		
	}
	
	

}
