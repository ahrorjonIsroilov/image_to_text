package ent.button;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InlineBoards {
    private final InlineKeyboardMarkup board = new InlineKeyboardMarkup();

    public InlineKeyboardMarkup adsButton(Message message) {
        InlineKeyboardButton send = new InlineKeyboardButton("Yuborish 📦");
        send.setCallbackData("send");
        InlineKeyboardButton decline = new InlineKeyboardButton("Bekor qilish ❌");
        decline.setCallbackData("decline");
        List<List<InlineKeyboardButton>> defaultKeyboards;
        if (Objects.nonNull(message.getReplyMarkup())) defaultKeyboards = message.getReplyMarkup().getKeyboard();
        else defaultKeyboards = new ArrayList<>();
        defaultKeyboards.add(getRow(send));
        board.setKeyboard(defaultKeyboards);
        return board;
    }

    public InlineKeyboardMarkup postButton(Message message) {
        List<List<InlineKeyboardButton>> defaultKeyboards = message.getReplyMarkup().getKeyboard();
        defaultKeyboards.remove(defaultKeyboards.size() - 1);
        board.setKeyboard(defaultKeyboards);
        return board;
    }

    public InlineKeyboardMarkup resultProperties() {
        InlineKeyboardButton convertText = new InlineKeyboardButton("📇 faylga export qilish");
        convertText.setCallbackData("exportToTxt");
        convertText.setPay(true);
        InlineKeyboardButton translateToUz = new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFF tarjima qilish");
        translateToUz.setCallbackData("translateToUz");
        board.setKeyboard(Arrays.asList(getRow(convertText), getRow(translateToUz)));
        return board;
    }

    public InlineKeyboardMarkup exportButton() {
        InlineKeyboardButton convertText = new InlineKeyboardButton("📇 faylga export qilish");
        convertText.setCallbackData("exportToTxt");
        board.setKeyboard(Collections.singletonList(getRow(convertText)));
        return board;
    }

    private List<InlineKeyboardButton> getRow(InlineKeyboardButton... buttons) {
        return Arrays.stream(buttons).collect(Collectors.toList());
    }
}
