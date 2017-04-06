package com.Models;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class POSettings {

	private String POSuffix;
	private String POPrefix;
	private String POFooter;
	private String PONotes;
	private Long POSeqNo;
	
	public POSettings(String pOSuffix, String pOPrefix, String pOFooter, String pONotes, Long pOSeqNo) {
		super();
		POSuffix = pOSuffix;
		POPrefix = pOPrefix;
		POFooter = pOFooter;
		PONotes = pONotes;
		POSeqNo = pOSeqNo;
	}

	public POSettings()
	{
		
	}
	
	public String getPOSuffix() {
		return POSuffix;
	}

	public void setPOSuffix(String pOSuffix) {
		POSuffix = pOSuffix;
	}

	public String getPOPrefix() {
		return POPrefix;
	}

	public void setPOPrefix(String pOPrefix) {
		POPrefix = pOPrefix;
	}

	public String getPOFooter() {
		return POFooter;
	}

	public void setPOFooter(String pOFooter) {
		POFooter = pOFooter;
	}

	public String getPONotes() {
		return PONotes;
	}

	public void setPONotes(String pONotes) {
		PONotes = pONotes;
	}

	public Long getPOSeqNo() {
		return POSeqNo;
	}

	public void setPOSeqNo(Long pOSeqNo) {
		POSeqNo = pOSeqNo;
	}
	
	
	
}
