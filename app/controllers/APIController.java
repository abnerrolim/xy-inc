package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import models.APIMetadata;
import models.APIObject;
import models.APIRepository;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class APIController extends Controller {
	
	@Inject
	APIRepository repo;
	
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public Result saveAPI(){
        JsonNode json = request().body().asJson();
        APIMetadata api = Json.fromJson(json, APIMetadata.class);
        if(!repo.isUniqueName(api.getName())){
        	return badRequest("API name already exists and should be unique");
        }
        repo.save(api);
    	return created(Json.toJson(api));
    }
    
    public Result findAPIByName(String apiName){
    	APIMetadata api = repo.findByAPIName(apiName);
    	if(api != null)
    		return ok(Json.toJson(api));
    	else
    		return notFound();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result updateAPI(String apiName){
    	APIMetadata oldAPI = repo.findByAPIName(apiName);
    	if(oldAPI == null)
    		return badRequest("Invalid api");
        JsonNode json = request().body().asJson();
        APIMetadata api = Json.fromJson(json, APIMetadata.class);
        api.setId(oldAPI.getId());
        repo.update(api);
    	return noContent();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result saveObject(String apiName){
    	APIMetadata api = repo.findByAPIName(apiName);
    	if(api == null)
    		return badRequest("Invalid api");
        JsonNode json = request().body().asJson();
        APIObject requestObj = Json.fromJson(json, APIObject.class);
        APIObject toSave = new APIObject(api, requestObj.getObjectAsJsonNode());
        repo.save(toSave);
    	return created((Json.toJson(toSave)));
    }
    
    public Result getObject(String apiName, String objectid){
    	APIMetadata api = repo.findByAPIName(apiName);
    	if(api == null)
    		return badRequest("Invalid api");
    	APIObject obj = repo.findByID(objectid, api);
    	if(obj == null)
    		return notFound();
    	return ok(Json.toJson(obj));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result updateObject(String apiName, String objectid){
    	APIMetadata api = repo.findByAPIName(apiName);
    	if(api == null)
    		return badRequest("Invalid api");
    	APIObject obj = repo.findByID(objectid, api);
    	if(obj == null)
    		return badRequest("Invalid object id " + objectid + " from this API " + api.getName());
    	JsonNode json = request().body().asJson();
        APIObject updateObj = Json.fromJson(json, APIObject.class);
        obj.setObject(updateObj.getObject());//only object value is valid to update
        repo.update(obj);
    	return noContent();
    }
    
    public Result listAllObjects(String apiName){
    	APIMetadata api = repo.findByAPIName(apiName);
    	if(api == null)
    		return badRequest("Invalid api");
    	return ok(Json.toJson(repo.findAll(api)));
    }
    
    public Result removeObject(String apiName, String objectid){
    	APIMetadata api = repo.findByAPIName(apiName);
    	if(api == null)
    		return badRequest("Invalid api");
    	APIObject obj = repo.findByID(objectid, api);
    	if(obj == null)
    		return badRequest("Invalid object id " + objectid + " from this API " + api.getName());
    	repo.remove(obj);
		return noContent();
    }
    
}
