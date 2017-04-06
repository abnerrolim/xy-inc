package models;

import static org.jongo.Oid.withOid;

import java.util.Iterator;

import org.jongo.MongoCollection;

import com.google.inject.Inject;

import play.inject.Injector;
import uk.co.panaxiom.playjongo.PlayJongo;

public class APIRepositoryMongoDB implements APIRepository {


	private PlayJongo jongo;
	
	@Inject
	public APIRepositoryMongoDB  (Injector injector){
		jongo = injector.instanceOf(PlayJongo.class);
	}

	@Override
	public APIMetadata findByAPIName(String apiName) {
        return jongo.getCollection(APIMetadata.NAMESPACE).findOne("{name: #}", apiName).as(APIMetadata.class);
	}

	@Override
	public Iterator<APIObject> findAll(APIMetadata metadata) {
		return jongo.getCollection(metadata.name).find().as(APIObject.class);
	}

	@Override
	public APIObject findByID(String id, APIMetadata metadata) {
		return jongo.getCollection(metadata.name).findOne(withOid(id)).as(APIObject.class);
	}

	@Override
	public void save(APIObject newbie) {
		jongo.getCollection(newbie.getApiName()).save(newbie);		
	}

	@Override
	public void remove(APIObject toDelete) {
		jongo.getCollection(toDelete.getApiName()).remove(toDelete.getId());	
	}
	@Override
	public void update(APIObject toUpdate) {
		jongo.getCollection(toUpdate.getApiName()).update(toUpdate.getId()).with(toUpdate);
	}

	@Override
	public void save(APIMetadata newbie) {
		MongoCollection collection = jongo.getCollection(APIMetadata.NAMESPACE);
		if( collection.count("{name: #}", newbie.name) < 1l )
			collection.save(newbie);
		else
			throw new RuntimeException("API name " + newbie.name + "already exists. Name should be unique");			
	}

	@Override
	public void delete(APIMetadata toDelete) {
		jongo.getCollection(APIMetadata.NAMESPACE).remove(toDelete.getId());
	}
	@Override
	public void update(APIMetadata toUpdate) {
		jongo.getCollection(APIMetadata.NAMESPACE).update(toUpdate.getId()).with(toUpdate);
	}
	

}
