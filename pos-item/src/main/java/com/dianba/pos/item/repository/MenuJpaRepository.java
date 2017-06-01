package com.dianba.pos.item.repository;

import com.dianba.pos.item.po.Menu;
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

    /**
     * 获取商品信息根据 第三方商品id
     * @param menuKey
     * @return
     */
    Menu findByMenuKeyAndDisplayAndIsDelete(String menuKey, String display, String isDelete);
}
