package com.example.bank.Model;

public class TopUp {
	private String senum;
	private String scnum;
	private String expDate;

	public TopUp() {
	}

	public TopUp(String senum, String scnum, String expDate) {
		super();
		this.senum = senum;
		this.scnum = scnum;
		this.expDate = expDate;
	}

	public String getSenum() {
		return senum;
	}

	public void setSenum(String senum) {
		this.senum = senum;
	}

	public String getScnum() {
		return scnum;
	}

	public void setScnum(String scnum) {
		this.scnum = scnum;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	@Override
	public String toString() {
		return "TopUp [senum=" + senum + ", scnum=" + scnum + ", expDate=" + expDate + "]";
	}

}
