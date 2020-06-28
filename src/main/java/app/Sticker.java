package app;

public enum Sticker {
    THUMB_UP_CHUCK("CAACAgUAAxkBAAMXXvWoGyXPu5cE8_BIDt7eOXCV5mEAAm4DAALpCsgDfDQPP9rsBG0aBA"),
    THUMB_UP_CAT("CAACAgIAAxkBAAMsXvWse-pf9A6sQ3fXRCC6Bu8N3YcAAqMIAAJcAmUDnEqA4uxETvEaBA");

    private final String id;

    Sticker(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
