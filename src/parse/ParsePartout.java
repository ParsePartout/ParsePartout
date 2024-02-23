package parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*ParsePartout*/
public class ParsePartout {
    
	private static String homedir;
	private static String corpusPath;
	private static StringBuilders sb;
	private static Titre t;
	private static Auteur au;
	private static Abstrac ab;
	private static File f;
	

	
	public ParsePartout(File file) {
		f=file;
		homedir = System.getProperty("user.dir");
		setCorpusPath(homedir + "\\Corpus_2021\\");
		sb = new StringBuilders(file.getPath());
		String text=sb.extractPdfToText();
		String textF=sb.extractPdfToTextFirst();
		t = new Titre(f,text);
		ab = new Abstrac(text);
		int debut = getDebutZone(textF,t.getBonTitre());
		int fin = getFinZone(textF,ab.getAbstractParse());
		au = new Auteur(f,text,textF, debut, fin);	
		
	}
	
	//renvoie l'indice de fin du titre
	private static int getDebutZone(String textF, String titre) {
		for(int i=0;i<textF.length();i++) {
			int j=0;
			int k=i;
			while(textF.charAt(k)==titre.charAt(j)) {
				k+=1;
				j+=1;
				if(j==titre.length()) {
					return k;
				}
			}
		}
		return titre.length();
	}
	
	//renvoie l'indice du debut du abstract
	private static int getFinZone(String textF, String abstrac) {
		for(int i=0;i<textF.length();i++) {
			int j=0;
			int k=i;
			while(textF.charAt(k)==abstrac.charAt(j)) {
				k+=1;
				j+=1;
				if(j==abstrac.length()) {
					return i;
				}
			}
			
		}
		return abstrac.length();
	}
	public static String getNom(File f) {
		//return nom du fichier 
		return f.getName();
	}
	public static File creationFichierSansRename(String dossier, File f, String extension) {
		//creation du fichier texte sans rename
		File file = new File("./"+dossier+"/"+f.getName().substring(0,f.getName().length()-4) + extension);
		
		//verifie la creation
		if(file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	public static File creationFichierAvecRename(File f,String rename) throws IOException {
		//crée un fichier dans le dossier préparé + le rename selon le parametre
		File file = new File("./DejaParséAlorsTuVasFaireQuoi/"+rename+ ".txt");
		
		//verifie la creation
		if(file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	public static void putInfo(File out) throws IOException {
		//pour remplir le fichier 
		FileWriter fw = new FileWriter(out);
		BufferedWriter bw = new BufferedWriter(fw);

		//on compare pour savoir quelle est la bonne variable a prendre en compte
		String bonTitre = t.getBonTitre();
		ArrayList<String> bonAuteurs = au.getBonAuteur();
		int nbAuteur = bonAuteurs.size();
		ArrayList<String> mail = au.getMails();
		int nbMail=mail.size();
		
		//creation du texte
		bw.append("Nom du fichier :\n");
		bw.append("			"+f.getName()+"\n");
		
		if(!bonTitre.equals("")) {
			bw.append("Titre :\n");
			bw.append("			"+bonTitre+"\n");
		}
		
		if(nbAuteur!=0&&nbAuteur<=bonAuteurs.size()) {
			bw.append("Nombre d'auteur :\n ");
			bw.append("			"+String.valueOf(nbAuteur));
		}
		if(bonAuteurs.size()==1) {
			bw.append("\nAuteur :\n");
			bw.append("			"+bonAuteurs.get(0));
		}
		if(bonAuteurs.size()>1) {
			bw.append("\nAuteurs :");
			for(String a : bonAuteurs)
				bw.append("\n			"+a);
		}
		
		if(nbMail==1) {
			bw.append("\nMail :\n");
			bw.append("			"+mail.get(0));
		}
		if(nbMail>1) {
			bw.append("\nMails :");
			for(String m : mail)
				bw.append("\n			"+m);
		}
		
		if(ab!=null) {
			bw.append("\nAbstract :\n");
			bw.append("			"+ab.getAbstractParse()+"\n");	
		}		
		bw.close();
		fw.close();
	
	}
	public StringBuilders getSb() {
		return sb;
	}
	public static String getCorpusPath() {
		return corpusPath;
	}
	public static void setCorpusPath(String corpusPath) {
		ParsePartout.corpusPath = corpusPath;
	}
	
	public static void toXML(File out) throws IOException {
		String retour = 
		  "<article>\n"
		+ "	<preamble>"+f.getName()+"</preamble>\n"
		+ "	<title>"+t.getBonTitre()+"</title>\n"
		+ "	<auteurs>\n";
		
		for(int i=0; i<au.getBonAuteur().size(); i++ ) {
			retour  += "		<auteur>\n"
					+  "			<name>"+au.getBonAuteur().get(i)+"</name>\n";
			if(!au.getMails().isEmpty()) 
					retour += "			<mail>"+au.getMails().get(i)+"</mail>\n";
			retour += "		</auteur>\n";
		}

		retour+= 
		  "	</auteurs>\n"
		+ "	<abstract>"+ab.getAbstractParse()+"</abstract>\n"
		+ "	<biblio> </biblio>\n"
		+ "</article>";
		FileWriter fw = new FileWriter(out);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.append(retour);
		bw.close();
		fw.close();
	}
	public File getFile() {
		return f;
	}
	


}