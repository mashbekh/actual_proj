package com.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Invocation.Builder;

public class Try extends JerseyTest {

	    @Test
	    public void testHelloWorld() {
	    	ClientConfig clientConfig = new ClientConfig();
			clientConfig.register(JacksonFeature.class);

			Client client = ClientBuilder.newClient(clientConfig);

			WebTarget webTarget = client
					.target("http://localhost:8080/rest/hello/test");

			Builder request = webTarget.request();
			//request.header("Content-type", MediaType.APPLICATION_JSON);

			Response response = request.get();
			Assert.assertTrue(response.getStatus() == 200);
	    }
		 
	}
     
