package project.SPM.service;

import project.SPM.dto.CarDTO;
import project.SPM.dto.NoticeDTO;

import java.util.List;

public interface ManagementService {
    List<CarDTO> sendNotice(NoticeDTO noticeDTO) throws Exception;
}
