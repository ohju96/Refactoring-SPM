package project.SPM.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import project.SPM.Entity.UserEntity;
import project.SPM.dto.UserDTO;
import project.SPM.validator.UserValidator;
import project.SPM.vo.UserVo;
import project.SPM.service.impl.UserServiceImpl;
import project.SPM.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller("userController")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final UserValidator userValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(userValidator);
    }

    // 인덱스 페이지
    @GetMapping("/index")
    private String index(){
        return "index";
    }

    // 회원 가입 페이지 - 기본 화면
    @GetMapping("/regUser")
    public String regUserForm(Model model) {
        model.addAttribute("userVo", new UserVo());
        return "user/regUser";
    }

    // 회원 가입 페이지 - 로직 처리
    @PostMapping("/regUser/insert")
    public String InsertRegUser(@Validated @ModelAttribute UserVo userVo, BindingResult bindingResult, Model model){

        try {
            if (bindingResult.hasErrors()) {
                return "user/regUser";
            }

            if (!userVo.getUserPw().equals(userVo.getUserPwc())) {
                model.addAttribute("msg", "비밀번호 체크에 실패하였습니다. 다시 입력해주세요");
                model.addAttribute("url", "/user/regUser");
                return "redirect";
            }

            UserDTO userDTO = new UserDTO(
                    userVo.getUserNo(),
                    userVo.getUserName(),
                    userVo.getUserPn(),
                    userVo.getUserEmail(),
                    userVo.getUserId(),
                    userVo.getUserPw(),
                    userVo.getUserAddr()
            );

            String msg = userService.insertUser(userDTO);

            model.addAttribute("msg", msg);
            model.addAttribute("url", "/user/logIn");
            return  "redirect";

            // 서비스에서 아이디 및 이메일 중복 체크 시 Exception을 던지고 처리
        } catch (IllegalArgumentException httpStatusCodeException) {

            model.addAttribute("msg", httpStatusCodeException.getMessage());
            model.addAttribute("url", "/user/logIn");
            return "redirect";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // 로그인 페이지 - 기본 화면
    @GetMapping("/logIn")
    public String userLogin(Model model) {
        model.addAttribute("userVo", new UserVo());
        return "user/logIn";
    }

    // 로그인 페이지 - 로직 처리
    @PostMapping("/logIn/page")
    public String login(@ModelAttribute UserVo userVo, HttpServletRequest request, HttpSession session, Model model) throws Exception {

        // 로그인 전에 세션 삭제
        session = request.getSession(false);
        if (session != null) {
            session.invalidate(); //세션에 있는 정보가 싹 날라간다.
        }

        UserDTO userDTO = new UserDTO(
                userVo.getUserNo(),
                userVo.getUserName(),
                userVo.getUserPn(),
                userVo.getUserEmail(),
                userVo.getUserId(),
                userVo.getUserPw(),
                userVo.getUserAddr()
        );

        boolean res = userService.login(userDTO);

        if (res == true) {
            UserEntity dto = userService.loginSession(userDTO);
            session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, dto);
        } else {
            model.addAttribute("msg", "아이디 및 비밀번호를 다시 확인해주세요.");
            model.addAttribute("url", "/user/logIn");
            return "redirect";
        }

        model.addAttribute("msg", "로그인 성공");
        model.addAttribute("url", "/user/index");
        return "redirect";
    }

    // 로그아웃 로직 처리
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpSession session) {
        session.invalidate();
        return "redirect:/user/logIn";
    }

    // 비밀번호 찾기 페이지
    @GetMapping("/findPw")
    public String changePwPage() {
        return "user/findPw";
    }

    // 비밀번호 찾기 로직
    @PostMapping("/findPw")
    public String changePw(HttpServletRequest request, Model model) throws Exception {

        String userEmail = request.getParameter("address");

        String msg = userService.findPw(userEmail);

        model.addAttribute("msg", msg);
        model.addAttribute("url", "/user/logIn");

        return "redirect";
    }

    // 아이디 찾기 페이지
    @GetMapping("findId")
    public String findIdPage() {
        return "user/findId";
    }

    // 아이디 찾기 로직
    @PostMapping("findId")
    public String findId(HttpServletRequest request, Model model) throws Exception {

        String userEmail = request.getParameter("address");

        String msg = userService.findId(userEmail);

        model.addAttribute("msg", msg);
        model.addAttribute("url", "/user/logIn");

        return "redirect";
    }
}
