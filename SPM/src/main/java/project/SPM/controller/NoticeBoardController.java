package project.SPM.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.SPM.Entity.UserEntity;
import project.SPM.dto.NoticeBoardDto;
import project.SPM.dto.SessionIdDTO;
import project.SPM.dto.UserDTO;
import project.SPM.service.NoticeBoardService;
import project.SPM.util.CmmUtil;
import project.SPM.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/noticeBoard")
@RequiredArgsConstructor
@Controller
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;

    // 게시판 리스트 보여주기
    @GetMapping("/noticeBoardList")
    public String noticeBoardList(ModelMap modelMap) {
        log.info("### .noticeList start");

        // 공지 리스트 가져오기
        List<NoticeBoardDto> resultList = noticeBoardService.getNoticeBoardList();

        if (resultList == null) {
            resultList = new ArrayList<NoticeBoardDto>();
        }

        modelMap.addAttribute("resultList", resultList);

        //변수 초기화
        resultList = null;

        log.info("### .noticeList end");
        return "/noticeBoard/noticeBoardList";
    }

    // 게시판 상세보기
    @GetMapping("/noticeBoardInfo/{noticeBoardSeq}")
    public String noticeInfo(@PathVariable ("noticeBoardSeq") String noticeBoardSeq, ModelMap modelMap) throws Exception {
        log.info("### .noticeInfo start");

        String nSeq = noticeBoardSeq;//공지글번호
        log.info("### nSeq : {}", nSeq);

        NoticeBoardDto noticeBoardDto = new NoticeBoardDto();
        noticeBoardDto.setNoticeBoardSeq(Long.parseLong(nSeq));

        // 공지사항 상세정보 가져오기
        NoticeBoardDto requestDto = noticeBoardService.getNoticeBoardInfo(noticeBoardDto, true);

        log.info("### requestDto : {}", requestDto.getContents());

        if (requestDto == null) {
            requestDto = new NoticeBoardDto();
        }

        // 조회된 리스트 결과 넣기
        modelMap.addAttribute("requestDto", requestDto);

        log.info("### .noticeInfo end");
        return "/noticeBoard/noticeBoardInfo";
    }

    // 게시판 수정
    @GetMapping("/noticeBoardEditInfo")
    public String noticeBoardEditInfo(HttpServletRequest request, ModelMap modelMap) {
        log.info("### noticeEditInfo start");

        String msg = "";

        try {

            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
            log.info("### nSeq : {}", nSeq);

            NoticeBoardDto noticeBoardDto = new NoticeBoardDto();
            noticeBoardDto.setNoticeBoardSeq(Long.parseLong(nSeq));

            NoticeBoardDto resultDto = noticeBoardService.getNoticeBoardInfo(noticeBoardDto, false);

            if (resultDto == null) {
                resultDto = new NoticeBoardDto();
            }

            modelMap.addAttribute("resultDto", resultDto);

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info("### 실패 : {}", e.toString());
            e.printStackTrace();
        } finally {
            modelMap.addAttribute("msg", msg);
        }
        log.info("### noticeEditInfo end");
        return "/noticeBoard/noticeBoardEditInfo";
    }

    // 게시 글 수정
    @PostMapping("/noticeBoardUpdate")
    public String noticeBoardUpdate(HttpSession session, HttpServletRequest request, ModelMap modelMap) {
        log.info("### noticeBoardupdate start");

        String msg = "";
        UserEntity userEntity = (UserEntity) session.getAttribute(SessionConst.LOGIN_MEMBER);

        try {
            String user_id = userEntity.getUserId();//아이디
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
            String title = CmmUtil.nvl(request.getParameter("title"));
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeBoardYn"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));
            String regDt = CmmUtil.nvl(request.getParameter("regDt"));

            log.info("### userId : {}", user_id);
            log.info("### nSeq : {}", nSeq);
            log.info("### title : {}", title);
            log.info("### noticeYn : {}", noticeYn);
            log.info("### contents : {}", contents);

            NoticeBoardDto requestDto = new NoticeBoardDto();
            requestDto.setUserId(user_id);
            requestDto.setNoticeBoardSeq(Long.parseLong(nSeq));
            requestDto.setTitle(title);
            requestDto.setNoticeYn(noticeYn);
            requestDto.setContents(contents);
            requestDto.setRegDt(regDt);

            // 게시글 수정 DB
            noticeBoardService.updateNoticeBoardInfo(requestDto);

            msg = "수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info("### 실패 : {}", e.toString());
            e.printStackTrace();
        } finally {
            modelMap.addAttribute("msg", msg);
        }

        log.info("### noticeBoardupdate end");
        return "/noticeBoard/MsgToList";
    }

    // 게시판 글 삭제
    @GetMapping("/noticeBoardDelete")
    public String noticeBoardDelete(HttpServletRequest request, ModelMap modelMap) {
        log.info("### noticeBoardDelete start");

        String msg = "";

        try {

            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
            log.info("### nSeq : {}", nSeq);

            NoticeBoardDto requestDto = new NoticeBoardDto();
            requestDto.setNoticeBoardSeq(Long.parseLong(nSeq));


            // 게시글 삭제 DB
            noticeBoardService.deleteNoticeBoardInfo(requestDto);

            msg = "삭제되었습니다.";
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info("### 실패 : {}", e.toString());
            e.printStackTrace();
        } finally {
            modelMap.addAttribute("msg", msg);
        }

        log.info("### noticeBoardDelete end");
        return "/noticeBoard/MsgToList";
    }

    // 게시판 작성 페이지 이동
    @GetMapping("/noticeBoardReg")
    public String noticeBoardReg() {
        log.info("### noticeBoardReg start");
        log.info("### noticeBoardReg end");
        return "/noticeBoard/noticeBoardReg";
    }

    // 게시판 글 등록
    @PostMapping("/noticeBoardInsert")
    public String noticeBoardInsert(HttpSession session, HttpServletRequest request, ModelMap modelMap) {
        log.info("### noticeBoardInsert start");

        String msg = "";

        try {

////            String user_id = CmmUtil.nvl((String) session.getAttribute("user_Id"));//아이디
//            UserDTO userDTO = new UserDTO();
//            userDTO = (UserDTO) session.getAttribute("userDTO");

            UserEntity userEntity = (UserEntity) session.getAttribute(SessionConst.LOGIN_MEMBER);

            String user_id = CmmUtil.nvl(userEntity.getUserId());//아이디
            String title = CmmUtil.nvl(request.getParameter("title"));
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeYn"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));

            log.info("### userId : {}", user_id);
            log.info("### title : {}", title);
            log.info("### noticeYn : {}", noticeYn);
            log.info("### contents : {}", contents);

            NoticeBoardDto requestDto = new NoticeBoardDto();
            requestDto.setUserId(user_id);
            requestDto.setTitle(title);
            requestDto.setNoticeYn(noticeYn);
            requestDto.setContents(contents);

            noticeBoardService.insertNoticeInfo(requestDto);

            msg = "등록되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info("### 실패 : {}", e.toString());
            e.printStackTrace();
        } finally {
            modelMap.addAttribute("msg", msg);
        }

        log.info("### noticeBoardInsert end");
        return "/noticeBoard/MsgToList";
    }

}
