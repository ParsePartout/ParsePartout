package extract;

import java.util.ArrayList;

public class Index {
    private static int[]intro= {0,0};
    private static int[]corps= {0,0};
    private static int[]conclu= {0,0};
    private static int[]discu= {0,0};
    private static boolean concluFlag=false;
    private static int reference=0;
    private static String[]li;

    public Index (String tRow) {
        li=tRow.split("\n");
        for(int i=0;i<li.length;i++) {
//        	System.out.println(i+li[i]);
        }
        definirIntro();
        definirCorps();
        chercheConcluDiscu();
        if(conclu[0]>discu[0]) {
        	definirDiscuFromConclu();
        }else {
        	definirConcluFromDiscu();
        }
        definirReference();
    }
    
    public static void definirIntro() {
    	//ajout premier index intro
    	for(int i=0; i<li.length; i++) {
			if(li[i].toUpperCase().contains("INTRODUCTION")){
				intro[0] = i;
				break;
			}
		}
    }
    public static void definirCorps() {
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
    }
    public static void chercheConcluDiscu() {
    	//cherche index conclusion
    	for(int i=corps[0];i<li.length;i++) {
			if(li[i].toUpperCase().contains("CONCLUSION") && (li[i].split(" ")[0].matches("\\d+")
					||li[i].split(" ")[0].matches("[I-X]+\\."))) {
				corps[1]=i-1;
				conclu[0]=i;
				concluFlag=true;
				break;
			}
    	//cherche index discu
			if(li[i].toUpperCase().startsWith("DISCUSSION")
					||li[i].toUpperCase().startsWith("ACKNOWLEDGEMENTS")
					||li[i].toUpperCase().startsWith("ACKNOWLEDGMENTS")) {
				corps[1]=i-1;
				discu[0]=i;
				break;
			}
		}
    }
    public static void definirDiscuFromConclu() {
    	for(int i=conclu[0];i<li.length;i++) {
    		//cherche index discu
			if(li[i].toUpperCase().startsWith("ACKNOWLEDGEMENTS")
					||li[i].toUpperCase().startsWith("ACKNOWLEDGMENTS")
					||li[i].toUpperCase().startsWith("DISCUSSION")){
				conclu[1]=i-1;
				concluFlag=false;
				discu[0]=i;
				break;
			}
    	}
    }
    public static void definirConcluFromDiscu() {
    	//cherche index conclusion
    	for(int i=discu[0];i<li.length;i++) {
			if(li[i].toUpperCase().contains("CONCLUSION") && (li[i].split(" ")[0].matches("\\d+")
					||li[i].split(" ")[0].matches("[I-X]+\\."))) {
				discu[1]=i-1;
				conclu[0]=i;
				concluFlag=true;
				break;
			}
    	}
    }
    public static void definirReference() {
    	//definirReference
    	int startR;
    	if(concluFlag) {
    		startR=conclu[0];
    	}else startR=discu[0];
    	for(int i=startR;i<li.length;i++) {
    		if(li[i].toUpperCase().startsWith("REFERENCES")) {
    			reference=i;
    			if(concluFlag) {
    				conclu[1]=i-1;
    			}else discu[1]=i-1;
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