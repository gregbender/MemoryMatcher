package com.gregbender.memorymatcher;

public class GameEvent {

    public GameEventType type;
    public String message;
    public Object eventDetails;

    public GameEvent(GameEventType type, Object eventDetails, String message) {
        this.type = type;
        this.message = message;
        this.eventDetails = eventDetails;
    }
    public GameEvent(GameEventType type, Object eventDetails) {
        this(type, eventDetails, "No Details");
    }

    public GameEvent(GameEventType type) {
        this(type, null, "No Details");
    }
}


