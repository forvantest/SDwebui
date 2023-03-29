package vam.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import vam.dto.enumration.CheckPoint;

@Slf4j
@JsonInclude(Include.NON_NULL)
@Data
public class PlayRecordDTO implements Comparable {

	public PlayRecordDTO() {
		super();

		// default lora
		{
			loRA = false;
			loraDTOList.add(new LoraDTO("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)"));
			loraDTOList.add(new LoraDTO());
			loraDTOList.add(new LoraDTO());
			loraDTOList.add(new LoraDTO());
			loraDTOList.add(new LoraDTO());
		}

	}

	private String key = "";
	private CheckPoint checkPoint = null;
	private String prompt = "1boy, 1girl, arms behind back,  hetero, vaginal, sex, mating press, folded, missionary, head on pillow, spread legs, m_legs, navel, skirt lift, blush, solo_focus, long hair, black_hair, large penis, pussy, sweat, porn, blurry, blurry_background, depth_of_field, pov, lips, open mouth, tongue out, tongue, ahegao, rolling eyes, half-closed eyes, <lora:ahegaoRollingEyes_v1114:0.3>, aegyo sal, lying, on bed, on back, cum, bukkake, facial, cum in pussy, cum on body, overflow, drooling, puffy nipples, male pubic hair, thighhighs, open shirt, school uniform, best quality, masterpiece, realistic, photorealistic, 8K, RAW photo, ultra highres, ((smooth thighs))";
	private String negative_prompt = "(bad-artist:0.7), (worst quality, low quality:1.4), anal, (closed eyes: 1.4), bad-hands-5, mole, skin spots, acnes, skin blemishes, age spot";

	private Integer seed = -1;
	private Integer n_iter = -1;

	private Integer steps = 25;
	private Integer cfg_scale = 7;
	private Integer width = 768;
	private Integer height = 1024;

	private String sampler_name = "Euler a";
	public Boolean loRA = false;
	public Boolean sepeNet = false;

	private Double clipArt = 0.9;
	private Integer clipStep = 5;
	private String learnRatio = "0.0001";
	public Boolean ballDiff = false;
	private String embedding = "None";
	private String embeddingDesc = "";
	private Double ballAngle = 0.1;

	private String filename;

	private List<LoraDTO> loraDTOList = new ArrayList<>();

	@Override
	public int compareTo(Object o) {
		PlayRecordDTO playRecordDTO = (PlayRecordDTO) o;
		return seed - playRecordDTO.getSeed();
	}

}
