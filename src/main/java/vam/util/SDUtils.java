package vam.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import vam.dto.OperatorDTO;
import vam.dto.OutputFile;
import vam.dto.Txt2ImgDTO;

@Slf4j
@Component
public class SDUtils {
	public static List<Object> toDataList(OperatorDTO operatorDTO, Txt2ImgDTO txt2ImgDTO) {
		List<Object> dataList = new ArrayList<>();
		dataList.add("task(dob24x4iiky9vcv)");// 0
		dataList.add(operatorDTO.getPrompt());// 1
		dataList.add(operatorDTO.getNegative_prompt());// 2
		dataList.add(new ArrayList<>());// 3
		dataList.add(operatorDTO.getSteps());// 4
		dataList.add(operatorDTO.getSampler_name());// 5
		dataList.add(txt2ImgDTO.getRestore_faces());// 6
		dataList.add(txt2ImgDTO.getTiling());// 7
		dataList.add(operatorDTO.getBatch());// 8
		dataList.add(operatorDTO.getBatch_amount());// 9
		dataList.add(operatorDTO.getCfg_scale());// 10
		dataList.add(operatorDTO.getSeed());// 11
		dataList.add(operatorDTO.getN_iter());// 12
		dataList.add(0);// 13
		dataList.add(0);// 14
		dataList.add(0);// 15
		dataList.add(true);// 16
		dataList.add(operatorDTO.getHeight());// 17
		dataList.add(operatorDTO.getWidth());// 18
		dataList.add(false);// 19
		dataList.add(0);// 20
		dataList.add(2);// 21
		dataList.add("Latent");// 22
		dataList.add(0);// 23
		dataList.add(0);// 24
		dataList.add(0);// 25
		dataList.add(new ArrayList<>());// 26
		dataList.add("None");// 27
		dataList.add(operatorDTO.getLoRA());// 28
		dataList.add(operatorDTO.getSepeNet());// 29
		dataList.add("LoRA");// 30
		dataList.add("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)");// 31
		dataList.add(0);// 32
		dataList.add(0);// 33
		dataList.add("LoRA");// 34
		dataList.add("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)");// 35
		dataList.add(0);// 36
		dataList.add(0);// 37
		dataList.add("LoRA");// 38
		dataList.add("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)");// 39
		dataList.add(0);// 40
		dataList.add(0);// 41
		dataList.add("LoRA");// 42
		dataList.add("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)");// 43
		dataList.add(0);// 44
		dataList.add(0);// 45
		dataList.add("LoRA");// 46
		dataList.add("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)");// 47
		dataList.add(0);// 48
		dataList.add(0);// 49
		dataList.add(null);// 50
		dataList.add("Refresh models");// 51
		dataList.add(null);// 52
		dataList.add(operatorDTO.getClipArt());// 53
		dataList.add(operatorDTO.getClipStep());// 54
		dataList.add(operatorDTO.getLearnRatio());// 55
		dataList.add(operatorDTO.getBallDiff());// 56
		dataList.add(operatorDTO.getEmbedding());// 57
		dataList.add(operatorDTO.getEmbeddingDesc());// 58
		dataList.add(operatorDTO.getBallAngle());// 59
		dataList.add(false);// 60
		dataList.add(false);// 61
		dataList.add(false);// 62
		dataList.add("positive");// 63
		dataList.add("comma");// 64
		dataList.add(0);// 65
		dataList.add(false);// 66
		dataList.add(false);// 67
		dataList.add("");// 68
		dataList.add("Seed");// 69
		dataList.add("");// 70
		dataList.add("Nothing");// 71
		dataList.add("");// 72
		dataList.add("Nothing");// 73
		dataList.add("");// 74
		dataList.add(true);// 75
		dataList.add(false);// 76
		dataList.add(false);// 77
		dataList.add(false);// 78
		dataList.add(0);// 79
		dataList.add(null);// 80
		dataList.add(false);// 81
		dataList.add(50);// 82
		dataList.add(new OutputFile());// 83
		dataList.add("");// 84
		dataList.add("");// 85
		dataList.add("");// 86
		return dataList;

	}
}
