package extract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StringBuilders {
		private static String pdfInfo;
		private static String pdfToText;
		private static String pdfToTextFirstPage;
		private static String homedir;
		private static String os;
		private static String filePath;
		
		public StringBuilders(String fp) {
			os = System.getProperty("os.name").toLowerCase();
			homedir = System.getProperty("user.dir");
			filePath = fp;
			pdfInfo = extractPdfInfo();
			pdfToText=extractPdfToText();
			pdfToTextFirstPage=extractPdfToTextFirst();
			checkPremierePage();

		}
		
		//extracteur d'infos
		public  String extractPdfInfo() {
	        StringBuilder text = new StringBuilder();
	        try {
	            //commande console, encodage --> Ascii7 permet la gestion des accents4
	        	String toolPath = os.contains("windows") ? homedir + "\\lib\\xpdf-tools-win-4.05\\bin64\\pdfinfo" : homedir + "/lib/xpdf-tools-linux-4.05/bin64/pdfinfo";

	            String[] command = {toolPath, filePath};
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
	        //System.out.println(text.toString());
	        return text.toString();
	    }
	    public String extractPdfToText() {
	        StringBuilder text = new StringBuilder();
	    	try {

	    		//commande console, encodage --> Ascii7 permet la gestion des accents    		
	    		
	        	String toolPath = os.contains("windows") ? homedir + "\\lib\\xpdf-tools-win-4.05\\bin64\\pdftotext" : homedir + "/lib/xpdf-tools-linux-4.05/bin64/pdftotext";
		    	String[] command = {toolPath,"-enc","ASCII7","-nopgbrk", filePath, "-"};
		    	
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
	    	//System.out.println(text.toString());
	    	//retourne le texte en vrac
	    	return text.toString();
	    }
	    public  String extractPdfToTextFirst() {
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
	     	//System.out.println(text.toString());
	     	//retourne le texte en vrac
	     	return text.toString();
	     }
	    public  String extractPdfToTextRaw() {
	        StringBuilder text = new StringBuilder();
	    	try {

	    		//commande console, encodage --> Ascii7 permet la gestion des accents    		
	    		
	        	String toolPath = os.contains("windows") ? homedir + "\\lib\\xpdf-tools-win-4.05\\bin64\\pdftotext" : homedir + "/lib/xpdf-tools-linux-4.05/bin64/pdftotext";
		    	String[] command = {toolPath,"-raw","-enc","ASCII7","-nopgbrk", filePath, "-"};
		    	
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
	    	//System.out.println(text.toString());
	    	//retourne le texte en vrac
	    	return text.toString();
	    }
		public String extractPdfToTextTwo() {
			StringBuilder text = new StringBuilder();
	    	try {
	    		//commande console, encodage --> Ascii7 permet la gestion des accents    		
	        	String toolPath = os.contains("windows") ? homedir + "\\lib\\xpdf-tools-win-4.05\\bin64\\pdftotext" : homedir + "/lib/xpdf-tools-linux-4.05/bin64/pdftotext";
		    	String[] command = {toolPath,"-raw","-enc","ASCII7","-nopgbrk","-f","2","-l","2", filePath, "-"};
		    	
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
	    	
	    	return text.toString();
			
		}
		public void checkPremierePage() {
			String[]t=pdfToTextFirstPage.split("\n");
			if(t[0].split(" ").length>20) {
				setPdfToTextFirstPage(extractPdfToTextTwo());
			}
			
		}
	    //getter and setter 
		public  String getPdfInfo() {
			return pdfInfo;
		}
		public  void setPdfInfo(String pdfInfo) {
			this.pdfInfo = pdfInfo;
		}
		public  String getPdfToText() {
			return pdfToText;
		}
		public  void setPdfToText(String pdfToText) {
			this.pdfToText = pdfToText;
		}
		public  String getPdfToTextFirstPage() {
			return pdfToTextFirstPage;
		}
		public  void setPdfToTextFirstPage(String pdfToTextFirstPage) {
			this.pdfToTextFirstPage = pdfToTextFirstPage;
		}
		public  String getHomedir() {
			return homedir;
		}
}