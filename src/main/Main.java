package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import output.ParsePartout;

public class Main {
	private static String corpusPath;

    public static void main(String args[]) throws IOException {
		corpusPath = System.getProperty("user.dir") + "\\Corpus_2021_test";
		File directory = new File("./testIndex");
		directory.mkdir();
		File f = new File(corpusPath+"\\Das-Martins.pdf");
		File[] ff=new File(corpusPath).listFiles();
		for(File fi : ff) {
			System.out.println(fi.getName());
			ParsePartout pp = new ParsePartout(fi);
			ParsePartout.testIndex(directory.getName(), fi, ".txt");
		}
//		ParsePartout pp = new ParsePartout(f);
//		ParsePartout.testIndex(directory.getName(), f, ".txt");
    
		
//        if(args[0].equals("-d")) {
//        	File directory = new File(args[1]);
//            // Parcourez les fichiers du répertoire
//            File[] files = directory.listFiles();
//            
//        	if (files != null) {
//                for (File file : files) {
//                	System.out.println(file.getName());
//                    if (file.isFile() && file.getName().endsWith(".pdf")) {
//                    	ParsePartout pp = new ParsePartout(file);
//                    	if(args[2].equals("-t")) {
//                    		File dirText = new File("./DejaParséAlorsTuVasFaireQuoi/");
//                            dirText.mkdir();
//                            pp.putInfo(pp.creationFichierSansRename(dirText.getName(), file, ".txt")); 
//                    	}
//                    	else if(args[2].equals("-x")) {
//                    		File dirXml = new File("./boumXMLkeskiamaintenant/");
//                            dirXml.mkdir();
//                            pp.toXML(pp.creationFichierSansRename(dirXml.getName(), file, ".xml")); 
//                    	}
//                    	else if(args[2].equals("-all")) {
//                    		File dirText = new File("./DejaParséAlorsTuVasFaireQuoi/");
//                            dirText.mkdir();
//                            File dirXml = new File("./boumXMLkeskiamaintenant/");
//                            dirXml.mkdir();
//                            pp.putInfo(pp.creationFichierSansRename(dirText.getName(), file, ".txt")); 
//                            pp.toXML(pp.creationFichierSansRename(dirXml.getName(), file, ".xml")); 
//                    	}
//                    }
//                }
//            }        
//        }
//        else if(args[0].equals("-f")) {
//        	int i = 0;
//        	ArrayList<ParsePartout> output = new ArrayList<>();
//        	while(args[i]!= "-t" || args[i]!= "-x" || args[i]!= "-all"){
//        		output.add(new ParsePartout(new File(args[i])));
//        		i+=1;
//        	}
//        	if(args[i].equals("-t")) {
//        		File dirText = new File("./DejaParséAlorsTuVasFaireQuoi/");
//                dirText.mkdir();
//        		for(ParsePartout pp : output) {
//        			pp.putInfo(pp.creationFichierSansRename(dirText.getName(), pp.getFile(), ".txt")); 
//        		}
//        	}
//        	else if(args[i].equals("-x")) {
//        		File dirXml = new File("./parsingXML");
//                dirXml.mkdir();
//        		for(ParsePartout pp : output) {
//        			pp.toXML(pp.creationFichierSansRename(dirXml.getName(), pp.getFile(), ".xml"));
//        		}
//        	}
//        	else if(args[i].equals("-all")) {
//        		File dirText = new File("./parsingText");
//                dirText.mkdir();
//                File dirXml = new File("./parsingXML");
//                dirXml.mkdir();
//        		for(ParsePartout pp : output) {
//        			pp.putInfo(pp.creationFichierSansRename(dirText.getName(), pp.getFile(), ".txt")); 
//        			 pp.toXML(pp.creationFichierSansRename(dirXml.getName(), pp.getFile(), ".xml"));
//        		}
//        	}
//        		
//        	
//        }
//        else{
//        	System.out.println("usage : parsePartout [-d <directory> | -f <file>] [-output]");
//        }
        
		
    }
}
