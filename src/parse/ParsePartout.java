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
		corpusPath = homedir + "\\Corpus_2021\\";
		sb = new StringBuilders(file.getPath());
		String text=sb.extractPdfToText();
		String textF=sb.extractPdfToTextFirst();
		t = new Titre(f,text);
		System.out.println(t.getBonTitre());
		au = new Auteur(f,text,textF);

		ab = new Abstrac(text);
		
		
		
	}
	public static String getNom(File f) {
		//return nom du fichier 
		return f.getName();
	}
	public static File creationFichierSansRename(File f) {
		//creation du fichier texte sans rename
		File file = new File("./DejaParséAlorsTuVasFaireQuoi/"+f.getName().substring(0,f.getName().length()-4) + ".txt");
		
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
		
		//creation du texte
		bw.append("Nom du fichier :\n");
		bw.append("			"+f.getName()+"\n");
		
		if(!bonTitre.equals("")) {
			bw.append("Titre :\n");
			bw.append("			"+bonTitre+"\n");
		}
		/*
		if(nbAuteur!=0&&nbAuteur<=auteurs.size()) {
			bw.append("Nombre d'auteur :\n ");
			bw.append("			"+String.valueOf(nbAuteur));
		}
		if(auteurs.size()==1) {
			bw.append("\nAuteur :\n");
			bw.append("			"+bonAuteurs.get(0));
		}
		if(auteurs.size()>1) {
			bw.append("\nAuteurs :");
			for(String a : bonAuteurs)
				bw.append("\n			"+a);
		}
		*/
		if(ab!=null) {
			bw.append("\nAbstract :\n");
			bw.append("			"+ab.getAbstractParse()+"\n");	
		}		
		bw.close();
		fw.close();
	
	}
	


}