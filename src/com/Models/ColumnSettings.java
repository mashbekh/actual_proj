package com.Models;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class ColumnSettings {
	
	private String productCol;
	private String otherprodCol;
	private String unitsCol;
	private String otherunitCol;
	private String priceCol;
	private String otherpriceCol;
	
	

	public ColumnSettings(String productCol, String otherprodCol, String unitsCol, String otherunitCol, String priceCol,
			String otherpriceCol) {
		super();
		this.productCol = productCol;
		this.otherprodCol = otherprodCol;
		this.unitsCol = unitsCol;
		this.otherunitCol = otherunitCol;
		this.priceCol = priceCol;
		this.otherpriceCol = otherpriceCol;
	}

	public ColumnSettings()
	{
		
	}
	
	public String getProductCol() {
		return productCol;
	}

	public void setProductCol(String productCol) {
		this.productCol = productCol;
	}

	public String getUnitsCol() {
		return unitsCol;
	}

	public void setUnitsCol(String unitsCol) {
		this.unitsCol = unitsCol;
	}

	public String getPriceCol() {
		return priceCol;
	}

	public void setPriceCol(String priceCol) {
		this.priceCol = priceCol;
	}

	public String getOtherprodCol() {
		return otherprodCol;
	}

	public void setOtherprodCol(String otherprodCol) {
		this.otherprodCol = otherprodCol;
	}

	public String getOtherunitCol() {
		return otherunitCol;
	}

	public void setOtherunitCol(String otherunitCol) {
		this.otherunitCol = otherunitCol;
	}

	public String getOtherpriceCol() {
		return otherpriceCol;
	}

	public void setOtherpriceCol(String otherpriceCol) {
		this.otherpriceCol = otherpriceCol;
	}
	
	

}
