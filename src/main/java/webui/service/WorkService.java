package webui.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import webui.dto.enumration.CheckPoint;
import webui.dto.enumration.CheckPointType;
import webui.dto.enumration.Lora;
import webui.dto.enumration.LoraType;
import webui.dto.enumration.Prompt;
import webui.dto.enumration.Rescale;
import webui.dto.enumration.TextualInversion;

@Slf4j
@Service("work")
public class WorkService extends WorkServiceAbstract {

	public void txt2img_main() {
		for (int i = 0; i < 10000; i++) {
			long time1, time2;

			time1 = System.currentTimeMillis();

			modelService.loadModel();

//			txt2img_main_sub1();
			txt2img_main_sub();
//			txt2img_main_sub_random();
			time2 = System.currentTimeMillis();
			System.out.println("mission 花了：" + (time2 - time1) / 1000 + "秒");
		}
	}

	public void txt2img_main_sub1() {
		String targetDir = "default\\";
		CheckPoint checkPoint = CheckPoint.V08_V08;
		TextualInversion textualInversion = TextualInversion.ULZZANG.initWeight(0.6f);
		Set<Rescale> myRescale = Sets.newHashSet(Rescale.values());
		for (Rescale rescale : myRescale) {
			txt2img_mainTask(checkPoint, Prompt.BEST_NURSE,
					Arrays.asList(Lora.KOREAN_DOLL_LIKENESS.initWeight(0.2f, 0.9f)), textualInversion, rescale, 20,
					targetDir);// 影響不大
		}
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
			for (Rescale rescale : myRescale) {
				txt2img_mainTask(checkPoint, Prompt.PORN_GIRL5, myLora, textualInversion, rescale, 20, targetDir);// 影響不大
			}
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
