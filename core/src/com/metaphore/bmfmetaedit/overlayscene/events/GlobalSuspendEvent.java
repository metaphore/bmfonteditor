package com.metaphore.bmfmetaedit.overlayscene.events;

import com.crashinvaders.common.eventmanager.EventInfo;
import com.metaphore.bmfmetaedit.App;

/**
 * Allow to control global application pause
 */
public class GlobalSuspendEvent implements EventInfo {
    private static final GlobalSuspendEvent inst = new GlobalSuspendEvent();

    private ActionType actionType;

    public ActionType getActionType() {
        return actionType;
    }

    public static void dispatchHold() {
        inst.actionType = ActionType.HOLD;
        App.inst().getEvents().dispatchEvent(inst);
    }

    public static void dispatchRelease() {
        inst.actionType = ActionType.RELEASE;
        App.inst().getEvents().dispatchEvent(inst);
    }

    public enum ActionType {
        HOLD,
        RELEASE;
    }
}
