package vam.dto.enumration;

public enum Prompt {
	PORN_M_LEG(
			"1boy, 1girl, arms behind back,  hetero, vaginal, sex, mating press, folded, missionary, head on pillow, spread legs, m_legs, navel, skirt lift, blush, solo_focus, long hair, black_hair, large penis, pussy, sweat, porn, blurry, blurry_background, depth_of_field, pov, lips, open mouth, tongue out, tongue, ahegao, rolling eyes, half-closed eyes, <lora:ahegaoRollingEyes_v1114:0.3>, aegyo sal, lying, on bed, on back, cum, bukkake, facial, cum in pussy, cum on body, overflow, drooling, puffy nipples, male pubic hair, thighhighs, open shirt, school uniform, best quality, masterpiece, realistic, photorealistic, 8K, RAW photo, ultra highres, ((smooth thighs))",
			"(bad-artist:0.7), (worst quality, low quality:1.4), anal, (closed eyes: 1.4), bad-hands-5, mole, skin spots, acnes, skin blemishes, age spot"),;

	private String positive;
	private String negative;

	Prompt(String positive, String negative) {
		this.positive = positive;
		this.negative = negative;
	}

	public String getPositive() {
		return positive;
	}

	public void setPositive(String positive) {
		this.positive = positive;
	}

	public String getNegative() {
		return negative;
	}

	public void setNegative(String negative) {
		this.negative = negative;
	}

}
