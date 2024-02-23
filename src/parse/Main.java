package parse;

import java.io.File;
import java.io.IOException;

public class Main {
	private static String corpusPath;

    public static void main(String args[]) throws IOException {
		corpusPath = System.getProperty("user.dir") + "\\Corpus_2021";

    	File directory = new File(corpusPath);
        // Parcourez les fichiers du répertoire
        File[] files = directory.listFiles();
        File dir = new File("./DejaParséAlorsTuVasFaireQuoi");
        File dirxml = new File("./boumXMLkeskiamaintenant");
        dir.mkdir();
        dirxml.mkdir();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".pdf")) {
                	ParsePartout pp = new ParsePartout(file);
                    pp.putInfo(pp.creationFichierSansRename("./DejaParséAlorsTuVasFaireQuoi/", file, ".txt"));
                    pp.toXML(pp.creationFichierSansRename("./boumXMLkeskiamaintenant/", file, ".xml"));
                }
            }
        }
                      
    }
}
