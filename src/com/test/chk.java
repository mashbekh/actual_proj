package com.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/vv")
public class chk {

        @GET
        @Path("/hello")
        public String getHello() {
            return "Hello World!";
        }
    
}
