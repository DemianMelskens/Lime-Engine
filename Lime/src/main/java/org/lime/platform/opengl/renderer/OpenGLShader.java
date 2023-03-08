package org.lime.platform.opengl.renderer;

import org.joml.*;
import org.lime.core.renderer.Shader;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lime.core.utils.Log.LM_CORE_ERROR;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class OpenGLShader implements Shader {
    private int rendererId;

    public OpenGLShader(String vertexSource, String fragmentSource) {
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

    @Override
    public void bind() {
        glUseProgram(rendererId);
    }

    @Override
    public void unbind() {
        glUseProgram(0);
    }

    @Override
    public void tearDown() {
        glDeleteProgram(rendererId);
    }

    public void uploadUniformInt(String name, int value) {
        int location = glGetUniformLocation(rendererId, name);
        glUniform1i(location, value);
    }

    public void uploadUniformFloat(String name, float value) {
        int location = glGetUniformLocation(rendererId, name);
        glUniform1f(location, value);
    }

    public void uploadUniformFloat2(String name, Vector2f value) {
        int location = glGetUniformLocation(rendererId, name);
        glUniform2f(location, value.x, value.y);
    }

    public void uploadUniformFloat3(String name, Vector3f value) {
        int location = glGetUniformLocation(rendererId, name);
        glUniform3f(location, value.x, value.y, value.z);
    }

    public void uploadUniformFloat4(String name, Vector4f value) {
        int location = glGetUniformLocation(rendererId, name);
        glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    public void uploadUniformMat3(String name, Matrix3f value) {
        int location = glGetUniformLocation(rendererId, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(9);
            value.get(buffer);
            glUniformMatrix3fv(location, false, buffer);
        }
    }

    public void uploadUniformMat4(String name, Matrix4f value) {
        int location = glGetUniformLocation(rendererId, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            value.get(buffer);
            glUniformMatrix4fv(location, false, buffer);
        }
    }
}
