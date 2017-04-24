package com.wm.service.impl.image;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.image.ImageServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("imageService")
@Transactional
public class ImageServiceImpl extends CommonServiceImpl implements ImageServiceI {
	
}