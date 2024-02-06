package parse;

import java.io.File;
import java.io.FileInputStream;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
public class ParsePartout {

	public ParsePartout() {
		//constructeur
	}
	
	public static void pdftotext(File f) {
		try{
	        //Créer une instance PdfReader.
	        PdfReader pdf = new PdfReader(new FileInputStream(f));  
	   
	        //Récupérer le nombre de pages en pdf.
	        int nbrPages = pdf.getNumberOfPages(); 
	   
	        //Itérer le pdf à travers les pages.
	        for(int i=1; i <= nbrPages; i++) 
	        { 
	            //Extraire le contenu de la page à l'aide de PdfTextExtractor.
	            String content = PdfTextExtractor.getTextFromPage(pdf, i);
	   
	            //Afficher le contenu de la page sur la console.
	            System.out.println("Contenu du page : " + content);
	        }
		    
	        //Fermez le PdfReader.
	        pdf.close();
	    
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	public static void getAuteur() {
		
	}
	
	public static void getTitre() {
		
	}
	public static void getNom() {
		
	}
	public static void getAbstract() {
		
	}
	public static void main(String args[]){
		File f = new File("C:\\dev\\ParsePartout\\src\\parse\\Corpus_2021\\Boudin-Torres-2006.pdf");
	    pdftotext(f);
	    
	  }

}
