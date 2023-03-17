package org.lime.debug;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;
import static org.lime.core.utils.Log.LM_CORE_WARN;

class Instrumentor {
    private static Instrumentor instance;
    private String session;
    private FileOutputStream outputStream;
    private int profileCount;

    static Instrumentor get() {
        if (instance == null)
            instance = new Instrumentor();

        return instance;
    }

    private Instrumentor() {
        this.profileCount = 0;
    }

    void beginSession(String name, String filePath) {
        if (Profiler.enabled && Profiler.output) {
            LM_CORE_WARN(String.format("Profiler session: %s started!", name));

            LM_CORE_ASSERT(outputStream != null, "Failed to create session because there is already an session active!");

            try {
                outputStream = new FileOutputStream(filePath);
                writeHeader();
                session = filePath;
            } catch (FileNotFoundException e) {
                throw LM_CORE_EXCEPTION(String.format("Failed to create session with filePath: %s!", filePath));
            }
        }
    }

    void endSession() {
        if (Profiler.enabled && Profiler.output) {
            if (outputStream != null) {
                try {
                    writeFooter();
                    outputStream.close();
                    outputStream = null;
                    session = null;
                    profileCount = 0;
                } catch (IOException e) {
                    throw LM_CORE_EXCEPTION(String.format("Failed to close session with name: %s!", session));
                }
            }
        }
    }

    void writeProfile(ProfileResult result) {
        if (Profiler.enabled && Profiler.output) {
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
    }

    private void writeHeader() {
        try {
            outputStream.write("{\"otherData\": {}, \"traceEvents\":[".getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw LM_CORE_EXCEPTION(String.format("Failed to write header too session with name: %s!", session));
        }
    }

    private void writeFooter() {
        try {
            outputStream.write("]}".getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw LM_CORE_EXCEPTION(String.format("Failed to write footer too session with name: %s!", session));
        }
    }
}
