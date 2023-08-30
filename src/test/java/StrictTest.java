import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.ext.RuntimeDelegate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StrictTest {

    @Test
    public void testCookie_IsBuilderWorkingCorrectly() {
        var newCookie = new NewCookie.Builder("testcookie")
                .value("hello")
                .path("/")
                .version(1)
                .maxAge(-1)
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.STRICT)
                .build();

        assertEquals("/", newCookie.getPath());
        assertEquals("hello", newCookie.getValue());
        assertEquals(1, newCookie.getVersion());
        assertTrue(newCookie.isHttpOnly());
        assertTrue(newCookie.isSecure());
        assertEquals(NewCookie.SameSite.STRICT, newCookie.getSameSite());
    }

    @Test
    public void testCookie_Serialization() {
        var newCookie = new NewCookie.Builder("testcookie")
                .value("hello")
                .path("/")
                .version(1)
                .maxAge(-1)
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.STRICT)
                .build();

        String serializedCookie = RuntimeDelegate.getInstance().createHeaderDelegate(NewCookie.class).toString(newCookie);
        assertTrue(String.format("serialized cookie does not contain samesite-attribute: %s", serializedCookie), serializedCookie.toLowerCase().contains("samesite=strict"));
    }

    @Test
    public void testCookie_Deserialization() {
        String serializedCookie = "testcookie=hello;Version=1;Path=/;SameSite=Strict;Secure;HttpOnly";
        NewCookie deserializedCookie = RuntimeDelegate.getInstance().createHeaderDelegate(NewCookie.class).fromString(serializedCookie);

        assertEquals("/", deserializedCookie.getPath());
        assertEquals("hello", deserializedCookie.getValue());
        assertEquals(1, deserializedCookie.getVersion());
        assertTrue(deserializedCookie.isHttpOnly());
        assertTrue(deserializedCookie.isSecure());
        assertEquals("deserialized cookie does not contain correct value for samesite-attribute", NewCookie.SameSite.STRICT, deserializedCookie.getSameSite());
    }
}
