package parse;

public class Discussion {
	private String discuParse;
	private String texte;
	
	public Discussion(String txt) {
		texte=txt;
		setDiscuParse(parseDiscussion());
	}
	public int getIndexDebutDiscu() {
		String[] line = texte.split("\n");
		for(int i=0;i<line.length;i++) {
			if(line[i].toUpperCase().contains("DISCUSSION") && (line[i].split(" ")[0].matches("[0-9]+.?")||line[i].split(" ")[0].matches("[I-X]+\\."))) {
				return i;
			}
		}
		return 0;
	}
	public int getIndexFinDiscu() {
		String[] line = texte.split("\n");
		int debut = getIndexDebutDiscu()+1;
		for(int i=debut;i<line.length;i++) {
			if(line[i].toUpperCase().startsWith("ACKNOWLEDGEMENT") || line[i].toUpperCase().startsWith("ACKNOWLEDGMENT") || line[i].toUpperCase().startsWith("APPENDIX")
				 || line[i].toUpperCase().startsWith("REFERENCE") 
				 || line[i].matches("[0-9] ([A-Z]|[a-z]|-| )+") || line[i].matches("[I-X]+\\.([A-Z]|[a-z]|-| )+")){
				return i;
			}
		}
		return 0;
	}
	public String parseDiscussion() {
		String discu="";
		String[] line = texte.split("\n");
		
		int debut = getIndexDebutDiscu();
		int fin = getIndexFinDiscu();
		for(int i=debut+1;i<fin;i++) {
			if(debut==0)break;
			discu+="			"+line[i]+"\n";
		}
		return discu;
		
	}
	public String getDiscuParse() {
		return discuParse;
	}

	public void setDiscuParse(String discuParse) {
		this.discuParse = discuParse;
	}

}
