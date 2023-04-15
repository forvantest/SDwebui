package webui.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class ResultDTO {
	private List<Object> data;
	private Boolean is_generating = true;
	private Double duration;
	private Double average_duration;

	public ResultDTO() {
		super();
	}

}
