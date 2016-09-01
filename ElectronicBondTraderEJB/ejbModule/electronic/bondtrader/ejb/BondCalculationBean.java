package electronic.bondtrader.ejb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import electronic.bondtrader.jpa.Bond;
import electronic.bondtrader.jpa.Client;
import electronic.bondtrader.jpa.Trade;

/**
 * Session Bean implementation class BondCalculationBean
 */
@Stateless
@Remote(BondCalculationBeanRemote.class)
@Local(BondCalculationBeanLocal.class)

public class BondCalculationBean implements BondCalculationBeanRemote, BondCalculationBeanLocal {

	
	@PersistenceContext(unitName = "ElectronicBondTraderJPA-PU")
	EntityManager em;
    /**
     * Default constructor. 
     */
    public BondCalculationBean() {
        // TODO Auto-generated constructor stub
    }
    public List<Double> bookTrade (String isin, String quantity, String trade_date, String yield,  String client_id) {
		try
		{
			Query query = em
					.createQuery("select b from Bond as b" + " where b.bond_ID = :search");
			query.setParameter("search", isin);
			Bond currentBond = (Bond) query.getSingleResult();
			Integer quantity_here = Integer.parseInt(quantity);
			int client_id_here = Integer.parseInt(client_id);
			Double yield_here = Double.parseDouble(yield);
//			BondPriceCalculator calc = new BondPriceCalculator();
			List<Double> calculations = evaluateBond(currentBond, quantity_here, trade_date, yield_here);
//			BondPriceCalculator priceCalc = new BondPriceCalculator();
			
			return calculations;
		}
		catch(Exception e)
		{
			return null;
		}
	}
    
    
    public void buyBond(String trade_date, String quantity, String dirtyPrice, String client_id, String isin){
    	
    	Trade currentTrade = new Trade();
    	currentTrade.setTrade_Date(trade_date);
    	currentTrade.setTrade_Volume(Integer.parseInt(quantity));
    	currentTrade.setTrade_Price(Double.parseDouble(dirtyPrice));
    	currentTrade.setTrade_Type("BUY");
    	double settledAmt = Integer.parseInt(quantity) * Double.parseDouble(dirtyPrice);
    	currentTrade.setTrade_Amount(settledAmt);
//    	Query bquery = em
//				.createQuery("select b from Bond as b" + " where b.bond_ID = :search");
//		bquery.setParameter("search", isin);
//		Bond currentBond = (Bond) bquery.getSingleResult();
		currentTrade.setBond_ID(isin);
		
//		Query cquery = em
//				.createQuery("select c from Client as c" + " where c.client_Id = :search");
//		cquery.setParameter("search", Integer.parseInt(client_id));
//		Client currentClient = (Client) cquery.getSingleResult();
		currentTrade.setClient_Id(Integer.parseInt(client_id));
		em.persist(currentTrade);
		
		
    }
    
    public List<Trade> showAllTrades(){
    	Query query = em
				.createQuery("select t from Trade as t");
		
		List<Trade> listOfTrades = (List<Trade>) query.getResultList();
    	return listOfTrades;
    }
    
    public List<Double> recomputeTrade (String isin, String quant, String trade_date, String cleanPrice, String client_id) {
    	Query query = em
				.createQuery("select b from Bond as b" + " where b.bond_ID = :search");
		query.setParameter("search", isin);
		Bond currentBond = (Bond) query.getSingleResult();
		int client_id_here = Integer.parseInt(client_id);
		Double cleanPrice_here = Double.parseDouble(cleanPrice);
		double faceValue=100.0;	// 
		double couponRate=currentBond.getCoupon_Rate();	// from database		
		String stDate=currentBond.getStart_Date();   // from database		
		String matDate=currentBond.getMaturity_Date();		// from database
		//double yieldToMaturity = yield;	//User Input
		double presentValue = Double.parseDouble(cleanPrice);
		int quantity = Integer.parseInt(quant);
		double yield; 	// calculate
		double cashFlow;		// calculate
		double dirtyPrice;		// calculate
		double accruedInterest;	// calculate

		long noOfDays;
		int noOfYears;

		Date startDate;	
		Date tradeDate;
		Date settlementDate;
		Date maturityDate;
		
		double settledAmount;	
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/M/yyyy");
		try {
		startDate = dateformat.parse(stDate);
		tradeDate = dateformat.parse(trade_date);
		maturityDate = dateformat.parse(matDate);
		
		// add 2 days to trade date to calculate settlement date
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(tradeDate);
		cal.add(Calendar.DATE, 2);
		
		settlementDate=cal.getTime();
		noOfDays=countNoOfDays(settlementDate,maturityDate);
		if(noOfDays<0){
			System.out.println("Date not correct");
			return null;
		}
		else{
			//	 System.out.println("No of Days= "+ noOfDays);
			noOfYears=(int) Math.ceil(noOfDays/365.0); 
			cashFlow = calcCashFlow( couponRate, faceValue);
			yield=calcYield(noOfYears, presentValue, faceValue, cashFlow);
			//presentValue = calcPresentValue( faceValue, yieldToMaturity, noOfYears, cashFlow);	
			accruedInterest=calcAccruedInterest(startDate,settlementDate, cashFlow);
			dirtyPrice=calcDirtyPrice(presentValue, accruedInterest);
			settledAmount=calcSettledAmount(quantity, dirtyPrice);
			
			
			// Print all variables
			System.out.println(settlementDate);
			System.out.println("No of Days= "+ noOfDays);
			System.out.println("No of years= "+ noOfYears);
			System.out.println("Cash Flow= "+ cashFlow);
			System.out.println("Clean Pricee= "+ presentValue);
			System.out.println("accrued interest= "+ accruedInterest);
			System.out.println("dirty price= "+ dirtyPrice);
			System.out.println("Settled Amount = " + settledAmount);
			System.out.println("calculated ytm = " + yield);
			List<Double> result = Arrays.asList(yield,dirtyPrice,accruedInterest,settledAmount);//new ArrayList <Double>(presentValue,dirtyPrice,settledAmount);
			return result;
		}


	} catch (ParseException e) {	
		//	 System.out.println("error in date!!!!");
		e.printStackTrace();
		return null;
	}
		
    }
    




	public long countNoOfDays(Date startDate,Date endDate){

		long noOfDays = endDate.getTime() - startDate.getTime();
		return noOfDays/(60*60*24*1000);

	}

	public double calcCashFlow(double coupon, double fv){

		return (Math.round((coupon*fv/100)*1000))/1000.0;

	}

	public double calcPresentValue(double fv, double ytm, int noOfYears, double cf){

		double pv=0.0;

		for(int i=1;i<=noOfYears;i++){
			pv+=cf/Math.pow((1+ytm/100),i);
		}

		pv+=fv/Math.pow((1+ytm/100),noOfYears);

		return (Math.round(pv*1000))/1000.0;
	}

	public double calcAccruedInterest(Date issueDate, Date settlementDate, double cf){

		double accruedInterest;
		Calendar cal = Calendar.getInstance();
		cal.setTime(issueDate);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(settlementDate);
		int year = cal1.get(Calendar.YEAR);
		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.YEAR, year);
		cal2.set(Calendar.MONTH, month);
		cal2.set(Calendar.DAY_OF_MONTH,day);
		
		Date accruedDate = cal2.getTime();
		long noOfDays=countNoOfDays(accruedDate,settlementDate);
		if (noOfDays<0) {
			cal2.set(Calendar.YEAR, year-1);
			accruedDate = cal2.getTime();
			noOfDays=countNoOfDays(accruedDate,settlementDate);		
		}
		accruedInterest = noOfDays*cf/(360);
		return (Math.round(accruedInterest*1000))/1000.0;

	}

	public double calcDirtyPrice(double presentValue, double accruedInterest){

		double dirtyPrice;
		dirtyPrice = presentValue + accruedInterest;
		return (Math.round(dirtyPrice*1000))/1000.0;

	}
	
	public double calcSettledAmount(int quantity, double dirtyPrice){
		return (Math.round((quantity*dirtyPrice)*1000))/1000.0;
	}
	
	public double calcYield(int noOfYears, double presentValue, double faceValue,double cashFlow){
		double yield=200*(cashFlow+(faceValue-presentValue)/noOfYears)/(faceValue+presentValue);
		return (Math.round(yield*1000))/1000.0;
		
	}



	public  List<Double> evaluateBond(Bond currentBond, int quantity, String trDate, double yield ) {
		
		// TODO Auto-generated method stub
		double faceValue=100.0;	// 
		double couponRate=currentBond.getCoupon_Rate();	// from database
		
		
		String stDate=currentBond.getStart_Date();   // from database
		
		String matDate=currentBond.getMaturity_Date();		// from database


		double yieldToMaturity = yield;	//User Input


		double presentValue; 	// calculate
		double cashFlow;		// calculate
		double dirtyPrice;		// calculate
		double accruedInterest;	// calculate

		long noOfDays;
		int noOfYears;

		Date startDate;	
		Date tradeDate;
		Date settlementDate;
		Date maturityDate;
		
		double settledAmount;		
		
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/M/yyyy");
			try {
			startDate = dateformat.parse(stDate);
			tradeDate = dateformat.parse(trDate);
			maturityDate = dateformat.parse(matDate);
			
			// add 2 days to trade date to calculate settlement date
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(tradeDate);
			cal.add(Calendar.DATE, 2);
			
			settlementDate=cal.getTime();
			noOfDays=countNoOfDays(settlementDate,maturityDate);
			if(noOfDays<0){
				System.out.println("Date not correct");
				return null;
			}
			else{

				//	 System.out.println("No of Days= "+ noOfDays);
				noOfYears=(int) Math.ceil(noOfDays/365.0); 
				cashFlow = calcCashFlow( couponRate, faceValue);
				presentValue = calcPresentValue( faceValue, yieldToMaturity, noOfYears, cashFlow);	
				accruedInterest=calcAccruedInterest(startDate,settlementDate, cashFlow);
				dirtyPrice=calcDirtyPrice(presentValue, accruedInterest);
				settledAmount=calcSettledAmount(quantity, dirtyPrice);
				//yield=calcYield(noOfYears, presentValue, faceValue, cashFlow);
				
				// Print all variables
				System.out.println(settlementDate);
				System.out.println("No of Days= "+ noOfDays);
				System.out.println("No of years= "+ noOfYears);
				System.out.println("Cash Flow= "+ cashFlow);
				System.out.println("Clean Pricee= "+ presentValue);
				System.out.println("accrued interest= "+ accruedInterest);
				System.out.println("dirty price= "+ dirtyPrice);
				System.out.println("Settled Amount = " + settledAmount);
				System.out.println("calculated ytm = " + yield);
				List<Double> result = Arrays.asList(presentValue,dirtyPrice,accruedInterest,settledAmount);//new ArrayList <Double>(presentValue,dirtyPrice,settledAmount);
				return result;
			}


		} catch (ParseException e) {	
			//	 System.out.println("error in date!!!!");
			e.printStackTrace();
			return null;
		}
		
	}
	}


