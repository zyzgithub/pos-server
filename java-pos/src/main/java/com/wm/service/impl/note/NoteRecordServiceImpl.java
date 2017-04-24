package com.wm.service.impl.note;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.note.NoteRecordServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("noteRecordService")
@Transactional
public class NoteRecordServiceImpl extends CommonServiceImpl implements NoteRecordServiceI {
	
}