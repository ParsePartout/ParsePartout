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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getAuteur() {
        ArrayList<String> potentialAuthors = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Z][a-z]+(-[A-Z][a-z]+)?\\s[A-Z][a-z]+(-[A-Z][a-z]+)?");

        Matcher matcher = pattern.matcher(texte.substring(0, Math.min(texte.length(), 500)));
        while (matcher.find()) {
            potentialAuthors.add(matcher.group());
        }

        HashMap<String, Integer> compteur = new HashMap<>();
        for (String str : potentialAuthors) {
            compteur.put(str, 0);
        }

        String[] words = texte.split(" ");  
        for (String word : words) {
            for (String potAuthor : potentialAuthors) {
                String[] names = potAuthor.split("\\s");
                String firstname = names[0];
                String lastname = names[1];

                if (word.contains(firstname) || word.contains(lastname)) {
                	
                    int count = compteur.get(potAuthor);
                    compteur.put(potAuthor, count + 1);
                }
            }
        }
        for (Map.Entry m : compteur.entrySet()) {
            System.out.println("Auteur: "+m.getKey()+", Cpt: "+m.getValue());
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
    public static void main(String args[]) {
        File f = new File("C:\\dev\\ParsePartout\\src\\parse\\Corpus_2021\\Boudin-Torres-2006.pdf");
        pdfToText(f);
        getAuteur();
    }
}
