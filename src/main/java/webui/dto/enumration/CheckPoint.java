package webui.dto.enumration;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum CheckPoint {
	// @formatter:off
	_2GUOFENG2_V20(14, "2Guofeng2_v20.safetensors",CheckPointType.LEGEND),
	 _3GUOFENG3_V32LIGHT(0, "3Guofeng3_v32Light.safetensors",CheckPointType.LEGEND),
	 _3Guofeng3_v33(0, "3Guofeng3_v33.safetensors",CheckPointType.LEGEND), 
	_3MOONNIREAL_3MOONNIREALV2(4, "3moonNIReal_3moonNIRealV2.safetensors",CheckPointType.NORMAL), // very real, 
	ABYSSORANGEMIX2_HARD(0, "abyssorangemix2_Hard.safetensors",CheckPointType.CARTOON), // cartoon
	ABYSSORANGEMIX3AOM3_AOM3A1B(0, "abyssorangemix3AOM3_aom3a1b.safetensors",CheckPointType.CARTOON), // cartoon
	BASIL_MIX_FIXED(31, "Basil_mix_fixed.safetensors",CheckPointType.LEGEND_BLACKWHITE), 
	BEAUTYPROMIX_V1(32, "beautypromix_v1.safetensors",CheckPointType.NORMAL),
	BLEND2SEXY_BTSFP16(15, "blend2sexy_btsFp16.safetensors",CheckPointType.NORMAL), // face good, but not very beautiful
	CHIKMIX_V1(3, "chikmix_V1.safetensors",CheckPointType.LEGEND), 
	CHIKMIX_V2(3, "chikmix_V2.safetensors",CheckPointType.LEGEND), 
	CHILLOUTMIX_NIPRUNEDFP32FIX(17, "chilloutmix_NiPrunedFp32Fix.safetensors",CheckPointType.LEGEND),
	CHILLOUTMIX_NI_FP16(33, "ChilloutMix-ni-fp16.safetensors",CheckPointType.LEGEND), 
	DREAMFUL_V10LIGHT(15, "dreamful_v10Light.safetensors",CheckPointType.CARTOON_FAILED), // face good, but too many failed
	FACEBOMBMIX_V1BAKEDVAE(15, "facebombmix_v1Bakedvae.safetensors",CheckPointType.CARTOON_BETTER), // face good 
	FANTASTICMIXREAL_V10(13, "fantasticmixReal_v10.safetensors",CheckPointType.LEGEND_BLACKWHITE), // face good, but too many failed
	FINAL_PRUNE(99, "final-prune.ckpt",CheckPointType.CARTOON),
	//GOODASIANGIRLFACE_GOODASIANGIRLFACEV12(34, "goodAsianGirlFace_goodAsianGirlFaceV12.safetensors",CheckPointType.NONE),// fat face
	HENMIXREAL_V10(35, "henmixReal_v10.safetensors",CheckPointType.NORMAL),
	KOREANSTYLE25D_KOREANSTYLE25DBAKED(12, "koreanstyle25D_koreanstyle25DBaked.safetensors",CheckPointType.CARTOON),
	KOTOSMIX_V10(12, "kotosmix_v10.safetensors",CheckPointType.CARTOON_BETTER),
	//LAZYMIXREALAMATEUR_V10(36, "lazymixRealAmateur_v10.safetensors",CheckPointType.NONE),// too ugly
	//LUCKYSTRIKEMIX_V02REALISTIC(5, "luckyStrikeMix_V02Realistic.safetensors",CheckPointType.NORMAL), // too ugly
	MEINAHENTAI_V21(5, "meinahentai_v21.safetensors",CheckPointType.CARTOON), 
	NEVERENDINGDREAMNED_BAKEDVAE(14, "neverendingDreamNED_bakedVae.safetensors",CheckPointType.CARTOON_BETTER),
	NEWMARSMIX_N(0, "newmarsmix_N.safetensors",CheckPointType.LOST_COLOR_LORA), // not good enough
	PERFECTWORLD_V1(37, "perfectWorld_v1.safetensors",CheckPointType.LOST_COLOR_LORA), // real, not good enough
	//PERFECTWORLD_V1BAKED(6, "perfectWorld_v1Baked.safetensors",CheckPointType.NONE),
	PERFECTWORLD_V2BAKED(7, "perfectWorld_v2Baked.safetensors",CheckPointType.NORMAL),
	//PFG_111SAFETENSORS(38, "pfg_111Safetensors.safetensors",CheckPointType.CARTOON), 
	REALDOSMIX_(8, "realdosmix_.safetensors",CheckPointType.CHILDREN), // age under 12
	REALISTICASIADOLLPEEING_V10(9, "realisticAsiaDollPeeing_v10.ckpt",CheckPointType.NORMAL), // real, not good enough
	SUNSHINEMIX_SUNLIGHTMIXPRUNED(11, "sunshinemix_sunlightmixPruned.safetensors",CheckPointType.CARTOON), // cartoon
	UBERREALISTICPORNMERGE_URPMV13(10, "uberRealisticPornMerge_urpmv13.safetensors",CheckPointType.ENGLISH),
	V08_V08(1, "V08_V08.safetensors",CheckPointType.LEGEND),
	V08_V08A(1, "V08_V08a.safetensors",CheckPointType.LEGEND), 
	YESMIX_V16ORIGINAL(4, "yesmix_v16Original.safetensors",CheckPointType.CARTOON),
	YORRRLMIX_V21(4, "yorrrlmix_v21.safetensors",CheckPointType.CARTOON),;

	
	private Integer rank;

	private String filename;
	
	private CheckPointType checkPointType;

	CheckPoint(Integer rank, String filename,CheckPointType checkPointType) {
		this.rank = rank;
		this.filename = filename;
		this.checkPointType=checkPointType;
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
				CheckPoint.PERFECTWORLD_V2BAKED,
				CheckPoint._3MOONNIREAL_3MOONNIREALV2));
	}
	
	public static Set<CheckPoint> getBad() {
		return new LinkedHashSet<>(Arrays.asList(
				CheckPoint.BASIL_MIX_FIXED, 
				CheckPoint.BEAUTYPROMIX_V1,
				CheckPoint.HENMIXREAL_V10,
				CheckPoint.KOREANSTYLE25D_KOREANSTYLE25DBAKED,
				CheckPoint.NEVERENDINGDREAMNED_BAKEDVAE,
				CheckPoint.NEWMARSMIX_N,
				CheckPoint.REALDOSMIX_));
	}
	
	public static Set<CheckPoint> getCartoon() {
		return new LinkedHashSet<>(Arrays.asList(
				CheckPoint.ABYSSORANGEMIX2_HARD, 
				CheckPoint.SUNSHINEMIX_SUNLIGHTMIXPRUNED,
				CheckPoint.YORRRLMIX_V21));
	}
	// @formatter:on

	public static CheckPoint[] getSortedValues() {
		return Stream.of(values()).sorted((o1, o2) -> {
			return o1.getRank().compareTo(o2.getRank());
		}).toArray(CheckPoint[]::new);
	}

	public CheckPointType getCheckPointType() {
		return checkPointType;
	}

	public void setCheckPointType(CheckPointType checkPointType) {
		this.checkPointType = checkPointType;
	}

	public static Set<CheckPoint> getBy(CheckPointType checkPointType) {
		Set<CheckPoint> set = new LinkedHashSet<>();
		for (CheckPoint checkPoint : values()) {
			if (checkPointType == checkPoint.getCheckPointType())
				set.add(checkPoint);
		}
		return set;
	}

	public static CheckPoint findByFilename(String filename2) {
		for (CheckPoint checkPoint : CheckPoint.values()) {
			if (checkPoint.getFilename().startsWith(filename2))
				return checkPoint;
		}
		log.warn("---failed match CheckPoint:{} " , filename2);
		return null;
	}

}
