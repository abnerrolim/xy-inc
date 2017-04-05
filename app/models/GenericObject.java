package models;

import java.util.Iterator;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericObject {
	
    @JsonProperty("_id")
    public ObjectId id;
    
	public String objectMetadataName;
	
	public Object object;
	
	@JsonIgnore
	GenericObjectMetadata metadata;
	
	GenericObject(){
		
	}
	
	public GenericObject(GenericObjectMetadata metadata){
		this.metadata = metadata;
	}
	
    public void insert() {
    	this.metadata.getObjectResource().save(this);
    }

    public void remove() {
    	this.metadata.getObjectResource().remove(this.id);
    }
    
    public static  Iterator<GenericObject> list(GenericObjectMetadata metadata) {
        return metadata.getObjectResource().find().as(GenericObject.class);
    }
    
    public static GenericObject findById(String id, GenericObjectMetadata metadata) {
        return metadata.getObjectResource().findOne(new ObjectId(id)).as(GenericObject.class);
    }

}
