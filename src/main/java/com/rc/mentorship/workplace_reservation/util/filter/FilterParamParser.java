package com.rc.mentorship.workplace_reservation.util.filter;

import com.rc.mentorship.workplace_reservation.exception.FiltrationParamsFormatException;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class FilterParamParser {
    public List<Filter> parseAllParams(Map<String, String> filtersMap, Set<String> paramsToRemove) {
        filtersMap.keySet().removeAll(paramsToRemove);

        List<Filter> filters = new ArrayList<>();
        filtersMap.forEach((k, v) -> filters.add(parseParam(k, v)));
        return filters;
    }

    public Filter parseParam(String field, String param) {
        String[] params = param.split(":");
        if (params.length != 2) {
            throw new FiltrationParamsFormatException();
        }

        FilterType filterType = FilterType.getByShortName(params[0]);
        if (filterType == null) {
            throw new FiltrationParamsFormatException();
        }

        return new Filter(field, filterType, params[1]);
    }
}
