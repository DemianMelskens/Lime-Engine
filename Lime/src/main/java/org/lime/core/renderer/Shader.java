package org.lime.core.renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lime.core.utils.Log.LM_CORE_ERROR;
import static org.lwjgl.opengl.GL46.*;

public class Shader {

    private int rendererId;

    public Shader(String vertexSource, String fragmentSource) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);

        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            glDeleteShader(vertexShader);

            LM_CORE_ERROR("Vertex shader compilation failure!");
            LM_CORE_ERROR(glGetShaderInfoLog(vertexShader));
            return;
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);

        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            glDeleteShader(fragmentShader);

            LM_CORE_ERROR("Fragment shader compilation failure!");
            LM_CORE_ERROR(glGetShaderInfoLog(fragmentShader));
            return;
        }

        rendererId = glCreateProgram();
        int program = rendererId;
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);

        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            glDeleteProgram(program);
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);

            LM_CORE_ERROR("Shader program linking failure!");
            LM_CORE_ERROR(glGetProgramInfoLog(program));
            return;
        }

        glDetachShader(program, vertexShader);
        glDetachShader(program, fragmentShader);
    }

    public void bind() {
        glUseProgram(rendererId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    /**
     * you should always bind the shader before uploading a uniform
     */
    public void uploadUniformMat4(String name, Matrix4f matrix) {
        int location = glGetUniformLocation(rendererId, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            matrix.get(buffer);
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    private void tearDown() {
        glDeleteProgram(rendererId);
    }
}
