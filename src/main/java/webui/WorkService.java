package webui;

import java.util.Arrays;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import webui.dto.enumration.CheckPoint;
import webui.dto.enumration.CheckPointType;
import webui.dto.enumration.Lora;
import webui.dto.enumration.LoraType;
import webui.dto.enumration.Prompt;

@Slf4j
@Service("work")
public class WorkService extends WorkServiceAbstract {

	public void txt2img_main() {
		for (int i = 0; i < 10000; i++) {
			long time1, time2;

			time1 = System.currentTimeMillis();
//			txt2img_main_sub();
			txt2img_main_sub_random();
			time2 = System.currentTimeMillis();
			System.out.println("mission 花了：" + (time2 - time1) / 1000 + "秒");
		}
	}

	public void txt2img_main_sub() {
		String targetDir = "default\\";
		Set<CheckPoint> myCheckPoint = CheckPoint.getBy(CheckPointType.NONE);
		for (CheckPoint checkPoint : myCheckPoint) {
			txt2img_mainTask(checkPoint, Prompt.PORN_M_LEG, Arrays.asList(Lora.NONE.initWeight(0.1f, 1.0f)), 20,
					targetDir);// 影響不大
		}
	}

	public void txt2img_main_sub_random() {
		CheckPointType checkPointType = CheckPointType.LEGEND;
		LoraType loraType = LoraType.NONE;
		Prompt prompt = Prompt.PORN_NURSE;
		String targetDir = checkPointType.name() + "__" + loraType.name() + "__" + prompt.name()+"\\";
		txt2img_main_sub_random_do(checkPointType, loraType, prompt, targetDir);
	}

	
}
