package ent.processor;

import ent.button.InlineBoards;
import ent.button.MarkupBoards;
import ent.entity.auth.AuthUser;
import ent.entity.auth.Session;
import ent.entity.request.Request;
import ent.enums.Language;
import ent.enums.Role;
import ent.enums.State;
import ent.handler.BaseMethods;
import ent.service.DailyLimitService;
import ent.service.auth.AuthService;
import ent.service.extractor.ExtractorService;
import ent.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserProcessor {
    private final BaseMethods bm;
    private final MarkupBoards markup;
    private final InlineBoards inline;
    private final ExtractorService converter;
    private final AuthService service;
    private final RequestService requestService;
    private final Session sessions;
    private final DailyLimitService dlService;

    public void photoRecognize() {
        if (!(service.getSession(bm.chatId).getDailyLimit() <= 0)) {
            String result = service.lorem();
            SendMessage sendMessage = bm.msgObject(bm.chatId, result);
            sendMessage.setReplyMarkup(inline.resultProperties());
            Request request = Request.builder().requestBy(bm.chatId).created_at(LocalDateTime.now()).build();
            requestService.save(request);
            bm.bot.executeMessage(sendMessage);
            service.decrementDailyLimit(bm.chatId);
        } else {
            SendMessage sendMessage = bm.msgObject(bm.chatId, "<b>😕 Bugungi foydalanishlar soni tugadi. Ertaga yana botdan foydalanishingiz mumkin</b>");
            bm.bot.executeMessage(sendMessage);
        }
    }

    public void photoRecognizeUrl(Update update) {
        String url = update.getMessage().getText();
        if (bm.hasUrl(url)) {
            if (bm.hasUrl(url)) {
                String result = converter.prepareFile(url);
                SendMessage sendMessage = bm.msgObject(bm.chatId, result);
                bm.bot.executeMessage(sendMessage);
            } else {
                SendMessage sendMessage = bm.msgObject(bm.chatId, "Havolani tekshirib qayta yuboring!");
                bm.bot.executeMessage(sendMessage);
            }
        }
    }

    public void registerUser(BaseMethods bm) {
        String phoneNumber = service.preparePhoneNumber(bm.message.getContact());
        String userName = bm.message.getFrom().getUserName();
        String firstName = bm.message.getFrom().getFirstName();
        String lastName = bm.message.getFrom().getLastName();
        Long chatId = bm.message.getFrom().getId();
        AuthUser authUser = AuthUser.builder().username(userName).firstname(firstName).lastname(lastName).phoneNumber(phoneNumber).chatId(chatId).role(Role.USER.getCode()).state(State.DEFAULT.getCode()).dailyRequest(dlService.getLimitNum()).language(Language.UZ.getCode()).build();
        service.registerUser(authUser);
        SendMessage sendMessage = bm.msgObject(bm.chatId, "Tabriklaymiz! Ro'yxatdan o'tdingiz. 🥳\n\n Rasm yuborishingiz mumkin");
        SendSticker sticker = new SendSticker();
        sticker.setChatId(chatId.toString());
        sticker.setSticker(new InputFile("CAACAgIAAxkBAAEBBhRiVqQp620q1Kv4Eu9BfvfdQHxxnAACSQEAAladvQp1bSI3184pVCME"));
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        bm.bot.executeMessage(sendMessage);
        bm.bot.sendSticker(sticker);
    }
}
