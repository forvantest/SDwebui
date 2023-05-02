package webui.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import webui.dto.ModelDTO;
import webui.dto.enumration.CheckPoint;
import webui.dto.enumration.Vae;

@Slf4j
@Service("modelService")
public class ModelService extends WorkServiceAbstract {

	public static Integer clip_skip;
	public static Vae vae;
	public static CheckPoint checkPointNow = null;
	static Map<CheckPoint, ModelDTO> mapModel = new HashMap<>();
	static Map<String, ModelDTO> mapHash = new HashMap<>();

	public void loadModel() {
		List<ModelDTO> modelDTOList = doPost3();
		for (ModelDTO modelDTO : modelDTOList) {
			CheckPoint checkPoint = CheckPoint.findByFilename(modelDTO.getModel_name());
			mapModel.put(checkPoint, modelDTO);
			mapHash.put(modelDTO.getHash(), modelDTO);
		}
	}

	public static String getModel(CheckPoint checkPoint) {
		ModelDTO modelDTO = mapModel.get(checkPoint);
		String ss = String.format("Model hash: %s", modelDTO.getHash());
		return ss;
	}

	public static CheckPoint getByHash(String hash) {
		ModelDTO modelDTO = mapHash.get(hash);
		CheckPoint checkPoint = CheckPoint.findByFilename(modelDTO.getModel_name());
		return checkPoint;
	}

}
