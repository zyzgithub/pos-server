package com.dianba.pos.common.exception;

import com.dianba.pos.common.exception.core.AbstractApplicationException;
import com.dianba.pos.common.exception.core.AssertCore;
import org.springframework.util.StringUtils;

public class EnumerationCheckException
        extends AbstractApplicationException {
    private static final long serialVersionUID = -8947382885698480563L;

    public EnumerationCheckException(AssertCore coreEnum) {
        this(coreEnum, null, null);
    }

    public EnumerationCheckException(AssertCore coreEnum, String msg, Object tag) {
        super(Integer.valueOf(coreEnum.getCode()), StringUtils.isEmpty(msg) ? coreEnum.getMsg() : msg, tag);
    }
}
