package webui.dto.enumration;

public enum Rescale {
	// @formatter:off
	LATENT("Latent"), 
	LATENT_ANTIALIASED("Latent (antialiased)"),
	LATENT_BICUBIC("Latent (bicubic)"),
	LATENT_BICUBIC_ANTIALIASED("Latent (bicubic antialiased)"), 
	LATENT_NEAREST("Latent (nearest)"), 
	LATENT_NEAREST_EXACT("Latent (nearest-exact)"), 
	None("None"), 
	LANCZOS("Lanczos"), 
	NEAREST("Nearest"), 
	ESRGAN_4X("ESRGAN_4x"), 
//	LDSR("LDSR"), 
	SWINIR_4X("SwinIR_4x"), 
	;


	
	private String rescaleDesc;

	// @formatter:on

	Rescale(String rescaleDesc) {
		this.rescaleDesc = rescaleDesc;
	}

	public String getRescaleDesc() {
		return rescaleDesc;
	}

	public void setRescaleDesc(String rescaleDesc) {
		this.rescaleDesc = rescaleDesc;
	}

}
