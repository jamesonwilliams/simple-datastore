package com.amplifyframework.datastore.sample;

final class LogLine {
    private EventType eventType;
    private String title;
    private String details;

    private LogLine(EventType eventType, String title, String details) {
        this.eventType = eventType;
        this.title = title;
        this.details = details;
    }

    static LogLine create(EventType eventType, String title, String details) {
        return new LogLine(eventType, title, details);
    }

    static LogLine query(String title, String details) {
        return create(EventType.QUERY, title, details);
    }

    static LogLine subscription(String title, String details) {
        return create(EventType.SUBSCRIPTION, title, details);
    }

    static LogLine delete(String title, String details) {
        return create(EventType.DELETE, title, details);
    }

    static LogLine create(String title, String details) {
        return create(EventType.CREATE, title, details);
    }

    static LogLine update(String title, String details) {
        return create(EventType.UPDATE, title, details);
    }

    static LogLine error(String title, String details) {
        return create(EventType.ERROR, title, details);
    }

    EventType getEventType() {
        return eventType;
    }

    String getTitle() {
        return title;
    }

    String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "LogLine{" +
            "eventType=" + eventType +
            ", title='" + title + '\'' +
            ", details='" + details + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogLine logLine = (LogLine) o;

        if (getEventType() != logLine.getEventType()) return false;
        if (getTitle() != null ? !getTitle().equals(logLine.getTitle()) : logLine.getTitle() != null)
            return false;
        return getDetails() != null ? getDetails().equals(logLine.getDetails()) : logLine.getDetails() == null;
    }

    @Override
    public int hashCode() {
        int result = getEventType() != null ? getEventType().hashCode() : 0;
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getDetails() != null ? getDetails().hashCode() : 0);
        return result;
    }

    enum EventType {
        QUERY("QRY"),
        SUBSCRIPTION("SUB"),
        DELETE("DEL"),
        CREATE("NEW"),
        UPDATE("UPD"),
        ERROR("ERR");

        private final String text;

        EventType(String text) {
            this.text = text;
        }

        String getText() {
            return text;
        }
    }
}
