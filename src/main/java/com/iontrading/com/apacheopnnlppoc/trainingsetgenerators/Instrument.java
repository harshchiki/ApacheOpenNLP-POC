package com.iontrading.com.apacheopnnlppoc.trainingsetgenerators;

public class Instrument {
	private String instrumentID;
	private String isin;
	private String description;
	private String issuerId;
	private String currentStr;
	private String date;

	public Instrument(final String instrumentId, final String isin, final String desc, final String currencyStr, final String date) {
		this.instrumentID = instrumentId;
		this.isin = isin;
		this.description = desc;
		this.currentStr = currencyStr;
		this.date = date;
	}
	
	public String getInstrumentID() {
		return instrumentID;
	}
	public void setInstrumentID(String instrumentID) {
		this.instrumentID = instrumentID;
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIssuerId() {
		return issuerId;
	}
	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}
	public String getCurrentStr() {
		return currentStr;
	}
	public void setCurrentStr(String currentStr) {
		this.currentStr = currentStr;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@Override
	public int hashCode() {
		return this.instrumentID.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		Instrument other = (Instrument) o;
		if(other != null) {
			if(this.instrumentID.equals(other.instrumentID)) {
				return true;
			}
		}
		return false;
	}
}
