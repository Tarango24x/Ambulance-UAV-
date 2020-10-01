package com.firebase.client.core.persistence;

import com.firebase.client.core.view.QuerySpec;

public class TrackedQuery {
    public final boolean active;
    public final boolean complete;

    /* renamed from: id */
    public final long f259id;
    public final long lastUse;
    public final QuerySpec querySpec;

    public TrackedQuery(long id, QuerySpec querySpec2, long lastUse2, boolean complete2, boolean active2) {
        this.f259id = id;
        if (!querySpec2.loadsAllData() || querySpec2.isDefault()) {
            this.querySpec = querySpec2;
            this.lastUse = lastUse2;
            this.complete = complete2;
            this.active = active2;
            return;
        }
        throw new IllegalArgumentException("Can't create TrackedQuery for a non-default query that loads all data");
    }

    public TrackedQuery updateLastUse(long lastUse2) {
        return new TrackedQuery(this.f259id, this.querySpec, lastUse2, this.complete, this.active);
    }

    public TrackedQuery setComplete() {
        return new TrackedQuery(this.f259id, this.querySpec, this.lastUse, true, this.active);
    }

    public TrackedQuery setActiveState(boolean isActive) {
        return new TrackedQuery(this.f259id, this.querySpec, this.lastUse, this.complete, isActive);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        TrackedQuery query = (TrackedQuery) o;
        if (this.f259id == query.f259id && this.querySpec.equals(query.querySpec) && this.lastUse == query.lastUse && this.complete == query.complete && this.active == query.active) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((Long.valueOf(this.f259id).hashCode() * 31) + this.querySpec.hashCode()) * 31) + Long.valueOf(this.lastUse).hashCode()) * 31) + Boolean.valueOf(this.complete).hashCode()) * 31) + Boolean.valueOf(this.active).hashCode();
    }

    public String toString() {
        return "TrackedQuery{id=" + this.f259id + ", querySpec=" + this.querySpec + ", lastUse=" + this.lastUse + ", complete=" + this.complete + ", active=" + this.active + "}";
    }
}
