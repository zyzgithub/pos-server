package com.dianba.pos.payment.repository;

import com.dianba.pos.payment.po.PosReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosRewardJpaRepository extends JpaRepository<PosReward, Long> {

    List<PosReward> findByStatus(Integer status);

    List<PosReward> findByStatusAndType(Integer status, Integer type);
}
