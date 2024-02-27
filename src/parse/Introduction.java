package parse;

public class Introduction {
	
	private String introduction;
	
	public Introduction(String texte) {
		setIntroduction(parseIntroduction(texte));
	}
	
	public String parseIntroduction(String texte) {
		String[] lines = texte.split("\n");
		for(int i=0; i<lines.length; i++) {
			if(lines[i].toUpperCase().contains("INTRODUCTION")) {
				return lines[i+1];
			}
		}
		return null;
	}
	
	public String getIntroduction() {
		return introduction;
	}
	
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}
