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
		return jongo.getCollection(metadata.getName()).find().as(APIObject.class);
	}

	@Override
	public APIObject findByID(String id, APIMetadata metadata) {
		return findOnRepoById(id, metadata.getName());
	}

	@Override
	public void save(APIObject newbie) {
		jongo.getCollection(newbie.getApiName()).save(newbie);		
	}

	@Override
	public void remove(APIObject toDelete) {
		jongo.getCollection(toDelete.getApiName()).remove(withOid(toDelete.getId()));	
	}
	@Override
	public void update(APIObject toUpdate) {
		jongo.getCollection(toUpdate.getApiName()).update(withOid(toUpdate.getId())).with(toUpdate);
	}

	@Override
	public void save(APIMetadata newbie) {
		if(isUniqueName(newbie.getName()))
			jongo.getCollection(APIMetadata.NAMESPACE).save(newbie);
		else
			throw new RuntimeException("API name " + newbie.getName() + "already exists. Name should be unique");			
	}

	@Override
	public void remove(APIMetadata toDelete) {
		jongo.getCollection(APIMetadata.NAMESPACE).remove(withOid(toDelete.getId()));
	}
	
	@Override
	public boolean isUniqueName(String metadataName) {
		MongoCollection collection = jongo.getCollection(APIMetadata.NAMESPACE);
		return collection.count("{name: #}", metadataName) < 1l ;
	}
	
	@Override
	public void update(APIMetadata toUpdate) {
		jongo.getCollection(APIMetadata.NAMESPACE).update(withOid(toUpdate.getId())).with(toUpdate);
	}

	@Override
	public APIObject findOnRepoById(String id, String repo) {
		return jongo.getCollection(repo).findOne(withOid(id)).as(APIObject.class);
	}
	

}
