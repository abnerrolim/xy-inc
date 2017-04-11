package models;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import play.Logger;
import play.libs.Json;
import utils.KeepJsonAsStringDeserializer;

public class APIMetadata{

    @MongoId
    @MongoObjectId
	private String id;
	
	private String name;
	
	@JsonDeserialize(using=KeepJsonAsStringDeserializer.class)
	private String schema;

	@JsonIgnore
	public static final String NAMESPACE = "genericobjectmetadata";
	
	@JsonIgnore
	final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
	
	APIMetadata() {	}
	
	public APIMetadata(String name, JsonNode schema){
		this.name=name;
		this.setSchema(schema);
	}
	
	public APIMetadata(String name){
		this.name=name;
	}
	
	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getName(){
		return this.name;
	}
    
	public String getNamespace() {
		return NAMESPACE;
	}
	
	public void setSchema(String schema){
		JsonNode toJson = Json.parse(schema);
		setSchema(toJson);
	}
	
	public void setSchema(JsonNode schema){
		if(!JsonSchemaFactory.byDefault().getSyntaxValidator().schemaIsValid(schema))
			throw new IllegalArgumentException("Not a valid json schema!");
		this.schema = Json.stringify(schema);
	}
	
	@JsonIgnore
	public JsonNode getSchemaASJsonNode(){
		return Json.parse(this.schema);
	}

	public boolean isValidObject(JsonNode object) {
		try {
			ProcessingReport report = JsonSchemaFactory.byDefault().getJsonSchema(getSchemaASJsonNode()).validate(object);
			return report.isSuccess();
		} catch (ProcessingException e) {
			Logger.error("Error validationg json object" + object,e);
			return false;
		}
	}
	
}
