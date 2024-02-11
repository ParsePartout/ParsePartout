package parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParsePartout {
    
	private static String nameFile;
    public static StringBuilder pdfToText(String filepath) {
        StringBuilder text = new StringBuilder();
    	try {
	    	String[] command = {"pdftotext","-enc","ASCII7", filepath, "-"};
	        
	        Process process = Runtime.getRuntime().exec(command);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
	        String line;
	        while ((line = reader.readLine()) != null) {
	            text.append(line).append("\n");
	        }
	        process.waitFor();
    	} catch (IOException | InterruptedException e) {
    		e.printStackTrace();
    	}
    	return text;
    }

    public static void getAuteur(String texte) {
        ArrayList<String> potentialAuthors = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Z][a-z]+(-[A-Z][a-z]+)?( ([A-Z].)+)?( [a-z]*)? [A-Z][a-z]+(-[A-Z][a-z]+)?");

        Matcher matcher = pattern.matcher(texte.substring(20, Math.min(texte.length(), 300)));
        
        while (matcher.find()) {
        	//System.out.println(matcher.group());
            potentialAuthors.add(matcher.group());
        }
       
       Pattern firstnamePattern = Pattern.compile("[A-Z][a-z]+(-[A-Z][a-z]+)?");
       Pattern lastnamePattern = Pattern.compile("( ([A-Z].)+)?( [a-z]*)? [A-Z][a-z]+(-[A-Z][a-z]+)?");
        
        
        //compteur email
        HashMap<String, Integer> compteur = new HashMap<>();
        for (String str : potentialAuthors) {
            compteur.put(str, 0);
        }
        
        
        StringTokenizer tokenizer = new StringTokenizer(texte, "\n");
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
	            
	            if (line.contains("@")) {
	            	
	            	int cbArr=0;
		            for(int i=0; i<line.length();i++) {
		            	if(line.charAt(i)=='@') cbArr++;
		            }
		            for(int i=0;i<cbArr;i++) {
		            	if (line.substring(0,line.indexOf("@")).contains(firstname.toLowerCase()) || line.substring(0,line.indexOf("@")).contains(lastname.substring(1).toLowerCase())){
		            		int count = compteur.get(potAuthor);
					        compteur.put(potAuthor, count + 1);
		            	}
		            }		        	
		        }
            }
        }
        for (Map.Entry<String,Integer> m : compteur.entrySet()) {

        	//System.out.println("clé: "+m.getKey() + " | valeur: " + m.getValue());
        	if(m.getValue()>=1) System.out.println("Auteur : " + m.getKey());
        }
    
      
    }
    

    
	public static void getTitre() {
		
	}
	public static void getNom() {
		
	}
	public static void getAbstract(String texte) {
		String[] lines = texte.split("\n");
		ArrayList<String> res = new ArrayList<String>();
		
		int cpt=0;
		boolean flag = false;
		
		//ajout des lignes depuis abstract a introduction
		for(String line : lines) {
			
			if(line.toUpperCase().contains("ABSTRACT")) {
				flag=true;
			}
			if(line.toUpperCase().contains("INTRODUCTION")) {
				flag=false;
				break;
			}
			if(flag) {
				res.add(line);
				cpt++;
			}
		}

		//check si trop de ligne (fichier deux collones)
		if(res.size()>17) {
			for(int i =1; i<res.size(); i++) {
				res.remove(i);
			}
		}

		//affichage
		for(String r : res) {
			if(r!=null)
				System.out.println(r);
		}
	}
	public static void creationFichierSansRename() {
		
		File file = new File(nameFile.substring(0,nameFile.length()-4) + ".txt");
	}
	public static void  creationFichierAvecRename() {
		
	}
    public static void main(String args[]) throws IOException {
    	
    	String homeDirectory = System.getProperty("user.dir");
    	String filePath = Paths.get(homeDirectory, "Corpus_2021").toString();
    	File directory = new File(filePath);

        // Parcourez les fichiers du répertoire
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".pdf")) {
                    System.out.println("Testing file: " + file.getName());
                    StringBuilder texte = pdfToText(filePath + "/" + file.getName());
                    getAuteur(texte.toString());
                    System.out.println("------------------------");
                }
            }
        }
    }
}
