package webui.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import webui.dto.enumration.CheckPoint;
import webui.dto.enumration.Lora;

@JsonInclude(Include.NON_NULL)
@Data
public class SeedDTO {
	private CheckPoint checkPoint ;
	private Lora lora;

	public SeedDTO() {
		super();
	}

	public SeedDTO(CheckPoint checkPoint, Lora lora) {
		this.checkPoint=checkPoint;
		this.lora=lora;
	}

}
