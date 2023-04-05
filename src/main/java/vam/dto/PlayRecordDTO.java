package vam.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import vam.dto.enumration.CheckPoint;
import vam.dto.enumration.Prompt;
import vam.dto.enumration.SampleName;

@Slf4j
@JsonInclude(Include.NON_NULL)
@Data
public class PlayRecordDTO implements Comparable {

	private String key = "";

	private CheckPoint checkPoint = null;
	private SampleName samplerName = null;
	private Prompt prompt = Prompt.PORN_M_LEG;
	private Integer steps = 35;
	private Integer cfg_scale = 7;
//	private Integer width = 768;
//	private Integer height = 1024;

	private Integer seed = -1;
	private Integer n_iter = -1;

	private Boolean loRA = false;
	private Boolean sepeNet = false;
	private List<LoraDTO> loraDTOList = new ArrayList<>();

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
			loraDTOList.add(new LoraDTO("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)"));
			loraDTOList.add(new LoraDTO());
			loraDTOList.add(new LoraDTO());
			loraDTOList.add(new LoraDTO());
			loraDTOList.add(new LoraDTO());
		}

	}

	@Override
	public int compareTo(Object o) {
		PlayRecordDTO playRecordDTO = (PlayRecordDTO) o;
		return seed - playRecordDTO.getSeed();
	}

	public Object getHeight() {
		if (Objects.nonNull(prompt))
			return prompt.getHeight();
		return 1920;
	}

	public Object getWidth() {
		if (Objects.nonNull(prompt))
			return prompt.getWidth();
		return 1280;
	}

	public String getFilename() {
		int startIndex = StringUtils.lastIndexOf(fullpath, "\\");
		String filename = StringUtils.substring(fullpath, startIndex + 1);
		return filename;
	}
}
