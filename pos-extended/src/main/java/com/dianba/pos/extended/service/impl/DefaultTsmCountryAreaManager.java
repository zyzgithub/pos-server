package com.dianba.pos.extended.service.impl;

import com.dianba.pos.extended.po.TsmCountryArea;
import com.dianba.pos.extended.repository.TsmCountryAreaJapRepository;
import com.dianba.pos.extended.service.TsmCountryAreaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/5/10 0010.
 */
@Service
public class DefaultTsmCountryAreaManager implements TsmCountryAreaManager {

    @Autowired
    private TsmCountryAreaJapRepository tsmCountryAreaJapRepository;
    @Override
    public TsmCountryArea getInfoById(Integer id) {
        return tsmCountryAreaJapRepository.getOne(Long.parseLong(id.toString()));
    }
}
