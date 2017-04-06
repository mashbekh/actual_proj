package com.Models;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class InvoiceSettings {
	
	private String invoiceSuffix;
	private String invoicePrefix;
	private Long invoiceSeqNo;
	private String invoiceFooter;
	private String invoiceNotes;
	
	
	public InvoiceSettings(String invoiceSuffix, String invoicePrefix, Long invoiceSeqNo, String invoiceFooter,
			String invoiceNotes) {
		super();
		this.invoiceSuffix = invoiceSuffix;
		this.invoicePrefix = invoicePrefix;
		this.invoiceSeqNo = invoiceSeqNo;
		this.invoiceFooter = invoiceFooter;
		this.invoiceNotes = invoiceNotes;
	}
	
	public InvoiceSettings()
	{
		
	}


	public String getInvoiceSuffix() {
		return invoiceSuffix;
	}


	public void setInvoiceSuffix(String invoiceSuffix) {
		this.invoiceSuffix = invoiceSuffix;
	}


	public String getInvoicePrefix() {
		return invoicePrefix;
	}


	public void setInvoicePrefix(String invoicePrefix) {
		this.invoicePrefix = invoicePrefix;
	}


	public Long getInvoiceSeqNo() {
		return invoiceSeqNo;
	}


	public void setInvoiceSeqNo(Long invoiceSeqNo) {
		this.invoiceSeqNo = invoiceSeqNo;
	}


	public String getInvoiceFooter() {
		return invoiceFooter;
	}


	public void setInvoiceFooter(String invoiceFooter) {
		this.invoiceFooter = invoiceFooter;
	}


	public String getInvoiceNotes() {
		return invoiceNotes;
	}


	public void setInvoiceNotes(String invoiceNotes) {
		this.invoiceNotes = invoiceNotes;
	}
	
	
	

}
