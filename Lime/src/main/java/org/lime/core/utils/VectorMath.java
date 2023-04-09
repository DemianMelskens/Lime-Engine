package org.lime.core.utils;

import org.joml.Math;
import org.joml.Vector3f;

public class VectorMath {

    public static Vector3f toDegrees(Vector3f radians) {
        return new Vector3f(
                (float) Math.toDegrees(radians.x),
                (float) Math.toDegrees(radians.y),
                (float) Math.toDegrees(radians.z)
        );
    }

    public static Vector3f toRadians(Vector3f degrees) {
        return new Vector3f(
                Math.toRadians(degrees.x),
                Math.toRadians(degrees.y),
                Math.toRadians(degrees.z)
        );
    }
}
