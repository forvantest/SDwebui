package webui.dto.enumration;

import org.springframework.util.StringUtils;

public enum TextualInversion  {
	PUREEROSFACE_V1( "PureErosFace_V1.pt"),
	NONE( ""),
	;

	private String filename;
	
	private Float weight;

	TextualInversion( String filename) {
		this.filename = filename;
	}

	public String appendTextualInversion() {
		if(this==TextualInversion.NONE)
			return "";
		
		String textualInversionName=StringUtils.replace(filename, ".pt", "");
		String textualInversionWeight=String.format(",(%s:%.1f)", textualInversionName,weight); 
		return textualInversionWeight;
	}
	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}
}
