package vam.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import vam.dto.OperatorDTO;
import vam.entity.Operator;
import vam.util.MapperUtils;

@Service
public class OperatorService {

	@Autowired
	OperatorRepository operatorRepository;

	@Autowired
	MapperUtils mapperUtils;

	public List<OperatorDTO> findBy(OperatorDTO operatorDTOQuery) {
		List<Operator> operatorList = findEnitiy(operatorDTOQuery);
		List<OperatorDTO> operatorDTOList = operatorList.stream().map(e -> mapperUtils.convertOperatorDTO(e))
				.collect(Collectors.toList());
		return operatorDTOList;
	}

	public List<OperatorDTO> findByName(OperatorDTO operatorDTO) {
		List<Operator> operatorList = operatorRepository.findByKey(operatorDTO.getKey());
		List<OperatorDTO> operatorDTOList = operatorList.stream().map(e -> mapperUtils.convertOperatorDTO(e))
				.collect(Collectors.toList());
		return operatorDTOList;
	}

	private List<Operator> findEnitiy(OperatorDTO operatorDTOQuery) {
		List<Operator> operatorList = operatorRepository.findByKey(operatorDTOQuery.getKey());
		return operatorList;
	}

	public Long count() {
		return operatorRepository.count();
	}

	public void save(OperatorDTO operatorDTO) {
		operatorRepository.save(mapperUtils.convertOperator(operatorDTO));
	}

	public void update(OperatorDTO operatorDTO) {
		List<Operator> list = findEnitiy(operatorDTO);
		if (!CollectionUtils.isEmpty(list)) {
			Operator operator = list.get(0);
			BeanUtils.copyProperties(operatorDTO, operator);
			operatorRepository.save(operator);
		}
	}

	public void delete(OperatorDTO operatorDTO) {
		List<Operator> list = findEnitiy(operatorDTO);
		if (!CollectionUtils.isEmpty(list)) {
			Operator operator = list.get(0);
			operatorRepository.delete(operator);
		}
	}

	public List<OperatorDTO> findAll() {
		List<Operator> list = operatorRepository.findAll();
		return list.stream().map(mapperUtils::convertOperatorDTO).collect(Collectors.toList());
	}

	public List<OperatorDTO> findByAuthor(String sourceDirectory) {
		List<Operator> list = operatorRepository.findByKey(sourceDirectory);
		return list.stream().map(mapperUtils::convertOperatorDTO).collect(Collectors.toList());
	}

}