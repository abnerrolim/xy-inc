package models;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class APIObject{

    @MongoId
    @MongoObjectId
    private String id;
    
	private String objectMetadataName;
	
	public Object object;
	
	APIObject(){
		
	}
	
	public String getId(){
		return this.id;
	}
	
	public APIObject(APIMetadata metadata){
		this.objectMetadataName = metadata.name;
	}
	
	public String getApiName() {
		return this.objectMetadataName;
	}

}
