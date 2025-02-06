package biz.rtyn.httpserver.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;

public class WebRootHandler {
    private File webRoot;

    public WebRootHandler(String webRootPath) throws FileNotFoundException {
        webRoot = new File(webRootPath);
        if(!webRoot.exists() || !webRoot.isDirectory()) {
            throw new FileNotFoundException("Webroot provided does not exist or is not a folder");
        }
    }

    public boolean checkIfEndsWithSlash(String relativePath) {
        return relativePath.endsWith("/");
    }

    public boolean checkIfProvidedRelativePathExists(String relativePath) {
        File file = new File(webRoot, relativePath);

        if (!file.exists()) {
            System.out.println(file.getPath());
            return false;
        }

        try {
            if (file.getCanonicalPath().startsWith(webRoot.getCanonicalPath())) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }

    public String getFileMimeType(String relativePath) throws FileNotFoundException {
        File file = getFile(relativePath);
        String mimeType = URLConnection.getFileNameMap().getContentTypeFor(file.getName());

        if (mimeType == null) {
            return "application/octet-stream";
        }

        return mimeType;
    }

    public byte[] getFileByteArrayData(String relativePath) throws FileNotFoundException, ReadFileException {
        File file = getFile(relativePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        try {
            fileInputStream.read(fileBytes);
            fileInputStream.close();
        } catch (IOException e) {
            throw new ReadFileException(e);
        }
        return fileBytes;
    }

    private File getFile(String relativePath) throws FileNotFoundException {
        if (checkIfEndsWithSlash(relativePath)) {
            relativePath += "index.html";
        }

        if (!checkIfProvidedRelativePathExists(relativePath)) {
            throw new FileNotFoundException("File not found: " + relativePath);
        }

        File file = new File(webRoot, relativePath);

        return file;
    }
}
