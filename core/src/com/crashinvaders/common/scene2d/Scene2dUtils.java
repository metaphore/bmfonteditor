package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;

public class Scene2dUtils {
    /**
     * Cancel touch focus for actor itself and for whole children hierarchy if it's a group
     */
    public static void cancelTouchFocus(Stage stage, Actor actor) {
        stage.cancelTouchFocus(actor);

        if (actor instanceof Group) {
            SnapshotArray<Actor> children = ((Group)actor).getChildren();
            if (children.size > 0) {
                Object[] snapshot = children.begin();
                for (int i = 0; i < children.size; i++) {
                    cancelTouchFocus(stage, (Actor)snapshot[i]);
                }
                children.end();
            }
        }
    }

    //FIXME this is rather dirty method, listener.clicked(null, 0, 0) may produce null pointer
    public static void simulateClick(Actor actor) {
        for (EventListener eventListener : actor.getListeners()) {
            if (eventListener instanceof ClickListener) {
                ClickListener listener = (ClickListener) eventListener;
                listener.clicked(null, 0, 0);
            }
        }
    }
}
