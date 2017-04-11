package controllers;

import static org.junit.Assert.assertTrue;
import static play.inject.Bindings.bind;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.JsonNode;

import models.APIMetadata;
import models.APIRepository;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

@RunWith(MockitoJUnitRunner.class)
public class APIControllerTest extends WithApplication {
	
	@Mock
	private APIRepository repo;
	
	@Override
	protected Application provideApplication() {
		return new GuiceApplicationBuilder()
				.overrides(bind(APIRepository.class).toInstance(repo))
				.build();
	}
	  
	@Test
	public void testSuccesfulSaveAPI(){
		  APIMetadata metadata = new APIMetadata("TESTE");
		  JsonNode schema = Json.parse("{\"title\": \"Person\",\"type\": \"object\",\"properties\": {\"firstName\": {\"type\": \"string\"},\"lastName\": {\"type\": \"string\"},\"age\": {\"description\": \"Age in years\",\"type\": \"integer\",\"minimum\": 0}},\"required\": [\"firstName\", \"lastName\"]}");
		  metadata.setSchema(schema);
		  JsonNode jsonNode = Json.toJson(metadata);
		  Mockito.when(repo.isUniqueName(metadata.getName())).thenReturn(true);
		  RequestBuilder request = new RequestBuilder().method("POST")
		            .bodyJson(jsonNode)
		            .uri(controllers.routes.APIController.saveAPI().url());
		    Result result = Helpers.route(request);
		    assertTrue(result.status() == Helpers.CREATED);
	  }
	
	@Test
	public void returnsBadRequestWhenCalledWithDuplicatedAPIName(){
		  APIMetadata metadata = new APIMetadata("TESTE");
		  JsonNode schema = Json.parse("{\"title\": \"Person\",\"type\": \"object\",\"properties\": {\"firstName\": {\"type\": \"string\"},\"lastName\": {\"type\": \"string\"},\"age\": {\"description\": \"Age in years\",\"type\": \"integer\",\"minimum\": 0}},\"required\": [\"firstName\", \"lastName\"]}");
		  metadata.setSchema(schema);
		  JsonNode jsonNode = Json.toJson(metadata);
		  Mockito.when(repo.isUniqueName(metadata.getName())).thenReturn(false);
		  RequestBuilder request = new RequestBuilder().method("POST")
		            .bodyJson(jsonNode)
		            .uri(controllers.routes.APIController.saveAPI().url());
		    Result result = Helpers.route(request);
		    assertTrue(result.status() == Helpers.BAD_REQUEST);
	  }
	
	@Test
	public void testFindAPIByName(){
		  APIMetadata metadata = new APIMetadata("TESTE");
		  JsonNode schema = Json.parse("{\"title\": \"Person\",\"type\": \"object\",\"properties\": {\"firstName\": {\"type\": \"string\"},\"lastName\": {\"type\": \"string\"},\"age\": {\"description\": \"Age in years\",\"type\": \"integer\",\"minimum\": 0}},\"required\": [\"firstName\", \"lastName\"]}");
		  metadata.setSchema(schema);
		  Mockito.when(repo.findByAPIName((metadata.getName()))).thenReturn(metadata);
		  RequestBuilder request = new RequestBuilder().method("GET")
		            .uri(controllers.routes.APIController.findAPIByName(metadata.getName()).url());
		    Result result = Helpers.route(request);
		    assertTrue(result.status() == Helpers.OK);
	  }
	
	
	@Test
	public void testNoutFoundFindAPIByName(){
		  APIMetadata metadata = new APIMetadata("TESTE");
		  JsonNode schema = Json.parse("{\"title\": \"Person\",\"type\": \"object\",\"properties\": {\"firstName\": {\"type\": \"string\"},\"lastName\": {\"type\": \"string\"},\"age\": {\"description\": \"Age in years\",\"type\": \"integer\",\"minimum\": 0}},\"required\": [\"firstName\", \"lastName\"]}");
		  metadata.setSchema(schema);
		  Mockito.when(repo.findByAPIName((metadata.getName()))).thenReturn(metadata);
		  RequestBuilder request = new RequestBuilder().method("GET")
		            .uri(controllers.routes.APIController.findAPIByName("INVALID API NAME").url());
		    Result result = Helpers.route(request);
		    assertTrue(result.status() == Helpers.NOT_FOUND);
	  }

}
