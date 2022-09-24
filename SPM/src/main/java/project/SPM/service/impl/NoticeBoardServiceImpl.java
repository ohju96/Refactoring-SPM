package project.SPM.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.SPM.Entity.NoticeBoardEntity;
import project.SPM.dto.NoticeBoardDto;
import project.SPM.repository.NoticeBoardRepository;
import project.SPM.service.NoticeBoardService;
import project.SPM.util.CmmUtil;
import project.SPM.util.DateUtil;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeBoardServiceImpl implements NoticeBoardService {

    private final NoticeBoardRepository noticeBoardRepository;

    // 공지 전체 가져오기
    @Override
    public List<NoticeBoardDto> getNoticeBoardList() {
        log.info("### .getNoticeBoardList start");

        List<NoticeBoardEntity> resultList = noticeBoardRepository.findAllByOrderByNoticeBoardSeqDesc();

        List<NoticeBoardDto> noticeBoardDtoList = new ObjectMapper().convertValue(resultList,
                new TypeReference<List<NoticeBoardDto>>() {
                });

        log.info("### .getNoticeBoardList end");
        return noticeBoardDtoList;
    }

    // 공지 상세 정보 가져오기
    @Transactional // 테이블 값을 변경하는 쿼리 실행은 트랜젝션 설정이 필요
    @Override
    public NoticeBoardDto getNoticeBoardInfo(NoticeBoardDto noticeBoardDto, boolean type) throws Exception {
        log.info("### .getNoticeBoardInfo start");

        if (type) {
            //조회수 증가
            int res = noticeBoardRepository.updateReadCnt(noticeBoardDto.getNoticeBoardSeq());
            log.info("### 조회수 증가 성공 여부 res : {}", res);
        }

        // 공지사항 상세 내역
        NoticeBoardEntity resultEntity = noticeBoardRepository.findByNoticeBoardSeq(noticeBoardDto.getNoticeBoardSeq());

        // 엔티티 값 Dto에 넣기
        NoticeBoardDto resultNoticeBoardDto = new ObjectMapper().convertValue(resultEntity, NoticeBoardDto.class);

        log.info("### .getNoticeBoardInfo end");
        return resultNoticeBoardDto;
    }

    // 해당 공지 수정
    @Transactional
    @Override
    public void updateNoticeBoardInfo(NoticeBoardDto noticeBoardDto) throws Exception {
        log.info("### .updateNoticeBoardInfo start");

        Long noticeBoardSeq = noticeBoardDto.getNoticeBoardSeq();

        String title = CmmUtil.nvl(noticeBoardDto.getTitle());
        String noticeYn = CmmUtil.nvl(noticeBoardDto.getNoticeYn());
        String contents = CmmUtil.nvl(noticeBoardDto.getContents());
        String userId = CmmUtil.nvl(noticeBoardDto.getUserId());

        log.info("### noticeBoardSeq : {}", noticeBoardSeq);
        log.info("### title : {}", title);
        log.info("### noticeYn : {}", noticeYn);
        log.info("### contents : {}", contents);
        log.info("### userId : {}", userId);

        // 현재 공지사항 조회수 가져오기
        NoticeBoardEntity resultEntity = noticeBoardRepository.findByNoticeBoardSeq(noticeBoardSeq);

        // 수정할 값들을 빌더를 통해 엔티티에 저장
        NoticeBoardEntity requestEntity = NoticeBoardEntity.builder()
                .noticeBoardSeq(noticeBoardSeq).title(title).noticeYn(noticeYn).contents(contents).userId(userId)
                .readCnt(resultEntity.getReadCnt())
                .build();

        // 데이터 수정
        noticeBoardRepository.save(requestEntity);

        log.info("### .updateNoticeBoardInfo end");
    }

    // 해당 공지 삭제
    @Override
    public void deleteNoticeBoardInfo(NoticeBoardDto noticeBoardDto) throws Exception {
        log.info("### .deleteNoticeBoardInfo start");

        Long noticeBoardSeq = noticeBoardDto.getNoticeBoardSeq();
        log.info("### noticeBoardSeq : {}", noticeBoardDto);

        // 데이터 수정
        noticeBoardRepository.deleteById(noticeBoardSeq);

        log.info("### .deleteNoticeBoardInfo end");
    }

    // 해당 공지 저장
    @Override
    public void insertNoticeInfo(NoticeBoardDto noticeBoardDto) throws Exception {
        log.info("### .insertNoticeInfo start");

        String title = CmmUtil.nvl(noticeBoardDto.getTitle());
        String noticeYn = CmmUtil.nvl(noticeBoardDto.getNoticeYn());
        String contents = CmmUtil.nvl(noticeBoardDto.getContents());
        String userId = CmmUtil.nvl(noticeBoardDto.getUserId());

        log.info("### title : {}", title);
        log.info("### noticeYn : {}", noticeYn);
        log.info("### contents : {}", contents);
        log.info("### userId : {}", userId);

        // 공지사항 저장을 위해 pk 값은 빌더에 추가하지 않는다.
        NoticeBoardEntity requestEntity = NoticeBoardEntity.builder()
                .title(title).noticeYn(noticeYn).contents(contents).userId(userId).readCnt(0L)
                .regId(userId).regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .chgId(userId).chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        // 공지 저장
        noticeBoardRepository.save(requestEntity);

        log.info("### .insertNoticeInfo end");
    }
}
