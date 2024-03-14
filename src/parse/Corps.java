package parse;

import java.util.ArrayList;

public class Corps {
	private static String corpsTxt;
	private static String txt;
	private static int indexIntroduction;
	private static int indexConclusion;
	
	public Corps (String t) {
		setTxt(t);
		setCorpsTxt(txt);
		
	}
	
	public static String getCorpsTxt() {
		return corpsTxt;
	}

	public static void setCorpsTxt(String t) {
		String[]line=t.split("\n");
		String r="";
		
		
		for(int i =0;i<line.length;i++) {
			if(line[i].startsWith("2.")
					|| line[i].startsWith("2 ")
					|| line[i].startsWith("II.")
					|| line[i].startsWith("1.1")) {
				setIndexIntroduction(i);
				break;
			}
		}
		for(int j = indexIntroduction; j<line.length;j++) {
			if(line[j].contains("Conclusion")
					|| line[j].contains("Discussion")
					|| line[j].contains("Acknowledgments")
					|| line[j].contains("References")
					|| line[j].contains("Discussion")){
				setIndexConclusion(j);
				break;
			}
		}
		System.out.println(line.length);
		System.out.println(indexIntroduction+"-->"+indexConclusion);
		
		for(int i = getIndexIntroduction();i<getIndexConclusion();i++) {
			r+=line[i]+" ";
		}
		Corps.corpsTxt = r;
	}

	public static String getTxt() {
		return txt;
	}

	public static void setTxt(String txt) {
		Corps.txt = txt;
	}

	public static int getIndexIntroduction() {
		return indexIntroduction;
	}

	public static void setIndexIntroduction(int index) {
		Corps.indexIntroduction = index;
	}

	public static int getIndexConclusion() {
		return indexConclusion;
	}

	public static void setIndexConclusion(int indexConclusion) {
		Corps.indexConclusion = indexConclusion;
	}
	
	
}