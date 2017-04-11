package models;

import java.util.Iterator;

public interface APIRepository {
	
	APIMetadata findByAPIName(String apiName);
	void save(APIMetadata newbie);
	void remove(APIMetadata toDelete);
	void update(APIMetadata toUpdate);
	boolean isUniqueName(String metadataName);
		
	Iterator<APIObject> findAll(APIMetadata metadata);
	APIObject findByID(String id, APIMetadata metadata);
	APIObject findOnRepoById(String id, String repo);
	void save(APIObject newbie);
	void remove(APIObject toDelete);
	void update(APIObject toUpdate);

}
