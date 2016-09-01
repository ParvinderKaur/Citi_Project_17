package electronic.bondtrader.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import electronic.bondtrader.jpa.Bond;
import electronic.bondtrader.jpa.Trade;

@Remote
public interface BondCalculationBeanRemote {

	public List<Double> bookTrade (String isin, String quantity, String trade_date, String yield,  String client_id);
	public long countNoOfDays(Date startDate,Date endDate);
	public double calcCashFlow(double coupon, double fv);
	public double calcPresentValue(double fv, double ytm, int noOfYears, double cf);
	public double calcAccruedInterest(Date issueDate, Date settlementDate, double cf);
	public double calcDirtyPrice(double presentValue, double accruedInterest);
	public double calcSettledAmount(int quantity, double dirtyPrice);
	public double calcYield(int noOfYears, double presentValue, double faceValue,double cashFlow);
	public  List<Double> evaluateBond(Bond currentBond, int quantity, String trDate, double yield );
	public List<Double> recomputeTrade (String isin, String quant, String trade_date, String cleanPrice, String client_id);
	public List<Trade> showAllTrades();
	public void buyBond(String trade_date, String quantity, String dirtyPrice, String client_id, String isin);
}
