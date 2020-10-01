package com.firebase.client.core.view.filter;

import com.firebase.client.core.view.Change;
import com.firebase.client.core.view.Event;
import com.firebase.client.snapshot.ChildKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildChangeAccumulator {
    static final /* synthetic */ boolean $assertionsDisabled = (!ChildChangeAccumulator.class.desiredAssertionStatus());
    private final Map<ChildKey, Change> changeMap = new HashMap();

    public void trackChildChange(Change change) {
        Event.EventType type = change.getEventType();
        ChildKey childKey = change.getChildKey();
        if (!$assertionsDisabled && type != Event.EventType.CHILD_ADDED && type != Event.EventType.CHILD_CHANGED && type != Event.EventType.CHILD_REMOVED) {
            throw new AssertionError("Only child changes supported for tracking");
        } else if (!$assertionsDisabled && change.getChildKey().isPriorityChildName()) {
            throw new AssertionError();
        } else if (this.changeMap.containsKey(childKey)) {
            Change oldChange = this.changeMap.get(childKey);
            Event.EventType oldType = oldChange.getEventType();
            if (type == Event.EventType.CHILD_ADDED && oldType == Event.EventType.CHILD_REMOVED) {
                this.changeMap.put(change.getChildKey(), Change.childChangedChange(childKey, change.getIndexedNode(), oldChange.getIndexedNode()));
            } else if (type == Event.EventType.CHILD_REMOVED && oldType == Event.EventType.CHILD_ADDED) {
                this.changeMap.remove(childKey);
            } else if (type == Event.EventType.CHILD_REMOVED && oldType == Event.EventType.CHILD_CHANGED) {
                this.changeMap.put(childKey, Change.childRemovedChange(childKey, oldChange.getOldIndexedNode()));
            } else if (type == Event.EventType.CHILD_CHANGED && oldType == Event.EventType.CHILD_ADDED) {
                this.changeMap.put(childKey, Change.childAddedChange(childKey, change.getIndexedNode()));
            } else if (type == Event.EventType.CHILD_CHANGED && oldType == Event.EventType.CHILD_CHANGED) {
                this.changeMap.put(childKey, Change.childChangedChange(childKey, change.getIndexedNode(), oldChange.getOldIndexedNode()));
            } else {
                throw new IllegalStateException("Illegal combination of changes: " + change + " occurred after " + oldChange);
            }
        } else {
            this.changeMap.put(change.getChildKey(), change);
        }
    }

    public List<Change> getChanges() {
        return new ArrayList(this.changeMap.values());
    }
}
