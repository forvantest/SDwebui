package webui.dto.enumration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Vae {
	// @formatter:off
	FINAL_PRUNE_VAE("final-prune.vae.pt"),
	VAE_FT_EMA_560000_EMA_PRUNED("vae-ft-ema-560000-ema-pruned.ckpt"),
	VAE_FT_MSE_840000_EMA_PRUNED("vae-ft-mse-840000-ema-pruned.safetensors"),
	None("None"), 
	;

	private String vaeDesc;

	// @formatter:on

	Vae(String vaeDesc) {
		this.vaeDesc = vaeDesc;
	}

	public String getVaeDesc() {
		return vaeDesc;
	}

	public void setVaeDesc(String vaeDesc) {
		this.vaeDesc = vaeDesc;
	}

	public static Vae getByVaeDesc(String vaeDesc) {
		for (Vae vae : values()) {
			if (vaeDesc.equals(vae.vaeDesc))
				return vae;
		}
		log.error("Vae: {}", vaeDesc);
		return null;
	}

}
