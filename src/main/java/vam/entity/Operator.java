package vam.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "operator")
public class Operator implements Serializable, Comparable {

	private String key = "";
	
	private String checkPoint = null;
	
	private Integer batch = 1;
	private Integer batch_amount = 1;

	private String prompt = "1boy, 1girl, arms behind back,  hetero, vaginal, sex, mating press, folded, missionary, head on pillow, spread legs, m_legs, navel, skirt lift, blush, solo_focus, long hair, black_hair, large penis, pussy, sweat, porn, blurry, blurry_background, depth_of_field, pov, lips, open mouth, tongue out, tongue, ahegao, rolling eyes, half-closed eyes, <lora:ahegaoRollingEyes_v1114:0.3>, aegyo sal, lying, on bed, on back, cum, bukkake, facial, cum in pussy, cum on body, overflow, drooling, puffy nipples, male pubic hair, thighhighs, open shirt, school uniform, best quality, masterpiece, realistic, photorealistic, 8K, RAW photo, ultra highres, ((smooth thighs))";
	private String negative_prompt = "(bad-artist:0.7), (worst quality, low quality:1.4), anal, (closed eyes: 1.4), bad-hands-5, mole, skin spots, acnes, skin blemishes, age spot";

	private Integer seed = -1;
	private Integer batch_size = 1;
	private Integer n_iter = 1;

	private Integer steps = 25;
	private Integer cfg_scale = 7;
	private Integer width = 768;
	private Integer height = 1024;

	private String sampler_name = "Euler a";
	public Boolean loRA = true;
	public Boolean sepeNet = false;

	private List<String> loraList = new ArrayList<>();

	private String filename = "";
	@Override
	public int compareTo(Object o) {
		Operator operatorDTO = (Operator) o;
		return batch - operatorDTO.getBatch();
	}

}
