package project.SPM.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.SPM.dto.WeatherDailyDto;
import project.SPM.dto.WeatherDto;
import project.SPM.service.WeatherService;
import project.SPM.util.CmmUtil;
import project.SPM.util.NetworkUtil;
import project.SPM.util.WeatherDateUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Override
    public WeatherDto getWeather(WeatherDto weatherDto) throws Exception {
        log.info("### .getWeather start");

        String lat = CmmUtil.nvl(weatherDto.getLat()); //위도
        String lon = CmmUtil.nvl(weatherDto.getLon()); //경도

        String apiParam = "?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric";
        log.info("apiParam : {}", apiParam);

        String json = NetworkUtil.get(WeatherService.apiURL + apiParam);
        log.info("json : {}", json);

        //json -> map
        Map<String, Object> resultMap = new ObjectMapper().readValue(json, LinkedHashMap.class);

        Map<String, Double> current = (Map<String, Double>) resultMap.get("current");
        log.info("current : {}", current);

        double currentTemp = current.get("temp"); //현재 기온
        log.info("현재 기온 : {}", currentTemp);

        // 일별 날씨 조회 최대 7일까지 제공
        List<Map<String, Object>> dailyList = (List<Map<String, Object>>) resultMap.get("daily");

        //날씨 정보를 저장할 데이터
        List<WeatherDailyDto> weatherDailyDtoList = new LinkedList<>();

        for (Map<String, Object> dailyMap : dailyList) {
            String day = WeatherDateUtil.getLongDateTime(dailyMap.get("dt"), "yyyy-MM-dd"); //기준 날짜
            String sunrise = WeatherDateUtil.getLongDateTime(dailyMap.get("sunrise")); //해뜨는 시간
            String sunset = WeatherDateUtil.getLongDateTime(dailyMap.get("sunset")); //해지는 시간
            String moonrise = WeatherDateUtil.getLongDateTime(dailyMap.get("moonrise"));
            String moonset = WeatherDateUtil.getLongDateTime(dailyMap.get("moonset"));

            log.info("today : {}", day);
            log.info("해뜨는 시간 : {}", sunrise);
            log.info("해지는 시간 : {}", sunset);
            log.info("달뜨는 시간 : {}", moonrise);
            log.info("해뜨는 시간 : {}", moonset);

            Map<String, Double> dailyTemp = (Map<String, Double>) dailyMap.get("temp");

            //숫자형태보다 문자열 형태가 데이터처리하기 쉽기 때문에 Double 형태를 문자열로 변경
            String dayTemp = String.valueOf(dailyTemp.get("day"));
            String dayTempMax = String.valueOf(dailyTemp.get("max"));
            String dayTempMin = String.valueOf(dailyTemp.get("min"));

            log.info("평균 기온 : {}", dayTemp);
            log.info("최고 기온 : {}", dayTempMax);
            log.info("최저 기온 : {}", dayTempMin);

            WeatherDailyDto weatherDailyDto = new WeatherDailyDto();
            weatherDailyDto.setDay(day);
            weatherDailyDto.setSunrise(sunrise);
            weatherDailyDto.setSunset(sunset);
            weatherDailyDto.setMoonrise(moonrise);
            weatherDailyDto.setMoonset(moonset);
            weatherDailyDto.setDayTemp(dayTemp);
            weatherDailyDto.setDayTempMax(dayTempMax);
            weatherDailyDto.setDayTempMin(dayTempMin);

            weatherDailyDtoList.add(weatherDailyDto); //일별 날씨 정보를 리스트에 추가하기
            weatherDailyDto = null;
        }

        WeatherDto weatherDtos = new WeatherDto();
        weatherDtos.setLat(lat);
        weatherDtos.setLon(lon);
        weatherDtos.setCurrentTemp(currentTemp);
        weatherDtos.setDailyList(weatherDailyDtoList);

        log.info("### .getWeather end");
        return weatherDtos;
    }
}
