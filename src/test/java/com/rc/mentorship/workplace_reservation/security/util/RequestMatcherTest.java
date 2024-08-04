package com.rc.mentorship.workplace_reservation.security.util;


import com.rc.mentorship.workplace_reservation.security.config.util.AccessVoter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestMatcherTest {
    @Test
    public void testExactMatch() {
        assertTrue(AccessVoter.uriMatches("/a/b/c", "/a/b/c"));
    }

    @Test
    public void testSingleLevelWildcardMatch() {
        assertTrue(AccessVoter.uriMatches("/a/*/c", "/a/b/c"));
        assertFalse(AccessVoter.uriMatches("/a/*/c", "/a/b/d/c"));
        assertTrue(AccessVoter.uriMatches("/a/*", "/a/b"));
        assertFalse(AccessVoter.uriMatches("/a/*", "/a/b/c"));
    }

    @Test
    public void testMultiLevelWildcardMatch() {
        assertTrue(AccessVoter.uriMatches("/a/**/c", "/a/b/d/c"));
        assertTrue(AccessVoter.uriMatches("/a/**/c", "/a/x/c"));
        assertTrue(AccessVoter.uriMatches("/**/c", "/a/b/c"));
        assertTrue(AccessVoter.uriMatches("/**/c", "/c"));
        assertTrue(AccessVoter.uriMatches("/a/**", "/a/b/c"));
        assertTrue(AccessVoter.uriMatches("/a/**", "/a/b/c/d"));
    }

    @Test
    public void testMixedWildcardAndExactMatch() {
        assertTrue(AccessVoter.uriMatches("/**", "/a/b/c"));
        assertTrue(AccessVoter.uriMatches("/a/*/c", "/a/x/c"));
        assertFalse(AccessVoter.uriMatches("/a/b/c", "/a/b/x"));
    }

    @Test
    public void testNoMatch() {
        assertFalse(AccessVoter.uriMatches("/a/b/c", "/a/b"));
        assertFalse(AccessVoter.uriMatches("/a/b/c", "/a/b/c/d"));
        assertFalse(AccessVoter.uriMatches("/a/*/d", "/a/b/c"));
        assertFalse(AccessVoter.uriMatches("/**/d", "/a/b/c"));
    }

    @Test
    public void testRootMatch() {
        assertTrue(AccessVoter.uriMatches("/", "/"));
        assertFalse(AccessVoter.uriMatches("/", "/a"));
        assertTrue(AccessVoter.uriMatches("/**", "/"));
    }
}
