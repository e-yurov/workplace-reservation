package com.rc.mentorship.workplace_reservation.security.config.util;

import com.rc.mentorship.workplace_reservation.security.config.configurers.MatchingEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

@RequiredArgsConstructor
public class RequestMatcher {
    private final List<MatchingEntry> matchers;

    public boolean match(HttpMethod method, String uri) {
        for (MatchingEntry matcher : matchers) {
            int vote = AccessVoter.vote(matcher, method, uri);
            switch (vote) {
                case AccessVoter.DENIED -> {
                    return false;
                }
                case AccessVoter.GRANTED -> {
                    return true;
                }
            }
        }
        return false;
    }

}
