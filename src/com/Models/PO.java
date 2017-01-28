package com.Models;

import java.util.Iterator;
import java.util.List;

public class PO {
	
	private POfrontEnd porder;
	private List<PODetailsFrontEnd> porderDetails;
	
	
	public POfrontEnd getPorder() {
		return porder;
	}
	public void setPorder(POfrontEnd porder) {
		this.porder = porder;
	}
	public List<PODetailsFrontEnd> getPorderDetails() {
		return porderDetails;
	}
	
	public void setPorderDetails(List<PODetailsFrontEnd> porderDetails) {
		this.porderDetails = porderDetails;
	}
	@Override
	public String toString() {
		
		String a = "PO details are :" + porder.toString()  + "***";
		String b=  "list elements are:";
		
		Iterator<PODetailsFrontEnd> itr = porderDetails.iterator();
		while(itr.hasNext())
		{
			PODetailsFrontEnd k  = itr.next();
			b+= k.toString() + "  ";
		}
		return a + "**" + b;
	}


	
	//print 
	
	
	

	
	

}
