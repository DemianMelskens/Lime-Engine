package org.lime.editor;

import org.lime.core.Application;

public class Editor extends Application {

    private Editor() {
        super();
        pushLayer(new EditorLayer());
    }

    public static void main(String[] args) {
        Application app = new Editor();
        app.run();
    }
}
