package vam.dto.enumration;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

public enum Lora {
	// @formatter:off
	_3LORAGUOFENG3LORA_V32LORABIGLIGHT("3loraGuofeng3Lora_v32LoraBigLight.safetensors",LoraType.CHECK_POINT),
	AHEGAOROLLINGEYES_V1114("ahegaoRollingEyes_v1114.safetensors",LoraType.BETTER),
//	AIAVYUAXX_V1("aiAVYUAXx_v1.safetensors",LoraType.AV),
	ANGELABABY_1("angelababy_1.safetensors",LoraType.AV),
	ASIAGIRLINUNIFORM_CHILLOUTMIX("asiaGirlInUniform_chilloutmix.safetensors",LoraType.AV),
	ASIANGIRLSABRINALORA_NOTUPDATE("asianGirlSabrinaLora_notUpdate.safetensors",LoraType.AV),
	ASIANGIRLXYBOBOLORA_NOTUPDATE("asianGirlXYBOBOLora_notUpdate.safetensors",LoraType.AV),
	ASIANGIRLZHAOXMLORA_NOTUPDATE("asianGirlZhaoxmLora_notUpdate.safetensors",LoraType.AV),
	AVSUZUMURAAJP_V10("avSuzumuraaJP_v10.safetensors",LoraType.AV),
	BREASTINCLASSBETTER_V141("breastinclassBetter_v141.safetensors",LoraType.BETTER),
	CHILLOUTMIXSS_XSS10("chilloutmixss_xss10.safetensors",LoraType.CHECK_POINT),
	CREAMPIEHAIRYPUSSY_CREAMPIEV11("creampieHairyPussy_creampieV11.safetensors",LoraType.PUSSY),
	CUTEGIRLMIX4_V10("cuteGirlMix4_v10.safetensors",LoraType.BEAUTY),
	CUTEKOREANGIRLLORA_CUTEKOREANGIRLLORA("cuteKoreanGirlLora_cuteKoreanGirlLora.safetensors",LoraType.COUNTRY_IDOLL),
	
	EASTASIANDOLL_V40("eastasianDoll_v40.safetensors",LoraType.COUNTRY_IDOLL),
	FASHIONGIRL_V51("fashionGirl_v51.safetensors",LoraType.BETTER),
	//FRAMEBINDER_V10("frameBinder_v10.safetensors",LoraType.NONE), 
	GANYUGENSHINIMPACT_OFFSET("ganyuGenshinImpact_offset.safetensors",LoraType.BETTER),
	HIPOLY3DMODELLORA_V10("hipoly3DModelLora_v10.safetensors",LoraType.BETTER),
	JAPANESE_DOLL_LIKENESS("Japanese-doll-likeness.safetensors",LoraType.COUNTRY_IDOLL),
	KBEAUKOREANBEAUTY_V15("kbeauKoreanBeauty_v15.safetensors",LoraType.COUNTRY_IDOLL),
	KOREAN_BEAUTIFULGIRL("korean_beautifulGirl.safetensors",LoraType.COUNTRY_IDOLL), 
	KOREAN_DOLL_LIKENESS("Korean-doll-likeness.safetensors",LoraType.COUNTRY_IDOLL),
	KOREANDOLLLIKENESS_V15("koreanDollLikeness_v15.safetensors",LoraType.COUNTRY_IDOLL),
	KOREANDOLLLIKENESS_V10066("koreandolllikeness_V10066.safetensors",LoraType.COUNTRY_IDOLL),
	KOREANDOLLLIKENESSV20_V20("koreandolllikenessV20_v20.safetensors",LoraType.COUNTRY_IDOLL),
	KOREANGALLOCONLORA_1("koreanGalLoconLora_1.safetensors",LoraType.COUNTRY_IDOLL), 
	KOREANGIRLS_KGIRLSCC("koreanGirls_kgirlscc.safetensors",LoraType.COUNTRY_IDOLL),
	KRISWUEXGIRLFRIEND_V10("krisWuEXGirlfriend_v10.safetensors",LoraType.BETTER),
	LORAKAIGIRLFACE_KAIGIRLFACE2("LoraKAigirlFace_kAigirlFace2.safetensors",LoraType.BETTER),
	MIKUYA_V15("mikuya_v15.safetensors",LoraType.AV), 
	MINESFIXASIANLIKENESS_V10("minesfixAsianLikeness_v10.safetensors",LoraType.COUNTRY_IDOLL),
	MLEGGESTUREULTIMATE_V51("mLegGestureUltimate_v51.safetensors",LoraType.ACT),
	MURKYSAFTERSEXLYING_1("murkysAfterSexLying_1.safetensors",LoraType.ACT), 
	MURKYSCUMONTONGUE_1("murkysCumOnTongue_1.safetensors",LoraType.ACT),
	MURKYSDANGLING_1("murkysDangling_1.safetensors",LoraType.ACT),
	MURKYSSUSPENDEDCONGRESS_1("murkysSuspendedCongress_1.safetensors",LoraType.ACT),
	MURKYSSUSPENDEDON_1("murkysSuspendedOn_1.safetensors",LoraType.ACT), 
	ORGASMFACE_V10("orgasmFace_v10.safetensors",LoraType.BETTER),
	POVMISSIONARYVAGINAL_V1("povMissionaryVaginal_v1.safetensors",LoraType.ACT),
	PUSSYSPREAD_V01("pussySpread_v01.safetensors",LoraType.PUSSY),
	REALISTICVAGINASGOD_GODPUSSY1V1("realisticVaginasGod_godpussy1V1.safetensors",LoraType.PUSSY),
	REALISTICVAGINASGOD_GODPUSSY1V2("realisticVaginasGod_godpussy1V2.safetensors",LoraType.PUSSY),
	REALISTICVAGINASWET_WETPUSSYGROOL("realisticVaginasWet_wetpussygrool.safetensors",LoraType.PUSSY),
	REALSPREADPUSSY_SPPSPREADPUSSYV3("realSpreadPussy_sppSpreadpussyV3.safetensors",LoraType.PUSSY),
	REALSPREADPUSSY_SPPSPREADPUSSYWV1("realSpreadPussy_sppSpreadpussyWV1.safetensors",LoraType.PUSSY),
	SAMDOESARTSSAMYANG_OFFSET("samdoesartsSamYang_offset.safetensors",LoraType.BETTER), 
	SHOJOVIBE_V11("shojovibe_v11.safetensors",LoraType.AV),
	SOPHONJAPENESEGIRL_SOPHONV12("sophonJapeneseGirl_sophonV12.safetensors",LoraType.AV),
//	SPREADPUSSY_V11("spreadPussy_v11.safetensors",LoraType.PUSSY), 
	STANDINGDOGGYSTYLE_V11A("StandingDoggystyle_v11a.safetensors",LoraType.ACT),
	TAIWANDOLLLIKENESS_V10("taiwanDollLikeness_v10.safetensors",LoraType.COUNTRY_IDOLL),
	
	DOASHEALERANGELOF_V10("doasHealerAngelOf_v10.safetensors",LoraType.BETTER),
	NONE("",LoraType.NONE),
	;
	// @formatter:on
	private LoraType loraType;

	private String filename;

	private Float weight;

	private Float weightStart;

	private Float weightEnd;

//	private List<Float> weightList = new ArrayList<>();

	Lora(String filename, LoraType loraType) {
		this.filename = filename;
		this.loraType = loraType;
	}

	public String appendLora() {
		if (this == Lora.NONE)
			return "";

		String loraName = StringUtils.replace(filename, ".safetensors", "");
		String loraWeight = String.format(",<lora:%s:%.1f>", loraName, weight);
		return loraWeight;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Lora initWeight(float weightStart, float weightEnd) {
		this.weightStart = weightStart;
		this.weight = weightStart;
		this.weightEnd = weightEnd;
		return this;
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

	public LoraType getLoraType() {
		return loraType;
	}

	public void setLoraType(LoraType loraType) {
		this.loraType = loraType;
	}

	public static Set<Lora> getBy(LoraType loraType) {
		Set<Lora> set = new LinkedHashSet<>();
		for (Lora lora : values()) {
			if (loraType == lora.getLoraType())
				set.add(lora);
		}
		return set;
	}

}
