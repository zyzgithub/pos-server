package com.dianba.pos.box.repository;

import com.dianba.pos.box.po.BoxItemLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoxItemLabelJpaRepository extends JpaRepository<BoxItemLabel, Long> {

    List<BoxItemLabel> findByRfidIn(List<String> rfids);
}
