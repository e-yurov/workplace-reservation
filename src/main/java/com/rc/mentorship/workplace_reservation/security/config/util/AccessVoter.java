package com.rc.mentorship.workplace_reservation.security.config.util;

import com.rc.mentorship.workplace_reservation.security.config.configurers.MatchingEntry;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;

@UtilityClass
public class AccessVoter {
    public static final int GRANTED = 1;
    public static final int NO_MATCH = 0;
    public static final int DENIED = -1;

    public static final String twoAsteriskRegexp = "[\\w/.\\-_]@";
    public static final String oneAsteriskRegexp = "[^/]+";

    int vote(MatchingEntry matcher, HttpMethod method, String uri) {
        if (!uriMatches(matcher.getPattern(), uri) ||
                (matcher.getMethod() != null && !matcher.getMethod().equals(method))) {
            return NO_MATCH;
        }
        if (matcher.getAccessGranter().hasAccess()) {
            return GRANTED;
        }
        return DENIED;
    }

    boolean uriMatches(String pattern, String uri) {
        if (uri == null || !uri.startsWith("/") || !pattern.startsWith("/")) {
            return false;
        }

        String regexpPattern = pattern
                .replace("/**", twoAsteriskRegexp)
                .replace("*", oneAsteriskRegexp)
                .replace('@', '*');
        return uri.matches(regexpPattern);
    }

}
