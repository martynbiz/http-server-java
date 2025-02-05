package biz.rtyn.httpserver.core.io;

import java.io.IOException;

public class ReadFileException extends Throwable {

    public ReadFileException(IOException e) {
        super(e);
    }
}
