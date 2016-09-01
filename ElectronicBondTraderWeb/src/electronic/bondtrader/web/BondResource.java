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

import com.sun.net.httpserver.Filter;

import electronic.bondtrader.ejb.BondCalculationBeanLocal;
//import electronic.bondcalculator.BondPriceCalculator;
import electronic.bondtrader.ejb.BondTraderBeanLocal;
import electronic.bondtrader.jpa.Bond;
import electronic.bondtrader.jpa.Client;

@RequestScoped
@Path("/bond")
@Produces({ "*/*", "application/json", "application/xml" })
@Consumes({ "*/*", "application/json", "application/xml" })
public class BondResource {

	private BondTraderBeanLocal traderBean;
	

	public BondResource() {
		Context context = null;
		try {
			context = new InitialContext();
			traderBean = (BondTraderBeanLocal) context.lookup(
					"java:global/ElectronicBondTrader/ElectronicBondTraderEJB/BondTraderBean!electronic.bondtrader.ejb.BondTraderBeanLocal");
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	/*
	 * API to list all bonds
	 */
	@GET
	@Produces("application/json")
	public Response getAllBonds_asResponse() {
		try
		{
			List<Bond> allBonds = traderBean.getAllBonds();
			return Response.status(200).entity(allBonds).build();
		}
		catch( Exception e)
		{
			return Response.ok(false).entity("API Error!").build();
		}
	}
	
	

	/*
	 * API to filter the TOP 5 bonds
	 */
	@GET
	@Produces ("application/json")
	@Path ("/top5")
	public Response getTop5Bonds_asResponse() {
		try
		{
			List<Bond> top5 = traderBean.getTopFiveBonds();
			return Response.status(200).entity(top5).build();
		}
		catch (Exception e)
		{
			return Response.status(200).entity("Handling API Error!").build();
		}
	}
	
	

	/*
	 * API to filter bonds by type only
	 */
	@GET
	@Produces("application/json")
	@Path("/{param}")
	public Response getBondsByType_asResponse(@PathParam("param") String filter) {
		try
		{
			if (filter.length() == 0)
				return Response.status(200).entity(traderBean.getAllBonds()).build();
			else
				return Response.status(200).entity(traderBean.getBondsByType(filter)).build();
		}
		catch (Exception e)
		{
			return Response.ok(false).entity("Handling API Error!").build();
		}
	}
	


	/*
	 * API to filter bonds by rating only
	 */
	@GET
	@Produces("application/json")
	@Path("/rating/{param}")
	public Response getBondsByRating_asResponse(@PathParam("param") String filter) {
		try
		{
			if (filter.length() == 0)
				return Response.status(200).entity(traderBean.getAllBonds()).build();
			else
				return Response.status(200).entity(traderBean.getBondsByRating(filter)).build();
		}
		catch ( Exception e)
		{
			return Response.ok(false).entity("API Error!").build();
		}
		
		
	}
	


	/*
	 * API to filter bonds by currency
	 */
	@GET
	@Produces("application/json")
	@Path("/currency/{param}")
	public Response getBondsByCurrency_asResponse(@PathParam("param") String curr) {
		try
		{
			if (curr.length() == 0)
				return Response.status(200).entity(traderBean.getAllBonds()).build();
			else
				return Response.status(200).entity(traderBean.getBondsByCurrency(curr)).build();
		}
		catch (Exception e)
		{
			return Response.ok(false).entity("API Error !").build();
		}
	}
	

	/*
	 * API to filter by rating & type
	 */
	@GET
	@Produces("application/json")
	@Path("/query/{type}/{rating}")
	public Response getBondsByQuery_asResponse(@PathParam("type") String typeName, @PathParam("rating") String rating) {
		try
		{
			if (typeName.length() == 0 || rating.length() == 0 )
			{
				return Response.ok(false).entity("Either of typeName/rating missing!").build();
			}
			else
			{
				List<Bond> bondsByCriteria = traderBean.getBondsByRatingAndType(rating, typeName);
				return Response.status(200).entity(bondsByCriteria).build();
			}
		}
		catch (Exception e)
		{
			return Response.ok().entity("API Error!").build();
		}

	}
	
	
	/*
	 * API to retrieve results from bonds as per QueryParam
	 */
	@GET
	@Produces("application/json")
	public Response getBonds1(@QueryParam("type") @DefaultValue("") String typeName, @QueryParam("rating") @DefaultValue("") String rating, @QueryParam("currency") @DefaultValue("") String curr) {
		try
		{
			if(typeName.length()==0 && rating.length() == 0 && curr.length() == 0)
				return Response.status(200).entity(traderBean.getAllBonds()).build();
			else if (typeName.length()==0 && rating.length() == 0 && curr.length() != 0)
				return Response.status(200).entity(traderBean.getBondsByCurrency(curr)).build();
			else if (typeName.length()==0 && curr.length() == 0)
				return Response.status(200).entity(traderBean.getBondsByRating(rating)).build();
			else if (curr.length()==0 && rating.length() ==0)
				return Response.status(200).entity(traderBean.getBondsByType(typeName)).build();
			else if (typeName.length()==0)
				return Response.status(200).entity(traderBean.getBondsByRatingAndCurrency(rating, curr)).build();
			else if (rating.length()==0)
				return Response.status(200).entity(traderBean.getBondsByTypeAndCurrency(typeName, curr)).build();
			else if (curr.length()==0)
				return Response.status(200).entity(traderBean.getBondsByRatingAndType(rating, typeName)).build();
			else
				return Response.status(200).entity(traderBean.getBondsByCriteria(typeName, rating, curr)).build();
		}
		catch (Exception e)
		{
			return Response.ok(false).entity("Handling API Error!").build();
		}
		
	}
	
	

	/*
	 * API to bonds by currency & type
	 */
	@GET
	@Produces("application/json")
	@Path("/query2/{type}/{curr}")
	public Response getBondsByQuery2_asResponse(@PathParam("type") String typeName, @PathParam("curr") String currency) {
		try
		{
			// handling the exception if type and currency is not string
			if ( !(typeName instanceof String) || !(currency instanceof String) )
			{
				return Response.ok(false).entity("type / currency not string ").build();
			}
			
			if ( typeName.length() == 0 || currency.length() == 0 )
			{
				return Response.ok(false).entity("Either of typeName/currency in path missing!").build();
			}
			else
			{
				List<Bond> bondsByCriteria = traderBean.getBondsByTypeAndCurrency(typeName, currency);
				return Response.status(200).entity(bondsByCriteria).build();
			}
		}
		catch (Exception e)
		{
			return Response.ok(false).entity("API Error!").build();
		}

	}
	
	
	
	/*
	 * API to filter bonds by rating & currency
	 */
	@GET
	@Produces("application/json")
	@Path("/query3/{rating}/{curr}")
	public Response getBondsByQuery3_asResponse(@PathParam("rating") String rating, @PathParam("curr") String currency ) {
		try
		{
			List<Bond> bondsByCriteria = traderBean.getBondsByRatingAndCurrency(rating, currency);
			return Response.status(200).entity(bondsByCriteria).build();
		}
		catch (Exception e)
		{
			return Response.ok(false).entity("API Error").build();
		}
	}
	

	
	/*
	 * API to filter the bonds according to criteria
	 */
	@GET
	@Produces("application/json")
	@Path("/query4/{type}/{rating}/{curr}")
	public Response getFilteredBonds_asResponse(@PathParam("type") String type,@PathParam("rating") String rating, @PathParam("curr") String currency ) {
		try
		{
			List<Bond> bondsByCriteria = traderBean.getBondsByCriteria(type, rating, currency);
			return Response.status(200).entity(bondsByCriteria).build();
		}
		catch(Exception e)
		{
			return Response.ok(false).entity("API Error!").build();
		}
	}
	
	
	/*
	 * API to get client info
	 */
	@GET
	@Produces("application/json")
	@Path("/client_info/")
	public Response populateClient() {
		try {
			List<Client> names = traderBean.allClients();
			// return names;
			return Response.status(200).entity(names).build();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("API Error!");
			return Response.ok(false).entity("API Error").build();
		}
	}
	
	
	/*
	 * API to return response if client information retrieval is successful
	 */
	@POST
	@Path("/client_retrieval_success")
	@Consumes("application/x-www-form-urlencoded")
	public Response clientRetrievalSuccess(@FormParam("id") String id) {
		String updated = "";
		try {
			return Response.status(200).entity("Success").build();
		} catch (Exception e) {
			return Response.ok(false).entity("API Error").build();
		}

	}

	
	/*
	 * API to retrieve client info according to id passed
	 */
	@GET
	@Path("/client_retrieval/{id}")
	@Produces("application/json")
	public Response clientRetrieval(@PathParam("id") String id) {
		String updated = "";
		try {
			List<Client> clientInfo = traderBean.getClientById(id);
			return Response.status(200).entity(clientInfo).build();
		} catch (Exception e) {
			return Response.ok(false).entity("API Error").build();
		}

	}

	
	/*
	 * API to test POST api
	 */
	@POST
	@Path("/post_test")
	@Consumes("application/x-www-form-urlencoded")
	public Response post(@FormParam("name") String name, @FormParam("contact") String n) {
		String updated = "";
		try {
			System.out.println("Created successfully");
			System.out.println(n + " " + name);
			Integer received_contact = Integer.parseInt(n);
			System.out.println(received_contact + 1);
			return Response.status(200).entity("Success").build();
		} catch (Exception e) {
			System.out.println("API error...");
			return Response.ok(false).entity("API Error").build();
		}

	}

}
