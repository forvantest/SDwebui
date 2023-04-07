package vam.dto.enumration;

import java.util.Set;

import com.google.common.collect.Sets;

public enum Prompt {
	PORN_M_LEG(
			"1boy, 1girl, hetero, vaginal, sex, mating press, folded, missionary, head on pillow, m_legs, skirt lift,hair ornament,jewelry,"
					+ " blush, solo_focus, large penis,pussy,sweat,focus pussy,labia, porn, blurry_background, depth_of_field, pov,"
					+ " lips, open mouth, tongue out, tongue, ahegao, rolling eyes, half-closed eyes, <lora:ahegaoRollingEyes_v1114:0.3>, aegyo sal,"
					+ " lying, high heels, on bed, on back, cum, bukkake, facial, cum in pussy, cum on body, overflow, drooling, puffy nipples, male pubic hair,"
					+ " thighhighs, best quality, masterpiece, realistic, photorealistic, 8K, RAW photo, ultra highres, ((smooth thighs))",
			"(bad-artist:0.7), (worst quality, low quality:1.4), anal, (closed eyes: 1.4), bad-hands-5, mole, skin spots, acnes, skin blemishes, age spot",
			768, 1024,
			Sets.newHashSet(CheckPoint.V08_V08, CheckPoint.CHIKMIX_V2, CheckPoint.YORRRLMIX_V21,
					CheckPoint.FANTASTICMIXREAL_V10, CheckPoint.KOREANSTYLE25D_KOREANSTYLE25DBAKED,
					CheckPoint.PERFECTWORLD_V2BAKED, CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX, CheckPoint.REALDOSMIX_,
					CheckPoint._2GUOFENG2_V20, CheckPoint._3GUOFENG3_V32LIGHT)),

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
			768, 1024, Sets.newHashSet()),
	PORN_SHOW(
			"best quality, masterpiece, upper body,extremely detailed 8K wallpaper, {an extremely delicate and beautiful},"
					+ "colorful,intricate detail,artbook,highly detailed,lying, legs up,spread anus, (gaping pussy:1.2),(A gaping ass:1.2),12-year-old girl,"
					+ "nsfw,nude,no panties,wet,sweat,soggy,Exquisite skin,After anal intercourse,,hip focus,messy_hair,m legs,neck ribbon,thigh strap",
			"(low quality, worst quality:1.4),(bad_prompt:0.8), (monochrome:1.1),Thick lips,penis,Multiple asshole,macropenis,Two vulvas,man,semen,"
					+ "extra limbs,duplicate, multiple anal,missing toes,deformed legs,deformed legs,missing toes,extra legs,(((canvas frame))), "
					+ "cartoon, 3d, ((disfigured))",
			512, 512, Sets.newHashSet()),
	PORN_SPREAD(
			"picture-perfect face, flawless, clean, masterpiece, professional artwork, famous artwork, cinematic lighting, cinematic bloom, "
					+ "perfect face, beautiful face, beautiful eyes, ((perfect female body)), divine, goddess, godlike, fantasy, dreamlike, unreal, "
					+ "science fiction, huge breasts:1.7,nsfw, breasts out, intricate detail, delicate pattern, sexy, charming, alluring, seductive, "
					+ "erotic, enchanting, 1girl,puffy nipples, nude, pussy, hairy pussy hairs:1.2,gape,creampie,extreme close up,(spread legs:1.3),",
			"watermark, text, error, blurry, jpeg artifacts, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, username, "
					+ "artist name, (worst quality, low quality:1.4), ((bad anatomy)), signature, text, logo,contact, ((extra limbs)), Six fingers,"
					+ "Low quality fingers,EasyNegative:0.5,monochrome:1.5, easynegative,",
			512, 512, Sets.newHashSet()),

	PORN_BACK(
			"picture-perfect face, flawless, clean, masterpiece, professional artwork, famous artwork, cinematic lighting, cinematic bloom, "
					+ "perfect face, beautiful face, beautiful eyes, ((perfect female body)), divine, goddess, godlike, fantasy, dreamlike, unreal, "
					+ "science fiction, huge breasts:1.7,nsfw, breasts out, intricate detail, delicate pattern, sexy, charming, alluring, seductive, "
					+ "erotic, enchanting, 1girl,puffy nipples, nude, pussy, hairy pussy hairs:1.2,gape,creampie,extreme close up,(spread legs:1.3),",
			"watermark, text, error, blurry, jpeg artifacts, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, username, "
					+ "artist name, (worst quality, low quality:1.4), ((bad anatomy)), signature, text, logo,contact, ((extra limbs)), Six fingers,"
					+ "Low quality fingers,EasyNegative:0.5,monochrome:1.5, easynegative,",
			640, 360, Sets.newHashSet()),

	PORN_SQUAT(
			"masterpiece, best quality, realistic, extremely intricate, exquisitely detailed, ((cum in pussy)), juicy pussy, flawless, clean, "
					+ "(narrow waist:1.3), (close-up:1.2), (pubic hair:1.15), (from below), focus pussy, cum drip, (legs up:1.4), mature female,.1 girl,"
					+ " solo, (fucked silly), sweat, pussy, pussy peek, urethra, clitoris, cervix, labia, gigantic breasts, nsfw, breasts out, choker, wide hip,"
					+ " delicate pattern, sexy, charming, alluring, seductive, erotic, ecstasy, long hair, pink hair, pink nipples, enchanting, high-heeled shoes,"
					+ " teacher, highlighting the teacher's beauty and sensuality, too much cum, ((cum)), ((cum on face)), after fellatio, tongue out,"
					+ " drooling, busty, (stationary restraints:1.3), (suspension:1.3), arms behind back, frame binder,"
					+ " K pop idol, Korean mixed",
			"(worst quality, low quality:1.4), penis, EasyNegative, signature, words, futanari", 640, 360,
			Sets.newHashSet()),

	PORN_SIT("absurdres, 1girl, 1boy, blonde hair, vaginal, sex, (cowgirl position:1.1), penis",
			"Negative prompt: (worst quality:1.2), (low quality:1.2), (lowres:1.1), (monochrome:1.1), (greyscale), multiple views, comic, sketch",
			512, 512, Sets.newHashSet()),

	PORN_GIRL1(
			"(8k, RAW photo, best quality, masterpiece:1.2), 1girl, (light brown cardigan),checkered_skirt,white beret,black sailor_shirt, ((skinny)), "
					+ "((puffy eyes)),brown hair,medium hair,cowboy shot,medium breasts, swept bangs,walking,outdoors,sunshine,light_rays,fantasy,blurry_foreground,"
					+ "rococo,street,crowd,hair_flower,close up,low tied hair, smile,half-closed eyes,sweet , cleavage cutout, underbust, taut shirt,"
					+ "	full body, ((legs spread on cock)), ((super wet skin)), (moaning), horny, pussy, ((Sexual intercourse)), ((sex)), ((fucked by man)), "
					+ "((POV from below)), ((Sexual penetration)), ((vast cum on woman's legs)), ((vast cum on woman's pussy)),(((Big cock inserted into vagina:1.3))), "
					+ "((5 fingers)), hetero, ((1girl above 1man)), ((1man below 1girl)), (((cowgirl position))), (straddling), luxury hotel, ((suite room)),"
					+ " bed, side lighting, high contrast, sexy lingeries,<lora:koreanDollLikeness_v15:0.6>,<lora:Taiwan-doll-likeness:0.15>,",
			"paintings, sketches, (worst quality:2), (low quality:2), (normal quality:2), lowres, normal quality, ((monochrome)), ((grayscale)),"
					+ " skin spots, acnes, skin blemishes, age spot, glans",
			512, 768, Sets.newHashSet()),
	PORN_GIRL2(
			"1 girl having sex with 1 man, (sharp focus:1.4), (smile:1.1), (realistic humid skin:1.4), (beautiful face:1.1), detailed eyes,sweet,slime girl,high heels, "
					+ "detailed face, (curvy body:0.8), (long black ponytail hair:1.2), bangs, depth of field, nude, naked, best quality, ultra high res, "
					+ "(photorealistic:1.4), ((puffy eyes)), full body, ((legs spread on cock)), ((super wet skin)), (moaning), horny, pussy, ((Sexual intercourse)),"
					+ " ((sex)), ((fucked by man)), ((POV from below)), ((Sexual penetration)), ((vast cum on woman's legs)), ((vast cum on woman's pussy)), "
					+ "((5 fingers)), hetero, ((1girl above 1man)), ((1man below 1girl)), (((cowgirl position))), (straddling), luxury hotel, ((suite room)), "
					+ "bed, side lighting, high contrast, sexy lingeries,<lora:koreanDollLikeness_v15:0.5>,<lora:taiwanDollLikeness_v10:0.4>",
			"((blurry)), animated, cartoon, duplicate, child, childish, paintings, sketches", 512, 768,
			Sets.newHashSet()),
	PORN_GIRL3(
			"1 girl having sex with 1 man, (sharp focus:1.4), (realistic humid skin:1.4), (beautiful face:1.1), detailed eyes,sweet,slime girl,high heels, "
					+ "(curvy body:0.8),  depth of field,   best quality, ultra high res, "
					+ "(photorealistic:1.4),   ((legs spread on cock)), ((super wet skin)), (moaning), horny,((Sexual intercourse)),"
					+ " ((sex)), ((fucked by man)), ((POV from below)), ((Sexual penetration)), ((vast cum on woman's legs)), ((vast cum on woman's pussy)), "
					+ "((5 fingers)), hetero, ((1girl above 1man)), ((1man below 1girl)), (((cowgirl position))), (straddling), luxury hotel, ((suite room)), "
					+ "(RAW photo, best quality), (realistic, photo-realistic:1.3), best quality ,masterpiece, an extremely delicate and beautiful, "
					+ "extremely detailed ,CG ,unity ,2k wallpaper, Amazing, finely detail, masterpiece,best quality, "
					+ "extremely detailed CG unity 8k wallpaper, ultra-detailed, highres, extremely detailed, iu,"
					+ "beautiful detailed girl,"
					+ "straight-on, staring, longeyelashes, (full body:1.3),  "
					+ "(beautiful ponytail:0.5),beautiful detailed eyes, beautiful detailed nose, realistic face, realistic body, look at viewer,  "
					+ "bed, side lighting, high contrast, sexy lingeries,<lora:koreanDollLikeness_v15:0.15>,<lora:taiwanDollLikeness_v10:0.4>",
			"((blurry)), animated, cartoon, duplicate, paintings, sketches, (worst quality:2), (low quality:2), (normal quality:2), lowres, "
					+ "normal quality, ((monochrome)), ((grayscale)),skin spots, acnes, skin blemishes, age spot, glans",
			512, 768, Sets.newHashSet()),
	PORN_GIRL4(
			"1 girl having sex with 1 man, (sharp focus:1.4), (realistic humid skin:1.4),sweet,slime girl,high heels,((legs spread on cock)), "
			+ "((super wet skin)), (moaning), ((Sexual intercourse)), "
			+ " ((sex)), ((fucked by man)), ((POV from below)), ((Sexual penetration)), ((vast cum on woman's legs)), ((vast cum on woman's pussy)), "
			+ "((5 fingers)), hetero, ((1girl above 1man)), ((1man below 1girl)), (((cowgirl position))), (straddling), luxury hotel, ((suite room)), "
			+ "professional modelshoot style photo elegant woman, classy bedroom, wet, water on body, wet body, drops on face realistic, "
			+ "sharp focus, 8k high definition, insanely detailed, intricate, elegant, sunrays through window, curtains, wing eyeliner, eyeliner wings, "
			+ "hyper-realistic, ultra detailed, full body, detailed lingerie, stockings, showing tongue, (kissing:1.15), "
			+ "horny, (Kpop idol),  doggystyle, perfect hand, "
			+ "<lora:koreanDollLikeness_v15:0.45>, <lora:taiwanDollLikeness_v10:0.15> <lora:cuteGirlMix4_v10:0.15>",
			"paintings, sketches, (worst quality:2), (low quality:2), (normal quality:2), lowres, normal quality, ((monochrome)), ((grayscale)), skin spots, "
			+ "acnes, skin blemishes, age spot, glans,",
			512, 768, Sets.newHashSet()),;
	private String positive;
	private String lora;
	private String negative;
	private Integer width;
	private Integer height;
	private Set<CheckPoint> checkPointSet;

	Prompt(String positive, String negative, Integer width, Integer height, Set<CheckPoint> checkPointSet) {
		this.positive = positive;
		this.negative = negative;
		this.width = width;
		this.height = height;
		this.checkPointSet = checkPointSet;
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

	public Set<CheckPoint> getCheckPointSet() {
		return checkPointSet;
	}

	public String getLora() {
		return lora;
	}

	public void setLora(String lora) {
		this.lora = lora;
	}

}
