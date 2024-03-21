package controller;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class AirportPdfMaker {

    String fileName = "biglietti.pdf";

    public void createPDF(String[] text) throws Exception{
    	try {
    	    Document doc = new Document();
    	    PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(fileName));
    	    // setting font family, color
    	    Font fontHeader = new Font(Font.HELVETICA, 20, Font.BOLDITALIC);
    	    Font regular = new Font(Font.HELVETICA, 12);
    	    Font regularBold = new Font(Font.HELVETICA, 12, Font.BOLD);
    	    doc.open();

    	    PdfPTable table = new PdfPTable(1);
    	    table.setWidthPercentage(100);
    	    Paragraph title = new Paragraph(text[0], fontHeader);
    	    title.add("\n");
    	    doc.add(title);
    	    
    	    Paragraph para = new Paragraph();
    	    for (int index = 1; index < text.length; index++) {
    	        if (index % 2 == 1) {
    	        	para.add(new Paragraph(text[index], regularBold));
    	        } else {
    	        	para.add(new Paragraph(text[index], regular));
    	        	para.add("\n");
    	        }
    	        if (index % 4 == 0) {
    	        	table.addCell(para);
    	        	doc.add(table);
    	        	table = new PdfPTable(1);
    	        }
    	    }

    	    doc.close();
    	    writer.close();
    	} catch (DocumentException | FileNotFoundException e) {
    	    e.printStackTrace();
    	}
      
        System.out.println("PDF YES!");
    }
}