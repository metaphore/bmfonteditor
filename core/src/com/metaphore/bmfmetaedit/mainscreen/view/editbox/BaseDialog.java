package com.metaphore.bmfmetaedit.mainscreen.view.editbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.metaphore.bmfmetaedit.mainscreen.MainResources;

public abstract class BaseDialog<T> extends Dialog {

    private final MainResources resources;
    protected final ObjectMap<Actor, ResultProvider> resultProviders = new ObjectMap();

    private ResultAction resultAction;
    private boolean allowNullResult = false;
    private boolean cancelHide;
    private Actor defaultFocusActor;


    public BaseDialog(MainResources resources, String title) {
        super(title, resources.styles.wsDialog);
        this.resources = resources;
        pad(8f);
        padTop(21f);
        getTitleLabel().setAlignment(Align.center);

        Table content = getContentTable();
        content.pad(12f);

        // Remove default dialog buttons click logic
        Table buttonTable = getButtonTable();
        buttonTable.clearListeners();
        buttonTable.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if (!resultProviders.containsKey(actor)) return;

                while (actor.getParent() != buttonTable)
                    actor = actor.getParent();
                result(resultProviders.get(actor).generateResult());
                if (!cancelHide) hide();
                cancelHide = false;
            }
        });

        // Consume all key down events
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return true;
            }
        });
    }

    public BaseDialog allowNullResult() {
        allowNullResult = true;
        return this;
    }

    public BaseDialog onResult(ResultAction<T> resultAction) {
        this.resultAction = resultAction;
        return this;
    }

    public BaseDialog defaultFocus(Actor actor) {
        defaultFocusActor = actor;
        return this;
    }

    public void cancel() {
        cancelHide = true;
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null && defaultFocusActor != null) {
            Gdx.app.postRunnable(() -> getStage().setKeyboardFocus(defaultFocusActor));
        }
    }

    @Override
    protected final void result(Object object) {
        // Treat null as cancel
        if (!allowNullResult && object == null) return;

        if (resultAction != null) {
            resultAction.onResult((T) object);
        }
    }

    public interface ResultAction<T> {
        void onResult(T result);
    }

    public interface ResultProvider<T> {
        T generateResult();
    }

    public static class StaticResult<T> implements ResultProvider<T> {
        private final T value;

        public StaticResult(T value) {
            this.value = value;
        }

        @Override
        public T generateResult() {
            return value;
        }
    }

    @Override
    public Dialog button(String text) {
        return button(text, new StaticResult(null));
    }

    public Dialog button(String text, ResultProvider<T> resultProvider) {
        TextButton btn = new TextButton(text, resources.styles.tbsButton);
        getButtonTable().add(btn);
        resultProviders.put(btn, resultProvider);
        return this;
    }

    public Dialog key (final int keycode) {
        return key(keycode, new StaticResult(null));
    }

    public Dialog key (final int keycode, final ResultProvider<T> resultProvider) {
        addListener(new InputListener() {
            public boolean keyDown (InputEvent event, int keycode2) {
                if (keycode == keycode2) {
                    result(resultProvider.generateResult());
                    if (!cancelHide) hide();
                    cancelHide = false;
                }
                return false;
            }
        });
        return this;
    }

    @Override
    @Deprecated
    public Dialog key(int keycode, Object object) {
        throw new UnsupportedOperationException();
    }
    @Override
    @Deprecated
    public final Dialog button(String text, Object object) {
        throw new UnsupportedOperationException();
    }
    @Override
    @Deprecated
    public final Dialog button(String text, Object object, TextButton.TextButtonStyle buttonStyle) {
        throw new UnsupportedOperationException();
    }
    @Override
    @Deprecated
    public final Dialog button(Button button) {
        throw new UnsupportedOperationException();
    }
    @Override
    @Deprecated
    public final Dialog button(Button button, Object object) {
        throw new UnsupportedOperationException();
    }
}
