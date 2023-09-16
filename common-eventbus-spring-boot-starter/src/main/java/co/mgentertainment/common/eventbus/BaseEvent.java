package co.mgentertainment.common.eventbus;

import java.io.Serializable;

/**
 * @author larry
 */
public abstract class BaseEvent implements Serializable {

    private String eventId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
