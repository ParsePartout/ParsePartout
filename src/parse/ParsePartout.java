package parse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class ParsePartout {
    private static String texte = "";
	private static String nameFile;
    public static void pdfToText(File f) {
        try {
            PdfReader pdf = new PdfReader(new FileInputStream(f));
            int nbrPages = pdf.getNumberOfPages();

            for (int i = 1; i <= nbrPages; i++) {
                String content = PdfTextExtractor.getTextFromPage(pdf, i);
                texte += content;
            }
            pdf.close();
            System.out.println(texte);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getAuteur() {
        ArrayList<String> potentialAuthors = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Z]([A-Z]|[a-z]| ı| | ¸|´ | ` )+(-([A-Z]|[a-z]| ı| | ¸|´ | ` )+)?( ´)?( ([A-Z].)+)?( [a-z]*)? [A-Z]([A-Z]|[a-z]| ı| | ¸|´ | ` )+(-([A-Z]|[a-z]| ı| | ¸|´ | ` )+)?");
        
        Matcher matcher = pattern.matcher(texte.substring(0, Math.min(texte.length(), 300)));
        
        while (matcher.find()) {
            potentialAuthors.add(matcher.group());
        }

        
        String[] words = texte.split("\\s");
        Pattern firstnamePattern = Pattern.compile("[A-Z]([A-Z]|[a-z]| ı| | ¸|´ | ` )+(-([A-Z]|[a-z]| ı| | ¸|´ | ` )+)?( ´)?");
        Pattern lastnamePattern = Pattern.compile("( ([A-Z].)+)?( [a-z]*)? [A-Z]([A-Z]|[a-z]| ı| | ¸|´ | ` )+(-([A-Z]|[a-z]| ı| | ¸|´ | ` )+)?");
        
       

        //compteur email
        HashMap<String, Integer> compteurEmail = new HashMap<>();
        for (String str : potentialAuthors) {
            compteurEmail.put(str, 0);
        }
        //compteur occurence dans texte
        HashMap<String, Integer> compteurOcc = new HashMap<>();
        for (String str : potentialAuthors) {
        	compteurOcc.put(str, 0);
        }
        
        for (String word : words) {
          for (String potAuthor : potentialAuthors) {
        	  Matcher matcherFirstname = firstnamePattern.matcher(potAuthor);
              Matcher matcherLastname = lastnamePattern.matcher(potAuthor);
              String firstname = null;
              if (matcherFirstname.find()) {
            	firstname = matcherFirstname.group();
              }
              String lastname = null;
              if (matcherLastname.find()) {
            	  lastname = matcherLastname.group();
              }

	          if ((word.contains("@")) && (word.contains(firstname.toLowerCase()) || word.contains(lastname.toLowerCase()))) {
	              
	        	  int count = compteurEmail.get(potAuthor);
	              compteurEmail.put(potAuthor, count + 1);
	          }
	          if (word.contains(firstname) || word.contains(firstname.toLowerCase()) ||
	        		  word.contains(lastname) || word.contains(lastname.toLowerCase())) {
  	              int count = compteurOcc.get(potAuthor);
  	              compteurOcc.put(potAuthor, count + 1);
  	          }
          }
        	
        }
        for (Map.Entry m : compteurEmail.entrySet()) {
        	System.out.println("clé: "+m.getKey() + " | valeur: " + m.getValue());
        	if(m.getValue().equals(1)) System.out.println("Auteur : " + m.getKey());
        }
//        for (Map.Entry m : compteurOcc.entrySet()) {
//        	System.out.println("clé: "+m.getKey() + " | valeur: " + m.getValue());
//        	//if(m.getValue().equals(1)) System.out.println("Auteur : " + m.getKey());
//        }
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
    public static void main(String args[]) {
        File f = new File("C:\\dev\\ParsePartout\\src\\parse\\Corpus_2021\\Nasr.pdf");
        pdfToText(f);
        
        getAuteur();
    }
}
