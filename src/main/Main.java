package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import output.ParsePartout;

public class Main {
	private static String corpusPath;
	
	public Main(String[] args) {
		MenuText mt = new MenuText(args[1], this);
	}
	
	 public static void exec(String dir,ArrayList<String> ftp,String out) throws IOException {
	    	corpusPath = System.getProperty("user.dir") + "\\Corpus_2021";
	        
	        	File directory = new File(dir);
	            // Parcourez les fichiers du répertoire
	            File[] files = directory.listFiles();
	            
	        	if (files != null) {
	                for (File file : files) {
	                	
	                    if (file.isFile() && ftp.contains(file.getName())) {
	                    	System.out.println(file.getName());
	                    	ParsePartout pp = new ParsePartout(file);
	                    	if(out.equals("-t")) {
	                    		File dirText = new File("./TXTFiles/");
	                            dirText.mkdir();
	                            pp.putInfo(pp.creationFichierSansRename(dirText.getName(), file, ".txt")); 
	                    	}
	                    	else if(out.equals("-x")) {
	                    		File dirXml = new File("./XMLFiles/");
	                            dirXml.mkdir();
	                            pp.toXML(pp.creationFichierSansRename(dirXml.getName(), file, ".xml")); 
	                    	}
	                    	else if(out.equals("-all")) {
	                    		File dirText = new File("./TXTFiles/");
	                            dirText.mkdir();
	                            File dirXml = new File("./XMLFiles/");
	                            dirXml.mkdir();
	                            pp.putInfo(pp.creationFichierSansRename(dirText.getName(), file, ".txt")); 
	                            pp.toXML(pp.creationFichierSansRename(dirXml.getName(), file, ".xml")); 
	                    	}
	                    }
	                }
	            }       
	    }

    public static void main(String args[]) throws IOException{
		Main main = new Main(args);
	}
}
