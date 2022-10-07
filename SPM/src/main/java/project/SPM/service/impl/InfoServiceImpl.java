package project.SPM.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.SPM.Entity.UserEntity;
import project.SPM.dto.UserDTO;
import project.SPM.dto.ViewCarDTO;
import project.SPM.mapper.ICarMapper;
import project.SPM.mapper.ICheckMapper;
import project.SPM.repository.IUserRepository;
import project.SPM.service.InfoService;
import project.SPM.util.EncryptUtil;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {

    private final IUserRepository iUserRepository;
    private final ICarMapper iCarMapper;
    private final ICheckMapper iCheckMapper;

    @Override
    public boolean updateInfo(UserDTO userDTO) throws Exception {

        UserEntity userEntity = UserEntity.builder()
                .userNo(userDTO.getUserNo())
                .userName(userDTO.getUserName())
                .userPn(userDTO.getUserPn())
                .userEmail(userDTO.getUserEmail())
                .userId(userDTO.getUserId())
                .userPw(EncryptUtil.encHashSHA256(userDTO.getUserPw()))
                .userAddr(userDTO.getUserAddr())
                .build();

        iUserRepository.save(userEntity);
        boolean res = true;

        return res;
    }

    // 회원탈퇴
    @Override
    public boolean deleteUser(UserEntity userEntity) throws Exception {

        boolean res = false;


        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());

        res = iCarMapper.dropCar(userDTO);

        if (res == true) {

            List<ViewCarDTO> viewCarDTO= iCheckMapper.viewCheck(userDTO);
            iCheckMapper.dropUser(viewCarDTO);
            iUserRepository.delete(userEntity);
        }

        return res;
    }
}
