package webui.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "playrecord")
public class PlayRecord implements Serializable, Comparable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long key;

	private String task = "";
	private String checkPoint = null;
	private String sampler_name = "Euler a";
	private String prompt = "1boy, 1girl, arms behind back,  hetero, vaginal, sex, mating press, folded, missionary, head on pillow, spread legs, m_legs, navel, skirt lift, blush, solo_focus, long hair, black_hair, large penis, pussy, sweat, porn, blurry, blurry_background, depth_of_field, pov, lips, open mouth, tongue out, tongue, ahegao, rolling eyes, half-closed eyes, <lora:ahegaoRollingEyes_v1114:0.3>, aegyo sal, lying, on bed, on back, cum, bukkake, facial, cum in pussy, cum on body, overflow, drooling, puffy nipples, male pubic hair, thighhighs, open shirt, school uniform, best quality, masterpiece, realistic, photorealistic, 8K, RAW photo, ultra highres, ((smooth thighs))";
	private String negative_prompt = "(bad-artist:0.7), (worst quality, low quality:1.4), anal, (closed eyes: 1.4), bad-hands-5, mole, skin spots, acnes, skin blemishes, age spot";

	private Integer steps = 25;
	private Integer cfg_scale = 7;
	private Integer width = 1280;
	private Integer height = 1920;

	private Integer seed = -1;
	private Integer n_iter = -1;

	private Boolean loRA = true;
	private Boolean sepeNet = false;

	private String loraName1;
	private String loraName2;
	private String loraName3;
	private String loraName4;
	private String loraName5;

	private Double loraUNet1 = 0d;
	private Double loraUNet2 = 0d;
	private Double loraUNet3 = 0d;
	private Double loraUNet4 = 0d;
	private Double loraUNet5 = 0d;

	private Double loraTextEncoder1 = 0d;
	private Double loraTextEncoder2 = 0d;
	private Double loraTextEncoder3 = 0d;
	private Double loraTextEncoder4 = 0d;
	private Double loraTextEncoder5 = 0d;

	private Integer rank = 10;

	@Override
	public int compareTo(Object o) {
		PlayRecord playRecord = (PlayRecord) o;
		return (int) (key - playRecord.getKey());
	}

}
