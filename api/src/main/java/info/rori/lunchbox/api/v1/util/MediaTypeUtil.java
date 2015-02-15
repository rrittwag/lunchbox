package info.rori.lunchbox.api.v1.util;

import javax.ws.rs.core.MediaType;

public class MediaTypeUtil {
    private MediaTypeUtil() {
    }

    public final static String APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=utf-8";
}
