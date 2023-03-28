package vam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonInclude(Include.NON_NULL)
@Data
public class OutputFile implements Comparable {

	public OutputFile() {
		super();
	}

	private String data = "file=C:\\Games\\txt2img-images\\01404-3998181171-1boy, 1girl, arms behind back,  hetero, vaginal, sex, mating press, folded, missionary, head on pillow, spread legs, m_legs, nav.png";
	private Boolean is_file = true;
	private String name = "C:\\Games\\txt2img-images\\01404-3998181171-1boy, 1girl, arms behind back,  hetero, vaginal, sex, mating press, folded, missionary, head on pillow, spread legs, m_legs, nav.png";

	@Override
	public int compareTo(Object o) {
		return -1;
	}

}
