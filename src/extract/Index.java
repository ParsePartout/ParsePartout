package extract;

import java.util.ArrayList;

public class Index {
    private static int[]intro= {0,0};
    private static int[]corps= {0,0};
    private static int[]conclu= {0,0};
    private static int[]discu= {0,0};
    private static int reference=0;
    private static String texte;

    public Index (String tRow) {
        texte=tRow;
        definir();
        
    }

    public static void definir() {
    	String[]li=texte.split("\n");
    	//ajout index introduction
    	for(int i=0; i<li.length; i++) {
			if(li[i].toUpperCase().contains("INTRODUCTION")){
				intro[0] = i;
				break;
			}
		}
    	//ajout index corps
    	for(int i=intro[0];i<li.length;i++) {
    		if(li[i].startsWith("2.")
					|| li[i].startsWith("2 ")
					|| li[i].startsWith("II.")
					|| li[i].startsWith("1.1")) {
    			intro[1]=i-1;
    			corps[0]=i;
    			break;
    		}
    	}
    	
    	//ajout index conclusion
    	for(int i=corps[0];i<li.length;i++) {
			if(li[i].toUpperCase().contains("CONCLUSION") && (li[i].split(" ")[0].matches("\\d+")
					||li[i].split(" ")[0].matches("[I-X]+\\."))) {
				corps[1]=i-1;
				conclu[0]=i;
				conclu[1]=1;
				System.out.println("on passe conclu");

				break;
			}
		}
    	
    	//ajout index discussion
    	int startD=0;
    	if(conclu[1]==1) {
    		startD=conclu[0];
    	}else {
    		startD=corps[0];
    	}
    	for(int i=startD;i<li.length;i++) {
			if((li[i].toUpperCase().contains("DISCUSSION")
					||li[i].toUpperCase().contains("ACKNOWLEDGEMENTS"))  && (li[i].split(" ")[0].matches("[0-9]+.?")
					||li[i].split(" ")[0].matches("[I-X]+\\."))) {
				if(conclu[1]==1) {
					conclu[1]=i-1;
				}else {
					conclu[0]=0;
					conclu[1]=0;
					corps[1]=i-1;
				}
				discu[0]=i;
				discu[1]=1;
				break;
			}
		}
    	//ajout index reference
    	int startR=0;
    	if(discu[1]==1) {
    		startR=discu[0];
    	}else {
    		if(conclu[1]==1) {
        		startR=conclu[0];
    		}else {
    			startR=corps[0];
    		}
    	}
    	for(int i=startR;i<li.length;i++) {
    		if(li[i].toUpperCase().startsWith("REFERENCES")) {
				if(discu[1]==1) {
		    		discu[1]=i-1;
		    	}else {
		    		if(conclu[1]==1) {
		        		conclu[1]=i-1;
		        		discu[0]=0;
		        		discu[1]=0;
		    		}else {
		    			corps[1]=i-1;
		    			discu[0]=0;
		        		discu[1]=0;
		        		conclu[0]=0;
		        		conclu[1]=0;
		    		}
		    	}
				reference=i;
				break;
			}
		}
    }
    public static int[] getIntro() {
        return intro;
    }
    public static void setIntro(int[] intro) {
        Index.intro = intro;
    }
    public static int[] getCorps() {
        return corps;
    }
    public static void setCorps(int[] corps) {
        Index.corps = corps;
    }
    public static int[] getConclu() {
        return conclu;
    }
    public static void setConclu(int[] conclu) {
        Index.conclu = conclu;
    }
    public static int getReference() {
        return reference;
    }
    public static void setReference(int reference) {
        Index.reference = reference;
    }
    public static int[] getDiscu() {
        return discu;
    }
    public static void setDiscu(int[] discu) {
        Index.discu = discu;
    }

}