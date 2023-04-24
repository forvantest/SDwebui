package webui.dto.enumration;

import org.springframework.util.StringUtils;

public enum TextualInversion {
	PUREEROSFACE_V1("PureErosFace_V1.pt"), NONE(""), ULZZANG("ulzzang-6500-v1.1"),
	CORNEO_SIDE_DOGGY("corneo_side_doggy"), CORNEO_COWGIRL("corneo_cowgirl"),;

	private String filename;

	private Float weight;

	private Float weightStart;

	private Float weightEnd;

	TextualInversion(String filename) {
		this.filename = filename;
	}

	public String appendTextualInversion() {
		if (this == TextualInversion.NONE)
			return "";

		String textualInversionName = StringUtils.replace(filename, ".pt", "");
		String textualInversionWeight = String.format(",<%s:%.1f>", textualInversionName, weight);
		return textualInversionWeight;
	}
	
	public String appendTextualInversionPT() {
		if (this == TextualInversion.NONE)
			return "";

		String textualInversionName = StringUtils.replace(filename, ".pt", "");
		String textualInversionWeight = String.format(",(%s:%.1f)", textualInversionName, weight);
		return textualInversionWeight;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getWeightStart() {
		return weightStart;
	}

	public void setWeightStart(Float weightStart) {
		this.weightStart = weightStart;
	}

	public Float getWeightEnd() {
		return weightEnd;
	}

	public void setWeightEnd(Float weightEnd) {
		this.weightEnd = weightEnd;
	}

	public TextualInversion initWeight(float weightStart, float weightEnd) {
		this.weightStart = weightStart;
		this.weight = weightStart;
		this.weightEnd = weightEnd;
		return this;
	}

	public TextualInversion initWeight(float f) {
		this.weight = f;
		return this;
	}
}
