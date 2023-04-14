package org.lime.editor.scripts;

import org.lime.core.Input;
import org.lime.core.KeyCode;
import org.lime.core.scene.components.CameraComponent;
import org.lime.core.scene.components.TransformComponent;
import org.lime.core.scripting.Scriptable;
import org.lime.core.time.TimeStep;

public class CameraController extends Scriptable {

    @Override
    public void onUpdate(TimeStep timeStep) {
        var position = getComponent(TransformComponent.class).position;
        var isActive = getComponent(CameraComponent.class).isPrimary;
        float speed = 2.5f;

        if (isActive) {
            if (Input.isKeyPressed(KeyCode.KEY_A))
                position.x -= speed * timeStep.getSeconds();
            if (Input.isKeyPressed(KeyCode.KEY_D))
                position.x += speed * timeStep.getSeconds();
            if (Input.isKeyPressed(KeyCode.KEY_W))
                position.y += speed * timeStep.getSeconds();
            if (Input.isKeyPressed(KeyCode.KEY_S))
                position.y -= speed * timeStep.getSeconds();
        }
    }


}
