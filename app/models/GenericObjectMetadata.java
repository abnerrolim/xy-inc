package models;

import org.jongo.MongoCollection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import org.bson.types.ObjectId;


import uk.co.panaxiom.playjongo.PlayJongo;

public class GenericObjectMetadata {

    @JsonProperty("_id")
    public ObjectId id;
	
	public String name;
		
	public String schema;
	
	@Inject
	public static PlayJongo jongo;

    MongoCollection getObjectResource() {
        return jongo.getCollection(name);
    }
    
    private static MongoCollection objectEndpoints() {
        return jongo.getCollection("objectendpoint");
    }
    
    public void insert() {
    	if(uniqueName(this.name))
    		objectEndpoints().save(this);
    }

    public void remove() {
    	objectEndpoints().remove(this.id);
    }

    public static GenericObjectMetadata findByName(String name) {
        return objectEndpoints().findOne("{name: #}", name).as(GenericObjectMetadata.class);
    }
    
    public static boolean uniqueName(String name) {
        return objectEndpoints().count("{name: #}", name) < 1;
    }
}
