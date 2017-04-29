package com.dianba.pos.common.exception;

import com.dianba.pos.common.exception.core.AbstractApplicationRuntimeException;
import com.dianba.pos.common.exception.core.AssertCore;
import org.springframework.util.StringUtils;

public class DataNotFoundException
        extends AbstractApplicationRuntimeException {
    private static final long serialVersionUID = -8167430259032934913L;

    public DataNotFoundException(String msg) {
        super(Integer.valueOf(AssertCore.G4001.getCode()), StringUtils.isEmpty(msg) ? AssertCore.G4001.getMsg() : msg);
    }
}
