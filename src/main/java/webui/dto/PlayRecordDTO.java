package webui.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import webui.dto.enumration.CheckPoint;
import webui.dto.enumration.Lora;
import webui.dto.enumration.Rescale;
import webui.dto.enumration.SampleName;
import webui.dto.enumration.TextualInversion;
import webui.dto.enumration.Vae;
import webui.service.ModelService;

@Slf4j
@JsonInclude(Include.NON_NULL)
@Data
public class PlayRecordDTO implements Comparable {

	private String key = "";
	private String srcPng;

	private String prompt = "";
	private String Negative_prompt = "";

	private CheckPoint checkPoint = null;
	private Integer Clip_skip = 1;
	private Vae vae = Vae.VAE_FT_MSE_840000_EMA_PRUNED;

	private SampleName samplerName = null;
	private Float cfg_scale = 7f;
	private Boolean hiresFix = false;

	private Integer width;
	private Integer height;
	private Long seed;
	private Rescale rescale = Rescale.None;
	private Integer steps = 35;
	private Float denoising = 0.7f;
	private Integer hiresFixTimes = 0;
	private Float Hires_upscale = 0f;
	private Integer Hires_steps = 0;

	private Integer n_iter = -1;

	private Boolean loRA = false;
	private Boolean sepeNet = false;
	private List<LoraDTO> loraDTOList = new ArrayList<>();
	private List<Lora> loraList = new ArrayList<>();
	private List<TextualInversion> textualInversionList = new ArrayList<>();

	private Double clipArt = 0.9;
	private Integer clipStep = 5;
	private String learnRatio = "0.0001";
	private Boolean ballDiff = false;
	private String embedding = "None";
	private String embeddingDesc = "";
	private Double ballAngle = 0.1;

	private String fullpath;

	public PlayRecordDTO() {
		super();
		// default lora
		{
			loRA = false;
			loraDTOList.add(new LoraDTO(Lora._3LORAGUOFENG3LORA_V32LORABIGLIGHT));
			loraDTOList.add(new LoraDTO());
			loraDTOList.add(new LoraDTO());
			loraDTOList.add(new LoraDTO());
			loraDTOList.add(new LoraDTO());
		}
	}

	public PlayRecordDTO(Map<String, String> map) {
		this.prompt = map.get("");
		this.Negative_prompt = map.get("Negative prompt:");
		this.steps = myInteger(map.get("Steps:"));

		this.samplerName = SampleName.getByName(map.get("Sampler:"));
		this.cfg_scale = myFloat(map.get("CFG scale:"));
		this.seed = myLong(map.get("Seed:"));

		String size = map.get("Size:");
		String[] ss = StringUtils.split(size, "x");
		this.width = myInteger(ss[0]);
		this.height = myInteger(ss[1]);
		CheckPoint checkPoint = ModelService.getByHash(map.get("Model hash:"));
		this.checkPoint = checkPoint;

		this.denoising = myFloat(map.get("Denoising strength:"));

		this.Clip_skip = myInteger(map.get("Clip skip:"));

		this.Hires_upscale = myFloat(map.get("Hires upscale:"));
		this.Hires_steps = myInteger(map.get("Hires steps:"));
		this.rescale = Rescale.getByUpscaler(map.get("Hires upscaler:"));
		if (this.rescale!=Rescale.None)
			this.hiresFix = true;

//		"AddNet Enabled:",
//		"AddNet Module 1:",
//		"AddNet Model 1:",
//		"AddNet Weight A 1:",
//		"AddNet Weight B 1:"
	}

	private Float myFloat(String sInt) {
		if (Objects.isNull(sInt))
			return 0f;
		try {
			return Float.parseFloat(sInt);
		} catch (Exception e) {
			return 0f;
		}
	}

	private Integer myInteger(String sInt) {
		if (Objects.isNull(sInt))
			return 0;
		try {
			return Integer.parseInt(sInt);
		} catch (Exception e) {
			return 0;
		}
	}

	private Long myLong(String sInt) {
		if (Objects.isNull(sInt))
			return 0l;
		try {
			return Long.parseLong(sInt);
		} catch (Exception e) {
			return 0l;
		}
	}

	@Override
	public int compareTo(Object o) {
		PlayRecordDTO playRecordDTO = (PlayRecordDTO) o;
		return 1;
	}

	public String getFilename() {
		int startIndex = StringUtils.lastIndexOf(fullpath, "\\");
		String filename = StringUtils.substring(fullpath, startIndex + 1);
		return filename;
	}

	public String getKey() {
		int startIndex = StringUtils.lastIndexOf(fullpath, "\\");
		int endIndex = StringUtils.indexOf(fullpath, "-", startIndex + 8);
		String key = StringUtils.substring(fullpath, startIndex + 1, endIndex);
		return key;
	}

	public static boolean changeLoraWeight = true;

	public String getIdentifyName() {
		String identifyName = "";
		if (changeLoraWeight) {
			if (loraList.size() == 2) {
				identifyName = String.format(
						"weight1__%.1f weight2__%.1f sample__%s lora1__%s lora2__%s %s step_%s.png",
						loraList.get(0).getWeight(), loraList.get(1).getWeight(), samplerName, loraList.get(0).name(),
						loraList.get(1).name(), getKey(), getSteps());
			} else if (loraList.size() == 0) {
				identifyName = String.format("Clip_skip__%s sample__%s step_%s %s.png", Clip_skip, samplerName,
						getSteps(), (int) (Math.random() * 100000000));
			} else {
				identifyName = String.format("weight__%.1f %s lora__%s sample__%s %s step_%s.png",
						loraList.get(0).getWeight(), rescale.name(), loraList.get(0).name(), samplerName, getKey(),
						getSteps());
			}
		} else {
			identifyName = String.format("weight_%.1f %s TextualInversion_%s sample_%s step_%s.png",
					textualInversionList.get(0).getWeight(), getKey(), textualInversionList.get(0).name(), samplerName,
					getSteps());
		}
		return identifyName;
	}

	public String getOutputDir() {
		String outputDir = "";
		if (changeLoraWeight) {
			if (loraList.size() == 2) {
				outputDir = String.format("%s__%s__%s", checkPoint.name(), loraList.get(0).name(),
						loraList.get(1).name());
			} else if (loraList.size() == 0) {
				outputDir = String.format("%s__%s__", srcPng, checkPoint.name());
			} else {
				outputDir = String.format("%s__%s", checkPoint.name(), loraList.get(0).name());
			}
		} else {
			outputDir = String.format("%s__%s", checkPoint.name(), textualInversionList.get(0).name());
		}
		return outputDir;
	}

}
