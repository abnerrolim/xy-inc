package models;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.libs.Json;

public class APIObjectTest{
	
	private APIMetadata api;
	
	
	@Before
	public void initialize(){
		String schema = "{\"title\": \"Person\",\"type\": \"object\",\"properties\": {\"firstName\": {\"type\": \"string\"},\"lastName\": {\"type\": \"string\"},\"age\": {\"description\": \"Age in years\",\"type\": \"integer\",\"minimum\": 0}},\"required\": [\"firstName\", \"lastName\"]}";
		api = new APIMetadata("Name", Json.parse(schema));
	}
	
	@Test
	public void sucessfulCreate() throws JsonProcessingException, IOException{
		APIObject obj = new APIObject(api);
		String object = "{\"firstName\":\"Abner\",\"lastName\":\"Rolim\"}";
		JsonNode json = Json.parse(object);
		obj.setObjectAsJsonNode(json);
		
		assertTrue(api.getName().equals(obj.getApiName()));
		ObjectMapper mapper = new ObjectMapper();
		JsonNode ojectNode = mapper.readTree(object);
		assertTrue(ojectNode.equals(obj.getObjectAsJsonNode()));
	}

	@Test(expected=IllegalArgumentException.class)
	public void createWithInvalidObjectThrowsIllegalArgument(){
		String object = "{\"firstName\":\"Abner\"}";//without required lastName
		JsonNode json = Json.parse(object);
		new APIObject(api, json);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setInvalidObjectThrowsIllegalArgument(){
		APIObject obj = new APIObject(api);
		String object = "{\"firstName\":\"Abner\"}";//without required lastName
		JsonNode json = Json.parse(object);
		obj.setObjectAsJsonNode(json);
	}
	
	@Test
	public void setValidObjectIsSucessful() throws JsonProcessingException, IOException{
		APIObject obj = new APIObject(api);
		String object = "{\"firstName\":\"Abner\",\"lastName\":\"Rolim\"}";
		JsonNode json = Json.parse(object);
		obj.setObjectAsJsonNode(json);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode ojectNode = mapper.readTree(object);
		assertTrue(ojectNode.equals(obj.getObjectAsJsonNode()));
	}
	
}
