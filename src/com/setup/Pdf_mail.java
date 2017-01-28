package com.setup;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;




@Path("/pdf")
public class Pdf_mail {
	
	@GET
	@Path("/send")
	public String mail() throws DocumentException, IOException
	{
		String k = "<html><body>" + 
	
               "<div style= \"width: 100%;height: 30%\">" + 
			"<div style= \"float:left\">" + 
				"<h3 align=\"left\"><b>VISTAR PRINTSOL PVT LTD</b></h3>" + 
			"</div>" + 
				
			"<div style=\"float: right\">" + 
				"<h2 align=\"right\">TAX INVOICE</h2>" + 

				"<p align=\"right\">TIN: 29051326508, PAN: AAFCV3745D</p>" + 

				"<p align=\"right\">" + 
					"Vistar Printsol Pvt. Ltd.<br /> #18, 5th Cross, 14th Block,<br />" + 
					"BDA Layout, Nagarabhavi,<br /> Bengaluru, Karnataka 560072<br />"  + 
					"India<br /> <br /> +9180 23215371<br /> www.printsol.in" + 
				"</p>" +
			"</div>" + 
		"</div>" + 
		"<div style=\"clear: both\"></div>" +

		"<div style=\"width: 100%\">" + 
			"<hr />"  +
		"</div>" + 


		"<div style=\"width: 100%; height: 40%\">" + 

			"<div style=\"float: left\">" + 
				"<p align=\"left\">" + 
					"BILL TO<br /> Ess Enn Holdings<br /> No.48/2, Pawar Complex," + 
					"Near Kengeri<br /> Bus Stop,<br /> Mysore Road<br />"  + 
					"Bengaluru , Karnataka 560 060<br /> India<br /> <br />" + 
					"srikanth@essennho.com" + 

				"</p>" + 
			"</div>" + 

			"<div style=\"float: right\">" +
			"	<p align=\"right\">" +
			"		Invoice Number: 445<br /> <br /> Invoice Date: January 18," +
				"	2017<br /> <br /> Payment Due: January 18, 2017<br /> <br />" +
				"	<b> Amount Due (INR): 147.70</b>" +
				"</p>" +
			"</div>" +


		"</div>" +
		"<div style=\"clear: both\"></div>" +

		"<div style=\"width: 100%\">" +
		"	<hr />" +
		"</div>" +

		"<table width=100% style=\"border-collapse: collapse\"> " +
		"<thead>" + 
			"	<tr style=\"background-color:black;height: 35px\">" +
			"		<th style=\"color:white\">Product</th>" +
			"		<th style=\"color:white\">Quantity</th>" +
			"		<th style=\"color:white\">Price</th>" +
			"		<th style=\"color:white\">Amount</th>" +
			"	</tr>" +
			"</thead>" +


			"<tr >" +
			"	<td style=\"padding: 6px;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: black\"><b>Visiting cards</b><br />300 gsm" +
			"		gloss DS</td>" +
			"	<td style=\"padding: 6px; text-align: center;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: black\">100</td>" +
			"	<td style=\"padding: 6px; text-align: center;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: black\">1.40</td>" +
			"	<td style=\"padding: 6px; text-align: center;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: black\">140.00</td>" +
			"</tr>" +



			"<tr style=\"border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: red\">" +
			"	<td style=\"padding: 6px;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: black\"><b>Invitation cards</b><br />500 gsm" +
			"	gloss DS</td>" +
			"	<td style=\"padding: 6px; text-align: center;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: black\">100</td>" +
			"	<td style=\"padding: 6px; text-align: center;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: black\">1.40</td>" +
			"	<td style=\"padding: 6px; text-align: center;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: black\">140.00</td>" +
			"</tr>" +

			"<tr >" +
			"	<td colspan=\"3\" style=\"text-align: right; padding: 10px\"><b>Subtotal:</b></td>" +
			"	<td style=\"text-align: center;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: black\">280.00</td>" +
			"</tr>" +

			"<tr >" +
			"	<td colspan=\"3\" style=\"text-align: right; padding: 10px;\"><b>KST" +
			"			5.5%:</b></td>" +
			"	<td style=\"text-align: center;;border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: black\">15.40</td>" +
			"</tr>" +

			"<tr>" +
			"	<td colspan=\"3\" style=\"text-align: right; padding: 10px\"><b>Total (INR):</b></td>" +
			"	<td style=\"text-align: center\">295.40</td>" +
			"</tr>" +



		"</table>" +

		//"<div style=\"width: 100%; height: 20%\">" +
		"	<div style=\"float: left; width: 100%\">" +
		"		<p align=\"left\">" +
		"			<b>Notes</b><br /> Thank you for placing your order with us.<br />" +
		"			Cheque/DD to be issued in name of M/S. VISTAR PRINTSOL PVT. LTD.<br />" +
		"			Kindly visit us at www.printsol.in<br /> Do leave your review and" +
		"			comments on our facebook page www.facebook.com/vistarprintsol<br />" +
		"		</p>" +
		"	</div>" +

	//	"</div>" +

	    "<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />" +
	    "<br /><br />" + 
	    "<p align=\"center\" style=\"color:gray;width:50%\">This is a Computer Generated Invoice</p>" +
	    
	     "</body>" +
		"</html>";
			


    
		 OutputStream file = new FileOutputStream(new File(""));
		 Document document = new Document();
		 PdfWriter writer = PdfWriter.getInstance(document, file);
		 document.open();
		    InputStream is = new ByteArrayInputStream(k.getBytes());
		    XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
		    document.close();
		    file.close();
		
		    
		    File file1 = new File("");//full file path URL
		    String absolutePath = file1.getAbsolutePath();
		return absolutePath;
	}

}
