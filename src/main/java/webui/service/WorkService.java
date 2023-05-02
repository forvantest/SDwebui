package webui.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import webui.dto.PlayRecordDTO;
import webui.dto.enumration.CheckPoint;
import webui.dto.enumration.CheckPointType;
import webui.dto.enumration.Lora;
import webui.dto.enumration.LoraType;
import webui.dto.enumration.Prompt;
import webui.dto.enumration.Rescale;
import webui.dto.enumration.TextualInversion;
import webui.util.FileUtil;

@Slf4j
@Service("work")
public class WorkService extends WorkServiceAbstract {

	@Autowired
	IIOMetadataUpdater iioMetadataUpdater;

	public void txt2img_main() {
		modelService.loadModel();
		for (int i = 0; i < 1000; i++) {
			long time1, time2;

			time1 = System.currentTimeMillis();
			log.warn("RUN {} start", i);
			img2img_main();

//			txt2img_main_sub1();
			// txt2img_main_sub();
//			txt2img_main_sub_random();
			time2 = System.currentTimeMillis();
			System.out.println("mission 花了：" + (time2 - time1) / 1000 + "秒");
		}
	}

	public void img2img_main() {
		List<String> pngList = Arrays.asList("G5.png");
		for (String png : pngList) {
			img2img_task(png);
		}
//		FileFilter filter = new FileFilter() {
//			static List<String> skipPng = Arrays.asList("G1.png", "G2.png", "G3.png", "G10.png", "G11.png");
//
//			@Override
//			public boolean accept(File pathname) {
//				for (String png : skipPng) {
//					if (pathname.getName().endsWith(png))
//						return false;
//				}
//				return pathname.getName().endsWith(".png");
//			}
//		};
//
//		String rootPath = "C:\\Users\\jacky\\Pictures\\Saved Pictures\\";
//		File f = new File(rootPath);
//		File[] ff = f.listFiles(filter);
//		for (File fff : ff) {
//			img2img_task(fff.getName());
//		}
	}

	private void img2img_task(String srcPng) {
		log.warn("RUN {} start", srcPng);
		String pngPath = "C:\\Users\\jacky\\Pictures\\Saved Pictures\\" + srcPng;
		PlayRecordDTO playRecordDTO = makePlayRecordDTOfromPng(pngPath);
		playRecordDTO.setSrcPng(StringUtils.replace(srcPng, ".png", ""));
		List<PlayRecordDTO> tuneList = tune(playRecordDTO);
		for (PlayRecordDTO playRecordDTO1 : tuneList) {
			img2img_main_sub(playRecordDTO1);
		}
	}

	private List<PlayRecordDTO> tune(PlayRecordDTO srcRecordDTO) {

		List<PlayRecordDTO> playRecordDTOList = new ArrayList<>();

		for (int i = 1; i < 10; i++) {
			PlayRecordDTO playRecordDTO = new PlayRecordDTO();
			BeanUtils.copyProperties(srcRecordDTO, playRecordDTO);
			playRecordDTO.setClip_skip(i);
			playRecordDTOList.add(playRecordDTO);
		}

		return playRecordDTOList;
	}

	private PlayRecordDTO makePlayRecordDTOfromPng(String srcPng) {
		String parameters = null;
		try {
			parameters = iioMetadataUpdater.fetchParameter(srcPng);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PlayRecordDTO playRecordDTO = makePlayRecordDTO(parameters);
		return playRecordDTO;
	}

	private void img2img_main_sub(PlayRecordDTO playRecordDTO) {
		String targetDir = "default\\";
		switchCheckPoint(playRecordDTO.getCheckPoint());
		switchVae(playRecordDTO.getVae());
		switchClipSkip(playRecordDTO.getClip_skip());

		PlayRecordDTO playRecordDTO2 = txt2img(playRecordDTO, 1);
		if (playRecordDTO2 == null) {
			System.out.println("\n\n work failed!");
		} else {
			FileUtil.moveFileTo(WEBUI_SOME_PATH + targetDir, playRecordDTO2, "txt2img over");
		}
	}

	// @formatter:off
	static List<String> keywordList = Arrays.asList("", 
			"Negative prompt:", 
			"Steps:",
			"Sampler:",
			"CFG scale:",
			"Seed:",
			"Size:",
			"Model hash:",
			"Model:",
			"Denoising strength:",
			"Clip skip:",
			"ENSD:",
			"Hires upscale:",
			"Hires steps:",
			"Hires upscaler:",
			
			"AddNet Enabled:",
			"AddNet Module 1:",
			"AddNet Model 1:",
			"AddNet Weight A 1:",
			"AddNet Weight B 1:"
			);
	// @formatter:on
	private PlayRecordDTO makePlayRecordDTO(String parameters) {
		Map<String, String> map = new LinkedHashMap<>();
		for (int i = 0; i < keywordList.size(); i++) {
			if (i == 8) {
				String ss = "ss";
			}
			String param1 = null;
			if (i == keywordList.size() - 1) {
				int index = findKeyword(parameters, keywordList.get(i), 0);
				if (index >= 0)
					param1 = findKeyword(parameters, index, keywordList.get(i), parameters.length());
			} else {
				param1 = fetchValue(parameters, keywordList.get(i), keywordList.get(i + 1));
			}
			map.put(keywordList.get(i), param1);
		}
		PlayRecordDTO playRecordDTO = new PlayRecordDTO(map);
		return playRecordDTO;
	}

	private String fetchValue(String parameters, String keyword1, String keyword2) {
		int index1 = 0;
		if (!keyword1.equals("")) {
			index1 = findKeyword(parameters, keyword1, 0);
		}
		if (index1 < 0)
			return null;
		int index2 = findKeyword(parameters, keyword2, index1);
		if (index2 < 0) {
			index2 = findKeyword(parameters, ",", index1);
			if (index2 < 0) {
				index2 = parameters.length();
			} else {
				index2 += 2;
			}
		}
		String param1 = findKeyword(parameters, index1, keyword1, index2);
		if ("".equals(param1))
			return null;
		return param1;
	}

	private String findKeyword(String parameters, int index1, String keyword, int index2) {
		String param1 = StringUtils.substring(parameters, index1 + StringUtils.length(keyword), index2);
		if (StringUtils.endsWith(param1, ", ")) {
			param1 = StringUtils.substring(param1, 0, param1.length() - 2);
		}
		param1 = StringUtils.trim(param1);
		return param1;
	}

	private int findKeyword(String parameters, String keyword, int startIndex) {
		return StringUtils.indexOf(parameters, keyword, startIndex);
	}

	public void txt2img_main_sub1() {
		String targetDir = "default\\";
		CheckPoint checkPoint = CheckPoint.V08_V08;
		TextualInversion textualInversion = TextualInversion.ULZZANG.initWeight(0.6f);
		txt2img_mainTask(checkPoint, Prompt.BEST_NURSE, Arrays.asList(Lora.KOREAN_DOLL_LIKENESS.initWeight(0.2f, 0.9f)),
				textualInversion, targetDir);// 影響不大

	}

	public void txt2img_main_sub() {
		String targetDir = "default\\";
//		Set<CheckPoint> myCheckPoint = CheckPoint.getBy(CheckPointType.LEGEND);
		Set<CheckPoint> myCheckPoint = Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX);
		List<Lora> myLora = Arrays.asList(Lora.CUTEGIRLMIX4_V10.initWeight(0.4f));
		TextualInversion textualInversion = null;// TextualInversion.ULZZANG.initWeight(0.0f);
		Set<Rescale> myRescale = Sets.newHashSet(Rescale.values());

//		for (CheckPoint checkPoint : myCheckPoint) {
		for (int i = 0; i < 1; i++) {
			CheckPoint checkPoint = randomCheckPoint(myCheckPoint);
			txt2img_mainTask(checkPoint, Prompt.PORN_GIRL5, myLora, textualInversion, targetDir);// 影響不大
		}
	}

	public void txt2img_main_sub_random() {
		CheckPointType checkPointType = CheckPointType.NORMAL;
		LoraType loraType = LoraType.COUNTRY_IDOLL;
		Prompt prompt = Prompt.BEST_NURSE;
		TextualInversion textualInversion = TextualInversion.CORNEO_SIDE_DOGGY.initWeight(1.1f);
		String targetDir = checkPointType.name() + "__" + loraType.name() + "__" + prompt.name() + "\\";
		txt2img_main_sub_random_do(checkPointType, loraType, prompt, textualInversion, targetDir);
	}

}
