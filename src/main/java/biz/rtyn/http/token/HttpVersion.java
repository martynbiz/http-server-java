package biz.rtyn.http.token;

import biz.rtyn.http.HttpParsingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws HttpParsingException {
        Matcher matcher = httpVersionRegexPattern.matcher(literalVersion);
        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }

        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));

        HttpVersion tempBestCompatible = null;
        for (HttpVersion version : HttpVersion.values()) {
            if (version.LITERAL.equals(literalVersion)) {
                return version;
            } else {
                if (version.MAJOR == major) {
                    if (version.MINOR < minor) {
                        tempBestCompatible = version;
                    }
                }
            }
        }
        return tempBestCompatible;
    }

    private static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    HttpVersion(String literal, int major, int minor) {
        LITERAL = literal;
        MAJOR = major;
        MINOR = minor;
    }
}
