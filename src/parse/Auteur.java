package parse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import extract.StringBuilders;

public class Auteur {
	//liste d'auteur detecte grace au contenu du texte
	private static ArrayList<String> auteurParse;
	//liste d'auteur detecte dans les meta donnees
	private static ArrayList<String> auteurMeta;
	//liste d'auteur evalue comme etant le plus propice d'etre correct
	private static ArrayList<String> bonAuteur;
	//liste de mail
	private static ArrayList<String> mails;
	//liste d'affiliations des auteurs
	private static ArrayList<String> affiliations;
	private static ArrayList<String> otherAffiliations;
	private static ArrayList<String> bonAffiliations;
	private static String corpusPath;
	private static File file;
	private static Map<String, String> indiceAuteur;
	private static Map<String, String> indiceAffiliation;
	private static String pf; 
		

	
	public Auteur(File f,String texte,String texteF,int debut,int fin) {

		corpusPath = System.getProperty("user.dir") + "/Corpus_2021";
		file=f;
		auteurParse=parseAuteur(texte,texteF,debut,fin);
		auteurMeta=extractAuteur();
		bonAuteur=compareAuteur(auteurParse,auteurMeta);
		mails=checkMail(texte);
		affiliations=parseAffiliations(texteF);
		otherAffiliations=getAlternateAffiliations(texteF);
		indiceAuteur=parseIndiceAuteur(texteF);
		indiceAffiliation=parseIndiceAffiliation(texteF);
		bonAffiliations=compareAffiliations(texteF);
		pf=texteF;
	}
    public  ArrayList<String> extractAuteur() {
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
	public ArrayList<String> parseAuteur(String texte, String texteF, int debut, int fin) {
        ArrayList<String> potentialAuthors = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        Pattern pattern = Pattern.compile("([A-Z]. )?[A-Z][a-z]+(-[A-Z][a-z]+)?( ([A-Z].)+)?( [a-z]*)? [A-Z]([A-Z]|[a-z])+(-[A-Z][a-z]+)?(.[A-Z]([A-Z]|[a-z])+(-[A-Z][a-z]+)?)?");
        //System.out.println(texteF.substring(debut, Math.min(texte.length(), fin)));
        //reduit la zone à la fin du titre et le début de l'abstract
        Matcher matcher = pattern.matcher(texte.substring(debut, Math.min(texte.length(), fin)));
        ArrayList<String> mot = new ArrayList<>();
        mot.add("LIMSI");
        mot.add("Universite");
        mot.add("University");
        mot.add("Univ.");
        mot.add("Univ");
        mot.add("Xerox");
        mot.add("University");
        mot.add("Department");

        while (matcher.find()) {
        	String auteur = matcher.group();
            for (String s : mot) {
            	auteur = auteur.replaceAll(s,"");
            }
            potentialAuthors.add(auteur);
        }
        Pattern firstnamePattern = Pattern.compile("[A-Z][a-z]+(-[A-Z][a-z]+)?");
        Pattern lastnamePattern = Pattern.compile("( ([A-Z].)+)?( [a-z]*)? [A-Z]([A-Z]|[a-z])+(-[A-Z][a-z]+)?(.[A-Z]([A-Z]|[a-z])+(-[A-Z][a-z]+)?)?");
        
        StringTokenizer tokenizer = new StringTokenizer(texte, "\n");
        String previousline="";
        
        while (tokenizer.hasMoreTokens() && getNbAuteurMail(texte)>authors.size()) {
            String line = tokenizer.nextToken();
            
            if (line.contains("@")) {
            	int cbArr=0;
	            for(int i=0; i<line.length();i++) {
	            	if(line.charAt(i)=='@') cbArr++;
	            }
	            if(cbArr>1) {
	            	String[] lineSplit = line.split(" ");
	            	for(int i =0;i<lineSplit.length;i++) {
	            		if(lineSplit[i].contains("@")) {
				            for (String potAuthor : potentialAuthors) {
								Matcher matcherFirstname = firstnamePattern.matcher(potAuthor);
					            Matcher matcherLastname = lastnamePattern.matcher(potAuthor);
					            String firstname = null;
					            String lastname = null;
					            if (matcherFirstname.find() && matcherLastname.find()) {
			                        firstname = matcherFirstname.group();
			                        lastname = matcherLastname.group();
			                        
					            }
					            if(firstname!=null && lastname != null && (firstname.toLowerCase().contains("cedex") || lastname.toLowerCase().contains("cedex"))) break;
					            try {
						            ArrayList<String> alternateAuthor = getAlternateAuthor(firstname,lastname);
						            for(String author : alternateAuthor) {
						            	int indexArr=(lineSplit[i].indexOf("@")==-1)? lineSplit[i].length() : lineSplit[i].indexOf("@");
							            if (lineSplit[i].split(" ")[lineSplit[i].split(" ").length-1].substring(0,indexArr).contains(firstname.toLowerCase()) 
							            		|| lineSplit[i].substring(0,indexArr).contains(lastname.substring(1).toLowerCase())
							            		|| lineSplit[i].substring(0,indexArr).contains(author)){
							            	
							            	if(!authors.contains(potAuthor)) {
							            		authors.add(potAuthor);	
							            		break;
							            	}
							            }
						            }
					            }
					            catch(NullPointerException e) {}
				            //if(getNbAuteurMail(texteF)==authors.size()) break;
					        }
	            		}
	            		
	            	}
	            }
	            else {
	            	String[] lineSplit = line.split(" ");
	            	for(int i =0;i<lineSplit.length;i++) {
		            	for (String potAuthor : potentialAuthors) {
		            		if(authors.size()>=getNbAuteurMail(texte)) break;
		            		//System.out.println(potAuthor);
							Matcher matcherFirstname = firstnamePattern.matcher(potAuthor);
				            Matcher matcherLastname = lastnamePattern.matcher(potAuthor);
				            String firstname = null;
				            String lastname = null;
				            if (matcherFirstname.find() && matcherLastname.find()) {
		                        firstname = matcherFirstname.group();
		                        lastname = matcherLastname.group();
				            }
				            //System.out.println(firstname + "--" + lastname);
				            int indexArr=(lineSplit[i].indexOf("@")==-1)? lineSplit[i].length() : lineSplit[i].indexOf("@");
					        int indexArrPrevious = (previousline.indexOf("@")==-1)? previousline.length() : previousline.indexOf("@");
						    if (lineSplit[i].substring(0,indexArr).contains(firstname.toLowerCase()) 
						    	|| lineSplit[i].substring(0,indexArr).contains(lastname.substring(1).toLowerCase()) 
					        	|| previousline.substring(0,indexArrPrevious).contains(firstname.toLowerCase()) 
					        	|| previousline.substring(0,indexArrPrevious).contains(lastname.substring(1).toLowerCase())){
						    	if(!authors.contains(potAuthor))authors.add(potAuthor);
							    break;
						        }
						    if(lastname.substring(1).split(" ").length>1 && (lineSplit[i].substring(0,indexArr).contains(lastname.substring(1).split(" ")[0].toLowerCase()) 
						    	|| lineSplit[i].substring(0,indexArr).contains(lastname.substring(1).split(" ")[1].toLowerCase()))) {
	
						    	if(!authors.contains(potAuthor)) {
						    		authors.add(potAuthor);
						    	}
						    	break;
						    }
		            	}
					    	
			         }

	            	if(getNbAuteurMail(texte)>authors.size()) {
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
						        	if(!authors.contains(potAuthor)) {
						        		authors.add(potAuthor);
						        	}
						        	break;
						        }
				            }
		            	}
	            	}
	            }
	        }
            previousline=line;
        }
        if(authors.size()==0) {
        	for(String a : potentialAuthors) {
        		authors.add(a);
        	}
        }
        return authors;
    }
	private  ArrayList<String> compareAuteur(ArrayList<String> auteurs, ArrayList<String> auteursData) {
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
	public  int getNbAuteurWithCompteurEmail(HashMap<String, Integer> cptMail){
    	int cpt = 0;
    	for (Map.Entry<String,Integer> m : cptMail.entrySet()) {
        	if(m.getValue()>=1) cpt++;
    	}
    	return cpt;
    }
	public static  int getNbAuteurMail(String texte) {
        //essaie de deviner le nombre d'auteurs dans un texte
        int nb = 0;
        String[]txt = texte.split("\n");
        String previousLine=txt[0];
        for(String t : txt) {
            if(t.contains("@")) {
            	String[] spliter=t.split("@");
                if((spliter[0].contains("{") || spliter[0].contains("(")) && nb==0) {
                	String[] splitVirgule = spliter[0].replaceAll("[{}]", "").trim().split(",");
                	boolean flag = true;
                	for(String s : splitVirgule) {
                		if(!s.trim().matches("[a-z]([a-z]|\\.|\\-)+")) flag = false;
                	}
                	if(flag==true) nb = splitVirgule.length;
                }
                if(spliter[0].contains(",")) {
                	String[] splitVirgule = spliter[0].trim().replaceAll("[()]", "").split(",");
                	boolean flag = true;
                	for(String s : splitVirgule) {
                		if(!s.matches("([A-Z].|[a-z])([a-z]|\\.|\\-|[A-Z].)+")) flag = false;
                	}
                	if(flag==true) nb = splitVirgule.length;
                }
                
                if(spliter[0].equals("")) {
                	if(previousLine.contains(",")) {
                    	
	                	String[] splitVirgule = previousLine.trim().replaceAll("[()]", "").split(",");
	                	boolean flag = true;
	                	for(String s : splitVirgule) {
	                		if(!s.matches("[a-z]([a-z]|\\.|\\-)+")) flag = false;
	                	}
	                	if(flag==true) nb = splitVirgule.length;
                	}
                }
                try {
	                String[] tsplit = t.split(" ");
	              	for(String s : tsplit) {
	              		String afterArrobase = s.contains("@")? s.split("@")[1] : "";
	               		if(s.contains("@") && (s.split("@")[0].replaceAll("[().]", "").matches("[a-z]([a-z]|\\.|\\-|\\_)+") 
	               			|| afterArrobase.contains(".com")||afterArrobase.contains(".fr")||afterArrobase.contains(".org")||afterArrobase.contains(".net"))) {
	               			nb+=1;
	               		}
	                }
                }
              	catch(ArrayIndexOutOfBoundsException e) {}
            }
            previousLine=t;
        }
        return nb;
    }
    
   
    public  ArrayList<String>checkMail(String texte){ 
        ArrayList<String>mail=new ArrayList<String>(); 
        String[]txt = texte.split("\n"); 
        String previousLine="";
        int nbMail = getNbAuteurMail(texte);
        for(String t : txt) { 
        	if(t.contains("@") && !t.split("@")[0].equals("") && getNbAuteurMail(texte)>mail.size()) {
                if(t.contains("}")) {
                    String mailAccolade = t.split("}")[1].split(" ")[0]; 
                    String listePotentialA = t.split("}")[0].replaceAll("[{]", ""); 
                    String[]listeAccolade=listePotentialA.split(","); 
                    for (String a : listeAccolade) {
                        if(!(mail.contains(a.trim()+mailAccolade))&&nbMail>mail.size()) mail.add(cutPoint(a+mailAccolade)); 
                    } 
                } 
                
                else if(t.contains(")") && t.split("\\)")[1].trim().startsWith("@")) { 
                    String mailParenthese = t.split("\\)")[1]; 
                    String listePotentialP = t.split("\\)")[0].replaceAll("[(]", ""); 
                    String[]listeParenthese=listePotentialP.split(","); 
                    for (String p : listeParenthese) {
                    	if(mailParenthese.contains("@")) { 
                    		if(!(mail.contains(p.trim()+mailParenthese))&&nbMail>mail.size()) {
                    			mail.add(cutPoint(p+mailParenthese)); 
                    		}
                    	} 
                    }
                }
                else if(t.contains(",")&&!t.contains("}")) {
                	if(!(t.split(" ").length>1)) {
	                    String mailVirgule = "@"+t.split("@")[1]; 
	                    String listePotentielVirgule = t.split("@")[0]; 
	                    String[]listeVirgule=listePotentielVirgule.split(",");
	                    for(String p : listeVirgule) {
	                        if(!(mail.contains(p.trim()+mailVirgule))&&nbMail>mail.size()) mail.add(cutPoint(p+mailVirgule));
	                    }
                	}
                	else{
                		String[] splitLine = t.split(" ");
                		
                		for(String elt : splitLine) {
                			if(elt.contains("@") && nbMail>mail.size()) {
                				mail.add(elt.replaceAll("[()]", ""));
                			}
                		}
                	}

                }
                
                else{
	                String[]affinage=t.split(" "); 
	                for(String a : affinage) { 
	                	if(a.contains("@")) { 
	                		if(a.contains(")")) {
	                			if(!(mail.contains(cutPoint(a.replaceAll("[()]",""))))&&nbMail>mail.size()) { 
	                   				mail.add(cutPoint(a.replaceAll("[()]",""))); 
	                    			break;
	                			} 
	                		} 
	                		if(!mail.contains(a)&&nbMail>mail.size()) {
	                			mail.add(cutPoint(a)); 
	                		}
	                	}
	                }
                }
                
            }
        	else if(t.contains("@") && t.split("@")[0].equals("")) {
        		
        		String b = t.split("@")[0];
        		String apresArrobase = t;
        		t=previousLine;
        		if(t.contains("}")) { 
                    String listePotentialA = t.split("}")[0].replaceAll("[{]", ""); 
                    String[]listeAccolade=listePotentialA.split(","); 
                    for (String a : listeAccolade) { 
                        if(!(mail.contains(a.trim()+apresArrobase))&&nbMail>mail.size())mail.add(cutPoint(a+apresArrobase)); 
                    } 
                } 
                if(t.contains(")")) { 
                    String listePotentialP = t.split("\\)")[0].replaceAll("[(]", ""); 
                    String[]listeParenthese=listePotentialP.split(","); 
                    for (String p : listeParenthese) { 
                    		if(!(mail.contains(p.trim()+apresArrobase))&&nbMail>mail.size())mail.add(cutPoint(p+apresArrobase)); 
                    }
                    
                }
                if(b.contains(",")&&!b.contains("}")) {
                    String listePotentielVirgule = t.split("@")[0]; 
                    String[]listeVirgule=listePotentielVirgule.split(",");
                    for(String p : listeVirgule) {
                        if(!(mail.contains(p.trim()+apresArrobase))&&nbMail>mail.size())
                            mail.add(cutPoint(p+apresArrobase));
                    }

               }
                String[]affinage=t.split(" "); 
                for(String a : affinage) { 
                	if(a.contains("@")) { 
                		if(a.contains(")")) { 
                			if(!(mail.contains(cutPoint(a.replaceAll("[()]",""))))&&nbMail>mail.size()) { 
                				mail.add(cutPoint(a.replaceAll("[()]",""))); 
                				break; 
                			} 
                		} 
                	if(!(mail.contains(a))&&nbMail>mail.size())mail.add(cutPoint(a)); 
                	} 
                } 
                previousLine=t;
        	}
        	if(t.contains("Q") && !texte.contains("@") && mail.size()<1){ 
        		String[]gestionQ=t.split(" "); 
        		int index = getIndex(gestionQ,"Q"); 
        		if(index!=-1) { 
        			String mailQ = "@"+gestionQ[index].split("Q")[1]; 
        			if(!mail.contains(gestionQ[index])&&bonAuteur.size()>mail.size()) mail.add(cutPoint(gestionQ[index].replaceAll("Q", "@"))); 
        			for(int i=1;i<bonAuteur.size();i++) { 
            			try{if(!mail.contains(gestionQ[index-i]+mailQ)&&bonAuteur.size()>mail.size())mail.add(cutPoint(gestionQ[index-i]+mailQ));} 
            			catch(ArrayIndexOutOfBoundsException e) {}
        			} 
        		} 
        		 
        	}
        	if(nbMail>mail.size() && t.contains("}") && t.split("@").length==3) mail.add((t.split("@")[1]+"@"+t.split("@")[2]).split(" ")[1]);
        	
        }
        return mail; 
    } 
    public  String cutPoint(String t) { 
    	if (t.endsWith(".") || t.endsWith(",")) { 
            return t.substring(0, t.length() - 1); 
        } else { 
        	t= t.replaceAll(" ",""); 
        	return t.replaceAll(",",""); 
        } 
    } 
    public  int getIndex(String[]s,String r) { 
        for (int i = 0; i < s.length; i++) { 
        	if (s[i].contains(r))return i; 
        } 
        return -1;  
         
    } 
    public  ArrayList<String> getAlternateAuthor(String firstname, String lastname){
    	ArrayList<String>  alternateAuthor = new ArrayList<String>();
    	String[] lastnameTwoParts = lastname.substring(1).split(" ");

    	if(lastnameTwoParts.length<2) alternateAuthor.add((firstname.trim().substring(0,1).toLowerCase() + lastname.substring(1,2).toLowerCase()).trim()); //Florient Boudin -> fb
    	else{
    		//System.out.println(firstname + " " + lastname);
    		alternateAuthor.add((firstname.substring(0,1).toLowerCase() + lastnameTwoParts[0].substring(0,1).toLowerCase() + lastnameTwoParts[1].substring(0,1).toLowerCase()).trim());//Andre F.T. Martins -> afm
    	}
    	if(firstname.length()>3) alternateAuthor.add((firstname.substring(0,3).toLowerCase()).trim());
    	if(lastname.length()>3) alternateAuthor.add((lastname.trim().substring(0,3)));
    	alternateAuthor.add((firstname.substring(0,1)+lastname).toLowerCase().trim());
    	alternateAuthor.add((firstname+lastname.trim().substring(0,1)).trim().toLowerCase());


    	return alternateAuthor;
	}
    
    public static Map<String, String> parseIndiceAuteur(String text){
    	ArrayList<String> listLineWithAuthorAndIndice = new ArrayList<>();
    	Map<String, String> dictionnaire = new HashMap<>();
    	String ancien = "";
    	// on récupère le texte jusque l'abstract seulement
        String [] tabTextSplitInLine = text.split("\n");
        String textToAbstract = "";
        for (String s : tabTextSplitInLine) {
        	if (s.equals("Abstract") || s.equals("ABSTRACT")) break; //contains mieux ??????
        	textToAbstract += s+"\n";
        }
        // on récupère une liste des lignes contenant les auteurs et leurs indices
        String [] tabTextToAbstractSplitInLine = textToAbstract.split("\n");
        for (String l : tabTextToAbstractSplitInLine) {
        	for (String a : bonAuteur) {
        		if (l.contains(a)) {
        			if (!listLineWithAuthorAndIndice.contains(l)) {
        				listLineWithAuthorAndIndice.add(l);
        			} 			
        		}
        	}
        }
        for (String l : listLineWithAuthorAndIndice) {
        	// on split cette ligne à chaque virgule et on remplace les and entre les auteurs par des virgules
        	l = l.replaceAll(" and ", ",");
        	String [] tabLineWithAuthorAndIndiceSplit = l.split(",");  
        	for (String p : tabLineWithAuthorAndIndiceSplit) {
        		p = p.trim();
        		// si la partie split de la ligne contenant l'auteur est de longueur 1, alors on rajoute cette partie comme second indice de l'auteur se trouvant dans la partie précédente
        		if (p.length() ==1 ) {
        			String h = dictionnaire.get(ancien);
        			if (h.length()<=3 && !h.equals("")) {
        				h += ","+p;
        				dictionnaire.put(ancien, h);
        			}
        		}
        		// on associe l'auteur à son indice dans le dictionnaire 
        		for ( String q : bonAuteur) {
        			if (p.contains(q)) {
        				p = p.replaceAll(q,"");
        				p = p.trim();
        				if (p.length()==1 && !p.equals("")) {
        					ancien = q;
        					dictionnaire.put(q, p);
        				}
        				if (p.length()<=3 && p.contains(",") && !p.equals("")) {
        					ancien = q;
        					dictionnaire.put(q, p);
        				}
        			}
        		}
   
        	}
        }
        for (Map.Entry<String, String> entry : dictionnaire.entrySet()) {
            System.out.println("Clé : " + entry.getKey() + ", Valeur : " + entry.getValue());
        }
        
    	return dictionnaire;
    }
    
    public static Map<String, String> parseIndiceAffiliation(String text){
    	ArrayList<String> listLineWithAffiliationAndIndice = new ArrayList<>();
    	Map<String, String> dictionnaire = new HashMap<>();
    	// on récupère le texte jusque l'abstract seulement
        String [] tabTextSplitInLine = text.split("\n");
        String textToAbstract = "";
        for (String s : tabTextSplitInLine) {
        	if (s.equals("Abstract")) break; //contains mieux ??????
        	textToAbstract += s+"\n";
        }
        // on récupère une liste des lignes contenant les affiliations et leurs indices
        String [] tabTextToAbstractSplitInLine = textToAbstract.split("\n");
        for (String l : tabTextToAbstractSplitInLine) {
        	for (String a : affiliations) {
        		if (l.contains(a)) {
        			if (!listLineWithAffiliationAndIndice.contains(l)) {
        				listLineWithAffiliationAndIndice.add(l);
        			} 			
        		}
        	}
        }        
        // on récupère les indices et les lignes 
        for (String l : listLineWithAffiliationAndIndice) {
        	l= l.trim();
        	for ( String a : affiliations) {
        		if(l.contains(a)){
        			l=l.replaceAll(a, "");
        			l=l.replaceAll(";", "");
        			l=l.trim();
        			if (l.length()<=3 && !l.equals("")) {
        				dictionnaire.put(l, a);
        			}
        		}
        	}
        }
        for (Map.Entry<String, String> entry : dictionnaire.entrySet()) {
            System.out.println("Clé : " + entry.getKey() + ", Valeur : " + entry.getValue());
        }
        
    	return dictionnaire;
    }
     
    public static ArrayList<String> parseAffiliations(String text) {
        ArrayList<String> affiliations = new ArrayList<>();
        // on récupère le texte jusque l'abstract seulement
        String [] tabTextSplitInLine = text.split("\n");
        String textToAbstract = "";
        for (String s : tabTextSplitInLine) {
        	if (s.equals("Abstract")) break;  // contains mieux ??????
        	textToAbstract += s+"\n";
        }
        Pattern affiliationPattern = Pattern.compile(".*(Laboratoire|Lab|École|E cole|Ecole|Universidade|Institute|University|Université|([A-Z][a-z]* Inc\\.)|Département|Departement|Department|Univ.|Research|Universitat|Instituto|Insitut|DA-IICT|LIMSI-CNRS|INRIA).*");
        Matcher matcher = affiliationPattern.matcher(textToAbstract);

        while (matcher.find()) {
            String affiliation = matcher.group();
            affiliations.add(removeIndices(removeAuteursMails(affiliation)));
        }
        
        int numAuthors = bonAuteur.size(); // 
        int numAffiliations = affiliations.size();
        
        // on vérifie si le nombre d'affiliations est inférieur au nombre d'auteurs
        if (numAffiliations < numAuthors) {
            try{
            	String lastAffiliation = affiliations.get(numAffiliations - 1); // Récupére la dernière affiliation
            	while (affiliations.size() < numAuthors) {
                    affiliations.add(lastAffiliation);
                }
            }
            catch(IndexOutOfBoundsException e){}
            // on duplique la dernière affiliation jusqu'à ce que le nombre d'affiliations soit égal au nombre d'auteurs
            
        }
        return affiliations;
    }

    private static String removeAuteursMails(String affiliation) {
    	// on enlève les auteurs si il apparaissent au sein de l'affiliation
        for (String auteur : bonAuteur) {
            affiliation = affiliation.replaceAll(auteur, "");
        }
        // on enlève les mails si ils apparaissent au sein de l'affiliation
        for (String mail : mails) {
            affiliation = affiliation.replaceAll(mail, "");
        }
        affiliation = affiliation.replaceAll(";", "");
        affiliation = affiliation.replaceAll("E cole", "Ecole");
        affiliation = affiliation.replaceAll(" is with", "");
        
        return affiliation.trim();	
    }
    
    private static String removeIndices(String affiliation) {
        // on enlève les indexs qui se trouvent en début d'affiliation
        int indexFirstMaj = 0;
        for (int i = 0 ; i < affiliation.length(); i++) {
        	if (Character.isLetter(affiliation.charAt(i))){
        		if (Character.isUpperCase(affiliation.charAt(i))) {
        			indexFirstMaj = i;
        			break;
        		}
        	}
        }
        String affiliationSansIndice = "";
        for (int j = 0 ; j < affiliation.length(); j++) {
        	if (j >= indexFirstMaj) {
        		affiliationSansIndice += affiliation.charAt(j);
        	}
        }
        return affiliationSansIndice.trim();	
    }
    
    public static ArrayList<String> getAlternateAffiliations(String text) {
    	ArrayList<String> otherAffiliations = affiliations;
    	if (indiceAuteur != null && indiceAffiliation != null) { //indiceAuteur != null && indiceAffiliation != null ou !indiceAuteur.isEmpty() && !indiceAffiliation.isEmpty()
    		System.out.println("---------------");
    		int i = 0;
    		for (String a : bonAuteur) {
    			if (indiceAuteur.containsKey(a)) {
    				if (indiceAffiliation.containsKey(indiceAuteur.get(a))) {
    					affiliations.set(i, indiceAffiliation.get(indiceAuteur.get(a)));
    				}
    			}
    			i+=1;
    		}
    		return otherAffiliations;
    	}
    	
    	return affiliations;
    }
    
    public static ArrayList<String> compareAffiliations(String text){
    	if (indiceAuteur != null && indiceAffiliation != null) {
    		return otherAffiliations;
    	}
    	return affiliations;
    }
    
    //getter et setter
	public  ArrayList<String> getAuteurMeta() {
		return auteurMeta;
	}
	public  void setAuteurMeta(ArrayList<String> auteurMeta) {
		Auteur.auteurMeta = auteurMeta;
	}
	public  ArrayList<String> getAuteurParse() {
		return auteurParse;
	}
	public  void setAuteurParse(ArrayList<String> auteurParse) {
		Auteur.auteurParse = auteurParse;
	}
	public  ArrayList<String> getBonAuteur() {
		return bonAuteur;
	}
	public  void setAuteurTitre(ArrayList<String> auteurTitre) {
		Auteur.bonAuteur = auteurTitre;
	}
	public  ArrayList<String> getMails() { 
		return mails; 
	} 
	public  void setMails(ArrayList<String> mails) { 
		Auteur.mails = mails; 
	} 
	public ArrayList<String> getAffiliations() {
		return affiliations;
	}
	public void setAffiliations(ArrayList<String> affiliations) {
		Auteur.affiliations = affiliations;
	}
	public static ArrayList<String> getOtherAffiliations() {
		return otherAffiliations;
	}
	public static void setOtherAffiliations(ArrayList<String> otherAffiliations) {
		Auteur.otherAffiliations = otherAffiliations;
	}
	public static ArrayList<String> getBonAffiliations() {
		return bonAffiliations;
	}
	public static void setBonAffiliations(ArrayList<String> bonAffiliations) {
		Auteur.bonAffiliations = bonAffiliations;
	}
}
