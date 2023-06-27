package webui.dto.enumration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	R_ESRGAN_4X_PLUS("R-ESRGAN 4x+"), 
//	LDSR("LDSR"), 
	_4X_ULTRASHARP("4x-UltraSharp"), 
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

	public static Rescale getByUpscaler(String upscaler) {
		for (Rescale rescale : values()) {
			if (rescale.getRescaleDesc().equals(upscaler))
				return rescale;
		}
		log.error("Rescale: {}", upscaler);
		return Rescale.None;
	}

}
