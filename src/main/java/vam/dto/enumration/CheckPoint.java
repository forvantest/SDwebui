package vam.dto.enumration;

import java.util.stream.Stream;

public enum CheckPoint {
	_2GUOFENG2_V20(14, "2Guofeng2_v20.safetensors"), 
	//_3GUOFENG3_V32LIGHT(2, "3Guofeng3_v32Light.safetensors"), // not very good
	//_3MOONNIREAL_3MOONNIREALV2(4, "3moonNIReal_3moonNIRealV2.safetensors"),
	// ABYSSORANGEMIX2_HARD(0,"abyssorangemix2_Hard.safetensors"),//cartoon
	// BASIL_MIX_FIXED("Basil_mix_fixed.safetensors"),
	// BEAUTYPROMIX_V1("beautypromix_v1.safetensors"),
	BLEND2SEXY_BTSFP16(0,"blend2sexy_btsFp16.safetensors"),
	CHIKMIX_V2(3, "chikmix_V2.safetensors"),
	// CHILLOUTMIX_NIPRUNEDFP32FIX("chilloutmix_NiPrunedFp32Fix.safetensors"),
	// CHILLOUTMIX_NI_FP16("ChilloutMix-ni-fp16.safetensors"),
	//DREAMFUL_V10LIGHT(15, "dreamful_v10Light.safetensors"), // face good, but too many failed
	FANTASTICMIXREAL_V10(13, "fantasticmixReal_v10.safetensors"), // face good, but too many failed
	// FINAL_PRUNE("final-prune.ckpt"),
	// GOODASIANGIRLFACE_GOODASIANGIRLFACEV12("goodAsianGirlFace_goodAsianGirlFaceV12.safetensors"),
	// HENMIXREAL_V10("henmixReal_v10.safetensors"),
	KOREANSTYLE25D_KOREANSTYLE25DBAKED(12, "koreanstyle25D_koreanstyle25DBaked.safetensors"),
	// LAZYMIXREALAMATEUR_V10("lazymixRealAmateur_v10.safetensors"),
	// LUCKYSTRIKEMIX_V02REALISTIC(5,
	// "luckyStrikeMix_V02Realistic.safetensors"),//too ugly
	//NEVERENDINGDREAMNED_BAKEDVAE(14, "neverendingDreamNED_bakedVae.safetensors"),
	// NEWMARSMIX_N(0, "newmarsmix_N.safetensors"), // not good enough
	// PERFECTWORLD_V1("perfectWorld_v1.safetensors"), //real, not good enough
	//PERFECTWORLD_V1BAKED(6, "perfectWorld_v1Baked.safetensors"),
	PERFECTWORLD_V2BAKED(7, "perfectWorld_v2Baked.safetensors"),
	// PFG_111SAFETENSORS("pfg_111Safetensors.safetensors"),
	REALDOSMIX_(8, "realdosmix_.safetensors"), // age under 12
	// REALISTICASIADOLLPEEING_V10(9, "realisticAsiaDollPeeing_v10.ckpt"),//real,
	// not good enough

	SUNSHINEMIX_SUNLIGHTMIXPRUNED(11, "sunshinemix_sunlightmixPruned.safetensors"), // cartoon
	// UBERREALISTICPORNMERGE_URPMV13(10,
	// "uberRealisticPornMerge_urpmv13.safetensors"),

	V08_V08(1, "V08_V08.safetensors"), 
	YORRRLMIX_V21(4, "yorrrlmix_v21.safetensors"),;

	private Integer rank;

	private String filename;

	CheckPoint(Integer rank, String filename) {
		this.rank = rank;
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getRank() {
		return rank;
	}

	public static CheckPoint[] getSortedValues() {
		return Stream.of(values()).sorted((o1, o2) -> {
			return o1.getRank().compareTo(o2.getRank());
		}).toArray(CheckPoint[]::new);
	}
}
