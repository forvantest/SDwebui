package webui.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import webui.dto.ModelDTO;
import webui.dto.PlayRecordDTO;
import webui.dto.PredictDTO;
import webui.dto.ResultDTO;
import webui.dto.ResultDetailDTO;
import webui.dto.SeedDTO;
import webui.dto.Txt2ImgDTO;
import webui.dto.enumration.CheckPoint;
import webui.dto.enumration.CheckPointType;
import webui.dto.enumration.Lora;
import webui.dto.enumration.LoraType;
import webui.dto.enumration.Prompt;
import webui.dto.enumration.Rescale;
import webui.dto.enumration.SampleName;
import webui.dto.enumration.TextualInversion;
import webui.dto.enumration.Vae;
import webui.util.FileUtil;
import webui.util.SDUtils;

@Slf4j
@Service("work")
public abstract class WorkServiceAbstract {

	@Value("${server.Host}")
	private String host;

	String endpoint1 = "/sdapi/v1/txt2img";
	String endpoint3 = "/sdapi/v1/sd-models";
	String endpoint2 = "/run/predict/";

	protected String WEBUI_SOME_PATH = "C:\\Games\\";

	@Autowired
	ModelService modelService;

	@Autowired
	public ObjectMapper objectMapper;

	public URI buildUri(String host, String endpoint) {
		return UriComponentsBuilder.newInstance().scheme("http").host(host).port(7860).path(endpoint).build().toUri();
	}

	private RetryTemplate retryTemplate(String strUrl) {
		final RetryTemplate retryTemplate = new RetryTemplate();
//		retryTemplate.setRetryPolicy(endpoint.retryPolicy());
//		retryTemplate.setBackOffPolicy(endpoint.backOffPolicy());
		RetryListener[] listenter = new RetryListener[] { new RetryListenerSupport() {
			@Override
			public <T extends Object, E extends Throwable> void onError(RetryContext context,
					RetryCallback<T, E> callback, Throwable throwable) {
//				boolean canRetry = endpoint.retryPolicy().canRetry(context);
//				log.warn("Request failed, canRetry={}, retryContext={}", canRetry, context);
			}
		} };
		retryTemplate.setListeners(listenter);
		return retryTemplate;
	}

	/**
	 * @param accessToken
	 * @return
	 */
	private HttpHeaders buildRequestHeader(final String accessToken) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (Objects.nonNull(accessToken)) {
			// headers.set(HEADER_AUTHORIZATION, BEARER_TYPE + accessToken);
		}
		return headers;
	}

	private ResponseEntity<String> callRestClientAPI(String endpoint, String requestJSON, HttpMethod method) {
		HttpHeaders headers = buildRequestHeader(null);
		final URI uri = buildUri(host, endpoint);
		try {
			final Stopwatch sw = Stopwatch.createStarted();

			final HttpEntity<String> request = new HttpEntity<>(requestJSON, headers);

			final RestTemplate template = new RestTemplate();
			final RetryTemplate retryTemplate = retryTemplate(endpoint);
			final ResponseEntity<String> response = retryTemplate
					.execute(retryContetx -> template.exchange(uri, method, request, String.class));

			if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
				log.debug("[Responded] Rest API: {}, time: {}", uri, sw.stop());
			} else {
				log.warn("[Request Error] Rest API {} responded with error: {}", uri, response.getBody());
//				throw new ServiceErrorException(response.getStatusCode().value(),
//						"[Request Error] Rest API responded with error", response.getBody());
			}

			return response;

		} catch (HttpClientErrorException clientError) {
			final String responseBody = clientError.getResponseBodyAsString();
			final String errMsg = clientError.getMessage();
			if (clientError.getStatusCode() == HttpStatus.NOT_FOUND) {
				log.warn("[Resource Not Found] Rest API: {}, asset: {}", uri, errMsg);
//				throw new ServiceErrorException(clientError.getStatusCode().value(),
//						String.format("Resource Not Found: %s", errMsg), responseBody);
			} else {
				log.warn("Connect to Rest API failed: {}, errorMsg: {}", uri, errMsg);
//				throw new ServiceErrorException(clientError.getStatusCode().value(),
//						String.format("Connect to rest API failed, errorMsg: %s", errMsg), responseBody);
			}
		} catch (HttpServerErrorException serverError) {
			final String responseBody = serverError.getResponseBodyAsString();
			final String errMsg = serverError.getMessage();
			log.warn("Connect to Rest API failed: {}, errorMsg: {}", uri, errMsg);
//			throw new ServiceErrorException(serverError.getStatusCode().value(),
//					String.format("Connect to Rest API failed, errorMsg: %s", errMsg), responseBody);
		} catch (RuntimeException e) {
			log.error("Connect to Rest API failed:{}, errorMsg: {}", uri, e.getMessage(), e);
		}
		return null;
	}

	private PlayRecordDTO makePlayRecordDTO(String prompt, String Negative_prompt, List<Lora> loraList,
			TextualInversion textualInversion, CheckPoint checkPoint, SampleName sampleName) {
		PlayRecordDTO playRecordDTO = new PlayRecordDTO();
		playRecordDTO.setPrompt(prompt);
		playRecordDTO.setNegative_prompt(Negative_prompt);
		playRecordDTO.setCheckPoint(checkPoint);
		playRecordDTO.setSamplerName(sampleName);
		playRecordDTO.setLoraList(loraList);
		playRecordDTO.getTextualInversionList().add(textualInversion);
		return playRecordDTO;
	}

	protected PlayRecordDTO txt2img(PlayRecordDTO playRecordDTO, int batch) {
		for (int i = 0; i < batch; i++) {
			log.info("txt2img {}:{} {}", playRecordDTO.getCheckPoint().name(), i,
					playRecordDTO.getSamplerName().name());
			String filename = doPost2(playRecordDTO);
			if (filename != null)
				log.info("txt2img done {}:{} {}", playRecordDTO.getCheckPoint().name(), i,
						playRecordDTO.getSamplerName().name());
			else {
				log.error("txt2img failed {}:{} {}", playRecordDTO.getCheckPoint().name(), i,
						playRecordDTO.getSamplerName().name());
				return null;
			}
		}
		return playRecordDTO;
	}

//	private boolean txt2img2(Prompt prompt, CheckPoint checkPoint, SampleName sampleName, int batch) {
//		Txt2ImgDTO txt2ImgDTO = new Txt2ImgDTO();
//		txt2ImgDTO.setPrompt(prompt.getPositive());
//		txt2ImgDTO.setNegative_prompt(prompt.getNegative());
//		txt2ImgDTO.setSampler_name(sampleName.getOpCode());
//		txt2ImgDTO.setWidth(prompt.getWidth());
//		txt2ImgDTO.setHeight(prompt.getHeight());
//		for (int i = 0; i < batch; i++) {
//			log.info("txt2img {}:{} {}", checkPoint.name(), i, sampleName.name());
//			boolean result1 = doPost3(txt2ImgDTO);
//			if (result1)
//				log.info("txt2img done {}:{} {}", checkPoint.name(), i, sampleName.name());
//			else {
//				log.error("txt2img failed {}:{} {}", checkPoint.name(), i, sampleName.name());
//				return false;
//			}
//		}
//		return true;
//	}

	private boolean doPost3(Txt2ImgDTO txt2ImgDTO) {
		ResponseEntity<String> rs = null;
		try {
			String requestJSON = objectMapper.writeValueAsString(txt2ImgDTO);
			rs = callRestClientAPI(endpoint1, requestJSON, HttpMethod.POST);
			if (rs.getStatusCodeValue() == 200) {
				ResultDetailDTO resultDetailDTO = objectMapper.readValue(rs.getBody(), ResultDetailDTO.class);
				List<String> objectList = resultDetailDTO.getImages();
//				Object firstObject = objectList.get(0);
//				if (firstObject instanceof List) {
//					List list = (List) firstObject;
//					Map map = (Map) list.get(0);
//					log.info("return {}", map.get("name"));
//				}
				return true;
			}
			log.info("post error: {}", rs.getStatusCodeValue());
			return false;
		} catch (UnrecognizedPropertyException e) {
			e.printStackTrace();
			return false;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return false;
		}
	}

	private String doPost2(PlayRecordDTO playRecordDTO) {
		try {
			PredictDTO predict = new PredictDTO();
			List<Object> objList = SDUtils.toDataList(1, 1, playRecordDTO);
			if (playRecordDTO.getHiresFix())
				SDUtils.appendHiRES(objList, playRecordDTO);
			predict.setData(objList);
			String requestJSON = objectMapper.writeValueAsString(predict);
			ResponseEntity<String> rs = callRestClientAPI(endpoint2, requestJSON, HttpMethod.POST);
			if (rs == null) {
				log.info("post error: {}", rs.getStatusCodeValue());
			} else if (rs.getStatusCodeValue() == 200) {
				ResultDTO resultDTO = objectMapper.readValue(rs.getBody(), ResultDTO.class);
				List<Object> objectList = resultDTO.getData();
				Object firstObject = objectList.get(0);
				if (firstObject instanceof List) {
					List list = (List) firstObject;
					Map map = (Map) list.get(0);
					log.debug("return file name {}", map.get("name"));
					playRecordDTO.setFullpath((String) map.get("name"));
					// playRecordService.save(playRecordDTO);
				}
				return playRecordDTO.getFilename();
			}
			log.info("post error: {}", rs.getStatusCodeValue());
			return null;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected void switchCheckPoint(CheckPoint checkPoint) {
		if (checkPoint.equals(modelService.checkPointNow))
			return;
		modelService.checkPointNow = checkPoint;
		log.warn("switchCheckPoint: {}", checkPoint);
		List<Object> data = new ArrayList<>();
		data.add(checkPoint.getFilename());
		PredictDTO predict = new PredictDTO(641, data);
		predict.setSession_hash("h9i4ycj51ig");
		boolean result1 = doPost1(predict);

		if (result1) {
			data = new ArrayList<>();
			predict = new PredictDTO(644, data);
			predict.setSession_hash("h9i4ycj51ig");
			boolean result2 = doPost1(predict);
		}
	}

	protected void switchVae(Vae vae) {
		if (vae==modelService.vae)
			return;
		modelService.vae = vae;
		log.warn("switchVae: {}", vae);
		List<Object> data = new ArrayList<>();
		data.add(vae.getVaeDesc());
		PredictDTO predict = new PredictDTO(642, data);
		predict.setSession_hash("h9i4ycj51ig");
		boolean result1 = doPost1(predict);

		if (result1) {
			data = new ArrayList<>();
			predict = new PredictDTO(644, data);
			predict.setSession_hash("h9i4ycj51ig");
			boolean result2 = doPost1(predict);
		}
	}

	protected void switchClipSkip(Integer clip_skip) {
		if (clip_skip.equals(modelService.clip_skip))
			return;
		modelService.clip_skip = clip_skip;
		log.warn("switchClipSkip: {}", clip_skip);
		List<Object> data = new ArrayList<>();
		data.add(clip_skip);
		PredictDTO predict = new PredictDTO(643, data);
		predict.setSession_hash("ty9xy1um1sl");
		boolean result1 = doPost1(predict);

		if (result1) {
			data = new ArrayList<>();
			predict = new PredictDTO(644, data);
			predict.setSession_hash("ty9xy1um1sl");
			boolean result2 = doPost1(predict);
		}
	}

	private boolean doPost1(PredictDTO predict) {
		try {
			String requestJSON = objectMapper.writeValueAsString(predict);
			ResponseEntity<String> rs = callRestClientAPI(endpoint2, requestJSON, HttpMethod.POST);
			if (rs.getStatusCodeValue() == 200) {
				log.info("post done");
				return true;
			}
			log.info("post error: {}", rs.getStatusCodeValue());
			return false;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return false;
		}
	}

	protected List<ModelDTO> doPost3() {
		try {
			ResponseEntity<String> rs = callRestClientAPI(endpoint3, "", HttpMethod.GET);
			if (rs.getStatusCodeValue() == 200) {
				List<ModelDTO> modelDTOList = objectMapper.readValue(rs.getBody(), new TypeReference<List<ModelDTO>>() {
				});
				log.info("post done models:{}", modelDTOList.size());
				return modelDTOList;
			}
			log.info("post error: {}", rs.getStatusCodeValue());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void txt2img_main_sub_random_do(CheckPointType checkPointType, LoraType loraType, Prompt prompt,
			TextualInversion textualInversion, String targetDir) {
		List<SeedDTO> aySeed = new ArrayList<>();

		Set<CheckPoint> myCheckPoint = CheckPoint.getBy(checkPointType);
		// CheckPoint checkPoint = randomCheckPoint(myCheckPoint);
		Set<Lora> myLora = Lora.getBy(loraType);

		Set<Rescale> myRescale = Sets.newHashSet(Rescale.values());
		// Lora lora = randomLora(myLora);
		for (CheckPoint checkPoint1 : myCheckPoint) {
			for (Lora lora1 : myLora) {
				aySeed.add(new SeedDTO(checkPoint1, lora1));
			}
		}
		int total_size = aySeed.size();
		System.out.println("\n\n total seed: " + total_size);

		for (int i = 0; i < 10000; i++) {
			SeedDTO seedDTO = randomSeed(aySeed);
			if (Objects.isNull(seedDTO))
				break;
			log.info("{} {} {}/{}", seedDTO.getCheckPoint(), seedDTO.getLora(), i, total_size);

			txt2img_mainTask(seedDTO.getCheckPoint(), prompt, Arrays.asList(seedDTO.getLora().initWeight(0.2f, 1.1f)),
					textualInversion, targetDir);// 影響不大
		}
	}

	private SeedDTO randomSeed(List<SeedDTO> aySeed) {
		if (aySeed.size() == 0)
			return null;

		int index = (int) (Math.random() * aySeed.size());
		SeedDTO seedDTO = aySeed.get(index);
		aySeed.remove(index);
		return seedDTO;
	}

	protected CheckPoint randomCheckPoint(Set<CheckPoint> myCheckPoint) {
		CheckPoint[] ayCheckPoint = myCheckPoint.toArray(new CheckPoint[myCheckPoint.size()]);
		int index = (int) (Math.random() * ayCheckPoint.length);
		return ayCheckPoint[index];
	}

	private Lora randomLora(Set<Lora> mySingleLora) {
		Lora[] ayLora = mySingleLora.toArray(new Lora[mySingleLora.size()]);
		int index = (int) (Math.random() * ayLora.length);
		return ayLora[index];
	}

//	public void txt2img_main_sub1() {
//		Set<Lora> mySingleLora = Lora.getBy(LoraType.PUSSY);
//// Lora.CREAMPIEHAIRYPUSSY_CREAMPIEV11 best 0.6
////Lora.POVMISSIONARYVAGINAL_V1 影響人物卡通化
////Lora.SPREADPUSSY_V11 一般anal 不太好用 0.7
////Lora.PUSSYSPREAD_V01 不太好用 0.4
////Lora.REALISTICVAGINASGOD_GODPUSSY1V1 還可以 0.6極限
//		String targetDir = "default\\";
//		for (Lora myLora : mySingleLora) {
//			txt2img_mainTask(Sets.newHashSet(CheckPoint._2GUOFENG2_V20), Prompt.PORN_M_LEG,
//					Arrays.asList(myLora.initWeight(0.2f, 0.7f)), 20, targetDir);// 影響不大
//			txt2img_mainTask(Sets.newHashSet(CheckPoint._3GUOFENG3_V32LIGHT), Prompt.PORN_M_LEG,
//					Arrays.asList(myLora.initWeight(0.2f, 0.7f)), 20, targetDir);// 影響不大
//			txt2img_mainTask(Sets.newHashSet(CheckPoint._3Guofeng3_v33), Prompt.PORN_M_LEG,
//					Arrays.asList(myLora.initWeight(0.2f, 0.7f)), 20, targetDir);// 影響不大
//		}
//	}

	public void txt2img_main_sub2() {
//		txt2img_mainTask(Sets.newHashSet(CheckPoint.V08_V08), Prompt.PORN_NURSE,
//				Arrays.asList(Lora.KOREANDOLLLIKENESS_V15.initWeight(0.1f, 0.9f)), 20);

//		txt2img_mainTask(Sets.newHashSet(CheckPoint._3Guofeng3_v33), Prompt.PORN_M_LEG,
//				Arrays.asList(Lora.CREAMPIEHAIRYPUSSY_CREAMPIEV11.initWeight(0.1f, 1.0f),
//						Lora.SPREADPUSSY_V11.initWeight(0.1f, 1.0f)),
//				20);

		Set<Lora> mySingleLora = new LinkedHashSet<>(Arrays.asList(Lora.ASIANGIRLXYBOBOLORA_NOTUPDATE,
				Lora.ASIANGIRLZHAOXMLORA_NOTUPDATE, Lora.MINESFIXASIANLIKENESS_V10, Lora.SHOJOVIBE_V11));
		// Lora.ANGELABABY_1 不像
		// Lora.ASIAGIRLINUNIFORM_CHILLOUTMIX 不夠好
		// Lora.ASIANGIRLSABRINALORA_NOTUPDATE 不夠好
		// Lora.ASIANGIRLXYBOBOLORA_NOTUPDATE 好一點 0.9-->
		// Lora.ASIANGIRLZHAOXMLORA_NOTUPDATE 好一點 0.9-->
		// Lora.EASTASIANDOLL_V40 卡通
		// Lora.FASHIONGIRL_V51
		// Lora.HIPOLY3DMODELLORA_V10 醜
		// Lora.JAPANESE_DOLL_LIKENESS 不夠好
		// Lora.KRISWUEXGIRLFRIEND_V10 下巴短
		// Lora.MIKUYA_V15 尚可 出圖率高
		// Lora.MINESFIXASIANLIKENESS_V10 尚可 出圖率高
		// Lora.SHOJOVIBE_V11 best 國光
		// Lora.SOPHONJAPENESEGIRL_SOPHONV12 太老

		// Lora.FRAMEBINDER_V10, 綑綁
		// Lora.MLEGGESTUREULTIMATE_V51, M_Leg 0.2
		// Lora.STANDINGDOGGYSTYLE_V11A, 站立後背 0.2
		for (Lora myLora : mySingleLora) {
//			txt2img2(Prompt.PORN_M_LEG, CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX, SampleName.DPM2_A, 1);

//			txt2img_mainTask(Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX), Prompt.PORN_M_LEG,
//					Arrays.asList(myLora.initWeight(0.1f, 2.0f)), 20);// 影響不大
		}

//		txt2img_mainTask(Sets.newHashSet(CheckPoint._3Guofeng3_v33), Prompt.PORN_M_LEG,
//				Arrays.asList(Lora.CREAMPIEHAIRYPUSSY_CREAMPIEV11.initWeight(0.1f, 0.7f)), 35);

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.UBERREALISTICPORNMERGE_URPMV13), Prompt.PORN_GIRL2,
//				Arrays.asList(Lora.KOREANDOLLLIKENESS_V15.initWeight(0.4f, 0.9f),
//						Lora.TAIWANDOLLLIKENESS_V10.initWeight(0.1f, 0.7f)),
//				35);

//		txt2img_mainTask(Sets.newHashSet(CheckPoint._3Guofeng3_v33), Prompt.PORN_M_LEG,
//				Arrays.asList(Lora.AHEGAOROLLINGEYES_V1114.initWeight(0.0f, 0.4f)), 20);

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX), Prompt.PORN_GIRL5,
//				Arrays.asList(Lora.AHEGAOROLLINGEYES_V1114.initWeight(0.0f, 0.4f)), 20);//影響不大

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX), Prompt.PORN_GIRL5,
//				Arrays.asList(Lora.TAIWANDOLLLIKENESS_V10.initWeight(0.1f, 0.7f)), 35);//很明顯的影響

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX), Prompt.PORN_GIRL5,
//				Arrays.asList(Lora.KOREANDOLLLIKENESS_V15.initWeight(0.1f, 1.0f)), 35);//很明顯的影響

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX), Prompt.PORN_GIRL5,
//				Arrays.asList(Lora.CUTEGIRLMIX4_V10.initWeight(0.1f, 0.6f)), 35);

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX), Prompt.PORN_GIRL5,
//				Arrays.asList(Lora.CUTEKOREANGIRLLORA_CUTEKOREANGIRLLORA.initWeight(0.2f, 1.0f)), 35);//再高 臉會太大

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX), Prompt.PORN_GIRL5,
//				Arrays.asList(Lora.KBEAUKOREANBEAUTY_V15.initWeight(0.7f, 1.0f)), 20);//影響不大

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX), Prompt.PORN_GIRL5,
//				Arrays.asList(Lora._3LORAGUOFENG3LORA_V32LORABIGLIGHT.initWeight(0.1f, 1.0f)), 20);//影響不大

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX), Prompt.PORN_GIRL5,
//				Arrays.asList(Lora.CREAMPIEHAIRYPUSSY_CREAMPIEV11.initWeight(0.1f, 1.0f)), 20);//容易Bx2

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.FANTASTICMIXREAL_V10), Prompt.PORN_M_LEG,
//				Arrays.asList(Lora.AHEGAOROLLINGEYES_V1114.initWeight(0.0f, 0.4f)), 20);

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.CHIKMIX_V1), Prompt.PORN_M_LEG,
//				Arrays.asList(Lora.AHEGAOROLLINGEYES_V1114.initWeight(0.0f, 0.4f)), 20);

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.CHIKMIX_V2), Prompt.PORN_M_LEG,
//				Arrays.asList(Lora.AHEGAOROLLINGEYES_V1114.initWeight(0.0f, 0.4f)), 20);

//		txt2img_mainTask(Sets.newHashSet(CheckPoint.V08_V08A), Prompt.PORN_M_LEG,
//				Arrays.asList(Lora.AHEGAOROLLINGEYES_V1114.initWeight(0.0f, 0.4f)), 20);
	}

//	public void txt2img_mainTask(Set<CheckPoint> myCheckPoint, Prompt prompt, List<Lora> myLora,
//			TextualInversion textualInversion, Integer step, String targetDir) {
//		for (CheckPoint checkPoint : myCheckPoint) {
//			txt2img_mainTask(checkPoint, prompt, myLora, textualInversion, step, targetDir);
//		}
//	}

	public void txt2img_mainTask(CheckPoint checkPoint, Prompt prompt, java.util.List<Lora> myLora,
			TextualInversion textualInversion, String targetDir) {
		System.out.println("\n\n checkpoint: " + checkPoint.name());
		switchCheckPoint(checkPoint);

		for (int i = 0; i < 10000; i++) {
			txt2img_mainTask_render(checkPoint, prompt, myLora, textualInversion, targetDir);
			if (myLora.get(0).getWeightEnd() == null) {
				break;
			} else if (!nextWeight(myLora)) {
				for (Lora lora : myLora) {
					System.out.print("  " + lora.appendLora());
				}
			} else {
				break;
			}
		}
		System.out.println("\n\n finish: " + checkPoint.name());
	}

	private boolean nextWeight(List<Lora> myLora) {
		Lora lora0 = null;// myLora.get(0);
		Lora lora1 = null;// myLora.get(1);
		Lora lora2 = null;// myLora.get(2);

		if (myLora.size() >= 1)
			lora0 = myLora.get(0);
		if (myLora.size() >= 2)
			lora1 = myLora.get(1);
		if (myLora.size() >= 3)
			lora2 = myLora.get(2);

		if (nextWeight(lora0))
			if (nextWeight(lora1))
				if (nextWeight(lora2))
					return true;

		return false;
	}

	private boolean nextWeight(Lora lora) {
		if (lora == null)
			return true;
		if (lora.getWeightEnd() == null)
			return false;

		if (lora.getWeight() + 0.1f > lora.getWeightEnd()) {
			lora.setWeight(lora.getWeightStart());
			return true;
		} else {
			lora.setWeight(lora.getWeight() + 0.1f);
			return false;
		}
	}

	public void txt2img_mainTask_render(CheckPoint checkPoint, Prompt prompt, List<Lora> loraList,
			TextualInversion textualInversion, String targetDir) {
//		List<SampleName> mySample = Arrays.asList(SampleName.DPM_PLUS_SDE_KARRAS, SampleName.DPM_PLUS_2M_KARRAS,
//				SampleName.DPM_PLUS_2S_A_KARRAS, SampleName.EULER_A);
		List<SampleName> mySample = Arrays.asList(SampleName.DPM_PLUS_2M_KARRAS);
		for (SampleName sampleName : mySample) {
			PlayRecordDTO playRecordDTO = makePlayRecordDTO(prompt.getPositive(), prompt.getNegative(), loraList,
					textualInversion, checkPoint, sampleName);
			txt2img(playRecordDTO, 1);
			if (playRecordDTO == null) {
				System.out.println("\n\n work failed!");
				break;
			} else {
				FileUtil.moveFileTo(WEBUI_SOME_PATH + targetDir, playRecordDTO, "txt2img over");
			}
		}
	}

	public void txt2img_test() {
		List<SampleName> mySample = Arrays.asList(SampleName.DPM_PLUS_SDE_KARRAS, SampleName.DPM_PLUS_2M_KARRAS,
				SampleName.DPM_PLUS_2S_A_KARRAS, SampleName.EULER_A);
//		List<SampleName> mySample = Arrays.asList( SampleName.DPM_PLUS_2M_KARRAS);
		// CheckPoint[] myCheckPoint = CheckPoint.getSortedValues();
		Set<CheckPoint> myCheckPoint = Sets.newHashSet(CheckPoint.UBERREALISTICPORNMERGE_URPMV13);
//		Set<CheckPoint> myCheckPoint = Prompt.PORN_M_LEG.getCheckPointSet();

		HashSet<Lora> myLora = new LinkedHashSet<>(Arrays.asList(Lora.KOREAN_BEAUTIFULGIRL));

//		myLora.add(Lora.TAIWANDOLLLIKENESS_V10);
//		myLora.add(Lora.CUTEGIRLMIX4_V10);//  0.4 0.5 0.6
//		myLora.add(Lora.CUTEKOREANGIRLLORA_CUTEKOREANGIRLLORA);//  0.4 0.5 0.6 固定臉
		/*
		 * myLora.add(Lora.KOREAN_BEAUTIFULGIRL);//perfect good 0.3 0.9 //
		 * myLora.add(Lora.KOREAN_DOLL_LIKENESS);//冷白 0.4 0.5 0.6, but still 外國人 //
		 * myLora.add(Lora.KOREANDOLLLIKENESS_V10066);//good, but still 外國人
		 * myLora.add(Lora.KOREANDOLLLIKENESS_V15);//very good 0.6 ->
		 * myLora.add(Lora.KOREANGALLOCONLORA_1);//very good 0.4 0.5 -> //
		 * myLora.add(Lora.KOREANGIRLS_KGIRLSCC); //
		 * myLora.add(Lora.KBEAUKOREANBEAUTY_V15);//0.5 0.6 完全外國人 //
		 * myLora.add(Lora.EASTASIANDOLL_V40); //0.3 0.5 0.6 不夠亞洲
		 */
		TextualInversion textualInversion = TextualInversion.NONE;
		for (int i = 0; i < 10000; i++) {
			for (CheckPoint checkPoint : myCheckPoint) {
				System.out.println("\n\n checkpoint: " + checkPoint.name());
				switchCheckPoint(checkPoint);
				for (Lora lora : myLora) {
					for (float j = 0.1f; j <= 1.2f; j += 0.1f) {
						if (lora == Lora.NONE)
							lora.setWeight(0.4f);
						else
							lora.setWeight(j);

						if (textualInversion == TextualInversion.NONE)
							textualInversion.setWeight(0.7f);
						else
							textualInversion.setWeight(j);

						for (SampleName sampleName : mySample) {
//							PlayRecordDTO playRecordDTO = txt2img(Prompt.PORN_GIRL5, lora, textualInversion, checkPoint,
//									sampleName, 1, 35);
//							if (playRecordDTO == null) {
//								System.out.println("\n\n work failed!");
//								break;
//							} else {
//								FileUtil.moveFileTo(WEBUI_SOME_PATH, playRecordDTO, "txt2img over");
//							}
						}
//						if (lora == Lora.NONE)
//							break;
					}
				}
				System.out.println("\n\n finish: " + checkPoint.name());
			}
		}
	}
}
