package parse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*ParsePartout*/
public class ParsePartout {
    
	private static String os;
	private static String homedir;
	private static String corpusPath;
	
	public ParsePartout() {
		os=System.getProperty("os.name").toLowerCase();
		homedir = System.getProperty("user.dir");
		corpusPath = Paths.get(homedir,"Corpus_2021").toString();
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
	public static void putInfo(File from, File out) throws IOException {
		//pour remplir le fichier 
		FileWriter fw = new FileWriter(out);
		BufferedWriter bw = new BufferedWriter(fw);
		
		//texte en vrac
		String block = getString(from.getName());
		String blockf = getStringFirst(from.getName());	
		
		//on extraie les variables
		String titre = getTitre(block);
		ArrayList<String>auteurs=getAuteur(block,blockf);
		String abstrac = getAbstract(block);
		
		//on extraie les metadonnees
		String titreData = getTitreData(from);
		ArrayList<String> auteursData=getAuteurData(from);
		
		//on compare pour savoir quelle est la bonne variable a prendre en compte
		String bonTitre = getBonTitre(titre,titreData);
//		ArrayList<String> bonAuteurs = getBonAuteurs(auteurs,auteursData);
//		int nbAuteur = bonAuteurs.size();
		
		//creation du texte
		bw.append("Nom du fichier :\n");
		bw.append("			"+from.getName()+"\n");
		
		if(!titre.equals("")) {
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
		if(abstrac!=null) {
			bw.append("\nAbstract :\n");
			bw.append("			"+abstrac+"\n");	
		}		
		bw.close();
		fw.close();
	
	}
	


}