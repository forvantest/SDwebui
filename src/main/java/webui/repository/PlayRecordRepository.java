package webui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import webui.entity.PlayRecord;

@Repository
public interface PlayRecordRepository extends JpaRepository<PlayRecord, Long> {

	List<PlayRecord> findByKey(String key);

}