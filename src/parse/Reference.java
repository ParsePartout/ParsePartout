package parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reference {
  private static String refParse;
  
  public Reference(String texte){
    setRefParse(parseRef(texte));
  }

  //methode pour recup les references via le parsing
  public static String parseRef(String texte) {
    String[] lines = texte.split("\n");
    String retour = "";
    int lastRef=0;
    String firstChr=" ";
    for(int i=0; i<lines.length; i++) {
      if(lines[i].startsWith("")) lines[i]=lines[i].substring(1);
      if(lines[i].toUpperCase().equals("REFERENCES")) {
    	  lastRef=i+1;
      }

    }
    if(lines[lastRef].isEmpty()) lastRef+=1;
    firstChr=lines[lastRef].substring(0, 1);
    for(int i=lastRef; i<lines.length; i++) {
		try {
			int str = Integer.parseInt(lines[i]);
		}catch(NumberFormatException e){
			if(lines[i]!="\n" && !lines[i].isEmpty()) {
    			
    			if(Character.isLowerCase(lines[i].charAt(0)) || !compareChar(firstChr,lines[i].substring(0,1))) retour+=" "+lines[i];
    			else retour+="\n			"+lines[i];
    			
    		}
		}    		    			    	
    }
    return retour;
  }
  
public static boolean compareChar(String c,String c2) {
	boolean comp = true;
	Pattern p = Pattern.compile("[^\\w]");
    Matcher m = p.matcher(c);
	if(m.find()) {
		if(c.equals(c2)) return true;
		else return false;
	}
	
	return comp;
}

public static String getRefParse() {
	return refParse;
}

public static void setRefParse(String refParse) {
	Reference.refParse = refParse;
}  
}