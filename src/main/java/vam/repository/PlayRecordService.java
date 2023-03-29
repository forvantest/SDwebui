package vam.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import vam.dto.PlayRecordDTO;
import vam.entity.PlayRecord;
import vam.util.MapperUtils;

@Service
public class PlayRecordService {

	@Autowired
	PlayRecordRepository playRecordRepository;

	@Autowired
	MapperUtils mapperUtils;

	public List<PlayRecordDTO> findBy(PlayRecordDTO playRecordDTOQuery) {
		List<PlayRecord> playRecordList = findEnitiy(playRecordDTOQuery);
		List<PlayRecordDTO> playRecordDTOList = playRecordList.stream().map(e -> mapperUtils.convertOperatorDTO(e))
				.collect(Collectors.toList());
		return playRecordDTOList;
	}

	public List<PlayRecordDTO> findByName(PlayRecordDTO playRecordDTO) {
		List<PlayRecord> playRecordList = playRecordRepository.findByKey(playRecordDTO.getKey());
		List<PlayRecordDTO> playRecordDTOList = playRecordList.stream().map(e -> mapperUtils.convertOperatorDTO(e))
				.collect(Collectors.toList());
		return playRecordDTOList;
	}

	private List<PlayRecord> findEnitiy(PlayRecordDTO playRecordDTOQuery) {
		List<PlayRecord> playRecordList = playRecordRepository.findByKey(playRecordDTOQuery.getKey());
		return playRecordList;
	}

	public Long count() {
		return playRecordRepository.count();
	}

	public void save(PlayRecordDTO playRecordDTO) {
		playRecordRepository.save(mapperUtils.convertPlayRecord(playRecordDTO));
	}

	public void update(PlayRecordDTO playRecordDTO) {
		List<PlayRecord> list = findEnitiy(playRecordDTO);
		if (!CollectionUtils.isEmpty(list)) {
			PlayRecord playRecord = list.get(0);
			BeanUtils.copyProperties(playRecordDTO, playRecord);
			playRecordRepository.save(playRecord);
		}
	}

	public void delete(PlayRecordDTO playRecordDTO) {
		List<PlayRecord> list = findEnitiy(playRecordDTO);
		if (!CollectionUtils.isEmpty(list)) {
			PlayRecord playRecord = list.get(0);
			playRecordRepository.delete(playRecord);
		}
	}

	public List<PlayRecordDTO> findAll() {
		List<PlayRecord> list = playRecordRepository.findAll();
		return list.stream().map(mapperUtils::convertOperatorDTO).collect(Collectors.toList());
	}

	public List<PlayRecordDTO> findByAuthor(String sourceDirectory) {
		List<PlayRecord> list = playRecordRepository.findByKey(sourceDirectory);
		return list.stream().map(mapperUtils::convertOperatorDTO).collect(Collectors.toList());
	}

}