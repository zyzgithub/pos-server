package com.dianba.pos.menu.repository;

import com.dianba.pos.menu.po.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuTypeJpaRepository extends JpaRepository<MenuType, Integer> {

}
