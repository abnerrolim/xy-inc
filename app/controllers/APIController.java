package controllers;

import models.APIObject;
import models.APIRepository;

import com.google.inject.Inject;

import models.APIMetadata;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class APIController extends Controller {
	@Inject
	APIRepository repo; 
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public Result test(){
    	APIMetadata ob = new APIMetadata();
    	ob.name = "TESTE3";
    	repo.save(ob);
    	APIObject myGenericObject = new APIObject(ob);
    	myGenericObject.object = Json.parse("{\"name\":\"sususus\",\"casa\":1212}");
    	repo.save(myGenericObject);
    	APIObject founded = repo.findByID(myGenericObject.getId(), ob);
    	return ok(Json.toJson(founded));
    }
    
}
