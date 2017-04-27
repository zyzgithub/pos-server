package com.dianba.pos.menu.repository;

import com.dianba.pos.menu.po.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

}
