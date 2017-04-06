package models;

import java.util.Iterator;

public interface APIRepository {
	
	APIMetadata findByAPIName(String apiName);
	void save(APIMetadata newbie);
	void delete(APIMetadata toDelete);
	void update(APIMetadata toUpdate);
		
	Iterator<APIObject> findAll(APIMetadata metadata);
	APIObject findByID(String id, APIMetadata metadata);
	void save(APIObject newbie);
	void remove(APIObject toDelete);
	void update(APIObject toUpdate);

}
