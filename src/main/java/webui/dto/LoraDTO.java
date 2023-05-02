package webui.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import webui.dto.enumration.Lora;

@Slf4j
@JsonInclude(Include.NON_NULL)
@Data
public class LoraDTO {

	private String loraType = "LoRA";

	private Lora lora = Lora.NONE;

	private Double uNet = 0d;

	private Double textEncoder = 0d;

	public LoraDTO() {
		super();
	}

	public LoraDTO(Lora lora) {
		this.lora = lora;
	}

	public List<Object> pack() {
		List ay = new ArrayList<>();
		ay.add(loraType);
		ay.add(lora.getLoraName());
		ay.add(uNet);
		ay.add(textEncoder);
		return ay;
	}

}
