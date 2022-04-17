package ent.handler;

import ent.button.InlineBoards;
import ent.processor.SendAdProcessor;
import ent.service.extractor.FileService;
import ent.service.extractor.TranslatorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CallBackHandler implements IBaseHandler {

    private final SendAdProcessor adProcessor;
    private final BaseMethods a;
    private final InlineBoards inline;
    private final FileService fileService;
    private final TranslatorService translatorService;

    @SneakyThrows
    @Override
    public void handle(Update update) {
        a.prepare(update);
        String data = update.getCallbackQuery().getData();
        if ("send".equals(data)) {
            a.bot.executeMessage(new DeleteMessage(a.chatId.toString(), update.getCallbackQuery().getMessage().getMessageId()));
            adProcessor.send();
        } else if (data.equals("exportToTxt")) {
            String result = update.getCallbackQuery().getMessage().getText();
            SendDocument document = new SendDocument();
            document.setCaption("🔰 @TasvirniMatngaBot tomonidan yaratildi");
            document.setDocument(new InputFile(fileService.getFile(result)));
            document.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
            a.bot.sendDoc(document);
        } else if (data.equals("translateToUz")) {
            String result = update.getCallbackQuery().getMessage().getText();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(a.chatId.toString());
            editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            editMessageText.setEntities(update.getCallbackQuery().getMessage().getEntities());
            editMessageText.setText(result + "\n\nO'zbekcha tarjima:\n" + translatorService.getTranslated(result));
            editMessageText.setReplyMarkup(inline.exportButton());
            a.bot.executeMessage(editMessageText);
        }
    }
}
