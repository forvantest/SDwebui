package webui.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class ModelDTO {
	private String title;
	private String model_name;
	private String hash;
	private String sha256;
	private String filename;
	private String config;

	public ModelDTO() {
		super();
	}

}
