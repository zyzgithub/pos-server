package com.dianba.pos.menu.repository;

import com.dianba.pos.menu.po.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MenuJpaRepository extends JpaRepository<Menu, Integer> {


    /**
     * 获取增值服务商品信息
     *
     * @param isFlash
     * @param merchantId
     * @param codeId
     * @return
     */
    List<Menu> findAllByIsFlashAndMerchantIdAndPrintTypeAndCodeId(Integer isFlash, Integer merchantId,
                                                                  Integer printTypeId, Integer codeId);

    Menu findByPrintType(Integer printType);
}
