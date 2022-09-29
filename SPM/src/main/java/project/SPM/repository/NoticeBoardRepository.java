package project.SPM.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.SPM.Entity.NoticeBoardEntity;

import java.util.List;

@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoardEntity, Long> {

    List<NoticeBoardEntity> findAllByOrderByNoticeBoardSeqDesc();

    NoticeBoardEntity findByNoticeBoardSeq(Long noticeBoardSeq);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE notice A SET A.read_cnt = IFNULL(A.read_cnt, 0) + 1 WHERE A.notice_Board_seq = :noticeBoardSeq",
    nativeQuery = true)
    int updateReadCnt(@Param("noticeBoardSeq") Long noticeBoardSeq);
}
