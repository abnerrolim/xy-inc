package models;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class APIMetadata{

    @MongoId
    @MongoObjectId
	private String id;
	
	public String name;
		
	public String schema;
	
	public static final String NAMESPACE = "genericobjectmetadata";
	
	public String getId(){
		return this.id;
	}
    
	public String getNamespace() {
		return NAMESPACE;
	}
}
