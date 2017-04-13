package com.Products;

import javax.ws.rs.Path;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.Models.Products;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/products")
public class ProductsServiceImpl {

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProduct(@QueryParam("product") String prod, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		Products product = objectMapper.readValue(prod, Products.class);
		ProductsDaoImpl prodDao = new ProductsDaoImpl();
		Products prodct = prodDao.addProduct(product, companyId);
		return Response.status(Response.Status.OK)
				   .entity(prodct).build();
	}
	

	@POST
	@Path("/getList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductlist( @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		
		ProductsDaoImpl prodDao = new ProductsDaoImpl();
		List<Products> prodList = prodDao.getProducts(companyId);
		return Response.status(Response.Status.OK)
				   .entity(prodList).build();
	}
	
	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProducts( @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		
		ProductsDaoImpl prodDao = new ProductsDaoImpl();
		List<Products> prodList = prodDao.getAllProduct(companyId);
		return Response.status(Response.Status.OK)
				   .entity(prodList).build();
	}
	
	
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductlist( @QueryParam("product") String prod, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		Products product = objectMapper.readValue(prod, Products.class);
		ProductsDaoImpl prodDao = new ProductsDaoImpl();
		Products prdt = prodDao.updateProduct(product, companyId);
		return Response.status(Response.Status.OK)
				   .entity(prdt).build();
	}
	
	@POST
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProduct( @QueryParam("productId") String prodId, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		
		ProductsDaoImpl prodDao = new ProductsDaoImpl();
		String msg = prodDao.deleteProduct(prodId, companyId);
		return Response.status(Response.Status.OK)
				   .entity(msg).build();
	}
}
