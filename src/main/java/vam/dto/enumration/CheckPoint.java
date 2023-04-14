package vam.dto.enumration;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

public enum CheckPoint {
	// @formatter:off
	_2GUOFENG2_V20(14, "2Guofeng2_v20.safetensors"),
	 _3GUOFENG3_V32LIGHT(0, "3Guofeng3_v32Light.safetensors"),
	 _3Guofeng3_v33(0, "3Guofeng3_v33.safetensors"), 
	_3MOONNIREAL_3MOONNIREALV2(4, "3moonNIReal_3moonNIRealV2.safetensors"), // real
	ABYSSORANGEMIX2_HARD(0, "abyssorangemix2_Hard.safetensors"), // cartoon
	BASIL_MIX_FIXED(31, "Basil_mix_fixed.safetensors"), 
	BEAUTYPROMIX_V1(32, "beautypromix_v1.safetensors"),
	BLEND2SEXY_BTSFP16(15, "blend2sexy_btsFp16.safetensors"), // face good, but too many failed
	CHIKMIX_V1(3, "chikmix_V1.safetensors"), 
	CHIKMIX_V2(3, "chikmix_V2.safetensors"), 
	CHILLOUTMIX_NIPRUNEDFP32FIX(17, "chilloutmix_NiPrunedFp32Fix.safetensors"),
	CHILLOUTMIX_NI_FP16(33, "ChilloutMix-ni-fp16.safetensors"), 
	DREAMFUL_V10LIGHT(15, "dreamful_v10Light.safetensors"), // face good, but too many failed
	FANTASTICMIXREAL_V10(13, "fantasticmixReal_v10.safetensors"), // face good, but too many failed
	FINAL_PRUNE(99, "final-prune.ckpt"),
	GOODASIANGIRLFACE_GOODASIANGIRLFACEV12(34, "goodAsianGirlFace_goodAsianGirlFaceV12.safetensors"),
	HENMIXREAL_V10(35, "henmixReal_v10.safetensors"),
	KOREANSTYLE25D_KOREANSTYLE25DBAKED(12, "koreanstyle25D_koreanstyle25DBaked.safetensors"),
	LAZYMIXREALAMATEUR_V10(36, "lazymixRealAmateur_v10.safetensors"),
	LUCKYSTRIKEMIX_V02REALISTIC(5, "luckyStrikeMix_V02Realistic.safetensors"), // too ugly
	NEVERENDINGDREAMNED_BAKEDVAE(14, "neverendingDreamNED_bakedVae.safetensors"),
	NEWMARSMIX_N(0, "newmarsmix_N.safetensors"), // not good enough
	PERFECTWORLD_V1(37, "perfectWorld_v1.safetensors"), // real, not good enough
	PERFECTWORLD_V1BAKED(6, "perfectWorld_v1Baked.safetensors"),
	PERFECTWORLD_V2BAKED(7, "perfectWorld_v2Baked.safetensors"),
	PFG_111SAFETENSORS(38, "pfg_111Safetensors.safetensors"), 
	REALDOSMIX_(8, "realdosmix_.safetensors"), // age under 12
	REALISTICASIADOLLPEEING_V10(9, "realisticAsiaDollPeeing_v10.ckpt"), // real, not good enough
	SUNSHINEMIX_SUNLIGHTMIXPRUNED(11, "sunshinemix_sunlightmixPruned.safetensors"), // cartoon
	UBERREALISTICPORNMERGE_URPMV13(10, "uberRealisticPornMerge_urpmv13.safetensors"),
	V08_V08(1, "V08_V08.safetensors"),
	V08_V08A(1, "V08_V08a.safetensors"), 
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

	public static Set<CheckPoint> getReal() {
		return new LinkedHashSet<>(Arrays.asList(
				CheckPoint.V08_V08, 
				CheckPoint.V08_V08A,
				CheckPoint._2GUOFENG2_V20,
				CheckPoint._3GUOFENG3_V32LIGHT,
				CheckPoint._3Guofeng3_v33,
				CheckPoint.CHIKMIX_V1,
				CheckPoint.CHIKMIX_V2,
				CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX,
				CheckPoint.CHILLOUTMIX_NI_FP16,
				CheckPoint.UBERREALISTICPORNMERGE_URPMV13));
	}
	
	public static Set<CheckPoint> getNormal() {
		return new LinkedHashSet<>(Arrays.asList(
				CheckPoint.DREAMFUL_V10LIGHT, 
				CheckPoint.FANTASTICMIXREAL_V10,
				CheckPoint.BLEND2SEXY_BTSFP16,
				CheckPoint.REALISTICASIADOLLPEEING_V10,
				CheckPoint.PERFECTWORLD_V1,
				CheckPoint.PERFECTWORLD_V1BAKED,
				CheckPoint.PERFECTWORLD_V2BAKED,
				CheckPoint._3MOONNIREAL_3MOONNIREALV2));
	}
	
	public static Set<CheckPoint> getBad() {
		return new LinkedHashSet<>(Arrays.asList(
				CheckPoint.BASIL_MIX_FIXED, 
				CheckPoint.BEAUTYPROMIX_V1,
				CheckPoint.GOODASIANGIRLFACE_GOODASIANGIRLFACEV12,
				CheckPoint.HENMIXREAL_V10,
				CheckPoint.KOREANSTYLE25D_KOREANSTYLE25DBAKED,
				CheckPoint.LAZYMIXREALAMATEUR_V10,
				CheckPoint.LUCKYSTRIKEMIX_V02REALISTIC,
				CheckPoint.NEVERENDINGDREAMNED_BAKEDVAE,
				CheckPoint.NEWMARSMIX_N,
				CheckPoint.PFG_111SAFETENSORS,
				CheckPoint.REALDOSMIX_,
				CheckPoint.LUCKYSTRIKEMIX_V02REALISTIC));
	}
	
	public static Set<CheckPoint> getCartoon() {
		return new LinkedHashSet<>(Arrays.asList(
				CheckPoint.ABYSSORANGEMIX2_HARD, 
				CheckPoint.SUNSHINEMIX_SUNLIGHTMIXPRUNED,
				CheckPoint.YORRRLMIX_V21));
	}
	
	public static CheckPoint[] getSortedValues() {
		return Stream.of(values()).sorted((o1, o2) -> {
			return o1.getRank().compareTo(o2.getRank());
		}).toArray(CheckPoint[]::new);
	}
	// @formatter:on
}
