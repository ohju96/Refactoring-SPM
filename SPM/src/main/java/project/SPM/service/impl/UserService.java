package project.SPM.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import project.SPM.Entity.UserEntity;
import project.SPM.dto.MailDTO;
import project.SPM.dto.UserDTO;
import project.SPM.repository.IUserRepository;
import project.SPM.repository.impl.UserRepository;
import project.SPM.service.IUserService;
import project.SPM.util.EncryptUtil;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("UserService")
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final IUserRepository iUserRepository;

    private final MailSender mailSender;

    // 회원 가입 로직
    @Override
    public String insertUser(UserDTO userDTO) throws Exception {

        log.debug("### InsertUser 실행");


        /**
         *  1-1. builder pattern 활용하여 Entity에 값을 세팅
         */
        UserEntity userEntity = UserEntity.builder()
                .userNo(userDTO.getUserNo())
                .userName(userDTO.getUserName())
                .userPn(userDTO.getUserPn())
                .userEmail(userDTO.getUserEmail())
                .userId(userDTO.getUserId())
                .userPw(EncryptUtil.encHashSHA256(userDTO.getUserPw()))
                .userAddr(userDTO.getUserAddr())
                .build();

        log.info("userEntity에 담긴 값 확인 ={}", userEntity);

        /**
         *  1-2. ID 및 Email 중복 체크
         */
        boolean res = validateDuplicateUser(userEntity);
        log.debug("### 중복 체크 후 res : {}", res);

        String msg;

        if (res == false) {
            // 중복 체크 완료 후 회원 가입 로직
            iUserRepository.save(userEntity);
            msg = "회원가입에 성공했습니다.";
        } else {
            msg = "중복된 정보가 있습니다.";
        }

        log.debug("### msg : {}", msg);

        return msg;
    }

    private boolean validateDuplicateUser(UserEntity userEntity) {

        log.info(this.getClass().getName() + "중복 체크 로직 실행");

        log.info("중복 체크를 위해 받아온 아이디 값 = {}", userEntity.getUserId());
        log.info("중복 체크를 위해 받아온 이메일 값 = {}", userEntity.getUserEmail());

        // 아이디 중복 체크
        boolean id = iUserRepository.existsByUserId(userEntity.getUserId());
        if (id == true) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 이메일 중복 체크
        boolean email = iUserRepository.existsByUserEmail(userEntity.getUserEmail());
        if (email == true) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        return false;
    }

    // 로그인 로직
    // TODO: 2022-06-02 로그인 로직 IUserRepository 사용하도록 수정 필요 
    @Override
    public boolean login(UserDTO userDTO) throws Exception {

        boolean res;

        // 빌더 패턴 활용하여 Entity 값 셋팅
        UserEntity userEntity = UserEntity.builder()
                .userNo(userDTO.getUserNo())
                .userName(userDTO.getUserName())
                .userPn(userDTO.getUserPn())
                .userEmail(userDTO.getUserEmail())
                .userId(userDTO.getUserId())
                .userPw(EncryptUtil.encHashSHA256(userDTO.getUserPw()))
                .userAddr(userDTO.getUserAddr())
                .build();

        log.info("Service에서 로그인 체크 DTO 받은 Entity 값 = {}", userEntity);

        // 로그인을 위해 ID, PW 조회
        res = checkLogin(userEntity);

        return res;
    }

    private boolean checkLogin(UserEntity userEntity) {
        log.info(this.getClass().getName() + "로그인 체크 로직 실행");
        log.info("로그인 체크를 위해 받아온 비밀번호 값 = {}", userEntity.getUserPw());

        boolean res;

        List<UserEntity> byPw = userRepository.checkLogin(userEntity.getUserId(),userEntity.getUserPw());

        if (byPw != null && !byPw.isEmpty() ) {
            log.info("res 1 ={}", byPw);
            res = true;
        } else {
            log.info("res 0 ={}", byPw);
            res = false;
        }

        return res;

    }

    // 세션에 담기 위해 값 조회
    @Override
    public UserEntity loginSession(UserDTO userDTO) throws Exception {

        List<UserEntity> userDTOList = iUserRepository.findAllByUserId(userDTO.getUserId());
        UserEntity userEntity = userDTOList.get(0);

        return userEntity;
    }

    // 비밀번호찾기
    @Override
    public String findPw(String userEmail) throws Exception {

        String msg;

        try {

            List<UserEntity> userDTOList = iUserRepository.findAllByUserEmail(userEmail);

            MailDTO mailDTO = new MailDTO();

            if (userDTOList.get(0).getUserEmail().equals(userEmail)) {
                log.debug("### if start");
                String result = changePw();

                UserEntity userEntity = UserEntity.builder()
                        .userNo(userDTOList.get(0).getUserNo())
                        .userName(userDTOList.get(0).getUserName())
                        .userPn(userDTOList.get(0).getUserPn())
                        .userEmail(userDTOList.get(0).getUserEmail())
                        .userId(userDTOList.get(0).getUserId())
                        .userPw(EncryptUtil.encHashSHA256(result))
                        .userAddr(userDTOList.get(0).getUserAddr())
                        .build();

                iUserRepository.save(userEntity);

                mailDTO.setAddress(userEmail);
                mailDTO.setTitle("[소경관] : 임시비밀번호");
                mailDTO.setMessage("임시비밀번호 : [ " + result + " ]");

                sendMail(mailDTO);
                msg = "가입하신 이메일로 임시비밀번호가 전송되었습니다.";

            } else {
                msg = "이메일 전송에 실패하였습니다. 다시 확인해주세요.";
            }

        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            msg = "비밀번호 찾기에 실패하였습니다. 이메일을 다시 확인해주세요.";
        }

        return msg;
    }

    // 비밀번호 랜덤 10자리 생성
    @Override
    public String findId(String userEmail) throws Exception {

        String msg;
        try {
            List<UserEntity> userDTOList = iUserRepository.findAllByUserEmail(userEmail);
            MailDTO mailDTO = new MailDTO();

            boolean isEnd;
            if (userDTOList.get(0).getUserEmail().equals(userEmail)) {
                log.debug("### if start");

                mailDTO.setAddress(userEmail);
                mailDTO.setTitle("[소경관] : 아이디 찾기");
                mailDTO.setMessage("아이디 : [ " + userDTOList.get(0).getUserId() + " ]");

                sendMail(mailDTO);
                msg="가입하신 이메일로 아이디가 전송되었습니다.";
                isEnd = true;
            } else if (userDTOList == null){
                msg="이메일 전송에 실패하였습니다. 이메일을 다시 확인해주세요.";
                isEnd = true;
            } else {
                msg="이메일 전송에 실패하였습니다. 다시 시도해주세요.";
                isEnd = true;
            }

            if (isEnd == true) {
                return msg;
            }

        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            msg = "이메일 전송에 실패하였습니다. 이메일을 다시 확인해주세요.";
        }

        return msg;

    }

    public static String changePw() {

        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String res = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            res += charSet[idx];
        }
        return res;
    }
    // 아이디 찾기

    // 메일 보내기
//    @Override
    public boolean sendMail(MailDTO mailDTO) throws Exception {

        log.debug("### sendMail start");

        boolean res = true;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        message.setFrom("testohju@gmail.com");
        message.setReplyTo("testohju@gmail.com");
        mailSender.send(message);

        log.debug("### sendMail END");

        return res;

    }

}