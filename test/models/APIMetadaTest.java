package models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.libs.Json;

public class APIMetadaTest {
	
	@Test
	public void sucessfulCreate() throws JsonProcessingException, IOException{
		APIMetadata meta = new APIMetadata("TESTE");
		//valid schema
		String schema = "{\"title\": \"Person\",\"type\": \"object\",\"properties\": {\"firstName\": {\"type\": \"string\"},\"lastName\": {\"type\": \"string\"},\"age\": {\"description\": \"Age in years\",\"type\": \"integer\",\"minimum\": 0}},\"required\": [\"firstName\", \"lastName\"]}";
		meta.setSchema(schema);
		assertTrue("TESTE".equals(meta.getName()));
		ObjectMapper mapper = new ObjectMapper();
		JsonNode schemaNode = mapper.readTree(schema);
		assertTrue(schemaNode.equals(meta.getSchemaASJsonNode()));
	}
	
	@Test(expected=RuntimeException.class)
	public void settingMalformedJsonStringrowsException(){
		APIMetadata meta = new APIMetadata("TESTE");
		String schema = "{\"name\" \"local\"}";//invalid json
		meta.setSchema(schema);
	}
	
	@Test(expected=RuntimeException.class)
	public void settingMalformedJsonStringSchemaTrowsException(){
		APIMetadata meta = new APIMetadata("TESTE");
		String schema = "{\"name\":\"myschema\", \"local\":\"local\",\"multiplyOf\":\"ok\"\"}";//invalid schema
		meta.setSchema(schema);
	}
	
	@Test(expected=RuntimeException.class)
	public void settingMalformedJsonNodeSchemaTrowsException(){
		APIMetadata meta = new APIMetadata("TESTE");
		String schema = "{\"name\":\"myschema\", \"local\":\"local\",\"multiplyOf\":\"ok\"\"}";//invalid schema
		meta.setSchema(Json.toJson(schema));
	}
	
	@Test
	public void validateWihtValidJsonObjectBySchema(){
		APIMetadata meta = new APIMetadata("TESTE");
		String schema = "{\"title\": \"Person\",\"type\": \"object\",\"properties\": {\"firstName\": {\"type\": \"string\"},\"lastName\": {\"type\": \"string\"},\"age\": {\"description\": \"Age in years\",\"type\": \"integer\",\"minimum\": 0}},\"required\": [\"firstName\", \"lastName\"]}";
		meta.setSchema(schema);
		String object = "{\"firstName\":\"Abner\",\"lastName\":\"Rolim\"}";
		assertTrue(meta.isValidObject(Json.parse(object)));
	}
	
	@Test
	public void validateWihtInValidJsonObjectBySchema(){
		APIMetadata meta = new APIMetadata("TESTE");
		String schema = "{\"title\": \"Person\",\"type\": \"object\",\"properties\": {\"firstName\": {\"type\": \"string\"},\"lastName\": {\"type\": \"string\"},\"age\": {\"description\": \"Age in years\",\"type\": \"integer\",\"minimum\": 0}},\"required\": [\"firstName\", \"lastName\"]}";
		meta.setSchema(schema);
		String object = "{\"firstName\":\"Abner\"}";//without required lastName
		assertFalse(meta.isValidObject(Json.parse(object)));
	}
}
