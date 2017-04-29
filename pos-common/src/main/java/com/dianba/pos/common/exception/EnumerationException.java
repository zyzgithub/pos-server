package com.dianba.pos.common.exception;

import com.dianba.pos.common.exception.core.AbstractApplicationRuntimeException;
import com.dianba.pos.common.exception.core.AssertCore;
import org.springframework.util.StringUtils;

public class EnumerationException
        extends AbstractApplicationRuntimeException {
    private static final long serialVersionUID = -8947382885698480563L;

    public EnumerationException(AssertCore coreEnum) {
        super(coreEnum, coreEnum.getMsg());
    }

    public EnumerationException(AssertCore coreEnum, String msg, Object tag) {
        super(Integer.valueOf(coreEnum.getCode()), StringUtils.isEmpty(msg) ? coreEnum.getMsg() : msg, tag);
    }
}
