package project.SPM.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.SPM.service.PapagoService;
import project.SPM.util.NetworkUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PapagoServiceImpl implements PapagoService {

    @Value("${naver.papago.clientId}")
    private String clientId;

    @Value("${naver.papago.clientSecret}")
    private String clientSecret;

    //== 네이버 API 사용을 위한 접속 정보 설정하기 ==//
    private Map<String, String> setNaverInfo() {
        log.info("### setNaverInfo start");
        HashMap<String, String> requestheader = new HashMap<>();

        requestheader.put("X-Naver-Client-Id", clientId);
        requestheader.put("X-Naver-Client-Secret", clientSecret);

        log.info("### clientId : {}", clientId);
        log.info("### clientSecret : {}", clientSecret);

        log.info("### setNaverInfo end");
        return requestheader;
    }

    @Override
    public String translate(String textMessage) throws Exception {
        log.info("### translate start");

        // 번약할 문장
        String text = textMessage;

        // 언어 설정
        String source = "ko"; // 번역 전
        String target = "en"; // 번역 후

        //한국어 -> 영어 번역을 위한 파라미터 설정
        String postParams = "source=" + source + "&target=" + target + "&text=" + URLEncoder.encode(text, "UTF-8");
        log.info("### postParams : {}", postParams);

        //Papago API 호출
        String json = NetworkUtil.post(PapagoService.translateApiURL, this.setNaverInfo(), postParams);
        log.info("### json : {}", json);

        // JSON 구조를 Map 데이터 구조로 변경
        // 키와 값 구조의 JSON 구조로부터 데이터를 쉽게 가져오기 위해 Map 데이터구조로 변경
        Map<String, Object> resultMap = new ObjectMapper().readValue(json, LinkedHashMap.class);

        // 결과 내용 중 message 정보 가져오기
        Map<String, Object> messageMap = (Map<String, Object>) resultMap.get("message");

        // message 결과 내용 중 result 정보 가져오기
        Map<String, String> messageResultMap = (Map<String, String>) messageMap.get("result");
        log.info("### messageResultMap : {}", messageResultMap);

        String translatedText = messageResultMap.get("translatedText");
        log.info("### translatedText : {}", translatedText);

        messageMap = null;
        messageResultMap = null;
        resultMap = null;

        log.info("### translate end");
        return translatedText;
    }
}
