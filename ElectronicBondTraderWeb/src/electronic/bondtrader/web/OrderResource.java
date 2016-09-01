package electronic.bondtrader.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import electronic.bondtrader.ejb.BondCalculationBeanLocal;
import electronic.bondtrader.ejb.BondTraderBeanLocal;
import electronic.bondtrader.jpa.Trade;

class Result {
	public double cleanPrice;
	public double dirtyPrice;
	public double accruedInterest;
	public double settledAmount;
	public Result(double cleanPrice, double dirtyPrice, double accruedInterest, double settledAmount) {
		super();
		this.cleanPrice = cleanPrice;
		this.dirtyPrice = dirtyPrice;
		this.accruedInterest = accruedInterest;
		this.settledAmount = settledAmount;
	}	
}

class ResultRecompute {
	public double yield;
	public double dirtyPrice;
	public double accruedInterest;
	public double settledAmount;
	public ResultRecompute(double yield, double dirtyPrice, double accruedInterest, double settledAmount) {
		super();
		this.yield = yield;
		this.dirtyPrice = dirtyPrice;
		this.accruedInterest = accruedInterest;
		this.settledAmount = settledAmount;
	}	
}

@RequestScoped
@Path("/order")
@Produces({ "*/*", "application/json", "application/xml" })
@Consumes({ "*/*", "application/json", "application/xml" })
public class OrderResource {
	Result result;
	ResultRecompute resultRecompute;
	
	private BondCalculationBeanLocal calculatorBean;
	
	public OrderResource() {
		Context context = null;
		try {
			context = new InitialContext();
			
			calculatorBean = (BondCalculationBeanLocal) context.lookup(
					"java:global/ElectronicBondTrader/ElectronicBondTraderEJB/BondCalculationBean!electronic.bondtrader.ejb.BondCalculationBeanLocal");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*
	 * API to check Get rest api
	 */
	@GET
	@Produces("text/plain")
	public String getText(@FormParam("isin") String name, @FormParam("quantity") String quantity,
			@FormParam("trade_date") String trade_date, @FormParam("yield") String yield,
			@FormParam("client_id") String client_id){
		return name+quantity+trade_date+yield+client_id;
	}
	
	
	/*
	 * API for computation of bond related values before buying
	 */
	@POST
	@Path("/compute_trade")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response computeBond(@FormParam("isin") String name, @FormParam("quantity") String quantity,
			@FormParam("trade_date") String trade_date, @FormParam("yield") String yield,
			@FormParam("client_id") String client_id) {

		try {
			// TODO: calculation stuff!
			//TODO : days between trade_date and settlement is negative ! handle that
			
			// int isin = Integer.parseInt(name);
			//Bond bond = bean.getBondsById(name);
			
			List<Double> calculations = 
					calculatorBean.bookTrade(name, quantity, trade_date, yield, client_id);
			
			if (calculations == null)
			{
				return Response.ok(false).entity("Date not correct / Some parameter supplied erroneous!").build();
			}
			
			result = new Result (calculations.get(0),calculations.get(1),calculations.get(2),calculations.get(3));
			return Response.status(200).entity(result).build();
			//return Response.status(200).entity("Working...").build();
		} catch (Exception e) {
			System.out.println("API error...");
			return Response.ok(false).entity("API Error").build();
		}

	}
	
	
	/*
	 * API to compute trade values before buying
	 */
	@GET
	@Path("/compute_trade")
	@Produces("application/json")
	public Response showResult(){
		try {
			return Response.status(200).entity(result).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.ok(false).entity("API Error").build();

			
		}
	}
	
	
	/*
	 * API for recomputation
	 */
	@POST
	@Path("/recompute_trade")
	@Consumes("application/x-www-form-urlencoded")
	public Response recomputeBond(@FormParam("isin") String isin, @FormParam("quantity") String quantity,
			@FormParam("trade_date") String trade_date, @FormParam("settle_date") String settle_date,
			@FormParam("clean_price") String clean_price, @FormParam("client_id") String client_id) {

		try 
		{
			//TODO : days between trade_date and settlement is negative ! handle that
			// date format = mm/dd/yyyy
			//irrelevant
//			DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
//			Date tradeDate = df.parse(trade_date);
//			Date settleDate = df.parse(settle_date);
//			if ( tradeDate.compareTo(settleDate) > 0 )
//			{
//				return Response.ok(false).entity("Trade Date is after Settlement Date! Try Again").build();
//			}
			List<Double> calculations = 
					calculatorBean.recomputeTrade(isin, quantity, trade_date, clean_price, client_id);
			
			//handling exception of wrong date : recent edit
			if (calculations == null)
			{
				return Response.ok(false).entity("Date not correct / Some parameter supplied erroneous!").build();
			}
			
			resultRecompute = new ResultRecompute (calculations.get(0),calculations.get(1),calculations.get(2),calculations.get(3));
			return Response.status(200).entity(resultRecompute).build();
		} 
		catch (Exception e) 
		{
			System.out.println("API error...");
			return Response.ok(false).entity("API Error").build();
		}

	}
	
	
	/*
	 * API to recompute the bond values on changing the yield/ clean price
	 */
	@GET
	@Path("/recompute_trade")
	@Produces("application/json")
	public Response showResultRecompute(){
		try 
		{
			return Response.status(200).entity(resultRecompute).build();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.ok(false).entity("API Error").build();	
		}
	}
	
	
	/*
	 * API to buy bond
	 * takes in input <the parameters> mentioned
	 */
	@POST
	@Path("/buy")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response buyTrade(@FormParam("isin") String isin, @FormParam("quantity") String quantity,
			@FormParam("trade_date") String trade_date,
			@FormParam("dirty_price") String dirty_price, @FormParam("client_id") String client_id){
		try {
				calculatorBean.buyBond(trade_date, quantity, dirty_price, client_id, isin);
				return Response.status(200).entity("Trade completed successfully").build();
		} catch (Exception e) {
			System.out.println("API error...");
			return Response.ok(false).entity("API Error").build();
		}
		
	}
	
	
	/*
	 * API to get all the transactions done till date
	 */
	@GET
	@Path("/show_trades")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response showTrades(){
		
		try{
			List<Trade> allTrades = calculatorBean.showAllTrades();
			return Response.status(200).entity(allTrades).build();
		}
		catch (Exception e) {
			System.out.println("API error...");
			return Response.ok(false).entity("API Error").build();
		}
	}
	
}
