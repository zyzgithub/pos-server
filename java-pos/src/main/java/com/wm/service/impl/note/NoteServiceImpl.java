package com.wm.service.impl.note;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.note.NoteServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("noteService")
@Transactional
public class NoteServiceImpl extends CommonServiceImpl implements NoteServiceI {
	
}