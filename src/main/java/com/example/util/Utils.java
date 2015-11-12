package com.example.util;

import com.example.dto.LocalQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

/**
 * Created by pedroxs on 12/11/15.
 */
public abstract class Utils {
    private Utils() {
    }

    public static String getSortString(Sort sort) {
        return sort == null ? "" : sort.toString().replace(": ", ",");
    }

    public static String normalizeQuery(LocalQuery query) {
        String queryAttr = StringUtils.defaultString(StringUtils.lowerCase(query.getAttribute()));
        if (StringUtils.isEmpty(StringUtils.trimToEmpty(query.getQueryString()))) {
            return StringUtils.EMPTY;
        }
        return queryAttr;
    }

}
