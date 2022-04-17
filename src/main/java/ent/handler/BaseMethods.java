package ent.handler;


import ent.Bot;
import ent.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.toIntExact;

@Component
@RequiredArgsConstructor
public class BaseMethods {
    public Message message;
    public CallbackQuery callbackQuery;
    public String mText;
    public User user;
    public Long chatId;
    public final Bot bot;

    public void prepare(Update update) {
        if (update.hasCallbackQuery()) message = update.getCallbackQuery().getMessage();
        else message = update.getMessage();
        callbackQuery = update.getCallbackQuery();
        mText = message.getText();
        chatId = message.getChatId();
        user = message.getFrom();
    }

    public SendMessage msgObject(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId + "",text);
        sendMessage.setParseMode("html");
        return sendMessage;
    }

    protected EditMessageText eMsgObject(Long chatId, Update update, String text) {
        long message_id;
        if (update.hasCallbackQuery()) message_id = update.getCallbackQuery().getMessage().getMessageId();
        else message_id = update.getMessage().getMessageId();
        EditMessageText sendMessage = new EditMessageText();
        sendMessage.setText(text);
        sendMessage.setMessageId(toIntExact(message_id));
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    protected SendPhoto ePhoto(Long chatId, String caption, String path) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(new File(path)));
        sendPhoto.setChatId(chatId.toString());
        sendPhoto.setCaption(caption);
        sendPhoto.setParseMode("HTML");
        return sendPhoto;
    }

    public boolean isNumeric(String limit) {
        if (limit == null) return false;
        try {
            Integer.parseInt(limit);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean hasUrl(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    protected boolean hasPhoto(Update update) {
        return update.getMessage().hasPhoto();
    }

    protected boolean hasTextEquals(Update update, String text) {
        return update.getMessage().hasText() && update.getMessage().getText().equals(text);
    }

    protected boolean hasText(Update update) {
        return update.getMessage().hasText();
    }

    protected boolean hasContact(Update update) {
        return update.getMessage().hasContact();
    }
}
