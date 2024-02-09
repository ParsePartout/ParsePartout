package parse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
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
        Pattern pattern = Pattern.compile("[A-Z]([A-Z]|[a-z]| ı| | ¸|´ | ` )+(-([A-Z]|[a-z]| ı| | ¸|´ | ` )+)?( ´)?( ([A-Z].)+)?( [a-z]*)? [A-Z]([A-Z]|[a-z]| ı| | ¸|´ | ` )+(-([A-Z]|[a-z]| ı| | ¸|´ | `)+)?");
        
        Matcher matcher = pattern.matcher(texte.substring(30, Math.min(texte.length(), 300)));
        
        while (matcher.find()) {
        	System.out.println(matcher.group());
            potentialAuthors.add(matcher.group());
        }

        
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
        
        StringTokenizer tokenizer = new StringTokenizer(texte, "\n");
        String previousline = null;
        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken();
            
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
	         
	            if (firstname != null) {
	                firstname = replaceChar(firstname);
	            }

	            if (lastname != null) {
	                lastname = replaceChar(lastname);

	            }
		        if ((line.contains("@")) && (line.contains(firstname.toLowerCase()) || line.contains(lastname.toLowerCase().substring(0,1)) || previousline.contains(firstname.toLowerCase()) || previousline.contains(lastname.toLowerCase()))) {
		        	int count = compteurEmail.get(potAuthor);
		            compteurEmail.put(potAuthor, count + 1);
		        }
		        if (line.contains(firstname) || line.contains(firstname.toLowerCase()) || line.contains(lastname) || line.contains(lastname.toLowerCase())) {
		        	int count = compteurOcc.get(potAuthor);
	  	            compteurOcc.put(potAuthor, count + 1);
	  	        }
            }
            previousline=line;
        	
        }
        for (Map.Entry<String,Integer> m : compteurEmail.entrySet()) {
        	//System.out.println("clé: "+m.getKey() + " | valeur: " + m.getValue());
        	if(m.getValue()>=1) System.out.println("Auteur : " + m.getKey());
        }
//        for (Map.Entry m : compteurOcc.entrySet()) {
//        	System.out.println("clé: "+m.getKey() + " | valeur: " + m.getValue());
//        	if(m.getValue().equals(1)) System.out.println("Auteur : " + m.getKey());
//        }
    }

    
    private static String replaceChar(String name) {
    	return name.replaceAll(" ı", "i")
                .replaceAll("´ ", "")
                .replaceAll("c ¸", "c")
    			.replaceAll(" `", "");
    	
           
        
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
        File f = new File("Corpus_2021/Das_Martins.pdf");//kjhvkhhf dvhfdhvhbdhsbv
        pdfToText(f);
        
        getAuteur();
    }
}
