package webui.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class OutputFileDTO implements Comparable {

	private String name = "C:\\Games\\txt2img-images\\01404-3998181171-1boy, 1girl, arms behind back,  hetero, vaginal, sex, mating press, folded, missionary, head on pillow, spread legs, m_legs, nav.png";
	private String data = "file=C:\\Games\\txt2img-images\\01404-3998181171-1boy, 1girl, arms behind back,  hetero, vaginal, sex, mating press, folded, missionary, head on pillow, spread legs, m_legs, nav.png";
	private Boolean is_file = true;

	public OutputFileDTO() {
		super();
	}

	public OutputFileDTO(String name, String data, Boolean is_file) {
		super();
		this.name = name;
		this.data = data;
		this.is_file = is_file;
	}

	@Override
	public int compareTo(Object o) {
		return -1;
	}

}
