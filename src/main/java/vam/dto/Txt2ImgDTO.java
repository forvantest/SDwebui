package vam.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonInclude(Include.NON_NULL)
@Data
public class Txt2ImgDTO implements Comparable {

	public Txt2ImgDTO() {
		super();
	}

	private Boolean enable_hr = false;
	private Integer denoising_strength = 0;
	private Integer firstphase_width = 0;
	private Integer firstphase_height = 0;
	private Integer hr_scale = 0;
	private String hr_upscaler = "string";
	private Integer hr_second_pass_steps = 0;
	private Integer hr_resize_x = 0;
	private Integer hr_resize_y = 0;
	private String prompt = "string";
	private List<String> styles = new ArrayList<>();
	private Integer seed = -1;
	private Integer subseed = -1;
	private Integer subseed_strength = 0;
	private Integer seed_resize_from_h = -1;
	private Integer seed_resize_from_w = -1;
	private Integer batch_size = 1;
	private Integer n_iter = 1;
	private Integer steps = 50;
	private Integer cfg_scale = 7;
	private Integer width = 512;
	private Integer height = 512;
	private Boolean restore_faces = false;
	private Boolean tiling = false;
	private Boolean do_not_save_samples = false;
	private Boolean do_not_save_grid = false;
	private String negative_prompt = "string";

	private Integer eta = 0;
	private Integer s_churn = 0;
	private Integer s_tmax = 0;
	private Integer s_tmin = 0;
	private Integer s_noise = 0;
	private List<String> override_settings = new ArrayList<>();
	private Boolean override_settings_restore_afterwards = true;
	private List<String> script_args = new ArrayList<>();
	private Integer sampler_index = 0;
	private String sampler_name = "Euler a";
	private String script_name = "";
	private Boolean send_images = true;
	private Boolean save_images = false;
	private Set<String> alwayson_scripts = new HashSet<>();

	@Override
	public int compareTo(Object o) {
		return Integer.parseInt(prompt) - Integer.parseInt(prompt);
	}

}
