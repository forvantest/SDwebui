package vam.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class ResultDetailDTO {

	private List<String> images = new ArrayList<>();
	private Txt2ImgDTO parameters;

	private String info;

	public ResultDetailDTO() {
		super();
	}

}
