package project.SPM.service;

import project.SPM.dto.WeatherDto;

public interface WeatherService {

    String apiURL = "https://api.openweathermap.org/data/3.0/onecall";

    WeatherDto getWeather(WeatherDto weatherDto) throws Exception;
}
