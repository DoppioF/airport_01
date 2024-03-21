package controller;


import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

public class BasePdfMakerOpenPDF {

	public static final String DEST = "hello.pdf";
	 public static void main(String[] args) {
	  
	  try {
	   Document doc = new Document();
	   PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(DEST));
	   //setting font family, color
	   Font font = new Font(Font.HELVETICA, 16, Font.BOLDITALIC, Color.RED);
	   doc.open();
	   Paragraph para = new Paragraph("Hello! This PDF is created using abba de \n para fracis nor de impus tumer fa te niome", font);
	   PdfPTable table = new PdfPTable(1);
	   table.addCell(para);
	   doc.add(table);
	   doc.close();
	   writer.close();   
	  } catch (DocumentException | FileNotFoundException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }   
	 }

}