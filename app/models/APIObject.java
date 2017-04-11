package models;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.inject.Inject;

import play.libs.Json;
import utils.KeepJsonAsStringDeserializer;

public class APIObject{

    @MongoId
    @MongoObjectId
    private String id;
    
	private String objectMetadataName;

	@JsonDeserialize(using=KeepJsonAsStringDeserializer.class)
	private String object;
	
	@JsonIgnore
	APIRepository repo;
	
	@JsonIgnore
	private APIMetadata metadata;
	
	@Inject
	APIObject(APIRepository repo){
		this.repo = repo;
	}
	
	public String getId(){
		return this.id;
	}
	
	public APIObject(APIMetadata metadata, JsonNode object){
		setMetadata(metadata);
		setObjectAsJsonNode(object);
	}
	
	public APIObject(APIMetadata metadata){
		setMetadata(metadata);
	}
	@JsonIgnore
	public JsonNode getObjectAsJsonNode(){
		return Json.parse(getObject());
	}
	
	public String getObject(){
		return this.object;
	}
	
	@JsonIgnore
	public void setObjectAsJsonNode(JsonNode object){
		if(this.getMetadata().isValidObject(object))
			this.object = Json.stringify(object);
		else
			throw new IllegalArgumentException("The current object value is valid. Checks your json schema");
	}
	
	public void setObject(String object){
		this.setObjectAsJsonNode(Json.parse(object));
	}
	
	public String getApiName() {
		return this.objectMetadataName;
	}
	
	public void setMetadata(APIMetadata apiMetadata){
		this.objectMetadataName = apiMetadata.getName();
		this.metadata = apiMetadata;
	}
	
	private APIMetadata getMetadata(){
		if (metadata == null)
			metadata = repo.findByAPIName(this.getApiName());
		return metadata;
	}

}
