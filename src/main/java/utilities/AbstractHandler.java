package utilities;

import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractHandler {
    protected static final String NEXT_LINE = "\n";
    protected static final String OP_B_TAG = "<b>";
    protected static final String CL_B_TAG = "</b>";
    protected static final String OP_CODE_TAG = "<code>";
    protected static final String CL_CODE_TAG = "</code>";
    protected static final String NAME_TAG = "@";

    protected String message;
    protected String userName;
    protected String chatName;
    protected int userID;
    protected long chatID;
    protected boolean isGroupChat;

    public AbstractHandler(long chatID) {
        this.chatID = chatID;
    }

    public AbstractHandler() {
    }

    protected void initUpdate(Update update) {
        this.message = update.getMessage().getText();
        this.userName = update.getMessage().getFrom().getUserName();
        this.chatName = update.getMessage().getChat().getTitle();
        this.userID = update.getMessage().getFrom().getId();
        this.chatID = update.getMessage().getChatId();
        this.isGroupChat = update.getMessage().getChat().isGroupChat();
    }
}
