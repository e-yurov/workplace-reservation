package com.rc.mentorship.workplace_reservation.util.filter;

import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class FilterParamParser {
    public Map<String, Filter> parseAllParams(Map<String, String> filtersMap, Set<String> paramsToRemove) {
        filtersMap.keySet().removeAll(paramsToRemove);

        Map<String, Filter> filters = new HashMap<>();
        filtersMap.forEach((k, v) -> filters.put(k, parseParam(v)));
        return filters;
    }

    public Filter parseParam(String param) {
        String[] params = param.split("/");
        if (params.length != 2) {
            throw new FiltrationParamsFormatException();
        }

        FilterType filterType = FilterType.getByShortName(params[0]);
        if (filterType == null) {
            throw new FiltrationParamsFormatException();
        }

        return new Filter(filterType, params[1]);
    }
}
