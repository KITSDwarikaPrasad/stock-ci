package com.kfplc.ci.datafeed;

import java.util.Formatter;

public class ExpectedCSVRow {
	
	String storeCode;
	String ean;
	String stockLevel;
	String rangedFlg = "1";
	String opco = "UKBQ";
	/**
	 * @return the storeCode
	 */
	public String getStoreCode() {
		return storeCode;
	}
	/**
	 * @param storeCode the storeCode to set
	 */
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	/**
	 * @return the ean
	 */
	public String getEan() {
		return ean;
	}
	/**
	 * @param ean the ean to set
	 */
	public void setEan(String ean) {
		this.ean = ean;
	}
	/**
	 * @return the stockLevel
	 */
	public String getStockLevel() {
		return stockLevel;
	}
	/**
	 * @param stockLevel the stockLevel to set
	 */
	public void setStockLevel(String stockLevel) {
		this.stockLevel = stockLevel;
	}
	/**
	 * @return the rangedFlg
	 */
	public String getRangedFlg() {
		return rangedFlg;
	}
	/**
	 * @param rangedFlg the rangedFlg to set
	 */
	public void setRangedFlg(String rangedFlg) {
		this.rangedFlg = rangedFlg;
	}
	/**
	 * @return the opco
	 */
	public String getOpco() {
		return opco;
	}
	/**
	 * @param opco the opco to set
	 */
	public void setOpco(String opco) {
		this.opco = opco;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder rowStringBuilder = new StringBuilder();
		Formatter formatter = new Formatter(rowStringBuilder);
		formatter.format("%1$s,%2$13s,%3$s,%4$s,%5$s", 
				storeCode, ean, stockLevel, rangedFlg, opco);
		formatter.close();
		return rowStringBuilder.toString().replace(' ', '0');
	}
	
	
	
}
