package ent.processor;

import ent.button.InlineBoards;
import ent.entity.auth.Session;
import ent.enums.State;
import ent.handler.BaseMethods;
import ent.service.auth.AuthService;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Getter
public class SendAdProcessor {

    private final BaseMethods a;
    private final InlineBoards inline;
    private final Session session;
    private SendPhoto gSendPhoto;
    private SendDocument gSendDocument;
    private SendAudio gSendAudio;
    private SendVideo gSendVideo;
    private SendMessage gSendMessage;
    private final AuthService service;

    public SendAdProcessor(BaseMethods a, InlineBoards inline, Session session, AuthService service) {
        this.a = a;
        this.inline = inline;
        this.session = session;
        this.service = service;
    }

    public void process(Update update) {
        a.prepare(update);
        if (update.getMessage().hasText()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(a.chatId.toString());
            sendMessage.setText(update.getMessage().getText());
            sendMessage.setReplyMarkup(inline.adsButton(update.getMessage()));
            gSendMessage = sendMessage;
            a.bot.executeMessage(sendMessage);
            if (Objects.nonNull(update.getMessage().getReplyMarkup()))
                gSendMessage.setReplyMarkup(inline.postButton(update.getMessage()));
            else gSendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        } else if (update.getMessage().hasPhoto()) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(a.chatId.toString());
            sendPhoto.setPhoto(new InputFile(update.getMessage().getPhoto().get(0).getFileId()));
            sendPhoto.setCaption(update.getMessage().getCaption());
            sendPhoto.setCaptionEntities(update.getMessage().getCaptionEntities());
            sendPhoto.setReplyMarkup(inline.adsButton(update.getMessage()));
            gSendPhoto = sendPhoto;
            a.bot.sendPhoto(sendPhoto);
            if (Objects.nonNull(update.getMessage().getReplyMarkup()))
                gSendPhoto.setReplyMarkup(inline.postButton(update.getMessage()));
            else gSendPhoto.setReplyMarkup(new ReplyKeyboardRemove(true));
        } else if (update.getMessage().hasDocument()) {
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(a.chatId.toString());
            sendDocument.setDocument(new InputFile(update.getMessage().getDocument().getFileId()));
            sendDocument.setCaption(update.getMessage().getCaption());
            sendDocument.setCaptionEntities(update.getMessage().getCaptionEntities());
            sendDocument.setReplyMarkup(inline.adsButton(update.getMessage()));
            gSendDocument = sendDocument;
            a.bot.sendDoc(sendDocument);
            if (Objects.nonNull(update.getMessage().getReplyMarkup()))
                gSendDocument.setReplyMarkup(inline.postButton(update.getMessage()));
            else gSendDocument.setReplyMarkup(new ReplyKeyboardRemove(true));
        } else if (update.getMessage().hasAudio()) {
            SendAudio sendAudio = new SendAudio();
            sendAudio.setChatId(a.chatId.toString());
            sendAudio.setAudio(new InputFile(update.getMessage().getAudio().getFileId()));
            sendAudio.setCaption(update.getMessage().getCaption());
            sendAudio.setCaptionEntities(update.getMessage().getCaptionEntities());
            sendAudio.setReplyMarkup(inline.adsButton(update.getMessage()));
            gSendAudio = sendAudio;
            a.bot.sendAudio(sendAudio);
            if (Objects.nonNull(update.getMessage().getReplyMarkup()))
                gSendAudio.setReplyMarkup(inline.postButton(update.getMessage()));
            else gSendAudio.setReplyMarkup(new ReplyKeyboardRemove(true));
        } else if (update.getMessage().hasVideo()) {
            SendVideo sendVideo = new SendVideo();
            sendVideo.setChatId(a.chatId.toString());
            sendVideo.setVideo(new InputFile(update.getMessage().getVideo().getFileId()));
            sendVideo.setCaption(update.getMessage().getCaption());
            sendVideo.setCaptionEntities(update.getMessage().getCaptionEntities());
            sendVideo.setReplyMarkup(inline.adsButton(update.getMessage()));
            gSendVideo = sendVideo;
            a.bot.sendVideo(sendVideo);
            if (Objects.nonNull(update.getMessage().getReplyMarkup()))
                gSendVideo.setReplyMarkup(inline.postButton(update.getMessage()));
            else gSendVideo.setReplyMarkup(new ReplyKeyboardRemove(true));
        } else {
            SendMessage sendMessage = a.msgObject(a.chatId, "<b>Quyidagi media turlarni yuborish mumkin:</b>\n <code>Text, Photo, Audio, Video, Document</code>");
            a.bot.executeMessage(sendMessage);
        }
    }

    public void send() {
        List<Long> chatIds = service.getAllChatId();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        if (Objects.nonNull(gSendPhoto)) {
            Runnable runnable = () -> {
                for (Long id : chatIds) {
                    gSendPhoto.setChatId(id.toString());
                    a.bot.sendPhoto(gSendPhoto);
                }
            };
            executorService.execute(runnable);
        } else if (Objects.nonNull(gSendAudio)) {
            Runnable runnable = () -> {
                for (Long id : chatIds) {
                    gSendAudio.setChatId(id.toString());
                    a.bot.sendAudio(gSendAudio);
                }
            };
            executorService.execute(runnable);
        } else if (Objects.nonNull(gSendDocument)) {
            Runnable runnable = () -> {
                for (Long id : chatIds) {
                    gSendDocument.setChatId(id.toString());
                    a.bot.sendDoc(gSendDocument);
                }
            };
            executorService.execute(runnable);
        } else if (Objects.nonNull(gSendVideo)) {
            Runnable runnable = () -> {
                for (Long id : chatIds) {
                    gSendVideo.setChatId(id.toString());
                    a.bot.sendVideo(gSendVideo);
                }
            };
            executorService.execute(runnable);
        } else {
            Runnable runnable = () -> {
                for (Long id : chatIds) {
                    gSendMessage.setChatId(id.toString());
                    a.bot.executeMessage(gSendMessage);
                }
            };
            executorService.execute(runnable);
        }
        SendMessage sendMessage = a.msgObject(a.chatId, String.format("<b>Xabar <code> %s </code> ta foydalanuvchiga yuborildi</b>", chatIds.size()));
        session.setState(State.DEFAULT, a.chatId);
        a.bot.executeMessage(sendMessage);
    }
}
