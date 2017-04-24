package com.wm.service.impl.courier;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.courier.ScrambleLogServiceI;

@Service
@Transactional
public class ScrambleLogServiceImpl extends CommonServiceImpl implements ScrambleLogServiceI{

}
