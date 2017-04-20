package com.kfplc.ci.datafeed;

import java.util.Formatter;

/**
 * The class to hold data reflecting one row in input text file.
 * Configure Default values here itself, override by calling setter methods.
 * All fields are String only.
 * No need to left pad zeros separately as the class takes care of formatting.
 * @author prasad01
 */
public class InputTextRow {
	
	String record_identifier = "1";
	String full_store_code;
	String stock_date = "ddmmyyyy";
	String bqcode;
	String current_stock_quantity = "0";
	String optimum_stock_quantity = "0";
	String on_order_quantity = "0";
	String ranged_flag = "1";
	String cdl = "0";
	String out_of_stock = "0";
	String stocked_flag = "0";
	String creation_date = "ddmmyyyy";
	
	
	/**
	 * @return the record_identifier
	 */
	public String getRecord_identifier() {
		return record_identifier;
	}
	/**
	 * @param record_identifier the record_identifier to set
	 */
	public void setRecord_identifier(String record_identifier) {
		this.record_identifier = record_identifier;
	}
	/**
	 * @return the store_code
	 */
	public String getFull_store_code() {
		return full_store_code;
	}
	/**
	 * @param store_code the store_code to set
	 */
	public void setFull_store_code(String store_code) {
		this.full_store_code = store_code;
	}
	/**
	 * @return the stock_date
	 */
	public String getStock_date() {
		return stock_date;
	}
	/**
	 * @param stock_date the stock_date to set
	 */
	public void setStock_date(String stock_date) {
		this.stock_date = stock_date;
	}
	/**
	 * @return the bqcode
	 */
	public String getBqcode() {
		return bqcode;
	}
	/**
	 * @param bqcode the bqcode to set
	 */
	public void setBqcode(String bqcode) {
		this.bqcode = bqcode;
	}
	/**
	 * @return the current_stock_quantity
	 */
	public String getCurrent_stock_quantity() {
		return current_stock_quantity;
	}
	/**
	 * @param current_stock_quantity the current_stock_quantity to set
	 */
	public void setCurrent_stock_quantity(String current_stock_quantity) {
		this.current_stock_quantity = current_stock_quantity;
	}
	/**
	 * @return the optimum_stock_quantity
	 */
	public String getOptimum_stock_quantity() {
		return optimum_stock_quantity;
	}
	/**
	 * @param optimum_stock_quantity the optimum_stock_quantity to set
	 */
	public void setOptimum_stock_quantity(String optimum_stock_quantity) {
		this.optimum_stock_quantity = optimum_stock_quantity;
	}
	/**
	 * @return the on_order_quantity
	 */
	public String getOn_order_quantity() {
		return on_order_quantity;
	}
	/**
	 * @param on_order_quantity the on_order_quantity to set
	 */
	public void setOn_order_quantity(String on_order_quantity) {
		this.on_order_quantity = on_order_quantity;
	}
	/**
	 * @return the ranged_flag
	 */
	public String getRanged_flag() {
		return ranged_flag;
	}
	/**
	 * @param ranged_flag the ranged_flag to set
	 */
	public void setRanged_flag(String ranged_flag) {
		this.ranged_flag = ranged_flag;
	}
	/**
	 * @return the cdl
	 */
	public String getCdl() {
		return cdl;
	}
	/**
	 * @param cdl the cdl to set
	 */
	public void setCdl(String cdl) {
		this.cdl = cdl;
	}
	/**
	 * @return the out_of_stock
	 */
	public String getOut_of_stock() {
		return out_of_stock;
	}
	/**
	 * @param out_of_stock the out_of_stock to set
	 */
	public void setOut_of_stock(String out_of_stock) {
		this.out_of_stock = out_of_stock;
	}
	/**
	 * @return the stocked_flag
	 */
	public String getStocked_flag() {
		return stocked_flag;
	}
	/**
	 * @param stocked_flag the stocked_flag to set
	 */
	public void setStocked_flag(String stocked_flag) {
		this.stocked_flag = stocked_flag;
	}
	/**
	 * @return the creation_date
	 */
	public String getCreation_date() {
		return creation_date;
	}
	/**
	 * @param creation_date the creation_date to set
	 */
	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}
	
	
	/**
	 * The method to join the different components to create a single row.
	 * The method takes care of formatting.
	 * Formatting details are below -
	 *  <p>FIELD_NAME			PRECISION	Splitted row sample<p>
	 *  <p>Record_Identifier		1	1                      <p>
	 *  <p>Store_Code				6	FAE111                 <p>
	 *  <p>Stock_Date				8	02102016               <p>
	 *  <p>BQCode					8	20115883               <p>
	 *  <p>Current_Stock_Quantity	11	0000016.000            <p>
	 *  <p>Optimum_Stock_Quantity	11	00000000000            <p>
	 *  <p>On_Order_Quantity		11	00000000000            <p>
	 *  <p>Ranged_Flag				1	1                      <p>
	 *  <p>CDL						3	000                    <p>
	 *  <p>Out_Of_Stock				1	0                      <p>
	 *  <p>Stocked_Flag				1	0                      <p>
	 *  <p>Creation_Date			8	03102016               <p>
	 */
	
	public String formatAsRow() {
		StringBuilder rowStringBuilder = new StringBuilder();
		Formatter formatter = new Formatter(rowStringBuilder);
//		formatter.format("%4$2s %3$2s %2$2s %1$2s", "a", "b", "c", "d");
		formatter.format("%1$1s%2$6s%3$8s%4$8s%5$11s%6$11s%7$11s%8$1s%9$3s%10$1s%11$1s%12$8s", 
				record_identifier, full_store_code, stock_date, bqcode, current_stock_quantity, optimum_stock_quantity, on_order_quantity, ranged_flag, cdl, out_of_stock, stocked_flag, creation_date);
		formatter.close();
		return rowStringBuilder.toString().replace(' ', '0');
	}
	
}
