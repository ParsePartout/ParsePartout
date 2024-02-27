package parse;

public class Introduction {
	
	private String introduction;
	private int endIndex;
	
	public Introduction(String texte) {
		setIntroduction(parseIntroduction(texte));
	}
	
	public String parseIntroduction(String texte) {
		String[] lines = texte.split("\n");
		for(int i=0; i<lines.length; i++) {
			if(lines[i].toUpperCase().contains("INTRODUCTION")) {
				endIndex = i;
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

	public int getEndIndex() {
		return endIndex;
	}
}
