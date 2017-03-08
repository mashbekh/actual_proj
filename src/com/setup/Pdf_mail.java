package com.setup;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.hibernate.Hibernate;

import com.Models.FileUpload;
import com.Models.PurchaseOrder;
import com.Models.PurchaseOrderDetails;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
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
			


    
		 OutputStream file = new FileOutputStream(new File("C:\\Users\\vaish\\neon_workspace\\Accounting\\WebContent\\test1.pdf"));
		 Document document = new Document();
		 PdfWriter writer = PdfWriter.getInstance(document, file);
		 document.open();
		    InputStream is = new ByteArrayInputStream(k.getBytes());
		    XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
		    document.close();
		    file.close();
		
		    
		    File file1 = new File("C:\\Users\\vaish\\neon_workspace\\Accounting\\WebContent\\test1.pdf");//full file path URL
		    String absolutePath = file1.getAbsolutePath();
		return absolutePath;
	}
	
	@Path("/try")
	@GET
	public String itextpdf() throws DocumentException, MalformedURLException, IOException
	{
		FileUpload a1 = null;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		  PurchaseOrder a = em.find(PurchaseOrder.class, "CMP444PO4");
		 Hibernate.initialize(a.getPod());
		 Hibernate.initialize(a.getAdv_payment());
		 
		 a1 = em.find(FileUpload.class, 11);
		em.getTransaction().commit();
		em.close();
		
		
		 // steps 1
	    Document document = new Document();
	    // step 2
	    PdfWriter writer = PdfWriter.getInstance(document,
	        new FileOutputStream("C:\\Users\\vaish\\neon_workspace\\Accounting\\WebContent\\mail.pdf"));
	    writer.setPdfVersion(PdfWriter.VERSION_1_7);
	    document.open();
	   
	    PdfPTable table = new PdfPTable(2);
	    table.setWidthPercentage(100);
	    
	    PdfPCell cell1 = new PdfPCell();
	    Paragraph p2;
	    Image img = Image.getInstance(a1.getFile());
	    img.scaleAbsolute(220,115);
	    img.setAlignment(Image.LEFT); 
	    p2  = new Paragraph(new Chunk(img, 0, 0, true));
	    cell1.addElement(p2);
	    cell1.setBorder(PdfPCell.NO_BORDER);
	    cell1.setPadding(0);
	    table.addCell(cell1);
	    
	    PdfPCell cell = new PdfPCell();
	    Paragraph p = new Paragraph();
	    
	   
	    
	    
	    p = new Paragraph("TAX INVOICE" ,  new Font(FontFamily.TIMES_ROMAN,14.0f,Font.BOLD,BaseColor.BLACK));
	    p.setAlignment(Element.ALIGN_RIGHT);
	    cell.addElement(p);
	    
	    p = new Paragraph("TIN: 29051326508, PAN: AAFCV3745D", new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.GRAY));
	    p.setAlignment(Element.ALIGN_RIGHT);
	    cell.addElement(p);
	   
	    p = new Paragraph("Vistar Printsol Pvt. Ltd.", new Font(FontFamily.TIMES_ROMAN,12.0f,Font.BOLD,BaseColor.BLACK  ));
	    p.setAlignment(Element.ALIGN_RIGHT);
	    cell.addElement(p);
	    
	    
	    p = new Paragraph("#18, 5th Cross, 14th Block", new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.BLACK  ));
	    p.setAlignment(Element.ALIGN_RIGHT);
	    cell.addElement(p);
		
	    
	    p = new Paragraph("BDA Layout, Nagarabhavi", new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.BLACK  ));
	    p.setAlignment(Element.ALIGN_RIGHT);
	    cell.addElement(p);
	    
	    
	    p = new Paragraph("Bengaluru, Karnataka 560072", new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.BLACK  ));
	    p.setAlignment(Element.ALIGN_RIGHT);
	    cell.addElement(p);
	    
	    cell.addElement( Chunk.NEWLINE );
	    
	    p = new Paragraph("+9180 23215371", new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.BLACK  ));
	    p.setAlignment(Element.ALIGN_RIGHT);
	    cell.addElement(p);
	    
	    
	    cell.setBorder(PdfPCell.NO_BORDER);
	    cell.addElement( Chunk.NEWLINE );
	    table.addCell(cell);
	    document.add(table);
	    
	    document.add( Chunk.NEWLINE );
	    
	    //HR
	    
	    LineSeparator ls = new LineSeparator();
	    document.add(new Chunk(ls));
	    
	    
	    //table for To and PO details
	    
	    PdfPTable to_table = new PdfPTable(2);
	    to_table.setWidthPercentage(100);
	    PdfPCell cell2 = new PdfPCell();
	    Paragraph p1 = new Paragraph();
	    
	    p1 = new Paragraph("PO To : " ,  new Font(FontFamily.TIMES_ROMAN,14.0f,Font.BOLD,BaseColor.GRAY));
	    p1.setAlignment(Element.ALIGN_LEFT);
	    cell2.addElement(p1);
	    
	    p1 = new Paragraph("Suraj Papers" ,  new Font(FontFamily.TIMES_ROMAN,14.0f,Font.BOLD,BaseColor.BLACK));
	    p1.setAlignment(Element.ALIGN_LEFT);
	    cell2.addElement(p1);
	    
	    p1 = new Paragraph("No.48/2, Pawar Complex, Near Kengeri" ,  new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.BLACK));
	    p1.setAlignment(Element.ALIGN_LEFT);
	    cell2.addElement(p1);
	    
	    p1 = new Paragraph("Bus Stop,Mysore Road" ,  new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.BLACK));
	    p1.setAlignment(Element.ALIGN_LEFT);
	    cell2.addElement(p1);
	    
	    p1 = new Paragraph("Bengaluru , Karnataka 560 060" ,  new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.BLACK));
	    p1.setAlignment(Element.ALIGN_LEFT);
	    cell2.addElement(p1);
	    
	    p1 = new Paragraph("srikanth@essennho.com" ,  new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.BLACK));
	    p1.setAlignment(Element.ALIGN_LEFT);
	    cell2.addElement(p1);
	    
	    cell2.setBorder(PdfPCell.NO_BORDER);
	    to_table.addCell(cell2);
	    
	    
	    PdfPCell cell3 = new PdfPCell();
	    Paragraph p3 = new Paragraph();
	    
	    p3 = new Paragraph("Purchase Order# : CMP444PO4"  ,  new Font(FontFamily.TIMES_ROMAN,12.0f,Font.BOLD,BaseColor.BLACK));
	    p3.setAlignment(Element.ALIGN_RIGHT);
	    cell3.addElement(p3);
	    
	    p3 = new Paragraph("Date : 06/03/2017"  ,  new Font(FontFamily.TIMES_ROMAN,12.0f,Font.BOLD,BaseColor.BLACK));
	    p3.setAlignment(Element.ALIGN_RIGHT);
	    cell3.addElement(p3);
	    cell3.setBorder(PdfPCell.NO_BORDER);
	    to_table.addCell(cell3);
	    document.add(to_table);
	    
	    table = new PdfPTable(4);
	    table.setWidthPercentage(100);
	    table.setSpacingBefore(10);
	    table.setSpacingAfter(10);
	    //table.setWidths(new int[]{8, 4, 4, 4});
	    
	    
	    Font f =  new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.BLACK);
	    Font f1 =  new Font(FontFamily.TIMES_ROMAN,12.0f,Font.BOLD,BaseColor.BLACK);
	    
	    table.addCell(getCellHeader("Product", Element.ALIGN_MIDDLE, new Font(FontFamily.TIMES_ROMAN,12.0f,Font.BOLD,BaseColor.BLACK)));
	    table.addCell(getCellHeader("Qty", Element.ALIGN_MIDDLE, new Font(FontFamily.TIMES_ROMAN,12.0f,Font.BOLD,BaseColor.BLACK)));
	    table.addCell(getCellHeader("Price", Element.ALIGN_MIDDLE, new Font(FontFamily.TIMES_ROMAN,12.0f,Font.BOLD,BaseColor.BLACK)));
	    table.addCell(getCellHeader("Subtotal", Element.ALIGN_MIDDLE, new Font(FontFamily.TIMES_ROMAN,12.0f,Font.BOLD,BaseColor.BLACK)));
	    
	    
	    NumberFormat formatter = new DecimalFormat("#0.00");
	    
	    for(PurchaseOrderDetails q : a.getPod() )
	    {
	    	
	    	 table.addCell(getCell(
	    		        q.getProduct_id(), Element.ALIGN_LEFT, f));
	    	 table.addCell(getCell(
	    		        String.valueOf(q.getQuantity()), Element.ALIGN_RIGHT, f));
	    	 table.addCell(getCell(
	    			 String.valueOf(formatter.format(round(q.getCost()))), Element.ALIGN_RIGHT, f));
	    	 table.addCell(getCell(
	    			 String.valueOf(formatter.format(q.getAmount())), Element.ALIGN_RIGHT, f));
	    	 
	    	// PdfPCell c = new PdfPCell();
	    	// c.addElement(Chunk.NEWLINE);
	    	 
	    	 //table.addCell(Chunk.NEWLINE);
	    }
	   
	    //add sub totals to the table
	    
	    table.addCell(getCell("", Element.ALIGN_LEFT,f));
	    table.addCell(getCell("", Element.ALIGN_LEFT,f));
	   table.addCell(getCell("Sub Total", Element.ALIGN_LEFT,f1));
	   table.addCell(getCell(String.valueOf(formatter.format(a.getAmount())), Element.ALIGN_RIGHT,f));
	   
	   
	   table.addCell(getCell("", Element.ALIGN_LEFT,f));
	    table.addCell(getCell("", Element.ALIGN_LEFT,f));
	   table.addCell(getCell("Tax Total", Element.ALIGN_LEFT,f1));
	   table.addCell(getCell(String.valueOf(formatter.format(a.getTax_amount())), Element.ALIGN_RIGHT,f));
	   
	   table.addCell(getCell("", Element.ALIGN_LEFT,f));
	    table.addCell(getCell("", Element.ALIGN_LEFT,f));
	   table.addCell(getCell("Grand total", Element.ALIGN_LEFT,f1));
	   table.addCell(getCell(String.valueOf(formatter.format(a.getGrand_total())), Element.ALIGN_RIGHT,f));
	   document.add(table);
	    
	   PdfPTable notes = new PdfPTable(1);
	    notes.setWidthPercentage(100);
	    PdfPCell cell4 = new PdfPCell();
	    Paragraph p4 = new Paragraph();
	    
	    p4 = new Paragraph("Notes" ,  new Font(FontFamily.TIMES_ROMAN,14.0f,Font.BOLD,BaseColor.BLACK));
	    p4.setAlignment(Element.ALIGN_LEFT);
	    cell4.addElement(p4);
	    
	    p4 = new Paragraph("Do leave your review and comments on our facebook page www.facebook.com/vistarprintsol" ,  new Font(FontFamily.TIMES_ROMAN,12.0f,Font.NORMAL,BaseColor.BLACK));
	    p4.setAlignment(Element.ALIGN_LEFT);
	    cell4.addElement(p4);
	    
	    cell4.setBorder(PdfPCell.NO_BORDER);
	    notes.addCell(cell4);
	    document.add(notes);
	    
	    
	    
	    
	    
	    
	   
	    document.close();
	  
		return "created";
	}

	
	public PdfPCell getCellHeader(String value, int alignment, Font font) {
	    PdfPCell cell = new PdfPCell();
	    cell.setUseAscender(true);
	    cell.setUseDescender(true);
	    Paragraph p = new Paragraph(value, font);
	    p.setAlignment(alignment);
	    
	    int hex = 0xB0B0B0;
	    
	    int r = (hex & 0xFF0000) >> 16;
	    int g = (hex & 0xFF00) >> 8;
	    int b = (hex & 0xFF);
	    cell.setBackgroundColor(new BaseColor(r,g, b));
	    //cell.setBorder(PdfPCell.NO_BORDER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.addElement(p);
	    return cell;
	}
	
	public PdfPCell getCell(String value, int alignment, Font font) {
	    PdfPCell cell = new PdfPCell();
	    cell.setUseAscender(true);
	    cell.setUseDescender(true);
	    Paragraph p = new Paragraph(value, font);
	    p.setAlignment(alignment);
	    cell.setPadding(10);
	    cell.setBorder(PdfPCell.NO_BORDER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.addElement(p);
	    return cell;
	}
	
	public PdfPCell getCellTotal(String value, int alignment, Font font) {
	    PdfPCell cell = new PdfPCell();
	    cell.setUseAscender(true);
	    cell.setUseDescender(true);
	   cell.setColspan(3);
	    Paragraph p = new Paragraph(value, font);
	    p.setAlignment(alignment);
	    cell.setPadding(10);
	    cell.setBorder(PdfPCell.NO_BORDER);
	    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    cell.addElement(p);
	    return cell;
	}
	
	public double round(Double a)
	{
		 double roundOff = Math.round(a * 100.0) / 100.0;
		 return roundOff;
	}
	
}
