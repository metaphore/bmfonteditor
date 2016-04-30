package com.crashinvaders.common.scene2d;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ActionsExt {

    public static Action target(Actor target, Action action) {
        action.setTarget(target);
        return action;
    }

    public static Action unfocus(Actor actor) {
        return new UnfocusAction(actor);
    }

    public static class UnfocusAction extends Action {
        private final Actor actor;

        public UnfocusAction(Actor actor) {
            this.actor = actor;
        }

        @Override
        public boolean act(float delta) {
            Stage stage = actor.getStage();
            if (stage != null) {
                stage.unfocus(actor);
            }
            return true;
        }
    }
}
