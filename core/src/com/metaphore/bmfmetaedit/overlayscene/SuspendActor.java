package com.metaphore.bmfmetaedit.overlayscene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.crashinvaders.common.scene2d.ModalHolder;

class SuspendActor extends ModalHolder<Table> {

    private final Table contentTable;

    public SuspendActor(AssetManager assets) {
        this.cancelable(false);
        this.dismissOnBack(false);
        this.consumeInput(true);
        this.ignoreKeys(Input.Keys.F5, Input.Keys.F6, Input.Keys.F7, Input.Keys.F8, Input.Keys.F11);
        this.dimTint(0x000000bb);

        contentTable = new Table();
        contentTable.setTransform(false);
        content(contentTable);

        // Content
        {
//            BitmapFont font = assets.get("fonts/nokia8.fnt");
//            Label.LabelStyle ls = new Label.LabelStyle(font, Color.WHITE);
//
//            SkeletonActor sandglassSkeleton = new SkeletonActor(assets.get("skeletons/sandglass.json", SkeletonData.class));
//            sandglassSkeleton.getAnimState().setAnimation(0, "animation", true);
//
//            contentTable.add(new Label("Please wait", ls)).padRight(2f);
//            contentTable.add(sandglassSkeleton).padTop(1f);
        }

        contentTable.addAction(Actions.sequence(
                Actions.moveBy(0f, 10f),
                Actions.moveBy(0f, -10f, 0.25f, Interpolation.pow4Out)
        ));
    }
}
