package parse;

import java.io.File;
import java.io.FileInputStream;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
public class ParsePartout {
	private static String texte;
	private static String nameFile;
	
	
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
	            texte+=content;
	            //Afficher le contenu de la page sur la console.
	        }
		    
	        //Fermez le PdfReader.
	        pdf.close();
	    
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	public static void getAuteur() {
		ArrayList<String> 
		String[] words = texte.split(" ");
		for (int i=0;i<50;i++) {
			char letter = words[i].charAt(0);
			char letterMotSuiv = words[i+1].charAt(0);
			if(letter==Character.toUpperCase(letter) && letterMotSuiv==Character.toUpperCase(letterMotSuiv)){
					System.out.println(words[i] + " " + words[i+1]);
				
			}
		}
	}
	
	public static void getTitre() {
		
	}
	public static void getNom() {
		
	}
	public static void getAbstract() {
		
	}
	public static void creationFichierSansRename() {
		
		File file = new File(nameFile.substring(0,nameFile.length()-4) + ".txt");
	}
	public static void  creationFichierAvecRename() {
		
	}
	public static void main(String args[]){
		File f = new File("C:\\dev\\ParsePartout\\src\\parse\\Corpus_2021\\Boudin-Torres-2006.pdf");
		//gckcjcyc
	    pdftotext(f);
	    getAuteur();
	  }

}
