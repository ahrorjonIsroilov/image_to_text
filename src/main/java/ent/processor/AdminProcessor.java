package ent.processor;

import ent.button.MarkupBoards;
import ent.entity.auth.Session;
import ent.enums.State;
import ent.handler.BaseMethods;
import ent.service.DailyLimitService;
import ent.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class AdminProcessor {
    private final BaseMethods bm;
    private final MarkupBoards markup;
    private final AuthService service;
    private final Session sessions;
    private final DailyLimitService dlService;

    public void command_admin() {
        SendMessage sendMessage = bm.msgObject(bm.chatId, "<b>Welcome to admin panel 👮‍♂️</b>");
        sendMessage.setReplyMarkup(markup.adminPanel());
        bm.bot.executeMessage(sendMessage);
    }

    public void button_stat() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(bm.chatId.toString());
        sendMessage.setParseMode("HTML");
        sendMessage.setText(service.getStatistics());
        sessions.setState(State.DEFAULT, bm.chatId);
        bm.bot.executeMessage(sendMessage);
    }

    public void button_back() {
        SendMessage sendMessage = bm.msgObject(bm.chatId, "<b>🏘 Bosh menyu</b>");
        sendMessage.setReplyMarkup(markup.adminPanel());
        sessions.setState(State.DEFAULT, bm.chatId);
        bm.bot.executeMessage(sendMessage);
    }

    public void button_set_req() {
        SendMessage sendMessage = bm.msgObject(bm.chatId, "<b>100 dan kichik va 1 dan katta son kiriting</b>");
        sendMessage.setReplyMarkup(markup.back());
        sessions.setState(State.SET_LIMIT, bm.chatId);
        bm.bot.executeMessage(sendMessage);
    }

    public void button_send_ad(){
        sessions.setState(State.SEND_AD, bm.chatId);
        SendMessage sendMessage = bm.msgObject(bm.chatId, "<b>Xabar yuborishingiz mumkin</b>\n\n<i>🔰 Samarali reklama posti tayyorlash uchun @postBot dan foydalaning</i>");
        sendMessage.setReplyMarkup(markup.back());
        bm.bot.executeMessage(sendMessage);
    }

    public void function_set_req(Update update) {
        String limit = update.getMessage().getText();
        if (bm.isNumeric(limit)) {
            int limitNum = Integer.parseInt(limit);
            if (limitNum < 1 || limitNum > 100) {
                SendMessage sendMessage = bm.msgObject(bm.chatId, "<b>❗️ Iltimos to'g'ri ma'lumot yuboring </b>");
                bm.bot.executeMessage(sendMessage);
            }
            dlService.updateDailyLimit(limitNum);
            SendMessage sendMessage = bm.msgObject(bm.chatId, "<b>✅ O'zgarishlar saqlandi</b>");
            sendMessage.setReplyMarkup(markup.adminPanel());
            bm.bot.executeMessage(sendMessage);
        } else {
            SendMessage sendMessage = bm.msgObject(bm.chatId, "<b>❗️ Iltimos to'g'ri ma'lumot yuboring </b>");
            bm.bot.executeMessage(sendMessage);
        }
    }
}
