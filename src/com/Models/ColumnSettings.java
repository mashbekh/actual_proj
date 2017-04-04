package com.Models;

public class ColumnSettings {
	
	private String productCol;
	private String unitsCol;
	private String priceCol;
	
	public ColumnSettings(String productCol, String unitsCol, String priceCol) {
		super();
		this.productCol = productCol;
		this.unitsCol = unitsCol;
		this.priceCol = priceCol;
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
	
	

}
