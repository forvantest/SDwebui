package vam.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import vam.dto.LoraDTO;
import vam.dto.OutputFileDTO;
import vam.dto.PlayRecordDTO;
import vam.dto.Txt2ImgDTO;

@Slf4j
@Component
public class SDUtils {
	public static List<Object> toDataList(int batch, int batch_amount, PlayRecordDTO playRecordDTO,
			Txt2ImgDTO txt2ImgDTO) {
		List<Object> dataList = new ArrayList<>();
		dataList.add("task(dob24x4iiky9vcv)");// 0
		dataList.add(playRecordDTO.getPrompt().getPositive());// 1
		dataList.add(playRecordDTO.getPrompt().getNegative());// 2
		dataList.add(new ArrayList<>());// 3
		dataList.add(playRecordDTO.getSteps());// 4
		dataList.add(playRecordDTO.getSamplerName().getOpCode());// 5
		dataList.add(txt2ImgDTO.getRestore_faces());// 6
		dataList.add(txt2ImgDTO.getTiling());// 7
		dataList.add(batch);// 8
		dataList.add(batch_amount);// 9
		dataList.add(playRecordDTO.getCfg_scale());// 10
		dataList.add(playRecordDTO.getSeed());// 11
		dataList.add(playRecordDTO.getN_iter());// 12
		dataList.add(0);// 13
		dataList.add(0);// 14
		dataList.add(0);// 15
		dataList.add(true);// 16
		dataList.add(playRecordDTO.getHeight());// 17
		dataList.add(playRecordDTO.getWidth());// 18
		dataList.add(false);// 19
		dataList.add(0);// 20
		dataList.add(2);// 21
		dataList.add("Latent");// 22
		dataList.add(0);// 23
		dataList.add(0);// 24
		dataList.add(0);// 25
		dataList.add(new ArrayList<>());// 26
		dataList.add("None");// 27
		dataList.add(playRecordDTO.getLoRA());// 28
		dataList.add(playRecordDTO.getSepeNet());// 29

		for (int i = 0; i < 5; i++) {
			LoraDTO loraDTO = playRecordDTO.getLoraDTOList().get(i);
			dataList.addAll(loraDTO.pack());// 29
		}

//		dataList.add("LoRA");// 30
//		dataList.add("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)");// 31
//		dataList.add(0);// 32
//		dataList.add(0);// 33
//		dataList.add("LoRA");// 34
//		dataList.add("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)");// 35
//		dataList.add(0);// 36
//		dataList.add(0);// 37
//		dataList.add("LoRA");// 38
//		dataList.add("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)");// 39
//		dataList.add(0);// 40
//		dataList.add(0);// 41
//		dataList.add("LoRA");// 42
//		dataList.add("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)");// 43
//		dataList.add(0);// 44
//		dataList.add(0);// 45
//		dataList.add("LoRA");// 46
//		dataList.add("3loraGuofeng3Lora_v32LoraBigLight(cb303d82768c)");// 47
//		dataList.add(0);// 48
//		dataList.add(0);// 49

		dataList.add(null);// 50
		dataList.add("Refresh models");// 51
		dataList.add(null);// 52
		dataList.add(playRecordDTO.getClipArt());// 53
		dataList.add(playRecordDTO.getClipStep());// 54
		dataList.add(playRecordDTO.getLearnRatio());// 55
		dataList.add(playRecordDTO.getBallDiff());// 56
		dataList.add(playRecordDTO.getEmbedding());// 57
		dataList.add(playRecordDTO.getEmbeddingDesc());// 58
		dataList.add(playRecordDTO.getBallAngle());// 59
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
		dataList.add(new OutputFileDTO());// 83
		dataList.add("");// 84
		dataList.add("");// 85
		dataList.add("");// 86
		return dataList;

	}
}
