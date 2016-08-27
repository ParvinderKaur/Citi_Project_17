package electronic.bondtrader.web;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import electronic.bondtrader.ejb.BondTraderBeanLocal;
import electronic.bondtrader.jpa.Bond;

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
	public List<Bond> getBonds(/*@QueryParam ("filter") @DefaultValue("")  String filter*/){
		//if(filter.length() ==0)
		 	return bean.getAllBonds();
		//else
			//return null; //bean.getProductsByName(filter);
	}
}
