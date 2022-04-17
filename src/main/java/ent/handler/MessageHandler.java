package ent.handler;


import ent.button.MarkupBoards;
import ent.entity.auth.Session;
import ent.enums.Role;
import ent.enums.State;
import ent.processor.AdminProcessor;
import ent.processor.SendAdProcessor;
import ent.processor.UserProcessor;
import ent.service.DailyLimitService;
import ent.service.auth.AuthService;
import ent.service.extractor.ExtractorService;
import ent.service.request.RequestService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@Component
public class MessageHandler extends AbstractHandler<AuthService> implements IBaseHandler {

    private final SendAdProcessor adProcessor;
    private final UserProcessor up;
    private final BaseMethods a;
    private final MarkupBoards markup;
    protected final Session sessions;
    protected final ExtractorService converter;
    protected final RequestService requestService;
    private final DailyLimitService dlService;
    private final AdminProcessor ap;


    public MessageHandler(BaseMethods a, AuthService authService, SendAdProcessor adProcessor, UserProcessor up, MarkupBoards markup, Session sessions, ExtractorService converter, RequestService requestService, DailyLimitService dlService, AdminProcessor ap) {
        super(authService);
        this.a = a;
        this.adProcessor = adProcessor;
        this.up = up;
        this.markup = markup;
        this.sessions = sessions;
        this.converter = converter;
        this.requestService = requestService;
        this.dlService = dlService;
        this.ap = ap;
    }

    @Override
    public void handle(Update update) throws IOException {
        a.prepare(update);
        if (!service.isRegistered(a.chatId)) {
            if (!a.hasContact(update)) {
                SendMessage sendMessage = a.msgObject(a.chatId, "Botga a'zo bo'lish uchun telefon raqamingizni yuboring");
                sendMessage.setReplyMarkup(markup.phoneButton());
                a.bot.executeMessage(sendMessage);
            } else if (a.hasContact(update)) {
                up.registerUser(a);
            }
        } else {
            if (a.hasTextEquals(update, "/start")) {
                SendMessage sendMessage = a.msgObject(a.chatId, "<b>Hayrli kun 😎</b>");
                if (service.isAdmin(a.chatId)) sendMessage.setReplyMarkup(markup.adminPanel());
                sessions.setState(State.DEFAULT, a.chatId);
                a.bot.executeMessage(sendMessage);
            } else if (a.hasTextEquals(update, "/admin")) {
                if (service.isAdmin(a.chatId)) {
                    ap.command_admin();
                }
            } else if (a.hasTextEquals(update, "/help")) {
                SendMessage sendMessage = a.msgObject(a.chatId, helpText());
                a.bot.executeMessage(sendMessage);
            } else if (checkRole(a.chatId, Role.ADMIN) && a.hasTextEquals(update, "Statistics 📊")) {
                ap.button_stat();
            } else if (checkRole(a.chatId, Role.ADMIN) && a.hasTextEquals(update, "Back 🔙")) {
                ap.button_back();
            } else if (a.hasTextEquals(update, "Set request limit 🎯") && checkRole(a.chatId, Role.ADMIN)) {
                ap.button_set_req();
            } else if (sessions.checkState(State.SET_LIMIT, a.chatId) && checkRole(a.chatId, Role.ADMIN)) {
                ap.function_set_req(update);
            } else if (a.hasTextEquals(update, "/my_limit")) {
                Integer limit = service.getDailyLimit(a.chatId);
                String text = String.format("Bugungi qolgan urunishlaringiz: <code>%s</code>", limit);
                SendMessage sendMessage = a.msgObject(a.chatId, text);
                a.bot.executeMessage(sendMessage);
            } else if (a.hasTextEquals(update, "Send ads 🔖") && checkRole(a.chatId, Role.ADMIN)) {
                ap.button_send_ad();
            } else if (checkRole(a.chatId, Role.ADMIN) && sessions.checkState(State.SEND_AD, a.chatId)) {
                adProcessor.process(update);
            } else if (a.hasPhoto(update) && checkRole(a.chatId, Role.USER)) {
                up.photoRecognize();
            } else if (a.hasText(update) && checkRole(a.chatId, Role.USER)) {
                up.photoRecognizeUrl(update);
            }
        }
    }

    protected boolean checkRole(Long chatId, Role role) {
        return service.getSession(chatId).getRole().equals(role.getCode());
    }

    public String helpText() {
        StringBuilder builder = new StringBuilder();
        return builder.append("\uD83E\uDDE9 <b>Mavjud:</b>\n").append("  ➔ <b>Rasm</b> - Matnga ega har qanday rasm\n").append("  ➔ <b>URL</b> - faylga to'g'ridan-to'g'ri havola\n").append("       <b>misol:</b> <code>https://www.../rasm.jpg</code>\n").append("\n").append("⭕️ <b>Mavjud emas:</b>\n").append("  ➔ Rasmdagi qo'lda yozilgan matn\n").append("\n").append("\uD83D\uDD26 <b>Maxsus sarlahva buyruqlari:</b>  \n").append(" ➔ <code>>txt</code> - natijani tekst faylga export qilish    \n").append(" ➔ <code>>en</code> - natijani berilgan tilga tarjima qilish").toString();
    }
}