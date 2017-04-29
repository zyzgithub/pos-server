package com.dianba.pos.common.exception.lang;

import com.dianba.pos.common.exception.APIIllegalArgumentException;
import com.dianba.pos.common.exception.DataNotFoundException;
import com.dianba.pos.common.exception.EnumerationCheckException;
import com.dianba.pos.common.exception.EnumerationException;
import com.dianba.pos.common.exception.core.AssertCore;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class Assert {
    public static final void checkArgumentNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new APIIllegalArgumentException(msg);
        }
    }

    public static final void checkArgumentIsTrue(boolean b, String msg) {
        if (!b) {
            throw new APIIllegalArgumentException(msg);
        }
    }

    public static final void checkArgumentNotEmpty(Collection<?> obj, String msg) {
        checkArgumentIsTrue((obj == null) || (obj.isEmpty()), msg);
    }

    public static final void checkArgumentNotEmpty(CharSequence cs, String msg) {
        checkArgumentIsTrue((cs == null) || (cs.length() > 0), msg);
    }

    public static final void checkArgumentNotBlank(CharSequence cs, String msg) {
        checkArgumentIsTrue(StringUtils.hasText(cs), msg);
    }

    public static final void dataNotFound(Object obj, String msg) {
        if (obj == null) {
            throw new DataNotFoundException(msg);
        }
    }

    public static final void dataNotFound(Object obj) {
        dataNotFound(obj, null);
    }

    public static final void notEmpty(Collection<?> obj, AssertCore coreEnum, String msg) {
        isTrue((obj == null) || (obj.isEmpty()), coreEnum, msg);
    }

    public static final void notEmpty(CharSequence cs, AssertCore coreEnum, String msg) {
        isTrue((cs == null) || (cs.length() > 0), coreEnum, msg);
    }

    public static final void notBlank(CharSequence cs, AssertCore coreEnum, String msg) {
        isTrue(StringUtils.hasText(cs), coreEnum, msg);
    }

    public static final void notNull(Object obj, AssertCore coreEnum, String msg) {
        isTrue(obj != null, coreEnum, msg);
    }

    public static final void notNull(Object obj, AssertCore coreEnum) {
        isTrue(obj != null, coreEnum, "");
    }

    public static final void isTrue(boolean b, AssertCore coreEnum, String msg, Object obj) {
        if (!b) {
            throw new EnumerationException(coreEnum, msg, obj);
        }
    }

    public static final void isTrue(boolean b, AssertCore coreEnum, String msg) {
        if (!b) {
            throw new EnumerationException(coreEnum, msg, null);
        }
    }

    public static final void checkIsTrue(boolean b, AssertCore coreEnum, String msg)
            throws EnumerationCheckException {
        if (!b) {
            throw new EnumerationCheckException(coreEnum, msg, null);
        }
    }

    public static final void isTrue(boolean b, AssertCore coreEnum) {
        isTrue(b, coreEnum, "");
    }

    public static final void notEmpty(Collection<?> collection, AssertCore coreEnum) {
        isTrue((collection != null) && (!collection.isEmpty()), coreEnum, "");
    }

    public static final void isEmpty(Collection<?> collection, AssertCore coreEnum) {
        isTrue((collection == null) || (collection.isEmpty()), coreEnum, "");
    }
}
