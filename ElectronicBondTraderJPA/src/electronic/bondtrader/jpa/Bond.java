package electronic.bondtrader.jpa;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;



/**
 * The persistent class for the bondsmaster database table.
 * 
 */
@Entity
@Table(name="bondsmaster")
@NamedQuery(name="Bond.findAll", query="SELECT b FROM Bond b")
public class Bond implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private String bond_ID;

	private String bond_Currency;

	private String bond_Type;

	private double change;

	private double coupon_Rate;

	private String credit_Rating;

	private double face_Value;

	private double high;

	private String issuer_Name;

	private double last;

	private double low;

	private String maturity_Date;

	private int serial_ID;

	private String start_Date;

	private double yield;
	
//	private List<Trade> tradesOfBond = null;

	public Bond() {
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public String getBond_ID() {
		return this.bond_ID;
	}

	public void setBond_ID(String bond_ID) {
		this.bond_ID = bond_ID;
	}

	public String getBond_Currency() {
		return this.bond_Currency;
	}

	public void setBond_Currency(String bond_Currency) {
		this.bond_Currency = bond_Currency;
	}

	public String getBond_Type() {
		return this.bond_Type;
	}

	public void setBond_Type(String bond_Type) {
		this.bond_Type = bond_Type;
	}

	public double getChange() {
		return this.change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public double getCoupon_Rate() {
		return this.coupon_Rate;
	}

	public void setCoupon_Rate(double coupon_Rate) {
		this.coupon_Rate = coupon_Rate;
	}

	public String getCredit_Rating() {
		return this.credit_Rating;
	}

	public void setCredit_Rating(String credit_Rating) {
		this.credit_Rating = credit_Rating;
	}

	public double getFace_Value() {
		return this.face_Value;
	}

	public void setFace_Value(double face_Value) {
		this.face_Value = face_Value;
	}

	public double getHigh() {
		return this.high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public String getIssuer_Name() {
		return this.issuer_Name;
	}

	public void setIssuer_Name(String issuer_Name) {
		this.issuer_Name = issuer_Name;
	}

	public double getLast() {
		return this.last;
	}

	public void setLast(double last) {
		this.last = last;
	}

	public double getLow() {
		return this.low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public String getMaturity_Date() {
		return this.maturity_Date;
	}

	public void setMaturity_Date(String maturity_Date) {
		this.maturity_Date = maturity_Date;
	}

	public int getSerial_ID() {
		return this.serial_ID;
	}

	public void setSerial_ID(int serial_ID) {
		this.serial_ID = serial_ID;
	}

	public String getStart_Date() {
		return this.start_Date;
	}

	public void setStart_Date(String start_Date) {
		this.start_Date = start_Date;
	}

	public double getYield() {
		return this.yield;
	}

	public void setYield(double yield) {
		this.yield = yield;
	}
	
//	@OneToMany(mappedBy="bond", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
//	@JsonManagedReference (value="user-bond")
//	public List<Trade> getTradesOfBond() {
//		return tradesOfBond;
//	}
//
//	public void setTradesOfBond(List<Trade> tradesOfBond) {
//		this.tradesOfBond = tradesOfBond;
//	}

}