package parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StringBuilders {
		private static String pdfInfo;
		private static String pdfToText;
		private static String pdfToTextFirstPage;
		
		public StringBuilders(String filePath) {
			pdfInfo=extractPdfInfo(filePath);
			pdfToText=extractPdfToText(filePath);
			pdfToTextFirstPage=extractPdfToTextFirst(filePath);
		}
		
		//extracteur d'infos
		public static String extractPdfInfo(String filepath) {
	        StringBuilder text = new StringBuilder();
	        try {

	            //commande console, encodage --> Ascii7 permet la gestion des accents
	        	String toolPath = os.contains("windows") ? homedir + "/lib/xpdf-tools-windows-4.05/bin64/pdfinfo" : homedir + "/lib/xpdf-tools-linux-4.05/bin64/pdfinfo";
	            String[] command = {toolPath, filepath};
	            //execution de la commande
	            Process process = Runtime.getRuntime().exec(command);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                text.append(line).append("\n");
	            }

	        } catch (IOException  e) {
	            e.printStackTrace();
	        }
	        //retourne le texte en vrac

	        return text.toString();
	    }
	    public static String extractPdfToText(String filepath) {
	        StringBuilder text = new StringBuilder();
	    	try {

	    		//commande console, encodage --> Ascii7 permet la gestion des accents    		
	    		
	        	String toolPath = os.contains("windows") ? homedir + "/lib/xpdf-tools-windows-4.05/bin64/pdftotext" : homedir + "/lib/xpdf-tools-linux-4.05/bin64/pdftotext";
		    	String[] command = {toolPath,"-enc","ASCII7", filepath, "-"};
		        
		    	//execution de la commande
		        Process process = Runtime.getRuntime().exec(command);
		        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		        String line;
		        while ((line = reader.readLine()) != null) {
		            text.append(line).append("\n");
		        }
		        process.waitFor();
	    	} catch (IOException | InterruptedException e) {
	    		e.printStackTrace();
	    	}
	    	//retourne le texte en vrac
	    	return text.toString();
	    }
	    public static String extractPdfToTextFirst(String filePath) {
	  	   StringBuilder text = new StringBuilder();
	     	try {
	     		//commande console, encodage --> Ascii7 permet la gestion des accents    		
	 	    	String[] command = {"pdftotext","-enc","ASCII7","-l","1", filePath, "-"};
	 	        
	 	    	//execution de la commande
	 	        Process process = Runtime.getRuntime().exec(command);
	 	        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	 	        String line;
	 	        while ((line = reader.readLine()) != null) {
	 	            text.append(line).append("\n");
	 	        }
	 	        process.waitFor();
	     	} catch (IOException | InterruptedException e) {
	     		e.printStackTrace();
	     	}
	     	//retourne le texte en vrac
	     	return text.toString();
	     }
		
	    //getter and setter 
		public static String getPdfInfo() {
			return pdfInfo;
		}
		public static void setPdfInfo(String pdfInfo) {
			StringBuilders.pdfInfo = pdfInfo;
		}
		public static StringBuilder getPdfToText() {
			return pdfToText;
		}
		public static void setPdfToText(String pdfToText) {
			StringBuilders.pdfToText = pdfToText;
		}
		public static String getPdfToTextFirstPage() {
			return pdfToTextFirstPage;
		}
		public static void setPdfToTextFirstPage(String pdfToTextFirstPage) {
			StringBuilders.pdfToTextFirstPage = pdfToTextFirstPage;
		}
}
