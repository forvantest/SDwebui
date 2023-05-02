package webui.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import webui.dto.PlayRecordDTO;
import webui.entity.PlayRecord;

@Slf4j
@Component
public class MapperUtils {

	@Autowired
	public ObjectMapper objectMapper;

	public boolean usefull(String key) {
		if (StringUtils.endsWith(key, ":"))
			return false;

		if ("self".equals(key.toLowerCase()))
			return false;

		if ("custom".equals(key.toLowerCase()))
			return false;

		return true;
	}

	public PlayRecordDTO convertOperatorDTO(PlayRecord e) {
		// TODO Auto-generated method stub
		return null;
	}

	public PlayRecord convertPlayRecord(PlayRecordDTO playRecordDTO) {
		PlayRecord playRecord = new PlayRecord();
		BeanUtils.copyProperties(playRecordDTO, playRecord);
		playRecord.setPrompt(playRecordDTO.getPrompt());
		playRecord.setNegative_prompt(playRecordDTO.getNegative_prompt());
		playRecord.setTask(findTask(playRecordDTO.getFilename()));
		playRecord.setCheckPoint(playRecordDTO.getCheckPoint().getFilename());
		playRecord.setSampler_name(playRecordDTO.getSamplerName().getOpCode());
		return playRecord;
	}

	private String findTask(String filename) {
		int idx_start = StringUtils.indexOf(filename, "txt2img-images") + 15;
		int idx_end = StringUtils.indexOf(filename, "-", idx_start + 10);
		String task = StringUtils.substring(filename, idx_start, idx_end);
		return task;
	}

}
