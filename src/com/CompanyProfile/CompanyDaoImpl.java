package com.CompanyProfile;

import java.util.*;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.Key;

import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.Models.Company;
import com.Models.User;
import com.mongodb.MongoException;
import com.setup.Morphiacxn;

public class CompanyDaoImpl {

	public String createCompany(Company company, String userId)  throws AppException, EntityException //return company Id
	{
		Datastore ds = null;
		User user = null;
		Key<Company> key = null;
		String companyId = null;
		
		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			ObjectId oid  = new ObjectId(userId);
			Query<User> query = ds.createQuery(User.class).field("id").equal(oid);
			user = query.get();
			
			List<User> users = new ArrayList<>();
			users.add(user);
			company.setUsers(users);
			
			key = ds.save(company);
			companyId = key.getId().toString();
		}
		catch(MongoException e)
		{
			
		}
		catch(EntityException e)
		{
			
		}
		catch(Throwable e)
		{

		}
		return companyId;
	}

}
