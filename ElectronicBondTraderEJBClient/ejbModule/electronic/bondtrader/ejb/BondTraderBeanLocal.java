package electronic.bondtrader.ejb;

import java.util.List;

import javax.ejb.Local;

import electronic.bondtrader.jpa.Bond;

@Local
public interface BondTraderBeanLocal {
	
	public List<Bond> getAllBonds();
}
