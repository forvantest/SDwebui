package webui.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import webui.dto.ModelDTO;
import webui.dto.enumration.CheckPoint;

@Slf4j
@Service("modelService")
public class ModelService extends WorkServiceAbstract {

	public static CheckPoint checkPointNow = null;
	static Map<CheckPoint, ModelDTO> mapModel = new HashMap<>();

	public void loadModel() {
		List<ModelDTO> modelDTOList = doPost3();
		for (ModelDTO modelDTO : modelDTOList) {
			CheckPoint checkPoint = CheckPoint.findByFilename(modelDTO.getFilename());
			mapModel.put(checkPoint, modelDTO);
		}
	}

	public static String getModel(CheckPoint checkPoint) {
		ModelDTO modelDTO = mapModel.get(checkPoint);
		String ss = String.format("%s %s", modelDTO.getModel_name(), modelDTO.getHash());
		return ss;
	}

}
