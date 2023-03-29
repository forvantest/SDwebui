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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.google.common.base.Stopwatch;

import lombok.extern.slf4j.Slf4j;
import vam.dto.MetaJson;
import vam.dto.PlayRecordDTO;
import vam.dto.PredictDTO;
import vam.dto.ResultDTO;
import vam.dto.Txt2ImgDTO;
import vam.dto.VarFileDTO;
import vam.dto.enumration.BestGirl;
import vam.dto.enumration.BestScene;
import vam.dto.enumration.CheckPoint;
import vam.dto.meta.Dependence;
import vam.util.FileUtil;
import vam.util.SDUtils;

@Slf4j
@Service("work")
public class Work extends WorkDeployVarFile {
	String host = "127.0.0.1";
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

	public static URI buildUri(String host, String endpoint) {
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

	private ResponseEntity<String> callRestClientAPI(String requestJSON, HttpMethod method) {
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

	private boolean txt2img(CheckPoint checkPoint, int batch) {
		PlayRecordDTO playRecordDTO = new PlayRecordDTO();
		playRecordDTO.setCheckPoint(checkPoint);
		for (int i = 0; i < batch; i++) {
			log.info("txt2img {}:{}", checkPoint.name(), i);
			boolean result1 = doPost2(playRecordDTO);
			if (result1)
				log.info("txt2img done {}:{}", checkPoint.name(), i);
			else {
				log.error("txt2img failed {}:{}", checkPoint.name(), i);
				return false;
			}
		}
		return true;
	}

	private boolean doPost2(PlayRecordDTO playRecordDTO) {
		try {
			Txt2ImgDTO txt2ImgDTO = new Txt2ImgDTO();
			PredictDTO predict = new PredictDTO();
			predict.setData(SDUtils.toDataList(1, 1, playRecordDTO, txt2ImgDTO));
			String requestJSON = objectMapper.writeValueAsString(predict);
			ResponseEntity<String> rs = callRestClientAPI(requestJSON, HttpMethod.POST);
			if (rs.getStatusCodeValue() == 200) {
				ResultDTO resultDTO = objectMapper.readValue(rs.getBody(), ResultDTO.class);
				List<Object> objectList = resultDTO.getData();
				Object firstObject = objectList.get(0);
				if (firstObject instanceof List) {
					List list = (List) firstObject;
					Map map = (Map) list.get(0);
					log.info("return {}", map.get("name"));
					playRecordDTO.setFilename((String) map.get("name"));
					playRecordService.save(playRecordDTO);
				}
				return true;
			}
			log.info("post error: {}", rs.getStatusCodeValue());
			return false;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void switchCheckPoint(CheckPoint checkPoint) {
		List<Object> data = new ArrayList<>();
		data.add(checkPoint.getFilename());
		PredictDTO predict = new PredictDTO(591, data);
		boolean result1 = doPost1(predict);

		if (result1) {
			data = new ArrayList<>();
			predict = new PredictDTO(592, data);
			boolean result2 = doPost1(predict);
		}
	}

	private boolean doPost1(PredictDTO predict) {
		try {
			String requestJSON = objectMapper.writeValueAsString(predict);
			ResponseEntity<String> rs = callRestClientAPI(requestJSON, HttpMethod.POST);
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

	public void txt2img() {
		// List<CheckPoint> myChoose = Arrays.asList(CheckPoint._3GUOFENG3_V32LIGHT,
		// CheckPoint.CHIKMIX_V2);
		for (int n = 0; n <= 12; n++) {
			for (CheckPoint checkPoint : CheckPoint.values()) {
//			work.switchCheckPoint(checkPoint);
				boolean result1 = txt2img(checkPoint, 10);
				if (!result1) {
					System.out.println("\n\n work failed!");
					break;
				}
			}
		}
	}
}
