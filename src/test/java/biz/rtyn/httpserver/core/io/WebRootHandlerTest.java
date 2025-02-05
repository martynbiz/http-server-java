package biz.rtyn.httpserver.core.io;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WebRootHandlerTest {

    WebRootHandler webRootHandler;

    private Method checkIfEndsWithSlashMethod;
    private Method checkIfProvidedRelativePathExistsMethod;

    @BeforeAll
    public void beforeClass() throws WebRootNotFoundException, NoSuchMethodException {
        webRootHandler = new WebRootHandler("WebRoot");
        Class<WebRootHandler> cls = WebRootHandler.class;

        checkIfEndsWithSlashMethod = cls.getDeclaredMethod("checkIfEndsWithSlash", String.class);
        checkIfEndsWithSlashMethod.setAccessible(true);

        checkIfProvidedRelativePathExistsMethod = cls.getDeclaredMethod("checkIfProvidedRelativePathExists", String.class);
        checkIfProvidedRelativePathExistsMethod.setAccessible(true);
    }

    @Test
    void constructorGoodPath() {

    }

    @Test
    void constructorBadPath() {

    }

    @Test
    void constructorGoodPath2() {

    }

    @Test
    void constructorBadPath2() {

    }

    @Test
    void checkIfEndsWithSlashMethodFalse() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "index.html");
            assertFalse(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndsWithSlashMethodFalse2() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/index.html");
            assertFalse(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndsWithSlashMethodFalse3() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/private/index.html");
            assertFalse(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndsWithSlashMethodTrue() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "home/");
            assertTrue(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndsWithSlashMethodTrue2() {
        try {
            boolean result = (boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/");
            assertTrue(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootfilePathExists() {
        try {
            boolean result = (boolean) checkIfProvidedRelativePathExistsMethod.invoke(webRootHandler, "/index.html");
            assertTrue(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootfilePathExists2() {
        try {
            boolean result = (boolean) checkIfProvidedRelativePathExistsMethod.invoke(webRootHandler, "/./././index.html");
            assertTrue(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootfilePathExists3() {
        try {
            boolean result = (boolean) checkIfProvidedRelativePathExistsMethod.invoke(webRootHandler, "/notexists.html");
            assertFalse(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootfilePathExists4() {
        try {
            boolean result = (boolean) checkIfProvidedRelativePathExistsMethod.invoke(webRootHandler, "/../LICENSE");
            assertFalse(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileMimeTypeHtml() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/");
            assertEquals("text/html", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileMimeTypeJpg() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/images/happy.jpg");
            assertEquals("image/jpeg", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileMimeTypeDefault() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/favicon.ico");
            assertEquals("application/octet-stream", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileByteArrayData() {
        try {
            assertTrue(webRootHandler.getFileByteArrayData("/").length > 0);
        } catch (FileNotFoundException | ReadFileException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileByteArrayDataNotFound() {
        try {
            webRootHandler.getFileByteArrayData("/missing.html");
            fail();
        } catch (FileNotFoundException e) {
            // pass
        } catch (ReadFileException e) {
            fail(e);
        }
    }

}