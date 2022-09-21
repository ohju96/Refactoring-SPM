package project.SPM.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.SPM.dto.WeatherDto;
import project.SPM.service.WeatherService;
import project.SPM.util.CmmUtil;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping("/weather")
@Controller
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/todayWeather")
    public String todayWeatherPage() {
        return "weather/todayWeather";
    }

    @GetMapping("/getWeather")
    @ResponseBody
    public WeatherDto getWeather(HttpServletRequest request) throws Exception {
        log.info("### .getWeather start");

        String lat = CmmUtil.nvl(request.getParameter("lat"));
        String lon = CmmUtil.nvl(request.getParameter("lon"));

        WeatherDto weatherDto = new WeatherDto();
        weatherDto.setLat(lat);
        weatherDto.setLon(lon);

        // 내가 필요한 정보만 추출한 날씨 정보 가져오기
        WeatherDto weather = weatherService.getWeather(weatherDto);

        if (weather == null) {
            weather = new WeatherDto();
        }

        log.info("### .getWeather end");
        return weather;
    }
}
