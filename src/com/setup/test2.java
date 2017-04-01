package com.setup;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

public class test2 extends JerseyTest{
	
	  @Override
	    protected Application configure() {
	        return new ResourceConfig(test.class);
	    }

	    @Test
	    public void ordersPathParamTest() {
	        Response response = target("hello/test").request().get();
	        Assert.assertTrue(response.getStatus()==200);
	    }

}
