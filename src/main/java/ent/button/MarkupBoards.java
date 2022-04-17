package ent.button;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Collections;

@Component
public class MarkupBoards {

    private final ReplyKeyboardMarkup board = new ReplyKeyboardMarkup();

    public ReplyKeyboardMarkup phoneButton() {
        KeyboardButton phoneContact = new KeyboardButton("Telefon raqamini ulashish 📞");
        phoneContact.setRequestContact(true);
        KeyboardRow row1 = new KeyboardRow();
        row1.add(phoneContact);
        board.setKeyboard(Collections.singletonList(row1));
        board.setResizeKeyboard(true);
        board.setSelective(true);
        return board;
    }

    public ReplyKeyboardMarkup adminPanel() {
        KeyboardButton sendAd = new KeyboardButton("Send ads 🔖");
        KeyboardButton statistics = new KeyboardButton("Statistics 📊");
        KeyboardButton setRequestLimit = new KeyboardButton("Set request limit 🎯");
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add(sendAd);
        row1.add(statistics);
        row2.add(setRequestLimit);
        board.setKeyboard(Arrays.asList(row1, row2));
        board.setResizeKeyboard(true);
        board.setSelective(true);
        return board;
    }

    public ReplyKeyboardMarkup back() {
        KeyboardButton back = new KeyboardButton("Back 🔙");
        KeyboardRow row = new KeyboardRow();
        row.add(back);
        board.setKeyboard(Collections.singletonList(row));
        board.setResizeKeyboard(true);
        board.setSelective(true);
        return board;
    }
}
