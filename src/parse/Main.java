package parse;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException {
    	File directory = new File(corpusPath);
        // Parcourez les fichiers du répertoire
        File[] files = directory.listFiles();
        File dir = new File("./DejaParséAlorsTuVasFaireQuoi");
        dir.mkdir();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".pdf")) {
                    putInfo(file, creationFichierSansRename(file));
                }
            }
        }
    }
}
