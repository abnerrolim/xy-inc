package models;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;

import static org.jongo.Oid.withOid;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.times;

import org.jongo.Find;
import org.jongo.FindOne;
import org.jongo.MongoCollection;
import org.jongo.Update;

import play.inject.Injector;
import uk.co.panaxiom.playjongo.PlayJongo;

/**
 * @author abner
 *Shows mock use to test behavior from external dependencies
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class APIRepositoryMongoDBTest {
	
	@Mock
	private PlayJongo jongo;
	
	@Mock
	private MongoCollection mongoCollection;
	
	@Mock
	private Injector injector;
	
	private APIMetadata api;
	private APIObject obj;
	
	
	@Before
	public void configure(){
		api = new APIMetadata("TESTE");
		obj = new APIObject(api);
		Mockito.when(injector.instanceOf(PlayJongo.class)).thenReturn(jongo);
		Mockito.when(jongo.getCollection(anyString())).thenReturn(mongoCollection);
		Mockito.when(mongoCollection.findOne(anyString(),any())).thenReturn(Mockito.mock(FindOne.class));
		Mockito.when(mongoCollection.findOne(anyString())).thenReturn(Mockito.mock(FindOne.class));
		Mockito.when(mongoCollection.update(anyString())).thenReturn(Mockito.mock(Update.class));
		Mockito.when(mongoCollection.find()).thenReturn(Mockito.mock(Find.class));
		Mockito.when(mongoCollection.findOne(anyString(),any()).as(APIMetadata.class)).thenReturn(api);
		Mockito.when(mongoCollection.findOne(anyString(),any()).as(APIObject.class)).thenReturn(obj);
	}
	
	@Test
	public void findByAPINameCallsRightMethodsFromJongo(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		repo.findByAPIName(api.getName());
		Mockito.verify(jongo, times(1)).getCollection(APIMetadata.NAMESPACE);
		Mockito.verify(mongoCollection, times(1)).findOne("{name: #}", api.getName());
	}
	
	@Test
	public void findAllCallsRightMethodsFromJongo(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		repo.findAll(api);
		Mockito.verify(jongo, times(1)).getCollection(api.getName());
		Mockito.verify(mongoCollection, times(1)).find();
	}
	
	@Test
	public void findByIDCallsRightMethodsFromJongo(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		repo.findByID("id", api);
		Mockito.verify(jongo, times(1)).getCollection(api.getName());
		Mockito.verify(mongoCollection, times(1)).findOne(withOid("id"));
	}
	
	@Test
	public void saveAPIObjectCallsRightMethodsFromJongo(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		repo.save(obj);
		Mockito.verify(jongo, times(1)).getCollection(api.getName());
		Mockito.verify(mongoCollection, times(1)).save(obj);
	}
	
	@Test
	public void removeAPIObjectCallsRightMethodsFromJongo(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		repo.remove(obj);
		Mockito.verify(jongo, times(1)).getCollection(api.getName());
		Mockito.verify(mongoCollection, times(1)).remove(withOid(obj.getId()));
	}
	
	@Test
	public void updateAPIObjectCallsRightMethodsFromJongo(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		repo.update(obj);
		
		Mockito.verify(jongo, times(1)).getCollection(api.getName());
		Mockito.verify(mongoCollection, times(1)).update(withOid(obj.getId()));
	}
	
	@Test
	public void saveAPIMetadataCallsRightMethodsFromJongo(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		Mockito.when(mongoCollection.count(anyString(),anyString())).thenReturn(0l);
		repo.save(api);
		Mockito.verify(jongo, times(2)).getCollection(api.getNamespace());
		Mockito.verify(mongoCollection, times(1)).count("{name: #}", api.getName());
		Mockito.verify(mongoCollection, times(1)).save(api);
	}
	
	@Test(expected=RuntimeException.class)
	public void saveAPIMetadataWithRepeatedNameThrowsException(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		Mockito.when(mongoCollection.count(anyString(),anyString())).thenReturn(1l);
		repo.save(api);
	}
	
	@Test
	public void removeAPIMetadataCallsRightMethodsFromJongo(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		repo.remove(api);
		Mockito.verify(jongo, times(1)).getCollection(api.getNamespace());
		Mockito.verify(mongoCollection, times(1)).remove(withOid(api.getId()));
	}
	
	@Test
	public void updateAPIMetadataCallsRightMethodsFromJongo(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		repo.update(api);
		Mockito.verify(jongo, times(1)).getCollection(api.getNamespace());
		Mockito.verify(mongoCollection, times(1)).update(withOid(api.getId()));
	}
	
	@Test
	public void isUniqueName(){
		APIRepository repo = new APIRepositoryMongoDB(injector);
		Mockito.when(mongoCollection.count(anyString(),anyString())).thenReturn(0l);
		assertTrue(repo.isUniqueName(api.getName()));
		Mockito.verify(jongo, times(1)).getCollection(api.getNamespace());
		Mockito.verify(mongoCollection, times(1)).count("{name: #}", api.getName());
	}
}
