package electronic.bondtrader.ejb;

import java.util.List;

import javax.ejb.Remote;

import electronic.bondtrader.jpa.Bond;
import electronic.bondtrader.jpa.Client;

@Remote
public interface BondTraderBeanRemote {
	
	public List<Bond> getAllBonds();
	public List<Bond> getBondsByType(String typeName);
	public List<Bond> getBondsByRating (String rating);
	public List<Bond> getBondsByCurrency (String curr);
	public List<Bond> getBondsByCriteria (String type, String rating, String currency);
	//public void bookTrade (String isin, int quantity);
	public List<Client> allClients();
	public List<Client> getClientById( String Id );
	public Bond getBondsById(String id) ;
	public List<Bond> getTopFiveBonds();
	public List<Bond> getBondsByRatingAndType(String rating, String typeName);
	public List<Bond> getBondsByRatingAndCurrency(String rating, String currency);
	public List<Bond> getBondsByTypeAndCurrency(String type, String currency);
	
}
