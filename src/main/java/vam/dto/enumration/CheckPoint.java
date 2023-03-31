package vam.dto.enumration;

public enum CheckPoint {
	V08_V08("V08_V08.safetensors"), 
	_3GUOFENG3_V32LIGHT("3Guofeng3_v32Light.safetensors"), 
	CHIKMIX_V2("chikmix_V2.safetensors"),
	BEAUTYPROMIX_V1("beautypromix_v1.safetensors"), 
	_3MOONNIREAL_3MOONNIREALV2("3moonNIReal_3moonNIRealV2.safetensors"),
	BASIL_MIX_FIXED("Basil_mix_fixed.safetensors"),
	CHILLOUTMIX_NIPRUNEDFP32FIX("chilloutmix_NiPrunedFp32Fix.safetensors"),
	CHILLOUTMIX_NI_FP16("ChilloutMix-ni-fp16.safetensors"), 
	DREAMFUL_V10LIGHT("dreamful_v10Light.safetensors"),
	FANTASTICMIXREAL_V10("fantasticmixReal_v10.safetensors"), 
	FINAL_PRUNE("final-prune.ckpt"),
	GOODASIANGIRLFACE_GOODASIANGIRLFACEV12("goodAsianGirlFace_goodAsianGirlFaceV12.safetensors"),
	HENMIXREAL_V10("henmixReal_v10.safetensors"), 
	LAZYMIXREALAMATEUR_V10("lazymixRealAmateur_v10.safetensors"),
	LUCKYSTRIKEMIX_V02REALISTIC("luckyStrikeMix_V02Realistic.safetensors"),
	PERFECTWORLD_V1("perfectWorld_v1.safetensors"), 
	PERFECTWORLD_V1BAKED("perfectWorld_v1Baked.safetensors"),
	PERFECTWORLD_V2BAKED("perfectWorld_v2Baked.safetensors"), 
	PFG_111SAFETENSORS("pfg_111Safetensors.safetensors"),
	REALDOSMIX_("realdosmix_.safetensors"), 
	REALISTICASIADOLLPEEING_V10("realisticAsiaDollPeeing_v10.ckpt"),
	UBERREALISTICPORNMERGE_URPMV13("uberRealisticPornMerge_urpmv13.safetensors"),
	SUNSHINEMIX_SUNLIGHTMIXPRUNED("sunshinemix_sunlightmixPruned.safetensors");

	private String filename;

	CheckPoint(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

}
