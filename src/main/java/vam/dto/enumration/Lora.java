package vam.dto.enumration;

import org.springframework.util.StringUtils;

public enum Lora  {
	_3LORAGUOFENG3LORA_V32LORABIGLIGHT( "3loraGuofeng3Lora_v32LoraBigLight.safetensors"),
//	AHEGAOROLLINGEYES_V1114( "ahegaoRollingEyes_v1114.safetensors"),
	AIAVYUAXX_V1( "aiAVYUAXx_v1.safetensors"),
	ANGELABABY_1( "angelababy_1.safetensors"),
	ASIAGIRLINUNIFORM_CHILLOUTMIX( "asiaGirlInUniform_chilloutmix.safetensors"),
	ASIANGIRLSABRINALORA_NOTUPDATE( "asianGirlSabrinaLora_notUpdate.safetensors"),
	ASIANGIRLXYBOBOLORA_NOTUPDATE( "asianGirlXYBOBOLora_notUpdate.safetensors"),
	ASIANGIRLZHAOXMLORA_NOTUPDATE( "asianGirlZhaoxmLora_notUpdate.safetensors"),
	CHILLOUTMIXSS_XSS10( "chilloutmixss_xss10.safetensors"),
	CREAMPIEHAIRYPUSSY_CREAMPIEV11( "creampieHairyPussy_creampieV11.safetensors"),
	CUTEGIRLMIX4_V10( "cuteGirlMix4_v10.safetensors"),
	CUTEKOREANGIRLLORA_CUTEKOREANGIRLLORA( "cuteKoreanGirlLora_cuteKoreanGirlLora.safetensors"),
	EASTASIANDOLL_V40( "eastasianDoll_v40.safetensors"),
	FASHIONGIRL_V51( "fashionGirl_v51.safetensors"),
	FRAMEBINDER_V10( "frameBinder_v10.safetensors"),
	GANYUGENSHINIMPACT_OFFSET( "ganyuGenshinImpact_offset.safetensors"),
	HIPOLY3DMODELLORA_V10( "hipoly3DModelLora_v10.safetensors"),
	JAPANESE_DOLL_LIKENESS( "Japanese-doll-likeness.safetensors"),
	KBEAUKOREANBEAUTY_V15( "kbeauKoreanBeauty_v15.safetensors"),
	KOREAN_BEAUTIFULGIRL( "korean_beautifulGirl.safetensors"),
	KOREAN_DOLL_LIKENESS( "Korean-doll-likeness.safetensors"),
	KOREANDOLLLIKENESS_V15( "koreanDollLikeness_v15.safetensors"),
	KOREANDOLLLIKENESS_V10066( "koreandolllikeness_V10066.safetensors"),
	KOREANGALLOCONLORA_1( "koreanGalLoconLora_1.safetensors"),
	KOREANGIRLS_KGIRLSCC( "koreanGirls_kgirlscc.safetensors"),
	MIKUYA_V15( "mikuya_v15.safetensors"),
	MINESFIXASIANLIKENESS_V10( "minesfixAsianLikeness_v10.safetensors"),
	MLEGGESTUREULTIMATE_V51( "mLegGestureUltimate_v51.safetensors"),
	MURKYSAFTERSEXLYING_1( "murkysAfterSexLying_1.safetensors"),
	MURKYSCUMONTONGUE_1( "murkysCumOnTongue_1.safetensors"),
	MURKYSDANGLING_1( "murkysDangling_1.safetensors"),
	MURKYSSUSPENDEDCONGRESS_1( "murkysSuspendedCongress_1.safetensors"),
	MURKYSSUSPENDEDON_1( "murkysSuspendedOn_1.safetensors"),
	ORGASMFACE_V10( "orgasmFace_v10.safetensors"),
	SAMDOESARTSSAMYANG_OFFSET("samdoesartsSamYang_offset.safetensors"),
	SHOJOVIBE_V11( "shojovibe_v11.safetensors"),
	SOPHONJAPENESEGIRL_SOPHONV12( "sophonJapeneseGirl_sophonV12.safetensors"),
	SPREADPUSSY_V11( "spreadPussy_v11.safetensors"),
	STANDINGDOGGYSTYLE_V11A( "StandingDoggystyle_v11a.safetensors"),
	TAIWANDOLLLIKENESS_V10( "taiwanDollLikeness_v10.safetensors"),
	;

	private String filename;
	
	private Float weight;

	Lora( String filename) {
		this.filename = filename;
	}

	public String appendLora() {
		String loraName=StringUtils.replace(filename, ".safetensors", "");
		String loraWeight=String.format(",<lora:%s:%.1f>", loraName,weight); 
		return loraWeight;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}
}