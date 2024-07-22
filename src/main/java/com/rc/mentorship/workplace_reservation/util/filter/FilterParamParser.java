package com.rc.mentorship.workplace_reservation.util.filter;

import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class FilterParamParser {
    public Map<String, Filter> parseAllParams(Map<String, String> filtersMap,
                                              Set<String> paramsToRemove) {
        filtersMap.keySet().removeAll(paramsToRemove);

        Map<String, Filter> filters = new HashMap<>();
        filtersMap.forEach((k, v) -> filters.put(k, parseParam(k, v)));
        return filters;
    }

    public Filter parseParam(String paramName, String param) {
        String[] params = param.split("/");

        FilterType filterType;
        String value;
        if (params.length == 1) {
            filterType = FilterType.EQUALS;
            value = params[0];
        } else if (params.length == 2) {
            filterType = FilterType.getByShortName(params[0]);
            if (filterType == null) {
                throw new FiltrationParamsFormatException(paramName);
            }
            value = params[1];
        } else {
            throw new FiltrationParamsFormatException(paramName);
        }

        return new Filter(filterType, value);
    }
}
