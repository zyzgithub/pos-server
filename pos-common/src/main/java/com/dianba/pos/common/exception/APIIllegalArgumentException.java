package com.dianba.pos.common.exception;

import com.dianba.pos.common.exception.core.AbstractApplicationRuntimeException;
import com.dianba.pos.common.exception.core.AssertCore;
import org.springframework.util.StringUtils;

public class APIIllegalArgumentException
        extends AbstractApplicationRuntimeException {
    private static final long serialVersionUID = 6111831866235627234L;

    public APIIllegalArgumentException(String msg) {
        super(Integer.valueOf(AssertCore.G4004.getCode()), StringUtils.isEmpty(msg) ? AssertCore.G4004.getMsg() : msg);
    }
}
