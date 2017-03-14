package com.setup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.Models.FileUpload;


@Path("/file")
public class fileUpload {

	
	private Datastore datastore1;
	
	@Path("/upload")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@FormDataParam("logo") InputStream  is) throws IOException
	{

		datastore1 = Morphiacxn.getInstance().getMORPHIADB("test");
		byte[] a = IOUtils.toByteArray(is);
		FileUpload a1 = new FileUpload(a, 11);
		datastore1.save(a1);
		return Response.status(200).entity("success").build();
	}


	@Path("/getpic")
	@GET
	public String getimage()
	{
		datastore1 = Morphiacxn.getInstance().getMORPHIADB("test");
		Query<FileUpload> userQueryDS1 = datastore1.createQuery(FileUpload.class);
		userQueryDS1.field("idp").equal(11);
		FileUpload b = userQueryDS1.get();
		
		String base64Encoded = Base64.getEncoder().encodeToString(b.getFile());
		return base64Encoded;

	}

}
