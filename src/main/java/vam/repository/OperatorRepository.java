package vam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vam.entity.Operator;
import vam.entity.VarFile;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {

	List<Operator> findByKey(String key);

}