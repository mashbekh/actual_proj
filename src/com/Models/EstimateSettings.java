package com.Models;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class EstimateSettings {
	
	private String estimateSuffix;
	private String estimatePrefix;
	private Long estimateSeqNo;
	private String estimateFooter;
	private String estimateNotes;
	
	
	public EstimateSettings(String estimateSuffix, String estimatePrefix, Long estimateSeqNo, String estimateFooter,
			String estimateNotes) {
		super();
		this.estimateSuffix = estimateSuffix;
		this.estimatePrefix = estimatePrefix;
		this.estimateSeqNo = estimateSeqNo;
		this.estimateFooter = estimateFooter;
		this.estimateNotes = estimateNotes;
	}

	public EstimateSettings()
	{
		
	}
	public String getEstimateSuffix() {
		return estimateSuffix;
	}


	public void setEstimateSuffix(String estimateSuffix) {
		this.estimateSuffix = estimateSuffix;
	}


	public String getEstimatePrefix() {
		return estimatePrefix;
	}


	public void setEstimatePrefix(String estimatePrefix) {
		this.estimatePrefix = estimatePrefix;
	}


	public Long getEstimateSeqNo() {
		return estimateSeqNo;
	}


	public void setEstimateSeqNo(Long estimateSeqNo) {
		this.estimateSeqNo = estimateSeqNo;
	}


	public String getEstimateFooter() {
		return estimateFooter;
	}


	public void setEstimateFooter(String estimateFooter) {
		this.estimateFooter = estimateFooter;
	}


	public String getEstimateNotes() {
		return estimateNotes;
	}


	public void setEstimateNotes(String estimateNotes) {
		this.estimateNotes = estimateNotes;
	}
	
	

}
