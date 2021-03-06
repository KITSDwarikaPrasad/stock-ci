package com.kfplc.ci.datafeed;

import java.util.Formatter;

public class ExpectedCSVRow {
	
	String storeCode;
	String ean;
	String stockLevel = "0";
	String rangedFlg = "1";
	String opco ;
	boolean noOutputFlag = false;
	boolean hasHeaderFlag = true;
	boolean unprocessedFileFlag = false;
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
	
	
	/**
	 * Marks that the there will not be any row in Expected CSV file
	 * @return the noOutputFlag
	 */
	public boolean isNoOutputFlag() {
		return noOutputFlag;
	}
	/**
	 * To mark the there will not be any row in Expected CSV file
	 * @param noOutputFlag the noOutputFlag to set
	 */
	public void setNoOutputFlag(boolean noOutputFlag) {
		this.noOutputFlag = noOutputFlag;
	}
	

	/**
	 * @return the hasHeaderFlag
	 */
	public boolean isHasHeaderFlag() {
		return hasHeaderFlag;
	}
	/**
	 * @param hasHeaderFlag the hasHeaderFlag to set
	 */
	public void setHasHeaderFlag(boolean hasHeaderFlag) {
		this.hasHeaderFlag = hasHeaderFlag;
	}
	/**
	 * Marks that the data should be found in Unprocessed log file
	 * @return the unprocessedFileFlag
	 */
	
	public boolean isUnprocessedFileFlag() {
		return unprocessedFileFlag;
	}
	/**
	 * To marks that the data should be found in Unprocessed log file
	 * @param unprocessedFileFlag the unprocessedFileFlag to set
	 */
	public void setUnprocessedFileFlag(boolean unprocessedFileFlag) {
		this.unprocessedFileFlag = unprocessedFileFlag;
	}
	/**
	 * @return Foramatted String 
	 */
	public String formatAsRow() {
		StringBuilder rowStringBuilder = new StringBuilder();
		Formatter formatter = new Formatter(rowStringBuilder);
		formatter.format("%1$4s,%2$13s,%3$s,%4$s,%5$s", 
				storeCode, ean, stockLevel, rangedFlg, opco);
		formatter.close();
		return rowStringBuilder.toString().replace(' ', '0');
	}
	
	
	
}
