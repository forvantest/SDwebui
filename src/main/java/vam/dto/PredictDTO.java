package vam.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonInclude(Include.NON_NULL)
@Data
public class PredictDTO implements Comparable {

	public PredictDTO() {
		super();
	}

	private Integer fn_index = 211;
	private List<Object> data = new ArrayList<>();
	private String session_hash = "kw8l71zrw3d";

	public PredictDTO(Integer fn_index, List<Object> data) {
		super();
		this.fn_index = fn_index;
		this.data = data;
	}
	
	@Override
	public int compareTo(Object o) {
		PredictDTO predictDTO = (PredictDTO) o;
		return fn_index - predictDTO.getFn_index();
	}


}
