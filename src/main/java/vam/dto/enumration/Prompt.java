package vam.dto.enumration;

public enum Prompt {
	PORN_M_LEG(
			"1boy, 1girl, hetero, vaginal, sex, mating press, folded, missionary, head on pillow, spread legs, m_legs, navel, skirt lift,"
					+ " blush, solo_focus, long hair, black_hair, large penis, pussy, sweat, porn, blurry, blurry_background, depth_of_field, pov,"
					+ " lips, open mouth, tongue out, tongue, ahegao, rolling eyes, half-closed eyes, <lora:ahegaoRollingEyes_v1114:0.3>, aegyo sal,"
					+ " lying, on bed, on back, cum, bukkake, facial, cum in pussy, cum on body, overflow, drooling, puffy nipples, male pubic hair,"
					+ " thighhighs, open shirt, school uniform, best quality, masterpiece, realistic, photorealistic, 8K, RAW photo, ultra highres, ((smooth thighs))",
			"(bad-artist:0.7), (worst quality, low quality:1.4), anal, (closed eyes: 1.4), bad-hands-5, mole, skin spots, acnes, skin blemishes, age spot",
			768, 1024),
	PORN_AFTER(
			"ultra realistic 8k cg, picture-perfect face, flawless, masterpiece, professional artwork, famous artwork,"
					+ " cinematic lighting, cinematic bloom, perfect face, beautiful face, beautiful eyes, ((perfect female body, narrow waist)),"
					+ "intricate detail, 1girl, (mature female:1.4), (milf:1.3), fucked silly, sweat,vaginal, pussy,spread legs, m_legs, big breasts, nsfw,"
					+ " breasts out, sexy, charming, alluring, seductive, erotic, enchanting,  high heels, business suit,  pantyhose,"
					+ " shirt, skirt, tongue, too much cum, ((cum)), ((cum on tongue)),cum pool, after fellatio, ahegao, smile, <lora:murkysCumOnTongue_1:1>,"
					+ " cum on body, cum on face, ass, anal, after sex, bukkake, lying on back, <lora:murkysAfterSexLying_1:1>",
			"(worst quality, low quality:1.3), simple background, logo, watermark, text, (fused fingers), (too many fingers),crossed fingers,"
					+ "((multiple anus:1.5)), prolapse, (low quality, worst quality:1.4),(bad_prompt_version2:0.8), (monochrome:1.1), extra fingers,blurry,"
					+ " ((bad anatomy)), (((bad proportions))),loli, child, (low quality, worst quality:1.4),(bad_prompt_version2:0.8), (monochrome:1.1)",
			768, 1024),
	PORN_SHOW(
			"best quality, masterpiece, upper body,extremely detailed 8K wallpaper, {an extremely delicate and beautiful},"
					+ "colorful,intricate detail,artbook,highly detailed,lying, legs up,spread anus, (gaping pussy:1.2),(A gaping ass:1.2),12-year-old girl,"
					+ "nsfw,nude,no panties,wet,sweat,soggy,Exquisite skin,After anal intercourse,,hip focus,messy_hair,m legs,neck ribbon,thigh strap",
			"(low quality, worst quality:1.4),(bad_prompt:0.8), (monochrome:1.1),Thick lips,penis,Multiple asshole,macropenis,Two vulvas,man,semen,"
					+ "extra limbs,duplicate, multiple anal,missing toes,deformed legs,deformed legs,missing toes,extra legs,(((canvas frame))), "
					+ "cartoon, 3d, ((disfigured))",
			512, 512),
	PORN_SPREAD(
			"picture-perfect face, flawless, clean, masterpiece, professional artwork, famous artwork, cinematic lighting, cinematic bloom, "
					+ "perfect face, beautiful face, beautiful eyes, ((perfect female body)), divine, goddess, godlike, fantasy, dreamlike, unreal, "
					+ "science fiction, huge breasts:1.7,nsfw, breasts out, intricate detail, delicate pattern, sexy, charming, alluring, seductive, "
					+ "erotic, enchanting, 1girl,puffy nipples, nude, pussy, hairy pussy hairs:1.2,gape,creampie,extreme close up,(spread legs:1.3),",
			"watermark, text, error, blurry, jpeg artifacts, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, username, "
					+ "artist name, (worst quality, low quality:1.4), ((bad anatomy)), signature, text, logo,contact, ((extra limbs)), Six fingers,"
					+ "Low quality fingers,EasyNegative:0.5,monochrome:1.5, easynegative,",
			512, 512),

	PORN_BACK(
			"picture-perfect face, flawless, clean, masterpiece, professional artwork, famous artwork, cinematic lighting, cinematic bloom, "
					+ "perfect face, beautiful face, beautiful eyes, ((perfect female body)), divine, goddess, godlike, fantasy, dreamlike, unreal, "
					+ "science fiction, huge breasts:1.7,nsfw, breasts out, intricate detail, delicate pattern, sexy, charming, alluring, seductive, "
					+ "erotic, enchanting, 1girl,puffy nipples, nude, pussy, hairy pussy hairs:1.2,gape,creampie,extreme close up,(spread legs:1.3),",
			"watermark, text, error, blurry, jpeg artifacts, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, username, "
					+ "artist name, (worst quality, low quality:1.4), ((bad anatomy)), signature, text, logo,contact, ((extra limbs)), Six fingers,"
					+ "Low quality fingers,EasyNegative:0.5,monochrome:1.5, easynegative,",
			640, 360),

	PORN_SQUAT(
			"masterpiece, best quality, realistic, extremely intricate, exquisitely detailed, ((cum in pussy)), juicy pussy, flawless, clean, "
					+ "(narrow waist:1.3), (close-up:1.2), (pubic hair:1.15), (from below), focus pussy, cum drip, (legs up:1.4), mature female,.1 girl,"
					+ " solo, (fucked silly), sweat, pussy, pussy peek, urethra, clitoris, cervix, labia, gigantic breasts, nsfw, breasts out, choker, wide hip,"
					+ " delicate pattern, sexy, charming, alluring, seductive, erotic, ecstasy, long hair, pink hair, pink nipples, enchanting, high-heeled shoes,"
					+ " teacher, highlighting the teacher's beauty and sensuality, too much cum, ((cum)), ((cum on face)), after fellatio, tongue out,"
					+ " drooling, busty, (stationary restraints:1.3), (suspension:1.3), arms behind back, frame binder,"
					+ " K pop idol, Korean mixed",
			"(worst quality, low quality:1.4), penis, EasyNegative, signature, words, futanari", 640, 360),

	PORN_SIT("absurdres, 1girl, 1boy, blonde hair, vaginal, sex, (cowgirl position:1.1), penis",
			"Negative prompt: (worst quality:1.2), (low quality:1.2), (lowres:1.1), (monochrome:1.1), (greyscale), multiple views, comic, sketch",
			512, 512),;

	private String positive;
	private String negative;
	private Integer width;
	private Integer height;

	Prompt(String positive, String negative, Integer width, Integer height) {
		this.positive = positive;
		this.negative = negative;
		this.width = width;
		this.height = height;
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

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

}
