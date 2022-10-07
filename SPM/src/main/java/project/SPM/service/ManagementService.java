package project.SPM.service;

import project.SPM.dto.CarDTO;
import project.SPM.dto.NoticeDTO;
import project.SPM.dto.PapagoSmsDto;
import project.SPM.dto.ResultDto;

import java.util.List;

public interface ManagementService {
    List<CarDTO> sendNotice(NoticeDTO noticeDTO) throws Exception;
    ResultDto sendTranslatedText(PapagoSmsDto papagoSmsDto) throws Exception;
}
