package electronic.bondtrader.jpa;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="tradedetails")
@NamedQuery(name="Trade.findAll", query="SELECT t FROM Trade t")

public class Trade implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private int order_ID;
	private double trade_Amount;
	private String trade_Date;
	private double trade_Price;
	private String trade_Type;
	private int trade_Volume;
//	private Client client = null;
//	private Bond bond = null;
	
	private int client_Id;
	private String bond_ID;
	
	
	public Trade() {
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getOrder_ID() {
		return this.order_ID;
	}

	public void setOrder_ID(int order_ID) {
		this.order_ID = order_ID;
	}

	public double getTrade_Amount() {
		return this.trade_Amount;
	}

	public void setTrade_Amount(double trade_Amount) {
		this.trade_Amount = trade_Amount;
	}

	public String getTrade_Date() {
		return this.trade_Date;
	}

	public void setTrade_Date(String trade_Date) {
		this.trade_Date = trade_Date;
	}

	public double getTrade_Price() {
		return this.trade_Price;
	}

	public void setTrade_Price(double trade_Price) {
		this.trade_Price = trade_Price;
	}

	public String getTrade_Type() {
		return this.trade_Type;
	}

	public void setTrade_Type(String trade_Type) {
		this.trade_Type = trade_Type;
	}

	public int getTrade_Volume() {
		return this.trade_Volume;
	}

	public void setTrade_Volume(int trade_Volume) {
		this.trade_Volume = trade_Volume;
	}

	public int getClient_Id() {
		return client_Id;
	}

	public void setClient_Id(int client_Id) {
		this.client_Id = client_Id;
	}

	public String getBond_ID() {
		return bond_ID;
	}

	public void setBond_ID(String bond_ID) {
		this.bond_ID = bond_ID;
	}
	
//	@ManyToOne (fetch = FetchType.LAZY)
//	@JoinColumn(name="Client_Id")
//	@JsonBackReference (value="user-client")
//	public Client getClient() {
//		return client;
//	}
//
//	public void setClient(Client client) {
//		this.client = client;
//	}
	
//	@ManyToOne (fetch = FetchType.LAZY)
//	@JoinColumn(name="Bond_ID")
//	@JsonBackReference (value="user-bond")
//	public Bond getBond() {
//		return bond;
//	}
//
//	public void setBond(Bond bond) {
//		this.bond = bond;
//	}

}