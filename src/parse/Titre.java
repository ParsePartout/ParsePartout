package parse;

import java.io.File;

import extract.StringBuilders;

public class Titre {
	//titre detecte grace au contenu du texte
	private static String titreParse;
	//titre detecte dans les meta donnees
	private static String titreMeta;
	//titre evalue comme etant le plus propice d'etre correct
	private static String bonTitre;
	private static String corpusPath;
	private static File file;
	public Titre(File f, String texte) {
		file = f;
		titreParse=parseTitre(texte);
		titreMeta=extractTitre();
		bonTitre=compareTitre(titreParse,titreMeta);
	}

	//Methodes d'extraction et de comparaison des titres
	public  String parseTitre(String texte) {
		//methode pour retourner le titre du document
		String t = texte.split("\n")[0].startsWith("arXiv") ? texte.split("\n")[2] : texte.split("\n")[0];

		return t;
	}
	public  String extractTitre() {
        String fileName= file.getName();
        StringBuilders sb = new StringBuilders(corpusPath +"/"+ fileName);
        String infoParse = sb.extractPdfInfo();
        int i=0;
        for(String s :infoParse.split("\n")) {
            if(s.contains("Title")) {
                return infoParse.split("\n")[i].replace("Title:          ", "");
            }
            i++;
        }
        return "";
    }
    public  String compareTitre(String titre, String titreData) {
    	//methode pour comparer deux titre
        String[] t = {titre, titreData};
        //verif string vide
        if(t[0].equals("") || t[0]==null)
            return t[1];
        if(t[1].equals("") || t[1]==null)
            return t[0];

        //verif lettre
        for(int i=0; i<t[0].length(); i++) {
            if(t[0].toUpperCase().charAt(i)<='A' && t[0].toUpperCase().charAt(i)>='Z' ) {
                if(t[0].charAt(i)!='.' && t[0].charAt(i)!='-' && t[0].charAt(i)!=':' ) 
                    return t[1];
            }
        }

        for(int i=0; i<t[1].length(); i++) {
            if(t[1].toUpperCase().charAt(i)<='A' && t[1].toUpperCase().charAt(i)>='Z' ) {
                if(t[1].charAt(i)!='.' && t[1].charAt(i)!='-' && t[0].charAt(i)!=':') 
                    return t[0];
            }
        }
        //verif nombre de mots
        if(t[0].split(" ").length<2)
            return t[1];

        if(t[1].split(" ").length<2)
            return t[0];

        return t[0];
    }

    //getter et setter
	public  String getTitreParse() {
		return titreParse;
	}
	public  void setTitreParse(String titreParse) {
		Titre.titreParse = titreParse;
	}
	public  String getTitreMeta() {
		return titreMeta;
	}
	public  void setTitreMeta(String titreMeta) {
		Titre.titreMeta = titreMeta;
	}
	public String getBonTitre() {
		return bonTitre;
	}
	public  void setBonTitre(String bonTitre) {
		Titre.bonTitre = bonTitre;
	}
}
