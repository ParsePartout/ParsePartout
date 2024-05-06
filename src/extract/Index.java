package extract;

import java.util.ArrayList;

public class Index {
    private static int[]intro= {0,0};
    private static int[]corps= {0,0};
    private static int[]conclu= {0,0};
    private static int[]discu= {0,0};
    private static boolean concluFlag;
    private static boolean discuFlag;
    private static int reference=0;
    private static String[]li;
    private static String tokenIntro;

    public Index (String tRow) {
    	intro[0] = 0;
    	intro[1] = 0;
    	corps[0] = 0;
    	corps[1] = 0;
    	conclu[0] = 0;
    	conclu[1] = 0;
    	discu[0] = 0;
    	discu[1] = 0;
    	concluFlag=false;
    	discuFlag=false;
    	tRow.replace("", "");
        li=tRow.split("\n");
//        for(int i=0;i<li.length;i++) {
//        	System.out.println(li[i]);
//        }
        definirIntro();
        definirCorps();
        chercheConcluDiscu();
//        System.out.println("discu : "+ discuFlag);
//        System.out.println("conclu : "+ concluFlag);
        if(discuFlag) definirDiscu();
        if(concluFlag) definirConclu();
        definirReference();
    }
    
    public static void definirIntro() {
    	//ajout premier index intro
    	for(int i=0; i<li.length; i++) {
			if(li[i].toUpperCase().contains("INTRODUCTION")){
				intro[0] = i+1;
				tokenIntro = li[i].substring(0,2);
				break;
			}
		}
    	
    }
    public static void definirCorps() {
    	//ajout index corps
    	for(int i=intro[0];i<li.length;i++) {
    		if((li[i].startsWith("2.") && li[i].replace("2. ", "").substring(0,1).equals(li[i].replace("2. ", "").substring(0,1).toUpperCase()) && tokenIntro.matches("[0-9]."))
					|| (li[i].startsWith("2 ") && li[i].replace("2 ", "").substring(0,1).equals(li[i].replace("2 ", "").substring(0,1).toUpperCase()) && tokenIntro.matches("[0-9] "))
					|| (li[i].startsWith("II ") && li[i].replace("II ", "").substring(0,1).equals(li[i].replace("II ", "").substring(0,1).toUpperCase()) && tokenIntro.matches("[I-X] "))
					|| (li[i].startsWith("II.") && li[i].replace("II. ", "").substring(0,1).equals(li[i].replace("II. ", "").substring(0,1).toUpperCase()) && tokenIntro.matches("[I-X]."))){
    			
    			intro[1]=i;
    			corps[0]=i;
    			break;
    		}
    	}
    	 
    }
    public static void chercheConcluDiscu() {
    	for(int i=corps[0];i<li.length;i++) {
    		//cherche index discu
			if(li[i].toUpperCase().contains("DISCUSSION") && (li[i].split(" ")[0].matches("[0-9]+.?[0-9]?")||li[i].split(" ")[0].matches("[I-X]+\\."))) {
				corps[1]=i;
				discu[0]=i+1;
				discuFlag=true;
			}
	    	//cherche index conclusion
			else if(li[i].toUpperCase().contains("CONCLUSION") && (li[i].split(" ")[0].matches("[0-9]+.?")||li[i].split(" ")[0].matches("[I-X]+\\."))) {
				if(!discuFlag) corps[1]=i;
				conclu[0]=i+1;
				concluFlag=true;				
			}
    	
		}
    }
    
    public static void definirDiscu() {
    	for(int i=corps[1]+2;i<li.length;i++) {
    		if(li[i].toUpperCase().startsWith("ACKNOWLEDGEMENT") || li[i].toUpperCase().startsWith("ACKNOWLEDGMENT") 
					 || li[i].toUpperCase().startsWith("APPENDIX")
					 || li[i].toUpperCase().startsWith("REFERENCE")
					 || li[i].matches("[0-9] ([A-Z]|[a-z]|-| )+") 
					 || li[i].matches("[I-X]+\\. ([A-Z]|[a-z]|-| )+")
					 || li[i].toUpperCase().contains("CONCLUSION") && (li[i].split(" ")[0].matches("[0-9]+.?")||li[i].split(" ")[0].matches("[I-X]+\\."))) {
				
				discu[1]=i;
				break;
			}
    	}
    }
    public static void definirConclu() {
    	//cherche index conclusion
    	for(int i=corps[1]+2;i<li.length;i++) {
			if(!li[i].toUpperCase().contains("CONCLUSION") && (li[i].toUpperCase().startsWith("ACKNOWLEDGEMENT") || li[i].toUpperCase().startsWith("ACKNOWLEDGMENT") || li[i].toUpperCase().startsWith("REFERENCE") 
					 || li[i].matches("[0-9].? ([A-Z]|[a-z]|-| )+"))){
				
				conclu[1]=i;
				break;
			}
    	}
    }
    
    public static void definirReference() {
    	//definirReference
    	int startR=0;
    	if(conclu[1] != 0) startR = conclu[1];
    	else if(discu[1]!=0) startR = discu[1];
    	else startR = corps[1];
    	
    	for(int i=startR;i<li.length;i++) {
    		if(li[i].toUpperCase().contains("REFERENCE") ) {
    			reference=i+1;
    			break;
    		}
    	}
    }
 

    public int[] getIntro() {
        return intro;
    }
    public void setIntro(int[] intro) {
        Index.intro = intro;
    }
    public int[] getCorps() {
        return corps;
    }
    public void setCorps(int[] corps) {
        Index.corps = corps;
    }
    public int[] getConclu() {
        return conclu;
    }
    public void setConclu(int[] conclu) {
        Index.conclu = conclu;
    }
    public int getReference() {
        return reference;
    }
    public void setReference(int reference) {
        Index.reference = reference;
    }
    public int[] getDiscu() {
        return discu;
    }
    public void setDiscu(int[] discu) {
        Index.discu = discu;
    }

}