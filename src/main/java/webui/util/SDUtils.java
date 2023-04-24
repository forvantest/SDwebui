package webui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import webui.dto.LoraDTO;
import webui.dto.OutputFileDTO;
import webui.dto.PlayRecordDTO;
import webui.dto.Txt2ImgDTO;
import webui.dto.enumration.Lora;

@Slf4j
@Component
public class SDUtils {
	public static List<Object> toDataList(int batch, int batch_amount, PlayRecordDTO playRecordDTO,
			Txt2ImgDTO txt2ImgDTO, String model) {
		List<Object> dataList = new ArrayList<>();
		dataList.add("task(dob24x4iiky9vcv)");// 0

		StringBuffer sb = new StringBuffer();
		sb.append(playRecordDTO.getPrompt().getPositive());
//		if (playRecordDTO.changeLoraWeight) {
		for (Lora lora : playRecordDTO.getLoraList()) {
			sb.append(lora.appendLora());
		}
		if (!CollectionUtils.isEmpty(playRecordDTO.getTextualInversionList())) {
			if (playRecordDTO.getTextualInversionList().get(0) != null) {
				sb.append(playRecordDTO.getTextualInversionList().get(0).appendTextualInversionPT());
			}
		}
		dataList.add(sb.toString());// 1
		dataList.add(playRecordDTO.getPrompt().getNegative());// 2
		dataList.add(Arrays.asList(model));// 3
		dataList.add(playRecordDTO.getSteps());// 4
		dataList.add(playRecordDTO.getSamplerName().getOpCode());// 5
		dataList.add(txt2ImgDTO.getRestore_faces());// 6
		dataList.add(txt2ImgDTO.getTiling());// 7
		dataList.add(batch);// 8
		dataList.add(batch_amount);// 9
		dataList.add(playRecordDTO.getCfg_scale());// 10
		dataList.add(playRecordDTO.getPrompt().getSeed() == null ? -1 : playRecordDTO.getPrompt().getSeed());// 11
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
		dataList.add(playRecordDTO.getRescale().getRescaleDesc());// 22
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
		dataList.add(null);// 53
		dataList.add(playRecordDTO.getClipArt());// 54
		dataList.add(playRecordDTO.getClipStep());// 55
		dataList.add(playRecordDTO.getLearnRatio());// 56
		dataList.add(playRecordDTO.getBallDiff());// 57
		dataList.add(playRecordDTO.getEmbedding());// 58
		dataList.add(playRecordDTO.getEmbeddingDesc());// 59
		dataList.add(playRecordDTO.getBallAngle());// 60
		dataList.add(false);// 61
		dataList.add(false);// 62
		dataList.add(false);// 63
		dataList.add("positive");// 64
		dataList.add("comma");// 65
		dataList.add(0);// 66
		dataList.add(false);// 67
		dataList.add(false);// 68
		dataList.add("");// 69
		dataList.add("Seed");// 70
		dataList.add("");// 71
		dataList.add("Nothing");// 72
		dataList.add("");// 73
		dataList.add("Nothing");// 74
		dataList.add("");// 75
		dataList.add(true);// 76
		dataList.add(false);// 77
		dataList.add(false);// 78
		dataList.add(false);// 79
		dataList.add(0);// 80
		dataList.add(null);// 81
		dataList.add(false);// 82
		dataList.add(null);// 83
		dataList.add(false);// 84
		dataList.add(50);// 85
		dataList.add(new OutputFileDTO());// 86
		dataList.add("");// 87
		dataList.add("");// 88
		dataList.add("");// 89
		return dataList;

	}

	public static void appendHiRES(List<Object> dataList, PlayRecordDTO playRecordDTO) {
		dataList.set(16, false);// 16
		// dataList.set(17, 512);// 17
		// dataList.set(18, 384);// 18
		dataList.set(19, true);// 19
		dataList.set(20, playRecordDTO.getDenoising());// 20
		dataList.set(21, 2);// 21
		dataList.set(22, playRecordDTO.getRescale().getRescaleDesc());// 22
		dataList.set(23, playRecordDTO.getHiresFixTimes());// 23
	}
}
