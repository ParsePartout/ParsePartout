package parse;

public class Abstrac {
    private static String abstractParse;

    public Abstrac(String texte){
        abstractParse=parseAbstract(texte);
    }

    //methode pour recup l'abstract via le parsing
    public  String parseAbstract(String texte) {
        String[] lines = texte.split("\n");
        String retour = null;

        for(int i=0; i<lines.length; i++) {
            //Si l'abtrsact est en gros titre
        	
            if(lines[i].toUpperCase().equals("ABSTRACT" )){
                retour = lines[i+1];
                break;
            //si le mot abstract est compris dans l'abstract
            }else if(lines[i].toUpperCase().contains("ABSTRACT")) {
                //on enleve le mot abstract
                for(int j=1; j<lines[i].length(); j++) {
                    if(lines[i].charAt(j)>='A' && lines[i].charAt(j)<='Z' ) {
                        retour = lines[i].replace("ABSTRACT ","");
                        break;
                    }
                }
                break;
            }
        }

        //Si il ny a pas le mot Abstract
        boolean flag=false; 
        if(retour==null) {
            for(int i=0; i<lines.length; i++) {
                if(lines[i].toUpperCase().contains("INTRODUCTION")) {
                    retour = lines[i-1];
                    for(int j=i-1; j>0; j--) { 
                        if(!lines[j].equals("")) { 
                            retour = lines[j]; 
                            flag=true; 
                            break;
                        }
                    } 
                }
                if(flag) break; 
            } 
        }
        return retour;
    }
    //getter et setter
    public  String getAbstractParse() {
        return abstractParse;
    }
    public  void setAbstractParse(String abstractParse) {
        Abstrac.abstractParse = abstractParse;
    }
}