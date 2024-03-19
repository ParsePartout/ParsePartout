package output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import extract.Index;
import extract.StringBuilders;
import parse.Abstrac;
import parse.Auteur;
import parse.Titre;

/*ParsePartout*/
public class ParsePartout {
    
	private static String homedir;
	private static String corpusPath;
	private static StringBuilders sb;
	private static Titre t;
	private static Auteur au;
	private static Abstrac ab;
	private static File f;
	private static String textRaw;
	private static String intro;
	private static String corps;
	private static String conclu;
	private static String discu;
	private static String ref;
	private static Index i;
	
	
	public ParsePartout(File file) throws IOException {
		f=file;
		homedir = System.getProperty("user.dir");
		setCorpusPath(homedir + "\\Corpus_2021\\");
		sb = new StringBuilders(file.getPath());
		String text=StringBuilders.extractPdfToText();
		String textF=StringBuilders.extractPdfToTextFirst();
		textRaw=StringBuilders.extractPdfToTextRaw();
		t = new Titre(f,text);
		ab = new Abstrac(text);
		int debut = getDebutZone(textF,Titre.getBonTitre());
		int fin = getFinZone(textF,Abstrac.getAbstractParse());
		au = new Auteur(f,text,textF, debut, fin);	
		//on recupere intro/corps/conclu/discu/ref
		i = new Index(textRaw);
		setupIndex();
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
		
		//on recupere intro/corps/conclu/discu/ref
		Index i = new Index(textRaw);
		setupIndex();
		
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
		
		if(intro!=null) {
			bw.append("\nIntro :\n");
			bw.append("			"+intro+"\n");	
		}
		
		if(corps!=null) {
            bw.append("\nCorps :");
            bw.append("			"+corps+"\n");
        }
		if(conclu!=null) {
            bw.append("\nConclusion :\n");
            bw.append(conclu+"\n");
        }
		if(discu!=null) {
            bw.append("\nDiscussion :\n");
            bw.append(discu+"\n");
        }
		if(ref!=null) {
			bw.append("\nReference :\n");
			bw.append(ref);
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
		+ "	<abstract>\n	" + ab.getAbstractParse() + "\n	</abstract>\n"
		+ "	<introduction>\n	" + intro + "\n	</introduction>\n"
		+ "	<biblio>\n	" + ref + "\n	</biblio>\n"
		+ "</article>";
		FileWriter fw = new FileWriter(out);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.append(retour.replaceAll("&", "&amp;"));
		bw.close();
		fw.close();
	}
	public File getFile() {
		return f;
	}
	public static void setupIndex () throws IOException {
		String[]l=textRaw.split("\n");
		System.out.println("intro :"+i.getIntro()[0]+"--"+i.getIntro()[1]);
		
		System.out.println("corps :"+i.getCorps()[0]+"--"+i.getCorps()[1]);

		System.out.println("conclu :"+i.getConclu()[0]+"--"+i.getConclu()[1]);

		System.out.println("discu :"+i.getDiscu()[0]+"--"+i.getDiscu()[1]);

		System.out.println("ref :"+i.getReference()+"\n");

		for(int j=0;j<l.length;j++) {
			if(j>=i.getIntro()[0]&&j<i.getIntro()[1]) {
				intro+=l[j]+" ";
			}
			if(j>=i.getCorps()[0]&&j<i.getCorps()[1]) {
				corps+=l[j]+" ";
			}
			if(j>=i.getConclu()[0]&&j<i.getConclu()[1]) {
				conclu+=l[j]+" ";
			}
			if(j>=i.getDiscu()[0]&&j<i.getDiscu()[1]) {
				discu+=l[j]+" ";
			}
			if(j>=i.getReference()) {
				ref+=l[j]+" ";
			}
		}
	}
	


}