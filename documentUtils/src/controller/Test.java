package controller;

import java.util.ArrayList;
import java.util.List;

import com.pdflib.PDFlibException;
import com.pdflib.pdflib;

public class Test {

	public void createPdf(String[] headers, String[] text) {
		String outfile = "starter_table.pdf";
        String title = "Starter Table";
        
        final double llx = 50, lly = 50, urx = 550, ury = 800;
        int row, col, font, tbl = -1;
        final int rowmax = (text.length + 2), colmax = headers.length - 1;
        
        pdflib pdf = null;
        String headertext = headers[0] + " " + text[0];
        String result;
        String optlist;
        try {
			pdf = new pdflib();
			pdf.begin_document("starter_table.pdf", "");
			
			row = 1;
	        col = 1;
	        font = pdf.load_font("Helvetica", "unicode", "");
	        int fontBold = pdf.load_font("Helvetica-BoldOblique", "unicode", "");
	        
	        optlist = "fittextline={position=center font=" + font
	                + " fontsize=20} " + "colspan=" + colmax;
			List<Integer> tables = new ArrayList<>();
	        for (int startIndex = 1; startIndex < text.length; startIndex += 5) {
	        	tables.add(creaTabella(row, col, font, optlist, pdf, colmax, rowmax, headers, text, startIndex));
	        }
//			int table1 = creaTabella(row, col, font, optlist, pdf, colmax, rowmax, headertext, headers, text);
//			int table2 = creaTabella(row, col, font, optlist, pdf, colmax, rowmax, headertext, headers, text);
			
            int megaTableHeight = 100 + (19*text.length);
            optlist = "rowheight=" + (megaTableHeight < 0 ? "100%" : megaTableHeight) + " colwidth=100% fittextline={font=" + font
            		                        + " fontsize=16}";
            
            int tableCornice = -1;
            tableCornice = pdf.add_table_cell(tableCornice, 1, 1, " ", optlist);
            int pageNumber = 0;
            int tablesFitted = 0;
            do {
            	pageNumber++;
                pdf.begin_page_ext(0, 0, "width=a4.width height=a4.height");
                
                pdf.setfont(fontBold, 22);
                pdf.fit_textline("LUFTHANSIA", urx/2 - 40, ury, "fontsize=22");
                /*
                 * Shade every other row; draw lines for all table cells. Add
                 * "showcells showborder" to visualize cell borders
                 */
                optlist = "header=0 rowheightdefault=auto "
                   
                    + "stroke={{line=other}}";

                /* Place the table instance */
                result = "";
                
                for(int counter = 0; tablesFitted < tables.size() && !"_boxfull".equals(result); counter++) {
                	double nextUry = ury-(100 + (100 * counter));
                	if (nextUry < 100d) {
                		result = "_boxfull";
                	} else {
                		result = pdf.fit_table(tables.get(tablesFitted), llx+50, lly, urx-50, nextUry, optlist);
                		tablesFitted++;
                	}
                }
                pdf.fit_table(tableCornice, llx, lly, urx, ury - 50, optlist);
//                pdf.fit_table(table1, llx+50, lly, urx-50, ury-50, optlist);
//                pdf.fit_table(table2, llx+50, lly, urx-50, ury-150, optlist);
                
                pdf.setfont(font, 12);
                pdf.fit_textline("" + pageNumber, urx/2 + 20, 10, "fontsize=12");
                pdf.end_page_ext("");
                System.out.println("ciao");
            }
            while (result.equals("_boxfull"));

            /* Check the result; "_stop" means all is ok. */

            /* This will also delete Textflow handles used in the table */
            //pdf.delete_table(tbl, "");

            pdf.end_document("");
            System.out.println("PDF CREATO");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int creaTabella(int row, int col, int font, String optlist, pdflib pdf, int colmax, int rowmax, String[] headers, String[] text, int indexStart) throws Exception {
		row = 1;
        col = 1;
        font = pdf.load_font("Helvetica", "unicode", "");
        
        optlist = "fittextline={position=center font=" + font
                + " fontsize=20} " + "colspan=" + colmax;
        
        int tbl = -1;
        tbl = pdf.add_table_cell(tbl, col, row, headers[0] + " " + text[indexStart - 1], optlist);
        int fontBold = pdf.load_font("Helvetica-BoldOblique", "unicode", "");
        
        row++;
        for (int headersIndex = 1; headersIndex <= colmax; headersIndex++, col++) {
        	optlist = "colwidth=25% fittextline={font=" + fontBold
                    + " fontsize=14} margin=2";
        	tbl = pdf.add_table_cell(tbl, col, row, headers[headersIndex], optlist);
        }
        
        row++;
        for (int textIndex = 0; textIndex < colmax; row++) {
            for (col = 1; col <= colmax; col++, textIndex++) {
                String num;

                num = text[textIndex + indexStart];
                optlist = "fittextline={font=" + font
                    + " fontsize=12} margin=4";
                tbl = pdf.add_table_cell(tbl, col, row, num, optlist);
            }
        }
        return tbl;
	}
}
	
	
