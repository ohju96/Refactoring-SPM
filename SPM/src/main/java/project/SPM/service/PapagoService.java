package project.SPM.service;

public interface PapagoService {

    //Papago 번역 API
    String translateApiURL = "https://openapi.naver.com/v1/papago/n2mt";

    String translate(String textMessage) throws Exception;
}
