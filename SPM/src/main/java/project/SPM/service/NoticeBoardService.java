package project.SPM.service;

import project.SPM.dto.NoticeBoardDto;

import java.util.List;

public interface NoticeBoardService {

    // 공지 전체 가져오기
    List<NoticeBoardDto> getNoticeBoardList();

    // 공지 상세 정보 가져오기
    NoticeBoardDto getNoticeBoardInfo(NoticeBoardDto noticeBoardDto, boolean type) throws Exception;

    //해당 공지 수정
    void updateNoticeBoardInfo(NoticeBoardDto noticeBoardDto) throws Exception;

    //해당 공지 삭제
    void deleteNoticeBoardInfo(NoticeBoardDto noticeBoardDto) throws Exception;

    //해당 공지 저장
    void insertNoticeInfo(NoticeBoardDto noticeBoardDto) throws Exception;
}
