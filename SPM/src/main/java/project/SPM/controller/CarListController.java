package project.SPM.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.SPM.Entity.UserEntity;
import project.SPM.dto.CarDTO;
import project.SPM.dto.UserDTO;
import project.SPM.service.CarListService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/carList")
@RequiredArgsConstructor
public class CarListController {

    private final CarListService iCarListService;

    // 차량 리스트 페이지 - 기본 화면
    @GetMapping("/carList")
    public String carList() {
        return "carList/carList";
    }

     //차량 전체 리스트 페이지
    @GetMapping("/fullCarList")
    public String fullCarList(Model model, HttpSession session) throws Exception {

        log.info("### 전체 차량 리스트 컨트롤러 시작");
        UserEntity userEntity = (UserEntity) session.getAttribute("userDTO");
        log.info("### userEntity : " + userEntity.getUserId());

        UserDTO userDTO = new UserDTO(userEntity.getUserId());

        List<CarDTO> carDTOList = iCarListService.getFullCarList(userDTO);
        log.info("carDTOList : " + carDTOList.get(0).getUserId());
        log.info("carDTOList : " + carDTOList.get(0).getCarNumber());
        log.info("carDTOList : " + carDTOList.get(0).getSort());
        log.info("carDTOList : " + carDTOList.get(0).getChecks());
        log.info("carDTOList : " + carDTOList.get(0).getAddress());
        log.info("carDTOList : " + carDTOList.get(0).getPhoneNumber());
        log.info("carDTOList : " + carDTOList.get(0).getName());

        model.addAttribute(carDTOList);

        log.info("### 전체 차량 리스트 컨트롤러 종료");
        return "carList/fullCarList";

    }

    // 차량 주민 리스트 페이지 및 로직
    @GetMapping("/resident")
    public String resident(Model model, HttpSession session) throws Exception {

        UserEntity userEntity = (UserEntity) session.getAttribute("userDTO");

        UserDTO userDTO = new UserDTO(userEntity.getUserId());

        List<CarDTO> carDTOList = iCarListService.getResidentList(userDTO);

        model.addAttribute(carDTOList);

        return "carList/resident";
    }

    // 차량 주민 리스트 페이지 및 로직
    @GetMapping("/visit")
    public String visit(Model model, HttpSession session) throws Exception {

        UserEntity userEntity = (UserEntity) session.getAttribute("userDTO");

        UserDTO userDTO = new UserDTO(userEntity.getUserId());

        List<CarDTO> carDTOList = iCarListService.getVisitList(userDTO);

        model.addAttribute(carDTOList);

        return "carList/visit";
    }

    // 차량 블랙리스트 페이지 및 로직
    @GetMapping("/blackList")
    public String blackList(Model model, HttpSession session) throws Exception {

        UserEntity userEntity = (UserEntity) session.getAttribute("userDTO");

        UserDTO userDTO = new UserDTO(userEntity.getUserId());

        List<CarDTO> carDTOList = iCarListService.getBlackList(userDTO);

        model.addAttribute(carDTOList);

        return "carList/blackList";
    }

}
