package parse;

public class Conclusion {
	private String concluParse;
	private String texte;
	
	public Conclusion(String txt) {
		texte=txt;
		setConcluParse(parseConclusion());
	}
	public int getIndexDebutConclu() {
		String[] line = texte.split("\n");
		for(int i=0;i<line.length;i++) {
			if(line[i].toUpperCase().contains("CONCLUSION") && (line[i].split(" ")[0].matches("\\d+")||line[i].split(" ")[0].matches("[I-X]+\\."))) {
				return i;
			}
		}
		return 0;
	}
	public int getIndexFinConclu() {
		String[] line = texte.split("\n");
		int debut = getIndexDebutConclu()+1;
		for(int i=debut;i<line.length;i++) {
			if(line[i].toUpperCase().startsWith("ACKNOWLEDGEMENT") || line[i].toUpperCase().startsWith("ACKNOWLEDGMENT") || line[i].toUpperCase().startsWith("REFERENCE") 
				 || line[i].matches("[0-9] ([A-Z]|[a-z]|-| )+")){
				return i;
			}
		}
		return 0;
	}
	public String parseConclusion() {
		String conclu="";
		String[] line = texte.split("\n");
		
		int debut = getIndexDebutConclu();
		int fin = getIndexFinConclu();
		for(int i=debut+1;i<fin;i++) {
			if(debut==0)break;
			conclu+="			"+line[i]+"\n";
		}
		return conclu;
		
	}
	public String getConcluParse() {
		return concluParse;
	}

	public void setConcluParse(String concluParse) {
		this.concluParse = concluParse;
	}

}
