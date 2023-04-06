package org.lime.core.controllers;

import lombok.Getter;
import org.joml.Math;
import org.joml.Vector3f;
import org.lime.core.Input;
import org.lime.core.events.Event;
import org.lime.core.events.EventDispatcher;
import org.lime.core.events.application.WindowResizeEvent;
import org.lime.core.events.mouse.MouseScrollEvent;
import org.lime.core.renderer.camera.OrthographicCamera;
import org.lime.core.time.TimeStep;

import static org.lime.core.KeyCode.*;

public class OrthographicCameraController {

    private float aspectRatio;
    private float zoomLevel;
    private float cameraRotation;
    private Vector3f cameraPosition;
    private float cameraTranslationSpeed;
    private float cameraRotationSpeed;

    private boolean canRotate;
    @Getter
    private OrthographicCamera camera;

    public OrthographicCameraController(float aspectRatio, boolean canRotate) {
        this.aspectRatio = aspectRatio;
        this.canRotate = canRotate;
        this.zoomLevel = 1.0f;
        this.cameraRotation = 0.0f;
        this.cameraPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        this.cameraTranslationSpeed = 1.0f;
        this.cameraRotationSpeed = 90.0f;
        this.camera = new OrthographicCamera(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
    }

    public void onUpdate(TimeStep timestep) {
        if (Input.isKeyPressed(LM_KEY_A))
            cameraPosition.x -= (cameraTranslationSpeed * timestep.getSeconds());
        else if (Input.isKeyPressed(LM_KEY_D))
            cameraPosition.x += (cameraTranslationSpeed * timestep.getSeconds());

        if (Input.isKeyPressed(LM_KEY_W))
            cameraPosition.y += (cameraTranslationSpeed * timestep.getSeconds());
        else if (Input.isKeyPressed(LM_KEY_S))
            cameraPosition.y -= (cameraTranslationSpeed * timestep.getSeconds());

        if (canRotate) {
            if (Input.isKeyPressed(LM_KEY_Q))
                cameraRotation += (cameraRotationSpeed * timestep.getSeconds());
            else if (Input.isKeyPressed(LM_KEY_E))
                cameraRotation -= (cameraRotationSpeed * timestep.getSeconds());

            camera.setRotation(cameraRotation);
        }

        camera.setPosition(cameraPosition);
        cameraTranslationSpeed = zoomLevel;
    }

    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(this::onMouseScrolledEvent);
        dispatcher.dispatch(this::onWindowResizedEvent);
    }

    public void onResize(float width, float height) {
        aspectRatio = width / height;
        camera.setProjection(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
    }

    private boolean onMouseScrolledEvent(MouseScrollEvent event) {
        zoomLevel -= (event.getYOffset() * 0.25f);
        zoomLevel = Math.max(zoomLevel, 0.25f);
        camera.setProjection(-aspectRatio * zoomLevel, aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
        return false;
    }

    private boolean onWindowResizedEvent(WindowResizeEvent event) {
        onResize(event.getWidth(), event.getHeight());
        return false;
    }
}
