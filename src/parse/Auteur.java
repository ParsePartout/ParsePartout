package parse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Auteur {
	//liste d'auteur detecte grace au contenu du texte
	private static ArrayList<String> auteurParse;
	//liste d'auteur detecte dans les meta donnees
	private static ArrayList<String> auteurMeta;
	//liste d'auteur evalue comme etant le plus propice d'etre correct
	private static ArrayList<String> auteurTitre;
	
	public Auteur(File f,String texte,String texteF) {
		auteurParse=parseAuteur(texte,texteF);
		auteurMeta=extractAuteur(f);
		auteurTitre=compareAuteur(auteurParse,auteurMeta);
	}
    public static ArrayList<String> extractAuteur(File f) {
        ArrayList<String> al= new ArrayList<String>();
        String fileName= f.getName();
        StringBuilder info = pdfInfo(corpusPath +"/"+ fileName);
        String infoParse = info.toString();
        int i=0;
        for(String s :infoParse.split("\n")) {
            if(s.contains("Author") ) {
                for(String ss : infoParse.split("\n")[i].replace("Author:         ", "").split(";")) {

                    al.add(ss.trim());
                }
            }
            i++;
        }
        return al;
    }
	public static ArrayList<String> parseAuteur(String texte, String texteF) {
        ArrayList<String> potentialAuthors = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Z][a-z]+(-[A-Z][a-z]+)?( ([A-Z].)+)?( [a-z]*)? [A-Z]([A-Z]|[a-z])+(-[A-Z][a-z]+)?");

        Matcher matcher = pattern.matcher(texte.substring(20, Math.min(texte.length(), 500)));
        
        while (matcher.find()) {
        	//System.out.println(matcher.group());
            potentialAuthors.add(matcher.group());
        }
       
       Pattern firstnamePattern = Pattern.compile("[A-Z][a-z]+(-[A-Z][a-z]+)?");
       Pattern lastnamePattern = Pattern.compile("( ([A-Z].)+)?( [a-z]*)? [A-Z]([A-Z]|[a-z])+(-[A-Z][a-z]+)?");
        
        
        StringTokenizer tokenizer = new StringTokenizer(texte, "\n");
        String previousline="";
        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken();
            if (line.contains("@")) {
            	int cbArr=0;
	            for(int i=0; i<line.length();i++) {
	            	if(line.charAt(i)=='@') cbArr++;
	            }
	            
	            if(cbArr>1) {
	            	String[] lineSplit = line.split(" ");
	            	for(int i =0;i<cbArr;i++) {
			            for (String potAuthor : potentialAuthors) {
							Matcher matcherFirstname = firstnamePattern.matcher(potAuthor);
				            Matcher matcherLastname = lastnamePattern.matcher(potAuthor);
				            String firstname = null;
				            String lastname = null;
				            if (matcherFirstname.find() && matcherLastname.find()) {
		                        firstname = matcherFirstname.group();
		                        lastname = matcherLastname.group();
				            }
				            
				            ArrayList<String> alternateAuthor = getAlternateAuthor(firstname,lastname);
				            for(String author : alternateAuthor) {
					            int indexArr=(lineSplit[i].indexOf("@")==0)? lineSplit[i].length() : lineSplit[i].indexOf("@") ;
					            if (lineSplit[i].substring(0,indexArr).contains(firstname.toLowerCase()) 
					            		|| lineSplit[i].substring(0,indexArr).contains(lastname.substring(1).toLowerCase())
					            		|| lineSplit[i].substring(0,indexArr).contains(author)){
					            	if(!authors.contains(potAuthor))authors.add(potAuthor);					            	
					            	break;
					            }
				            }
				        }		        	
	            	}
	            }
	            else {
	            	
	            	for (String potAuthor : potentialAuthors) {
	            		if(authors.size()>=getNbAuteurMail(texteF)) break;
	            		//System.out.println(potAuthor);
						Matcher matcherFirstname = firstnamePattern.matcher(potAuthor);
			            Matcher matcherLastname = lastnamePattern.matcher(potAuthor);
			            String firstname = null;
			            String lastname = null;
			            if (matcherFirstname.find() && matcherLastname.find()) {
	                        firstname = matcherFirstname.group();
	                        lastname = matcherLastname.group();
			            }
			            
			            	
			            
			            int indexArr=(line.indexOf("@")==0)? line.length() : line.indexOf("@") ;
				        int indexArrPrevious = (previousline.indexOf("@")==-1)? previousline.length() : previousline.indexOf("@");
					    if (line.substring(0,indexArr).contains(firstname.toLowerCase()) 
					    	|| line.substring(0,indexArr).contains(lastname.substring(1).toLowerCase()) 
				        	|| previousline.substring(0,indexArrPrevious).contains(firstname.toLowerCase()) 
				        	|| previousline.substring(0,indexArrPrevious).contains(lastname.substring(1).toLowerCase())){	
					    	
					    	if(!authors.contains(potAuthor))authors.add(potAuthor);
						    break;
					        }
			         }
	            	
	            	
	            	if(getNbAuteurMail(texteF)>authors.size()) {
	            		for (String potAuthor : potentialAuthors) {
		            		
							Matcher matcherFirstname = firstnamePattern.matcher(potAuthor);
				            Matcher matcherLastname = lastnamePattern.matcher(potAuthor);
				            String firstname = null;
				            String lastname = null;
				            if (matcherFirstname.find() && matcherLastname.find()) {
		                        firstname = matcherFirstname.group();
		                        lastname = matcherLastname.group();
				            }
				            
				            ArrayList<String> alternateAuthor = getAlternateAuthor(firstname,lastname);
				            for(String author : alternateAuthor) {
				            	int indexArr=(line.indexOf("@")==0)? line.length() : line.indexOf("@") ;
						        if (line.substring(0,indexArr).contains(author)) { 
						        	if(!authors.contains(potAuthor))authors.add(potAuthor);
						        	break;
						        }
				            }
		            	}
	            	}
	            }
	        }
            previousline=line;
        }
        return authors;
    }
	private static ArrayList<String> compareAuteur(ArrayList<String> auteurs, ArrayList<String> auteursData) {
		return null;
	}
	
	//Aide au parse
	public static int getNbAuteurWithCompteurEmail(HashMap<String, Integer> cptMail){
    	int cpt = 0;
    	for (Map.Entry<String,Integer> m : cptMail.entrySet()) {
        	if(m.getValue()>=1) cpt++;
    	}
    	return cpt;
    }
    public static int getNbAuteurMail(String texte) {
        //essaie de deviner le nombre d'auteurs dans un texte
        int nb = 0;
        String[]txt = texte.split("\n");
        for(String t : txt) {
//            if(t.contains("abstract")t.contains("introduction")t.contains("Abstract")t.contains("Introduction")t.contains("ABSTRACT")t.contains("INTRODUCTION")) {
//                return nb;
//            }
            if(t.contains("@")) {
                for(int i=0;i<t.length();i++) {
                    if(String.valueOf(t.charAt(i)).equals("@")) {
                        nb+=1;
                    }
                }
                String[] spliter=t.split("@");
                if(spliter[0].contains("{")||spliter[0].contains("(")) {
                    //System.out.println(spliter[0]);
                    for(int i=0;i<spliter[0].length();i++) {
                        if(String.valueOf(t.charAt(i)).equals(",")) {
                            nb+=1;
                        }
                    }
                }
            }
        }
        return nb;
    }
    public static ArrayList<String> getAlternateAuthor(String firstname, String lastname){

    	ArrayList<String>  alternateAuthor = new ArrayList<String>();
    	String[] lastnameTwoParts = lastname.substring(1).split(" ");

    	if(lastnameTwoParts.length<2) alternateAuthor.add(firstname.trim().substring(0,1).toLowerCase() + lastname.substring(1,2).toLowerCase()); //Florient Boudin -> fb
    	else alternateAuthor.add(firstname.substring(0,1).toLowerCase() + lastnameTwoParts[0].substring(0,1).toLowerCase() + lastnameTwoParts[1].substring(0,1).toLowerCase());//Andre F.T. Martins -> afm
    	if(firstname.length()>3) alternateAuthor.add(firstname.substring(0,3).toLowerCase());
    	if(lastname.length()>3) alternateAuthor.add(lastname.substring(0,3));

    	
    	return alternateAuthor;
	}
    //getter et setter
	public static ArrayList<String> getAuteurMeta() {
		return auteurMeta;
	}
	public static void setAuteurMeta(ArrayList<String> auteurMeta) {
		Auteur.auteurMeta = auteurMeta;
	}
	public static ArrayList<String> getAuteurParse() {
		return auteurParse;
	}
	public static void setAuteurParse(ArrayList<String> auteurParse) {
		Auteur.auteurParse = auteurParse;
	}
	public static ArrayList<String> getAuteurTitre() {
		return auteurTitre;
	}
	public static void setAuteurTitre(ArrayList<String> auteurTitre) {
		Auteur.auteurTitre = auteurTitre;
	}
}
