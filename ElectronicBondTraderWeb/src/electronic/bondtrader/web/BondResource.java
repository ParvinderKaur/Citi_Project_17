package electronic.bondtrader.web;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import electronic.bondtrader.ejb.BondTraderBeanLocal;
import electronic.bondtrader.jpa.Bond;
import electronic.bondtrader.jpa.Client;

@RequestScoped
@Path("/bond")
@Produces({ "*/*", "application/json", "application/xml" })
@Consumes({ "*/*", "application/json", "application/xml" })
public class BondResource {

	private BondTraderBeanLocal bean;
	public BondResource(){
		Context context = null;
		try {
			context = new InitialContext();
			bean = (BondTraderBeanLocal)context.lookup("java:global/ElectronicBondTrader/ElectronicBondTraderEJB/BondTraderBean!electronic.bondtrader.ejb.BondTraderBeanLocal");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	@GET
	@Produces ("application/json")
	public List<Bond> getAllBonds(){
		return bean.getAllBonds();
	}
	
	
	@GET
	@Produces ("application/json")
	@Path("/{param}")
	public List<Bond> getBondsByType(@PathParam ("param") String filter){
		if(filter.length()==0)
			return bean.getAllBonds();
		return bean.getBondsByType(filter);
	}
	
	@GET
	@Produces ("application/json")
	@Path("/rating/{param}")
	public List<Bond> getBondsByRating(@PathParam ("param") String filter){
		if(filter.length()==0)
			return bean.getAllBonds();
		return bean.getBondsByRating(filter);
	}
	
	@GET
	@Produces ("application/json")
	@Path("/currency/{param}")
	public List<Bond> getBondsByCurrency(@PathParam ("param") String curr){
		if(curr.length()==0)
			return bean.getAllBonds();
		return bean.getBondsByCurrency(curr);
	}
	
	@GET
	@Produces ("application/json")
	@Path("/query/{type}/{rating}")
	public List<Bond> getBondsByQuery(@PathParam ("type") String typeName,
			@PathParam ("rating") String rating ){
		List<Bond> bondsByCriteria = bean.getBondsByCriteria(rating, typeName);
		return bondsByCriteria;
	
	}
	
	@GET
	@Produces ("application/json")
	@Path("/client_info/")
	public Response populateClient()
	{
		try 
		{
			List<Client> names = bean.allClients();
//			return names;
			return Response.status(200).entity(names).build();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("API Error!");
			return Response.ok(false).entity("API Error").build();
		}
	}
	
	
	@POST
	@Path("/client_retrieval")
	@Produces("application/json")
	@Consumes("application/x-www-form-urlencoded")
	public Response clientRetrieval(@FormParam("id") String id) {
		String  updated ="";
		try 
		{
			List<Client> clientInfo = bean.getClientById(id);
			return Response.status(200).entity(clientInfo).build();
		}
		catch(Exception e) 
		{
			return Response.ok(false).entity("API Error").build();
		}
		
	}
	
	@POST
	@Path("/compute_trade")
	@Consumes("application/x-www-form-urlencoded")
	public Response computeBond(@FormParam("isin") String name, @FormParam("quantity") String quantity,
			@FormParam("trade_date") String trade_date, 
			@FormParam("settle_date") String settle_date, @FormParam("yield") String yield ) {
		
		try {
			//TODO: calculation stuff!
			return Response.status(200).entity("SuccessFully Computed").build();
		}
		catch(Exception e) {
			System.out.println("API error...");
			return Response.ok(false).entity("API Error").build();
		}
		
	}
	
	
	@POST
	@Path("/recompute_trade")
	@Consumes("application/x-www-form-urlencoded")
	public Response recomputeBond(@FormParam("isin") String name, @FormParam("quantity") String quantity,
			@FormParam("trade_date") String trade_date, 
			@FormParam("settle_date") String settle_date, @FormParam("yield") String yield ) {
		
		try {
			//TODO: calculation stuff!
			return Response.status(200).entity("SuccessFully Re-Computed").build();
		}
		catch(Exception e) {
			System.out.println("API error...");
			return Response.ok(false).entity("API Error").build();
		}
		
	}
	
	
	@POST
	@Path("/post_test")
	@Consumes("application/x-www-form-urlencoded")
	public Response post(@FormParam("name") String name, @FormParam("contact") String n) {
		String  updated ="";
		try {
			System.out.println("Created successfully");
			System.out.println(n + " " + name);
			Integer received_contact = Integer.parseInt(n);
			System.out.println(received_contact+1);
			return Response.status(200).entity("Success").build();
		}
		catch(Exception e) {
			System.out.println("API error...");
			return Response.ok(false).entity("API Error").build();
		}
		
	}
	
	
	
}
	

