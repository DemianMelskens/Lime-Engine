package org.lime.platform.opengl.renderer;

import org.joml.*;
import org.lime.core.renderer.shader.Shader;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;
import static org.lime.core.utils.Log.LM_CORE_ERROR;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL46.*;

public class OpenGLShader extends Shader {
    private int rendererId;

    public OpenGLShader(String filePath) {
        super(filePath);
        String source = readFile(filePath);
        Map<Integer, String> shaderSources = preProcess(source);
        compile(shaderSources);
    }

    public OpenGLShader(String name, String vertexSource, String fragmentSource) {
        super(name);
        Map<Integer, String> shaderSources = Map.of(
                GL_VERTEX_SHADER, vertexSource,
                GL_FRAGMENT_SHADER, fragmentSource
        );
        compile(shaderSources);
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
    public void shutdown() {
        glDeleteProgram(rendererId);
    }

    @Override
    public void setInt(String name, int value) {
        uploadUniformInt(name, value);
    }

    @Override
    public void setFloat3(String name, Vector3f value) {
        uploadUniformFloat3(name, value);
    }

    @Override
    public void setFloat4(String name, Vector4f value) {
        uploadUniformFloat4(name, value);
    }

    @Override
    public void setMat4(String name, Matrix4f value) {
        uploadUniformMat4(name, value);
    }

    private void uploadUniformInt(String name, int value) {
        int location = glGetUniformLocation(rendererId, name);
        glUniform1i(location, value);
    }

    private void uploadUniformFloat(String name, float value) {
        int location = glGetUniformLocation(rendererId, name);
        glUniform1f(location, value);
    }

    private void uploadUniformFloat2(String name, Vector2f value) {
        int location = glGetUniformLocation(rendererId, name);
        glUniform2f(location, value.x, value.y);
    }

    private void uploadUniformFloat3(String name, Vector3f value) {
        int location = glGetUniformLocation(rendererId, name);
        glUniform3f(location, value.x, value.y, value.z);
    }

    private void uploadUniformFloat4(String name, Vector4f value) {
        int location = glGetUniformLocation(rendererId, name);
        glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    private void uploadUniformMat3(String name, Matrix3f value) {
        int location = glGetUniformLocation(rendererId, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(9);
            value.get(buffer);
            glUniformMatrix3fv(location, false, buffer);
        }
    }

    private void uploadUniformMat4(String name, Matrix4f value) {
        int location = glGetUniformLocation(rendererId, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            value.get(buffer);
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    private String readFile(String filePath) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(filePath)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LM_CORE_ASSERT(false, String.format("Could not open file '%s', reason: %s", filePath, e.getCause()));
        }
        return "";
    }

    private Map<Integer, String> preProcess(String shaderSource) {
        return Arrays.stream(shaderSource.split("((?=#type))"))
                .map(source -> source.split("((?=#version))"))
                .collect(Collectors.toMap(
                        values -> shaderTypeFromString(values[0]),
                        values -> values[1]
                ));
    }

    private int shaderTypeFromString(String type) {
        return switch (type.replace("#type", "").trim()) {
            case "vertex" -> GL_VERTEX_SHADER;
            case "fragment", "pixel" -> GL_FRAGMENT_SHADER;
            default -> throw LM_CORE_EXCEPTION("Shader type not supported");
        };
    }

    private void compile(Map<Integer, String> shaderSources) {
        int program = glCreateProgram();
        Set<Integer> shaderIds = shaderSources.entrySet()
                .stream()
                .map(entry -> {
                    int type = entry.getKey();
                    String source = entry.getValue();
                    int shader = glCreateShader(type);
                    glShaderSource(shader, source);
                    glCompileShader(shader);

                    if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
                        glDeleteShader(shader);

                        LM_CORE_ERROR(String.format("Shader compilation failure! (%s)", name));
                        LM_CORE_ERROR(glGetShaderInfoLog(shader));
                    }

                    glAttachShader(program, shader);
                    return shader;
                }).collect(Collectors.toSet());

        rendererId = program;

        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            glDeleteProgram(program);
            shaderIds.forEach(GL20::glDeleteShader);

            LM_CORE_ERROR("Shader program linking failure!");
            LM_CORE_ERROR(glGetProgramInfoLog(program));
            return;
        }

        shaderIds.forEach(shader -> glDetachShader(program, shader));
    }
}
