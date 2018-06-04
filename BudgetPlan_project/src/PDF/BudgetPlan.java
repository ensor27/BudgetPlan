package PDF;

import java.io.File ;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import com.itextpdf.io.util.DecimalFormatUtil;
import com.itextpdf.layout.border.Border;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Section;

public class BudgetPlan {
static Connection connection= WB.Connection2DB.dbConnector();
	
	private static Font catFont ;
	private static Font smallFont;
	private static Font smallFont2 = new Font(Font.FontFamily.TIMES_ROMAN, 10);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
	        Font.BOLD);
	

	public static void createRaport(String start, String end) throws SQLException, FileNotFoundException, DocumentException{

		FontFactory.register(BudgetPlan.class.getClassLoader().getResource("times.ttf").toString(), "times");
		catFont = FontFactory.getFont("times", BaseFont.CP1250, BaseFont.EMBEDDED, 18);
		smallFont = FontFactory.getFont("times", BaseFont.CP1250, BaseFont.EMBEDDED, 12);
		Document doc = new Document();
		
		// configure and get actual date
		SimpleDateFormat doNazwy = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat godz = new SimpleDateFormat("HH;mm");
		SimpleDateFormat nameofmonth = new SimpleDateFormat("MMMM");
		SimpleDateFormat nameofyear = new SimpleDateFormat("yyyy");
		Calendar calendar = Calendar.getInstance();
		
		String path = Parameters.getPathToSaveHours()+"/"+doNazwy.format(calendar.getTime())+"/";
		int year       = calendar.get(Calendar.YEAR);
		int month      = calendar.get(Calendar.MONTH);
		int day		= calendar.get(Calendar.DAY_OF_MONTH);

	
		// tests can be deleted later on
//		System.out.println("year \t\t: " + year);
//		System.out.println("month \t\t: " + months[month]);
//		System.out.println("day \t\t: "+ day);
//		System.out.println("start date from procedure \t\t:" + start);
//		System.out.println("end date from procedure \t\t:" + end);
//		
			
		//intialize begining of month one year ago
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat actualmonth = new SimpleDateFormat("MM");
		String sdate = "";
		String edate = "";
		int actualmonth = calendar.getInstance().get(Calendar.MONTH);
		
		//System.out.println("actual month : " + Integer.toString(i) );
		if (actualmonth<3  ){
			calendar.add(Calendar.YEAR , -1);
		}
		calendar.set(Calendar.MONTH,3);
		calendar.add(Calendar.DAY_OF_MONTH, -day+1);
		sdate = date.format(calendar.getTime());
		System.out.println("startday Date : " + sdate );
			
		//intialize end of month for one year ago
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		edate =  date.format(calendar.getTime());
		//System.out.println("Date : " + day);
		System.out.println("enddate Date : " + edate );
		
		
		
		
		
		File theDir = new File(path);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    try{
		        theDir.mkdir();
		    } 
		    catch(SecurityException se){
		        //handle it
		    }
		}
		int ileOtwartyProjektow=1;
		String [] headers = new String [7];
		int ileKolumn = headers.length;
		
		headers[0] = "ile";
		headers[1] = "project Nr";
		headers[2] = "description";
		headers[3] = "client";
		headers[4] = "price";
		headers[5] = "currency";
		headers[6] = "shipping calendar";
	
		
		String name = "Budget_plan "+start+" do "+end+".pdf";
		File f = new File(path+name);
		if(f.exists() && !f.isDirectory())
			name = godz.format(calendar.getTime())+" "+name;
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(path+name));
		doc.open();
		writer.setPageEvent(new PDF_MyFooter());
		
		//Zrobienie naglowka
		
		 Paragraph preface = new Paragraph();
        // We add one empty line
		 preface.add("\n");
        // Lets write a big header
        preface.add(new Paragraph("Budgetplan according hacosoft finished montage calendars", catFont));

        preface.add("\n");
              
    	//qty of openstanding projekts 2 and 6 and 0
        int qtyProject2and6 = 0;
		String b = "select count(*) from calendar where (Zakonczone = 0 AND (NrMaszyny like '2/%' or NrMaszyny like '6/%' or NrMaszyny like '0/%' ))";
		Statement b1 = connection.createStatement();
		ResultSet rsb = b1.executeQuery(b);
		while(rsb.next()){
			qtyProject2and6= rsb.getInt(1);
		}
		b1.close();
		rsb.close();
		
		   
    	//qty of openstanding projekts 4
        int qtyProject4 = 0;
		String e = "select count(*) from calendar where (Zakonczone = 0 AND (NrMaszyny like '4/%'))";
		Statement e1 = connection.createStatement();
		ResultSet rse = e1.executeQuery(e);
		while(rse.next()){
			qtyProject4= rse.getInt(1);
		}
		e1.close();
		rse.close();
		
		  
    	//qty of openstanding projekts 14
        int qtyProject14 = 0;
		String d = "select count(*) from calendar where (Zakonczone = 0 AND (NrMaszyny like '14/%'))";
        //String d = "SELECT count(*) FROM VERKOOP WHERE SERVICEAFDELING = 2 AND DUMMYSTRING = 'Zamow.'";
		Statement d1 = connection.createStatement();
		ResultSet rsd = d1.executeQuery(d);
		while(rsd.next()){
			qtyProject14= rsd.getInt(1);
		}
		d1.close();
		rsd.close();
		
		// print results on PDF
		preface.add(new Paragraph("opstanding projekts 2/6/0 \t : "+ qtyProject2and6, smallBold));
	    preface.add(new Paragraph("opstanding projekts 4 \t\t : "+ qtyProject4, smallBold));
	    preface.add(new Paragraph("opstanding projekts 14 \t\t : "+ qtyProject14, smallBold));
        preface.add("\n");
        doc.add(preface);
        
        //create PDF table
        PdfPTable tabPDF = new PdfPTable(ileKolumn);
        float widths[] = new float[] { 1, 4, 9, 6, 4, 2, 3};
        float headerwidths[] = new float[] {30};
	
        //------------------------------------------------------------
        //------------------loop for every month----------------------
        //------------------------------------------------------------
        for(int i=0;i<23;i++){
		
        	//print to PDF header for every month:
        	 	PdfPCell cellheader = new PdfPCell(new Phrase(nameofmonth.format(calendar.getTime())+ " "+ nameofyear.format(calendar.getTime() )));
       	        cellheader.setMinimumHeight(30);
       	        cellheader.setHorizontalAlignment(Element.ALIGN_CENTER);
       	        cellheader.setVerticalAlignment(Element.ALIGN_MIDDLE);
       	        cellheader.setBackgroundColor(BaseColor.ORANGE);
       	        cellheader.setColspan(7);
       	        tabPDF.addCell(cellheader);
     	
        	
        	// count the number of open projects in this particular month
       	        String a = "select count(*) from calendar where  DataKoniecMontazu between  '"+ sdate +"'  and  '"+ edate +"' ";
       	        Statement a1 = connection.createStatement();
       	        ResultSet rs2 = a1.executeQuery(a);
       	        while(rs2.next()){
       	        	ileOtwartyProjektow= rs2.getInt(1);
       	        }
       	        a1.close();
       	        rs2.close();
			
       	    //debug line
       	        System.out.println("number of open project in month \t :  "+ ileOtwartyProjektow);
       	        System.out.println(sdate);
       	        System.out.println(edate);
       	        
			//prepare table data for every month:
       	        //String sql1 = "select NrMaszyny, Opis, klient, Cena, Waluta, DataKoniecMontazu from calendar where (DataKoniecMontazu between  '"+ sdate +"'  and  '"+ edate +"') AND NOT NrMaszyny LIKE '14/%' order by DataKoniecMontazu ";
       	     String sql1 = "select NrMaszyny, Opis, klient, Cena, Waluta, DataKoniecMontazu from calendar where (DataKoniecMontazu between  '"+ sdate +"'  and  '"+ edate +"')  order by DataKoniecMontazu ";
       	        String [][] tab = new String [7][ileOtwartyProjektow];
       	        Statement st1 = connection.createStatement();
       	        ResultSet rs1 = st1.executeQuery(sql1);
       	        int counter = -1;
       	        while(rs1.next()){
       	        	counter++;
       	        	String nummer = String.valueOf(counter+1);
       	        	String projectnr = rs1.getString(1);
					String description = rs1.getString(2);
					String client = rs1.getString(3);
					String price = rs1.getString(4);
					String currency = rs1.getString(5);
					String shipmentdate = rs1.getString(6);
					
					// check if 0/ or 6/ is not doubled, if yes, then we eliminate this line
					if (projectnr.startsWith("0/") || projectnr.startsWith("6/") ) {
						int counter1 = 0;
					    System.out.println("detected 0 project hihihi   " + projectnr);
					    String sql4 = "SELECT COUNT(*) FROM calendar WHERE NrMaszyny Like'2/" + projectnr.substring(2) +"' OR NrMaszyny Like '6/"+ projectnr.substring(2)+"'";
					    Statement st4 = connection.createStatement();
						ResultSet rs4 = st4.executeQuery(sql4);
						while(rs4.next()){
							counter1= rs4.getInt(1);
						}
						rs4.close();
						rs4.close();
					    if (counter1==0) {
						   // detect project 0: only project 0 is available
						    tab[0][counter] = nummer;
							tab[1][counter] = projectnr;
							tab[2][counter] = description;
							tab[3][counter] = client;
							tab[4][counter] = price;
							tab[5][counter] = currency;
							tab[6][counter] = shipmentdate;
					    } else if (counter1==1) {
					    	// detected project 0: or project 2 exist or project 6 exist => do NOT add to table
					    	// detected project 6: only project 6 is available => add to table
					    	if (projectnr.startsWith("0/")) {
					    		ileOtwartyProjektow--;
							   	counter--;
					    	} else {
							    tab[0][counter] = nummer;
								tab[1][counter] = projectnr;
								tab[2][counter] = description;
								tab[3][counter] = client;
								tab[4][counter] = price;
								tab[5][counter] = currency;
								tab[6][counter] = shipmentdate;
					    	}
					    } else if (counter1==2) {
					    	// detected project 0: project 2 and project 6 exist => do NOT add to table
					    	// detected project 6: project 2 and project 6 exist => do NOT add to table
					    	ileOtwartyProjektow--;
						   	counter--;
					   }
					   
					} else { 
							// standard projects
							if (((projectnr.length()==8 && (projectnr.startsWith("6/") || projectnr.startsWith("2/")))) ){
						    tab[0][counter] = nummer;
							tab[1][counter] = projectnr;
							tab[2][counter] = description;
							tab[3][counter] = client;
							tab[4][counter] = price;
							tab[5][counter] = currency;
							tab[6][counter] = shipmentdate;
							} else if ( (projectnr.startsWith("4/") || projectnr.startsWith("14/")  )){
									
									String[] abc = projectnr.split("\\/");
									String leverancier = abc[0];
									String ordernummer = abc[1];
									String sql14 = "select verkoop.verkoopprijs , verkoop.munt from verkoop left join bestelling on bestelling.REFERENTIE like concat('%', verkoop.referentie) where bestelling.leverdatum between '"+sdate+"' and '"+edate+"' and bestelling.leverancier = "+leverancier+" and bestelling.ORDERNUMMER = "+ordernummer+"";
					       	      	Statement st14 = connection.createStatement();
					       	        ResultSet rs14 = st14.executeQuery(sql14);
					       	        while(rs14.next()){
										price= rs14.getString("verkoopprijs");
										currency=rs14.getString("munt");
									}
								    tab[0][counter] = nummer;
									tab[1][counter] = projectnr;
									tab[2][counter] = description;
									tab[3][counter] = client;
									tab[4][counter] = price;
									tab[5][counter] = currency;
									tab[6][counter] = shipmentdate;
							
							}
							else{
								ileOtwartyProjektow--;
							   	counter--;
							}
					}
				}
			
			// make summaries
       	        System.out.println(Integer.toString(ileOtwartyProjektow));
       	        Float totaleuro = 0f;
       	        Float totalpln = 0f;
       	        for(int m =0; m<ileOtwartyProjektow; m++){
       	        	if (tab[5][m].equals("EUR")) {	
       	        		totaleuro = totaleuro +  Float.parseFloat(tab[4][m]);
       	        		}
       	        	if (tab[5][m].equals("PLN")) {	
       	        		totalpln = totalpln +  Float.parseFloat(tab[4][m]);
       	        		}
       	        }
       	        System.out.println(String.valueOf(totaleuro));
       	       System.out.println(String.valueOf(totalpln));		
			
			//print next table
			for(int k = 0; k<ileOtwartyProjektow; k++){
				for(int j = 0; j<ileKolumn; j++){
					String zawartosc = tab[j][k];
					PdfPCell c2 = new PdfPCell(new Phrase(zawartosc, smallFont2));
					c2.setMinimumHeight(10);
					c2.setRowspan(1);
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					c2.setBorder(c2.NO_BORDER);
					tabPDF.addCell(c2);
				}
			}
			tabPDF.setWidths(widths);
			tabPDF.setWidthPercentage(100);
			tabPDF.setHorizontalAlignment(Element.ALIGN_CENTER);
			tabPDF.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			//ADD ONE SPACE LINES
			PdfPCell cellspace = new PdfPCell(new Phrase(""));
			cellspace.setMinimumHeight(10);
			cellspace.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellspace.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cellspace.setColspan(7);
			cellspace.setRowspan(1);
			cellspace.setBorder(cellspace.NO_BORDER);
   	        tabPDF.addCell(cellspace);
			
			
			//ADD summary
   	        Phrase abc = new Phrase(" 1EURO = 4.2PLN");
   	      	PdfPCell cellsum = new PdfPCell(new Phrase("SUM"));
				cellsum.setMinimumHeight(10);
				cellsum.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellsum.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cellsum.setColspan(4);
				cellsum.setBorder(cellsum.NO_BORDER);
			tabPDF.addCell(cellsum);
				cellsum.setColspan(3);
				cellsum.setPhrase(abc);
			tabPDF.addCell(cellsum);
			// new line 7 cells
			abc=new Phrase(String.valueOf(totalpln));
				cellsum.setColspan(3);
				cellsum.setPhrase(abc);
			tabPDF.addCell(cellsum); 
			abc=new Phrase("PLN");
				cellsum.setColspan(1);
				cellsum.setPhrase(abc);
			tabPDF.addCell(cellsum); 
				cellsum.setColspan(3);
				abc=new Phrase(String.format("%.1f", totalpln / 4.2) +" EUR ");	
				cellsum.setPhrase(abc);
			tabPDF.addCell(cellsum); 
			// new line 7 cells
				cellsum.setColspan(3);
				abc=new Phrase(String.format("%.1f",totaleuro));
				cellsum.setPhrase(abc);
			tabPDF.addCell(cellsum);
				abc=new Phrase("EUR");
				cellsum.setColspan(1);
				cellsum.setPhrase(abc);
			tabPDF.addCell(cellsum);	
				cellsum.setColspan(3);
				abc=new Phrase(" ");
				cellsum.setPhrase(abc);
			tabPDF.addCell(cellsum); 
			// new line 7 cells
				cellsum.setColspan(3);
				cellsum.setBorder(cellsum.TOP);
				abc=new Phrase(String.format("%.1f",(totaleuro+(totalpln/4.2))));
				cellsum.setPhrase(abc);
			tabPDF.addCell(cellsum);
				abc=new Phrase("EUR");
				cellsum.setColspan(1);
				cellsum.setPhrase(abc);
			tabPDF.addCell(cellsum);
				cellsum.setColspan(3);
				abc=new Phrase(" ");	
				cellsum.setPhrase(abc);
			tabPDF.addCell(cellsum); 
		
						
			//ADD ONE SPACE LINES
			cellspace = new PdfPCell(new Phrase(""));
			cellspace.setMinimumHeight(30);
			cellspace.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellspace.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cellspace.setColspan(7);
			cellspace.setRowspan(1);
			cellspace.setBorder(cellspace.NO_BORDER);
   	        tabPDF.addCell(cellspace);
			
			
			
			//prepare next dates for next loop
			calendar.add(Calendar.DAY_OF_MONTH, 1);	
			sdate = date.format(calendar.getTime());
			System.out.println("startday "+i+" Date : " +sdate );
			calendar.add(calendar.MONTH, 1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			edate = date.format(calendar.getTime());
			System.out.println("enddate "+i+" Date : " + edate );
			
		}
        doc.add(tabPDF);
    		
		
		doc.close();
		
		return;
	}


	
	
	
}