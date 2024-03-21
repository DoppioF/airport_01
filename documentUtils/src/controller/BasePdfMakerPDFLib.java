package controller;

import com.pdflib.PDFlibException;
import com.pdflib.pdflib;

public class BasePdfMakerPDFLib {
	
	public static void main(String argv[]) {
        /* This is where the data files are. Adjust as necessary. */
        String searchpath = "../input";
        String outfile = "starter_table.pdf";
        String title = "Starter Table";


        int row, col, font, image, graphics, tf = -1, tbl = -1;
        final int rowmax = 50, colmax = 4;
        int exitcode = 0;

        pdflib p = null;
        final double llx = 50, lly = 50, urx = 550, ury = 800;
        String headertext = "Table header (row 1)";
        String result;
        String optlist;

        /* Dummy text for filling a cell with multi-line Textflow */
        final String tf_text = "Lorem ipsum dolor sit amet, consectetur "
            + "adi&shy;pi&shy;sicing elit, sed do eius&shy;mod tempor "
            + "incidi&shy;dunt ut labore et dolore magna ali&shy;qua. Ut enim ad "
            + "minim ve&shy;niam, quis nostrud exer&shy;citation ull&shy;amco "
            + "la&shy;bo&shy;ris nisi ut ali&shy;quip ex ea commodo "
            + "con&shy;sequat. Duis aute irure dolor in repre&shy;henderit in "
            + "voluptate velit esse cillum dolore eu fugiat nulla pari&shy;atur. "
            + "Excep&shy;teur sint occae&shy;cat cupi&shy;datat non proident, sunt "
            + "in culpa qui officia dese&shy;runt mollit anim id est laborum. ";

        try {
            p = new pdflib();

            /*
             * This means we must check return values of load_font() etc.
             * Set the search path for fonts and images etc.
             */
            p.set_option("errorpolicy=return SearchPath={{" + searchpath + "}}");

            if (p.begin_document("starter_table.pdf", "") == -1)
                throw new Exception("Error: " + p.get_errmsg());

            p.set_info("Creator", "PDFlib Cookbook");
            p.set_info("Title", title);

            /* -------------------- Add table cells -------------------- */

            /* ---------- Row 1: table header (spans all columns) */
            row = 1;
            col = 1;
            font = p.load_font("Helvetica", "unicode", "");

            if (font == -1)
                throw new Exception("Error: " + p.get_errmsg());

            optlist = "fittextline={position=center font=" + font
                    + " fontsize=14} " + "colspan=" + colmax;

            tbl = p.add_table_cell(tbl, col, row, headertext, optlist);

//            if (tbl == -1)
//                throw new Exception("Error adding cell: " + p.get_errmsg());

            /* ---------- Row 2: various kinds of content */
            row++;

            /* ----- Simple text cell */
            col = 1;

            optlist = "fittextline={font=" + font
                    + " fontsize=12 orientate=west}";

            tbl = p.add_table_cell(tbl, col, row, "vertical line", optlist);

//            if (tbl == -1)
//                throw new Exception("Error adding cell: " + p.get_errmsg());

            
            /* ----- Colorized background */
            col++;

            optlist = "fittextline={font=" + font + " fontsize=12 fillcolor=white} "
                    + "matchbox={fillcolor=orange}";

            tbl = p.add_table_cell(tbl, col, row, "colorized cell", optlist);

//            if (tbl == -1)
//                throw new Exception("Error adding cell: " + p.get_errmsg());

            
            /* ----- Multi-line text with Textflow */
            col++;

            optlist = "charref fontname=Helvetica fontsize=8 alignment=justify";

            tf = p.add_textflow(tf, tf_text, optlist);
//            if (tf == -1)
//                throw new Exception("Error: " + p.get_errmsg());

            optlist = "margin=2 textflow=" + tf;

            tbl = p.add_table_cell(tbl, col, row, "", optlist);

//            if (tbl == -1)
//                throw new Exception("Error adding cell: " + p.get_errmsg());


            /* ----- Rotated image */
            col++;

            tbl = p.add_table_cell(tbl, col, row, "rotated image", optlist);

//            if (tbl == -1)
//                throw new Exception("Error adding cell: " + p.get_errmsg());


            /* ---------- Row 3: various kinds of content */
            row++;
            
            /* ----- Diagonal stamp */
            col=1;

            optlist = "rowheight=50 fittextline={font=" + font + " fontsize=10 stamp=ll2ur}";

            tbl = p.add_table_cell(tbl, col, row, "diagonal stamp", optlist);

//            if (tbl == -1)
//                throw new Exception("Error adding cell: " + p.get_errmsg());


            /* ----- SVG graphics */
            col++;

            /* Load the graphics */


//            if (tbl == -1)
//                throw new Exception("Error adding cell: " + p.get_errmsg());


            /* ----- Annotation: Web link */
            col++;
            
            int action = p.create_action("URI", "url={https://www.pdflib.com}");
            
            optlist =  "margin=5 fittextline={fontname=Helvetica fontsize=14 fillcolor=blue} " +
            		   "annotationtype=Link fitannotation={action={activate " + action + "} linewidth=0}";
            tbl = p.add_table_cell(tbl, col, row, "Web link", optlist);

//            if (tbl == -1)
//                throw new Exception("Error adding cell: " + p.get_errmsg());
            

            /* ----- Form field */
            col++;

            int fieldfont = p.load_font("Helvetica", "winansi", "simplefont nosubsetting");
            
            optlist = "margin=5 fieldtype=textfield fieldname={name} " +
                      "fitfield={multiline linewidth=0 font=" + fieldfont + " fontsize=12 " +
            		  "alignment=center currentvalue={text field} scrollable=false}";

            tbl = p.add_table_cell(tbl, col, row, "", optlist);

//            if (tbl == -1)
//                throw new Exception("Error adding cell: " + p.get_errmsg());


            /* ---------- Fill row 3 and above with their numbers */
            for (row++; row <= rowmax; row++) {
                for (col = 1; col <= colmax; col++) {
                    String num;

                    num = "Col " + col + "/Row " + row;
                    optlist = "colwidth=25% fittextline={font=" + font
                        + " fontsize=10}";
                    tbl = p.add_table_cell(tbl, col, row, num, optlist);

//                    if (tbl == -1)
//                        throw new Exception("Error adding cell: "
//                            + p.get_errmsg());
                }
            }

            /* ---------- Place the table on one or more pages ---------- */

            /*
             * Loop until all of the table is placed; create new pages as long
             * as more table instances need to be placed.
             */
            do {
                p.begin_page_ext(0, 0, "width=a4.width height=a4.height");

                /*
                 * Shade every other row; draw lines for all table cells. Add
                 * "showcells showborder" to visualize cell borders
                 */
                optlist = "header=1 rowheightdefault=auto "
                    + "fill={{area=rowodd fillcolor={gray 0.9}}} "
                    + "stroke={{line=other}} ";

                /* Place the table instance */
                result = p.fit_table(tbl, llx, lly, urx, ury, optlist);

                if (result.equals("_error"))
                    throw new Exception("Couldn't place table : "
                        + p.get_errmsg());

                p.end_page_ext("");

            }
            while (result.equals("_boxfull"));

            /* Check the result; "_stop" means all is ok. */
            if (!result.equals("_stop")) {
                if (result.equals("_error")) {
                    throw new Exception("Error when placing table: "
                        + p.get_errmsg());
                }
                else {
                    /*
                     * Any other return value is a user exit caused by the
                     * "return" option; this requires dedicated code to deal
                     * with.
                     */
                    throw new Exception("User return found in Table");
                }
            }

            /* This will also delete Textflow handles used in the table */
            p.delete_table(tbl, "");

            p.end_document("");
        }
        catch (PDFlibException e) {
            System.err.println("PDFlib exception occurred:");
            System.err.println("[" + e.get_errnum() + "] " + e.get_apiname() +
                ": " + e.get_errmsg());
            exitcode = 1;
        }
        catch (Exception e) {
            System.err.println(e);
            exitcode = 1;
        }
        finally {
            if (p != null) {
                p.delete();
            }
            System.exit(exitcode);
        }
    }
}
