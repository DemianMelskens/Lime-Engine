package org.lime.editor;

import org.lime.core.Input;
import org.lime.core.KeyCode;
import org.lime.core.scene.components.CameraComponent;
import org.lime.core.scene.components.TransformComponent;
import org.lime.core.scripting.Scriptable;
import org.lime.core.time.TimeStep;

public class CameraController extends Scriptable {

    @Override
    public void onUpdate(TimeStep timeStep) {
        var transform = getComponent(TransformComponent.class).transform;
        var isActive = getComponent(CameraComponent.class).isPrimary;
        float speed = 5.0f;

        if (isActive) {
            if (Input.isKeyPressed(KeyCode.LM_KEY_A))
                transform.m30(transform.m30() - (speed * timeStep.getSeconds()));
            if (Input.isKeyPressed(KeyCode.LM_KEY_D))
                transform.m30(transform.m30() + (speed * timeStep.getSeconds()));
            if (Input.isKeyPressed(KeyCode.LM_KEY_W))
                transform.m31(transform.m31() + (speed * timeStep.getSeconds()));
            if (Input.isKeyPressed(KeyCode.LM_KEY_S))
                transform.m31(transform.m31() - (speed * timeStep.getSeconds()));
        }
    }


}
