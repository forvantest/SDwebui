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
public class LoraDTO {

	private String loraType = "LoRA";

	private String loraName="None";

	private Double uNet = 0d;

	private Double textEncoder = 0d;

	public LoraDTO() {
		super();
	}

	public LoraDTO(String loraName) {
		this.loraName = loraName;
	}

	public List<Object> pack() {
		List ay = new ArrayList<>();
		ay.add(loraType);
		ay.add(loraName);
		ay.add(uNet);
		ay.add(textEncoder);
		return ay;
	}

}
