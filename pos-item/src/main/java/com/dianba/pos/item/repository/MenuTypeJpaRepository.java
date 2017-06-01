package com.dianba.pos.item.repository;

import com.dianba.pos.item.po.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuTypeJpaRepository extends JpaRepository<MenuType, Integer> {

}
