package com.rc.mentorship.workplace_reservation.security.config.util;

import com.rc.mentorship.workplace_reservation.security.config.configurers.MatchingEntry;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;

@UtilityClass
public class AccessVoter {
    public static final int GRANTED = 1;
    public static final int NO_MATCH = 0;
    public static final int DENIED = -1;

    public static final String twoAsteriskRegexp = "[\\w/]@";
    public static final String oneAsteriskRegexp = "[^/]+";

    int vote(MatchingEntry matcher, HttpMethod method, String uri) {
        if (!uriMatches(matcher.getPattern(), uri) ||
                (method != null && !matcher.getMethod().equals(method))) {
            return NO_MATCH;
        }
        if (matcher.getAccessGranter().hasAccess()) {
            return GRANTED;
        }
        return DENIED;
    }

//    private boolean uriMatches(String pattern, String uri) {
//        if (uri == null || !uri.startsWith("/") || !pattern.startsWith("/")) {
//            return false;
//        }
//
//        String[] splitPattern = pattern.substring(1).split("/");
//        String[] splitPath = uri.substring(1).split("/");
//        int maxInd = Math.max(splitPattern.length, splitPath.length);
//        for (int i = 0; i < maxInd; i++) {
//            if (i == splitPattern.length || i == splitPath.length) {
//                return false;
//            }
//
//            if (splitPattern[i].equals("**")) {
//                return true;
//            }
//            if (!splitPattern[i].equals("*")) {
//                if (!splitPattern[i].equals(splitPath[i])) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    public boolean uriMatches(String pattern, String uri) {
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
