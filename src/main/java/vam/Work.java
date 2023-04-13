package vam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import vam.dto.MetaJson;
import vam.dto.PlayRecordDTO;
import vam.dto.PredictDTO;
import vam.dto.ResultDTO;
import vam.dto.ResultDetailDTO;
import vam.dto.Txt2ImgDTO;
import vam.dto.VarFileDTO;
import vam.dto.enumration.BestGirl;
import vam.dto.enumration.BestScene;
import vam.dto.enumration.CheckPoint;
import vam.dto.enumration.Lora;
import vam.dto.enumration.Prompt;
import vam.dto.enumration.SampleName;
import vam.dto.enumration.TextualInversion;
import vam.dto.meta.Dependence;
import vam.util.FileUtil;
import vam.util.SDUtils;

@Slf4j
@Service("work")
public class Work extends WorkDeployVarFile {

	@Value("${server.Host}")
	private String host;

	String endpoint1 = "/sdapi/v1/txt2img";
	String endpoint2 = "/run/predict/";

	@Autowired
	public WorkUnDeployVarFile workUnDeployVarFile;

	public void allHide(String hideDirectrory) {
		File dir = new File(VAM_ROOT_PATH + hideDirectrory);
		Set<String> varFileRefSet = fetchAllVarFiles(dir, VAR_EXTENSION);
		// varFileRefSet.forEach(v -> v.realHide(VAM_FILE_PREFS));
	}

	public void allUnHide(String hideDirectrory) {
		File dir = new File(VAM_ROOT_PATH + hideDirectrory);
		Set<String> varFileRefSet = fetchAllVarFiles(dir, VAR_EXTENSION);
		// list.forEach(v -> v.unHide(VAM_FILE_PREFS));
	}

	public void loadVarFileIntoDB(String targetDirectrory) {
		File dir = new File(VAM_ALLPACKAGES_PATH);
		allVarFilesToDB(dir);
		Long count = varFileService.count();
		System.out.println("total:" + count);
	}

	public void unDeploy(BestGirl bestGirl) {
		Map<String, VarFileDTO> mLack = new HashMap<>();
		Map<String, Set<String>> msLack = new HashMap<>();
		unDeploy(bestGirl.getDescription(), mLack, msLack);
		List<String> ls = msLack.keySet().stream().collect(Collectors.toList());
		Collections.sort(ls);
		ls.forEach(v -> {
			System.out.println("--- old depenence missing: " + v + "\t\t" + msLack.get(v).toString());
		});
	}

	public void unDeploy(String targetDirectory, Map<String, VarFileDTO> mLack, Map<String, Set<String>> msLack) {
		for (String key : mLack.keySet()) {
			Set<String> ss = msLack.get(key);
			if (Objects.isNull(ss)) {
				ss = new HashSet<>();
				msLack.put(key, ss);
			}
			ss.add(targetDirectory);
		}
		mLack.putAll(workUnDeployVarFile.process(targetDirectory, targetDirectory));
	}

	public void moveReference(String author) {
		Map<String, VarFileDTO> mLack = new HashMap<>();
		Map<String, Set<String>> msLack = new HashMap<>();
		BestGirl[] ea = BestGirl.values();
		if (Objects.isNull(author)) {
			for (int i = 0; i < ea.length; i++) {
				unDeploy(ea[i].getDescription(), mLack, msLack);
			}
			BestScene[] es = BestScene.values();
			for (int i = 0; i < es.length; i++) {
				unDeploy(es[i].getDescription(), mLack, msLack);
			}
		} else {
			unDeploy(author, mLack, msLack);
		}
		List<String> ls = msLack.keySet().stream().collect(Collectors.toList());
		Collections.sort(ls);
		ls.forEach(v -> {
			System.out.println("--- old depenence missing: " + v + "\t\t" + msLack.get(v).toString());
		});
	}

	public void deploy(String groupName) {
		BestGirl[] ea = BestGirl.values();
		for (int i = 0; i < ea.length; i++) {
			deployBestGirl(ea[i], groupName);
		}
		BestScene[] es = BestScene.values();
		for (int i = 0; i < es.length; i++) {
			deployBestScene(es[i], groupName);
		}
		switchAuthor(null, groupName);
	}

	public void deployBestGirl(BestGirl bestGirl, String groupName) {
		process(bestGirl.getDescription() + "/", groupName);
		if (StringUtils.isEmpty(groupName))
			switchAuthor(null, bestGirl.getDescription());
	}

	public void deployBestScene(BestScene bestScene, String groupName) {
		process(bestScene.getDescription() + "/", groupName);
		if (StringUtils.isEmpty(groupName))
			switchAuthor(null, bestScene.getDescription());
	}

	public void deployBestSceneGirl(BestScene bestScene, BestGirl bestGirl, String groupName) {
		process(bestScene.getDescription(), groupName);
		process(bestGirl.getDescription(), groupName);
		switchAuthor(null, groupName);
	}

	public void deployBestSceneGirl(BestScene bestScene, BestGirl bestGirl, int num, String groupName) {
		Map<String, VarFileDTO> var1 = process(bestScene.getDescription(), groupName);
		Map<String, VarFileDTO> var2 = processSomeGirl(Objects.nonNull(bestGirl) ? bestGirl.getDescription() : null,
				num, groupName);
		Map<String, VarFileDTO> varAll = new HashMap<>();
		varAll.putAll(var1);
		varAll.putAll(var2);
		switchAuthor(varAll, groupName);
	}

	public void deployOneSceneOneGirl(String bestSceneVarName, String bestGirlVarName, String groupName) {
		processSingle(bestSceneVarName, groupName);
		processSingle("Ispinox.Red3Some_Part2_1_2.latest", groupName);
		processSingle(bestGirlVarName, groupName);
		switchAuthor(null, groupName);
	}

	private void switchAuthor(Map<String, VarFileDTO> varAll, String author) {
		addPackage(author);
		addFavorite(author);
		additional(varAll, author);
	}

	public void deployBestGirl(BestGirl bestGirl) {
		process(bestGirl.getDescription(), bestGirl.getDescription() + "/");
		switchAuthor(null, bestGirl.getDescription());
	}

	public void deployBestScene(BestScene bestScene) {
		process(bestScene.getDescription() + "/", bestScene.getDescription() + "/");
		switchAuthor(null, bestScene.getDescription());
	}

	public void switchAuthor(Map<String, VarFileDTO> var, BestScene bs, BestGirl bg) {
		List<String> authorList = new ArrayList<>();
		authorList.add(bs.getDescription());
		authorList.add(bg.getDescription());
		switchAuthor(authorList);
	}

	private void switchAuthor(List<String> authorList) {
		for (String author : authorList) {
			addPackage(author);
			addFavorite(author);
		}
		// additional(author);
	}

	static Set<String> additionalVarList;
	{
		additionalVarList = new HashSet<>();
		additionalVarList.add("JayJayWon.BrowserAssist.latest");
		additionalVarList.add("JayJayWon.UIAssist(Patron).latest");
		additionalVarList.add("JayJayWon.VARHubThumbnails.latest");
		additionalVarList.add("JayJayWon.VUML.latest");
		additionalVarList.add("ToumeiHitsuji.DiviningRod.latest");
		additionalVarList.add("DoesNotCat.RealFakeLabias.latest");
		additionalVarList.add("JayJayWon.OrificeAligner.latest");
		additionalVarList.add("Captain Varghoss.BellyBulger.latest");
		additionalVarList.add("Nyaacho.MorphMassManager外观拼凑.11.latest");

		additionalVarList.add("AcidBubbles.BlendShapes.latest");
		additionalVarList.add("OrangeGumi.Pp_Danmenz_A_v004.1.latest");
//		additionalVarList.add("cotyounoyume.ExpressionBlushingAndTearsFullVer.latest");
//		additionalVarList.add("Saking55.AutoBulger.latest");
//		additionalVarList.add("JayJayWon.SexAssist.latest");
//		additionalVarList.add("Vinput.AutoThruster.latest");

	}

	private void additional(Map<String, VarFileDTO> varAll, String groupName) {
		additionalVarList.forEach(varFileName -> {
			VarFileDTO varFileDTO = findSuitableVarFile(new VarFileDTO("", varFileName));
			if (!varAll.containsKey(varFileDTO.getVarFileName())) {
				additional(varFileDTO, groupName);
				varAll.put(varFileName, varFileDTO);
			}
		});
	}

	private void additional(VarFileDTO varFileDTO, String groupName) {
		String linkFileName = VAM_ADDON_PATH + groupName + "\\___VarsLink___\\" + varFileDTO.getVarFileName();
		String targetFileName = varFileDTO.getFullPath() + varFileDTO.getVarFileName();
		File targetFile = new File(targetFileName);
		FileUtil.createLinkFile2(targetFile, linkFileName, false);
//		boolean b =
//		if (!b)
//			log.warn("\n---failed additional: " + targetFile);
	}

	private void addPackage(String author) {
		String linkFileName = VAM_FILE_ADDONPACKAGES;
		String targetFileName = VAM_ADDON_PATH + author;
		File targetFile = new File(targetFileName);
		FileUtil.createLinkFile2(targetFile, linkFileName, true);
	}

	private void addFavorite(String author) {
		String linkFileName = VAM_FILE_ADDONPACKAGESFILEPREFS;
		String targetFileName = VAM_ALLFAVORITE_PATH + author + "\\";
		File targetFile = new File(targetFileName);
		FileUtil.createLinkFile2(targetFile, linkFileName, true);
	}

	public void clearUseLessDB() {
		List<VarFileDTO> list = varFileService.findAll();
		list.forEach(varFileDTO -> {
			String targetFile = varFileDTO.getFullPath() + varFileDTO.getVarFileName();
			File dir = new File(targetFile);
			if (!dir.exists()) {
				varFileService.delete(varFileDTO);
				log.warn("\n---delete db: " + varFileDTO.getVarFileName());
			}
		});
	}

	public void deployBestSceneGirl_GK(String bestSceneVarName, BestGirl bestGirl, int num, String groupName) {
		VarFileDTO mergedVarFileDTO = new VarFileDTO(null, "jacky.merged.1.var");
		List<String> chineseVarList = Arrays.asList("jacky.sound.latest",
				"Zam55555.ZamS001SE_BusinessReception.latest");

		Map<String, VarFileDTO> var1 = processSingle(bestSceneVarName, groupName);
		Map<String, VarFileDTO> var3 = processSomeGirl(Objects.nonNull(bestGirl) ? bestGirl.getDescription() : null,
				num, groupName);
		Map<String, VarFileDTO> varAll = new HashMap<>();
		varAll.putAll(var1);
		for (String chineseVarName : chineseVarList) {
			Map<String, VarFileDTO> var2 = processSingle(chineseVarName, groupName);
			varAll.putAll(var2);
		}
		varAll.putAll(var3);
		switchAuthor(varAll, groupName);

		List<VarFileDTO> varFileDTOChineseList = new ArrayList<>();

		for (int i = 0; i < chineseVarList.size(); i++) {
			String chineseVar = chineseVarList.get(i);
			VarFileDTO varFileDTOuery1 = new VarFileDTO(null, chineseVar);
			VarFileDTO varFileDTODBChinese = findSuitableVarFile(varFileDTOuery1);
			varFileDTOChineseList.add(varFileDTODBChinese);
		}

		VarFileDTO varFileDTOuery2 = new VarFileDTO(null, bestSceneVarName);
		VarFileDTO varFileDTODBEnglish = findSuitableVarFile(varFileDTOuery2);
		VarFileDTO varFileDTOEnglish = readVarFile(varFileDTODBEnglish);

//		List<String> chineseSoundList2 = varFileDTOChinese.getSoundList();
//		List<String> chineseSoundList = chineseSoundList2;
//				.stream().filter(s -> StringUtils.indexOf(s, "moan") >= 0)
//				.collect(Collectors.toList());

//		varFileDTOChinese.setSoundList(chineseSoundList);

//		zipUtils.translateChinese(mergedVarFileDTO, varFileDTOChinese, varFileDTOEnglish, modifiedMetaJson,
//				VAM_FILE_ADDONPACKAGES);

		String unZipfileName = varFileDTOEnglish.getFullPath() + varFileDTOEnglish.getVarFileName();
		String outDir = VAM_FILE_ADDONPACKAGES + mergedVarFileDTO.makeKey() + "/";
		zipUtils.unZip(unZipfileName, outDir);
		List<String> uselessSoundList = translateUtils.translateChinese2(outDir, mergedVarFileDTO,
				varFileDTOChineseList, varFileDTOEnglish);
		zipUtils.removeUseLessSound(outDir, uselessSoundList);

		try {

			String modifiedMetaJson = modifiedMetaJson(mergedVarFileDTO, varFileDTOChineseList, uselessSoundList,
					varFileDTOEnglish.getMetaJson());
			overwriteMetaJson(outDir, modifiedMetaJson);
		} catch (Exception e) {
			e.printStackTrace();
		}

		zipUtils.doZip(outDir, VAM_FILE_ADDONPACKAGES + mergedVarFileDTO.getVarFileName());
	}

	private void overwriteMetaJson(String outDir, String modifiedMetaJson) {
		try {
			String path = outDir + "meta.json";

			FileInputStream fis = new FileInputStream(path);
			String content = IOUtils.toString(fis, "UTF-8");
			fis.close();

			File file = new File(path);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// Write in file
			bw.write(modifiedMetaJson);

			// Close connection
			bw.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private String modifiedMetaJson(VarFileDTO mergedVarFileDTO, List<VarFileDTO> varFileDTOChineseList,
			List<String> uselessSoundList, MetaJson metaJson) throws JsonProcessingException {
		List<String> contentList = metaJson.getContentList().stream().filter(s -> !uselessSoundList.contains(s))
				.collect(Collectors.toList());
//		List<String> chineseSoundList=varFileDTOChinese.getSoundList();
		// contentList.addAll(chineseSoundList);

		metaJson.setCreatorName(mergedVarFileDTO.getCreatorName());
		metaJson.setPackageName(mergedVarFileDTO.getPackageName());
		metaJson.setContentList(contentList);
		for (VarFileDTO varFileDTOChinese : varFileDTOChineseList) {
			metaJson.getDependencies().put(varFileDTOChinese.makeKey(), new Dependence());
		}
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(metaJson);
	}

	static List<String> skipSoundExtension = Arrays.asList("wav", "mp3");

	private boolean isSound(String content) {
		String atom = content.toLowerCase();
		String atomExtension = zipUtils.getExtension(atom);
		if (skipSoundExtension.contains(atomExtension)) {
			return true;
		} else
			return false;
	}

	public void makeVarPack() {
		String targetVarName = "jacky.sound.1";
		String sourcePath = "C:\\VAM-resource\\中文語音可替換素材包\\";
		String targetPath = VAM_FILE_ADDONPACKAGES + targetVarName + "\\Custom\\Sounds\\";

		VarFileDTO mergedVarFileDTO = new VarFileDTO(null, targetVarName + ".var");
		String unZipfileName = VAM_FILE_ADDONPACKAGES + mergedVarFileDTO.makeKey() + "\\";
		List<String> soundList = new ArrayList<>();
		try {
			soundList = zipUtils.copyDirectory(StringUtils.length(unZipfileName), sourcePath, targetPath).stream()
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		mergedVarFileDTO.setSoundList(soundList);
		zipUtils.makeMetaJson(unZipfileName, mergedVarFileDTO);

		zipUtils.doZip(unZipfileName, VAM_FILE_ADDONPACKAGES + mergedVarFileDTO.getVarFileName());
	}

	public void superDependence() {
		Set<String> allVarSet = new HashSet<>();

		Map<String, Integer> sumMap = new HashMap<>();
		for (VarFileDTO varFileDTO : varFileService.findAll()) {
			if (StringUtils.contains(varFileDTO.getFullPath(), "\\base")
					|| StringUtils.contains(varFileDTO.getFullPath(), "\\best_"))
				log.debug("target:" + varFileDTO.getFullPath());
			else
				continue;

			allVarSet.add(varFileDTO.makeKey());
			Set<String> dependSet = varFileDTO.getDependencies();
			for (String key : dependSet) {
				Integer count = sumMap.get(key);
				if (Objects.isNull(count)) {
					count = new Integer(0);
				}
				count++;
				sumMap.put(key, count);
			}
		}
		Map<String, Integer> sortedSumMap = sumMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));

		Map<String, Integer> lackMap = sortedSumMap.entrySet().stream()
				.filter(x -> !validateVarExist(x.getKey(), allVarSet))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));

		Map<String, Map<String, Integer>> creatorMap = new HashMap<>();
		for (Map.Entry<String, Integer> me : lackMap.entrySet()) {
			// System.out.println("depend count:" + me.getKey() + " " + me.getValue());
			String[] au = StringUtils.split(me.getKey(), ".");
			String creator = au[0];
			Map<String, Integer> cmap = creatorMap.get(creator);
			if (Objects.isNull(cmap)) {
				cmap = new HashMap<>();
				creatorMap.put(creator, cmap);
			}
			cmap.put(me.getKey(), me.getValue());
		}
		Map<String, Map<String, Integer>> sortedCreatorMap = creatorMap.entrySet().stream()
				.sorted(Collections
						.reverseOrder(Map.Entry.comparingByValue((o1, o2) -> Integer.compare(o1.size(), o2.size()))))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));

		for (Map.Entry<String, Map<String, Integer>> me : sortedCreatorMap.entrySet()) {
			for (Map.Entry<String, Integer> me1 : me.getValue().entrySet()) {
				System.out.println("depend creator:" + me.getKey() + "    " + me1.getKey() + "    " + me1.getValue());
			}
		}
	}

	private boolean validateVarExist(String varName, Set<String> allVarSet) {
		if (StringUtils.endsWith(varName, "latest"))
			varName = StringUtils.replace(varName, "latest", "1");
		return allVarSet.contains(varName);
	}

	public void girlAnalysis() {
//		Set<String> allVarSet = new HashSet<>();
		Map<String, Set<String>> authorMap = new HashMap<>();
		for (VarFileDTO varFileDTO : varFileService.findAll()) {
			if (StringUtils.contains(varFileDTO.getFullPath(), "\\best_girl"))
				log.debug("+++target:", varFileDTO.getFullPath());
//			else
//				continue;

			if (Objects.isNull(varFileDTO.getFemaleCount()) || varFileDTO.getFemaleCount() == 0) {
				log.info("---no girl:", varFileDTO.getVarFileName());
				continue;
			}

//			if(varFileDTO.getFemaleCount()>1) {
//				log.info("---many girl:{} {}",varFileDTO.getFemaleCount(), varFileDTO.getVarFileName());
//				continue;
//			}

			if (Objects.isNull(varFileDTO.getSceneJsonList()) || varFileDTO.getSceneJsonList().size() == 0) {
				log.info("---no scene:", varFileDTO.getVarFileName());
				continue;
			}

			if (varFileDTO.getSceneJsonList().size() > 1) {
				log.info("---many scene:{} {}", varFileDTO.getSceneJsonList().size(), varFileDTO.getVarFileName());
				continue;
			}

			String creatorName = varFileDTO.getCreatorName();
//			allVarSet.add(varFileDTO.makeKey());
//			Set<String> dependSet = varFileDTO.getDependencies();
			Set<String> authorSet = authorMap.get(creatorName);
			if (authorSet == null) {
				authorSet = new HashSet<>();
				authorMap.put(creatorName, authorSet);
			}
			authorSet.add(varFileDTO.makeKey());
		}

		Map<String, Set<String>> sortedSumMap = authorMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue((o1, o2) -> Integer.compare(o1.size(), o2.size())))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		for (String author : sortedSumMap.keySet()) {
			System.out.println("creator:" + author + "  girlCount:" + sortedSumMap.get(author).size());
		}

//		Map<String, Integer> lackMap = sortedSumMap.entrySet().stream().filter(x -> !validateVarExist(x.getKey(),allVarSet))
//				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
//
//		Map<String, Map<String, Integer>> creatorMap = new HashMap<>();
//		for (Map.Entry<String, Integer> me : lackMap.entrySet()) {
//			// System.out.println("depend count:" + me.getKey() + " " + me.getValue());
//			String[] au = StringUtils.split(me.getKey(), ".");
//			String creator = au[0];
//			Map<String, Integer> cmap = creatorMap.get(creator);
//			if (Objects.isNull(cmap)) {
//				cmap = new HashMap<>();
//				creatorMap.put(creator, cmap);
//			}
//			cmap.put(me.getKey(), me.getValue());
//		}
//		Map<String, Map<String, Integer>> sortedCreatorMap = creatorMap.entrySet().stream()
//				.sorted(Collections
//						.reverseOrder(Map.Entry.comparingByValue((o1, o2) -> Integer.compare(o1.size(), o2.size()))))
//				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
//
//		for (Map.Entry<String, Map<String, Integer>> me : sortedCreatorMap.entrySet()) {
//			for (Map.Entry<String, Integer> me1 : me.getValue().entrySet()) {
//				System.out.println("depend creator:" + me.getKey() + "    " + me1.getKey() + "    " + me1.getValue());
//			}
//		}
	}

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

	private ResponseEntity<String> callRestClientAPI(String endpoint2, String requestJSON, HttpMethod method) {
		HttpHeaders headers = buildRequestHeader(null);
		final URI uri = buildUri(host, endpoint2);
		try {
			final Stopwatch sw = Stopwatch.createStarted();

			final HttpEntity<String> request = new HttpEntity<>(requestJSON, headers);

			final RestTemplate template = new RestTemplate();
			final RetryTemplate retryTemplate = retryTemplate(endpoint2);
			final ResponseEntity<String> response = retryTemplate
					.execute(retryContetx -> template.exchange(uri, method, request, String.class));

			if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
				log.info("[Responded] Rest API: {}, time: {}", uri, sw.stop());
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

	private PlayRecordDTO txt2img(Prompt prompt, List<Lora> loraList, TextualInversion textualInversion,
			CheckPoint checkPoint, SampleName sampleName, int batch, Integer step) {
		PlayRecordDTO playRecordDTO = new PlayRecordDTO();
		playRecordDTO.setPrompt(prompt);
		playRecordDTO.setCheckPoint(checkPoint);
		playRecordDTO.setSamplerName(sampleName);
		playRecordDTO.getLoraList().addAll(loraList);
		playRecordDTO.getTextualInversionList().add(textualInversion);
		playRecordDTO.setSteps(step);
		for (int i = 0; i < batch; i++) {
			log.info("txt2img {}:{} {}", checkPoint.name(), i, sampleName.name());
			String filename = doPost2(playRecordDTO);
			if (filename != null)
				log.info("txt2img done {}:{} {}", checkPoint.name(), i, sampleName.name());
			else {
				log.error("txt2img failed {}:{} {}", checkPoint.name(), i, sampleName.name());
				return null;
			}
		}
		return playRecordDTO;
	}

	private boolean txt2img2(Prompt prompt, CheckPoint checkPoint, SampleName sampleName, int batch) {
		Txt2ImgDTO txt2ImgDTO = new Txt2ImgDTO();
		txt2ImgDTO.setPrompt(prompt.getPositive());
		txt2ImgDTO.setNegative_prompt(prompt.getNegative());
		txt2ImgDTO.setSampler_name(sampleName.getOpCode());
		txt2ImgDTO.setWidth(prompt.getWidth());
		txt2ImgDTO.setHeight(prompt.getHeight());
		for (int i = 0; i < batch; i++) {
			log.info("txt2img {}:{} {}", checkPoint.name(), i, sampleName.name());
			boolean result1 = doPost3(txt2ImgDTO);
			if (result1)
				log.info("txt2img done {}:{} {}", checkPoint.name(), i, sampleName.name());
			else {
				log.error("txt2img failed {}:{} {}", checkPoint.name(), i, sampleName.name());
				return false;
			}
		}
		return true;
	}

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
			Txt2ImgDTO txt2ImgDTO = new Txt2ImgDTO();
			PredictDTO predict = new PredictDTO();
			List<Object> objList = SDUtils.toDataList(1, 1, playRecordDTO, txt2ImgDTO);
			SDUtils.appendHiRES(objList);
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
					log.info("return {}", map.get("name"));
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

	private void switchCheckPoint(CheckPoint checkPoint) {
		if (checkPoint.equals(checkPointNow))
			return;
		checkPointNow = checkPoint;

		List<Object> data = new ArrayList<>();
		data.add(checkPoint.getFilename());
		PredictDTO predict = new PredictDTO(647, data);
		boolean result1 = doPost1(predict);

		if (result1) {
			data = new ArrayList<>();
			predict = new PredictDTO(648, data);
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

	static CheckPoint checkPointNow = null;

	public void txt2img_main() {
		for (int i = 0; i < 10000; i++) {
			long time1, time2;

			time1 = System.currentTimeMillis();
			txt2img_main_sub();
			time2 = System.currentTimeMillis();
			System.out.println("mission 花了：" + (time2 - time1) / 1000 + "秒");
		}
	}

	public void txt2img_main_sub() {
		txt2img_mainTask(Sets.newHashSet(CheckPoint.V08_V08A), Prompt.PORN_NURSE,
				Arrays.asList(Lora.KOREANDOLLLIKENESS_V15.initWeight(0.1f, 0.9f)), 20);
				
//		txt2img_mainTask(Sets.newHashSet(CheckPoint._2GUOFENG2_V20), Prompt.PORN_M_LEG,
//				Arrays.asList(Lora.CREAMPIEHAIRYPUSSY_CREAMPIEV11.initWeight(0.1f, 0.7f)), 20);
//
//		txt2img_mainTask(Sets.newHashSet(CheckPoint._3GUOFENG3_V32LIGHT), Prompt.PORN_M_LEG,
//				Arrays.asList(Lora.CREAMPIEHAIRYPUSSY_CREAMPIEV11.initWeight(0.1f, 0.7f)), 20);

		txt2img_mainTask(Sets.newHashSet(CheckPoint._2GUOFENG2_V20), Prompt.PORN_M_LEG,
				Arrays.asList(Lora.CREAMPIEHAIRYPUSSY_CREAMPIEV11.initWeight(0.1f, 0.9f),
						Lora.SPREADPUSSY_V11.initWeight(0.1f, 0.9f)),
				20);

		txt2img_mainTask(Sets.newHashSet(CheckPoint._3GUOFENG3_V32LIGHT), Prompt.PORN_M_LEG,
				Arrays.asList(Lora.CREAMPIEHAIRYPUSSY_CREAMPIEV11.initWeight(0.1f, 0.9f),
						Lora.SPREADPUSSY_V11.initWeight(0.1f, 0.9f)),
				20);
		
		txt2img_mainTask(Sets.newHashSet(CheckPoint._3Guofeng3_v33), Prompt.PORN_M_LEG,
				Arrays.asList(Lora.CREAMPIEHAIRYPUSSY_CREAMPIEV11.initWeight(0.1f, 0.9f),
						Lora.SPREADPUSSY_V11.initWeight(0.1f, 0.9f)),
				20);
		
		Set<Lora> mySingleLora = new LinkedHashSet<>(Arrays.asList(Lora.ANGELABABY_1,
				Lora.ASIAGIRLINUNIFORM_CHILLOUTMIX, Lora.ASIANGIRLSABRINALORA_NOTUPDATE,
				Lora.ASIANGIRLXYBOBOLORA_NOTUPDATE, Lora.ASIANGIRLZHAOXMLORA_NOTUPDATE, Lora.EASTASIANDOLL_V40,
				 Lora.FRAMEBINDER_V10, Lora.HIPOLY3DMODELLORA_V10, Lora.JAPANESE_DOLL_LIKENESS,
				Lora.KRISWUEXGIRLFRIEND_V10, Lora.MIKUYA_V15, Lora.MINESFIXASIANLIKENESS_V10,
				Lora.MLEGGESTUREULTIMATE_V51, Lora.SHOJOVIBE_V11, Lora.SOPHONJAPENESEGIRL_SOPHONV12,
				Lora.STANDINGDOGGYSTYLE_V11A));

		for (Lora myLora : mySingleLora) {
			txt2img_mainTask(Sets.newHashSet(CheckPoint.CHILLOUTMIX_NIPRUNEDFP32FIX), Prompt.PORN_M_LEG,
					Arrays.asList(myLora.initWeight(0.1f, 1.0f)), 20);// 影響不大
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

	public void txt2img_mainTask(Set<CheckPoint> myCheckPoint, Prompt prompt, List<Lora> myLora, Integer step) {
		for (CheckPoint checkPoint : myCheckPoint) {
			System.out.println("\n\n checkpoint: " + checkPoint.name());
			switchCheckPoint(checkPoint);
			for (int i = 0; i < 10000; i++) {
				if (!nextWeight(myLora)) {
					for (Lora lora : myLora) {
						System.out.print("  " + lora.appendLora());
					}
					System.out.println(" ");
					txt2img_mainTask_render(checkPoint, prompt, myLora, step);
				} else {
					break;
				}
			}

			System.out.println("\n\n finish: " + checkPoint.name());
		}
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

		if (lora.getWeight() + 0.1f > lora.getWeightEnd()) {
			lora.setWeight(lora.getWeightStart());
			return true;
		} else {
			lora.setWeight(lora.getWeight() + 0.1f);
			return false;
		}
	}

	public void txt2img_mainTask_render(CheckPoint checkPoint, Prompt prompt, List<Lora> loraList, Integer step) {
		List<SampleName> mySample = Arrays.asList(SampleName.DPM_PLUS_SDE_KARRAS, SampleName.DPM_PLUS_2M_KARRAS,
				SampleName.DPM_PLUS_2S_A_KARRAS, SampleName.EULER_A);
		TextualInversion textualInversion = TextualInversion.NONE;
		for (SampleName sampleName : mySample) {
			PlayRecordDTO playRecordDTO = txt2img(prompt, loraList, textualInversion, checkPoint, sampleName, 1, step);
			if (playRecordDTO == null) {
				System.out.println("\n\n work failed!");
				break;
			} else {
				FileUtil.moveFileTo(WEBUI_SOME_PATH, playRecordDTO, "txt2img over");
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
