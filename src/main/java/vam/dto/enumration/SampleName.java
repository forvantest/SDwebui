package vam.dto.enumration;

public enum SampleName {
	EULER_A("Euler a"), 
	EULER("Euler"), 
	LMS("LMS"), 
	Heun("Heun"), 
	DPM2("DPM2"), 
	DPM2_A("DPM2 a"),
	DPM_PLUS_2S_A("DPM++ 2S a"), 
	DPM_PLUS_2M("DPM++ 2M"), 
	DPM_PLUS_SDE("DPM++ SDE"), 
	DPM_FAST("DPM fast"),
	DPM_ADAPTIVE("DPM adaptive"), 
	LMS_KARRAS("LMS Karras"), 
	DPM2_KARRAS("DPM2 Karras"), 
	DPM2_A_KARRAS("DPM2 a Karras"),
	DPM_PLUS_2S_A_KARRAS("DPM++ 2S a Karras"), 
	DPM_PLUS_2M_KARRAS("DPM++ 2M Karras"),
	DPM_PLUS_SDE_KARRAS("DPM++ SDE Karras"), 
	DDIM("DDIM"), 
	PLMS("PLMS"), 
	UniPC("UniPC"),;

	private String opCode;

	SampleName(String opCode) {
		this.opCode = opCode;
	}

	public String getOpCode() {
		return opCode;
	}

}
