package project.SPM.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.SPM.chat.ChatHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatController {

    // 채팅방 입장 전
    @RequestMapping("/intro")
    public String intro() {
        return "/chat/intro";
    }

    // 채팅방 접속
    @RequestMapping("/room")
    public String room() {
        return "/chat/chatroom";
    }

    // 채팅방 목록
    @RequestMapping("roomList")
    @ResponseBody //특수 함수 결과를 JSON으로 변환
    public Set<String> roomList() {
        log.info(this.getClass().getName() + ".roomList start ###");
        log.info(this.getClass().getName() + ".roomList end ###");
        return ChatHandler.roomInfo.keySet();
    }
}
