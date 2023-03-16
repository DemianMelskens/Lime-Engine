package org.lime.debug;

import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;

class Instrumentor {
    private static Instrumentor instance;
    @Getter
    private boolean isRunning;
    private String session;
    private FileOutputStream outputStream;
    private int profileCount;

    static Instrumentor get() {
        if (instance == null)
            instance = new Instrumentor();

        return instance;
    }

    private Instrumentor() {
        this.isRunning = false;
        this.profileCount = 0;
    }

    void beginSession() {
        beginSession("results.json");
    }

    void beginSession(String filePath) {
        if (outputStream != null) {
            throw LM_CORE_EXCEPTION("Failed to create session because there is already an session active!");
        }
        isRunning = true;
        try {
            outputStream = new FileOutputStream(filePath);
            writeHeader();
            session = filePath;
        } catch (FileNotFoundException e) {
            throw LM_CORE_EXCEPTION(String.format("Failed to create session with filePath: %s!", filePath));
        }
    }

    void endSession() {
        if (outputStream != null) {
            isRunning = false;

            try {
                writeFooter();
                outputStream.close();
                session = null;
                profileCount = 0;
            } catch (IOException e) {
                throw LM_CORE_EXCEPTION(String.format("Failed to close session with name: %s!", session));
            }
        }
    }

    void writeProfile(ProfileResult result) {
        try {
            if (profileCount++ > 0)
                outputStream.write(",".getBytes());

            String name = result.getName().replace("\"", "'");
            outputStream.write("{".getBytes());
            outputStream.write("\"cat\":\"function\",".getBytes());
            outputStream.write(String.format("\"dur\":%f,", (result.getEnd() - result.getStart()) * 0.000001f).getBytes());
            outputStream.write(String.format("\"name\":\"%s\",", name).getBytes());
            outputStream.write("\"ph\":\"X\",".getBytes());
            outputStream.write("\"pid\":0,".getBytes());
            outputStream.write("\"tid\":0,".getBytes());
            outputStream.write(String.format("\"ts\":%f", (result.getStart() * 0.000001f)).getBytes());
            outputStream.write("}".getBytes());

            outputStream.flush();
        } catch (IOException e) {
            throw LM_CORE_EXCEPTION(String.format("Failed to write profile too session with name: %s!", session));
        }

    }

    void writeHeader() {
        try {
            outputStream.write("{\"otherData\": {}, \"traceEvents\":[".getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw LM_CORE_EXCEPTION(String.format("Failed to write header too session with name: %s!", session));
        }
    }

    void writeFooter() {
        try {
            outputStream.write("]}".getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw LM_CORE_EXCEPTION(String.format("Failed to write footer too session with name: %s!", session));
        }
    }
}
