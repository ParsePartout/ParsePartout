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
	private static ArrayList<String> bonAuteur;
	//liste de mail
	private static ArrayList<String> mails;
	private static String corpusPath;
	private static File file;
	
	public Auteur(File f,String texte,String texteF,int debut,int fin) {

		corpusPath = System.getProperty("user.dir") + "/Corpus_2021";
		file=f;
		auteurParse=parseAuteur(texte,texteF,debut,fin);
		auteurMeta=extractAuteur();
		bonAuteur=compareAuteur(auteurParse,auteurMeta);
		mails=getMail(texteF);
//		System.out.println(f.getName()+"-->"+bonAuteur+"---->"+mails);
	}
    public static ArrayList<String> extractAuteur() {
        ArrayList<String> al= new ArrayList<String>();
        String fileName= file.getName();
        StringBuilders sb = new StringBuilders(corpusPath +"/"+ fileName);
        String infoParse = sb.extractPdfInfo();

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
	public static ArrayList<String> parseAuteur(String texte, String texteF, int debut, int fin) {
        ArrayList<String> potentialAuthors = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Z][a-z]+(-[A-Z][a-z]+)?( ([A-Z].)+)?( [a-z]*)? [A-Z]([A-Z]|[a-z])+(-[A-Z][a-z]+)?");
        
        //reduit la zone à la fin du titre et le début de l'abstract
        Matcher matcher = pattern.matcher(texte.substring(debut, Math.min(texte.length(), fin)));

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
		//methode pour comparer deux listes d'auteurs

        //verification si le premier string de l'arraylist est vide
        if(auteurs.size()==0 || auteurs.get(0).equals("") || auteurs.get(0)==null)
        	return auteursData;
        if(auteursData.size()==0 ||auteursData.get(0).equals("") || auteursData.get(0)==null)
            return auteurs;

        //verification si chaque élément de l'array liste est composé de lettres
        for(String a: auteurs) {
            for(int i=0; i<a.length(); i++) {
                if(a.toUpperCase().charAt(i)<='A' && a.toUpperCase().charAt(i)>='Z' ) {
                    if(a.charAt(i)!='-' && a.charAt(i)!='.') 
                        return auteursData;
                }
            }
        }
        for(String aD: auteurs) {
            for(int i=0; i<aD.length(); i++) {
                if(aD.toUpperCase().charAt(i)<='A' && aD.toUpperCase().charAt(i)>='Z') {
                    if(aD.charAt(i)!='-' && aD.charAt(i)!='.') {
                    	return auteurs;
                    }
                }
            }
        }
        //Verification si array liste auteurs supérieure à auteursData et inversement
        if(auteurs.size()<auteursData.size()) return auteursData;
        if(auteurs.size()>auteursData.size()) return auteurs;
        return auteurs;
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
    
    public static ArrayList<String>getMail(String texte){
        ArrayList<String>mails=new ArrayList<String>();
        String[]txt = texte.split("\n");
        for(String t : txt) {
            if (t.contains("@")) {
                String[]affinage=t.split(" ");
                for(String a : affinage) {
                    if(a.contains("@")) {
                    	if(a.contains("{")) {
                    		
                    		String avantArobase=a.split("@")[0];
                    		System.out.println(avantArobase);
                    	}
//                        String resultString = a.replaceAll("[()]", "");
//                        mails.add(resultString);
                        }
                    }
                }
            }

        
        return mails;
    }
    public static ArrayList<String>checkMail(String texte){ 
        ArrayList<String>mail=new ArrayList<String>(); 
        String[]txt = texte.split("\n"); 
        for(String t : txt) { 
        	if (t.contains("@")) { 
                if(t.contains("}")) { 
                    String mailAccolade = t.split("}")[1]; 
                    String listePotentialA = t.split("}")[0].replaceAll("[{]", ""); 
                    String[]listeAccolade=listePotentialA.split(","); 
                    for (String a : listeAccolade) { 
                        if(!mail.contains(a.trim()+mailAccolade)&&getNbAuteurMail(texte)>mail.size())mail.add(cutPoint(a+mailAccolade)); 
                    } 
                } 
                if(t.contains(")")) { 
                    String mailParenthese = t.split("\\)")[1]; 
                    String listePotentialP = t.split("\\)")[0].replaceAll("[(]", ""); 
                    String[]listeParenthese=listePotentialP.split(","); 
                    for (String p : listeParenthese) { 
                    	if(mailParenthese.contains("@")) { 
                    		if(!mail.contains(p.trim()+mailParenthese)&&getNbAuteurMail(texte)>mail.size())mail.add(cutPoint(p+mailParenthese)); 
                    	} 
                    } 
                } 
                String[]affinage=t.split(" "); 
                for(String a : affinage) { 
                	if(a.contains("@")) { 
                		if(a.contains(")")) { 
                			if(!mail.contains(a.replaceAll("[()]",""))&&getNbAuteurMail(texte)>mail.size()) { 
                				mail.add(cutPoint(a.replaceAll("[()]",""))); 
                				break; 
                			} 
                		} 
                	if(!mail.contains(a)&&getNbAuteurMail(texte)>mail.size())mail.add(cutPoint(a)); 
                	} 
                } 
            } 
        	if(t.contains("Q")&&!texte.contains("@")){ 
        		String[]gestionQ=t.split(" "); 
        		int index = getIndex(gestionQ,"Q"); 
        		if(index!=-1) { 
        			String mailQ = "@"+gestionQ[index].split("Q")[1]; 
        			if(!mail.contains(gestionQ[index])&&bonAuteur.size()>mail.size()) mail.add(cutPoint(gestionQ[index].replaceAll("Q", "@"))); 
        			for(int i=1;i<bonAuteur.size();i++) { 
            			if(!mail.contains(gestionQ[index-i]+mailQ)&&bonAuteur.size()>mail.size())mail.add(cutPoint(gestionQ[index-i]+mailQ)); 
        			} 
        		} 
        		 
        	} 
        } 
        return mail; 
    } 
    public static String cutPoint(String t) { 
    	if (t.endsWith(".")) { 
            return t.substring(0, t.length() - 1); 
        } else { 
        	t= t.replaceAll(" ",""); 
        	return t.replaceAll(",",""); 
        } 
    } 
    public static int getIndex(String[]s,String r) { 
        for (int i = 0; i < s.length; i++) { 
        	if (s[i].contains(r))return i; 
        } 
        return -1;  
         
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
	public static ArrayList<String> getBonAuteur() {
		return bonAuteur;
	}
	public static void setAuteurTitre(ArrayList<String> auteurTitre) {
		Auteur.bonAuteur = auteurTitre;
	}
	public static ArrayList<String> getMails() { 
		return mails; 
	} 
	public static void setMails(ArrayList<String> mails) { 
		Auteur.mails = mails; 
	} 
}
