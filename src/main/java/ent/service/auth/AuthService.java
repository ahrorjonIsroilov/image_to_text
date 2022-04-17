package ent.service.auth;

import ent.entity.auth.AuthUser;
import ent.entity.auth.Session;
import ent.entity.auth.SessionUser;
import ent.enums.Role;
import ent.repo.auth.AuthRepo;
import ent.repo.request.RequestRepo;
import ent.service.AbstractService;
import ent.service.BaseService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService extends AbstractService<AuthRepo> implements BaseService {
    private final Session sessions;
    private final RequestRepo requestRepo;

    public AuthService(AuthRepo repo, Session sessions, RequestRepo requestRepo) {
        super(repo);
        this.sessions = sessions;
        this.requestRepo = requestRepo;
    }

    public void save(AuthUser user) {
        repo.save(user);
    }

    public AuthUser getById(Long id) {
        Optional<AuthUser> user = repo.findById(id);
        return user.orElse(null);
    }

    public AuthUser getByChatId(Long chatId) {
        return repo.findByChatId(chatId);
    }

    public List<AuthUser> getAll() {
        return (List<AuthUser>) repo.findAll();
    }

    public List<Long> getAllChatId() {
        return repo.getAllChatId();
    }

    public void deleteById(Long id) {
        sessions.removeSession(getById(id).getChatId());
        repo.deleteById(id);
    }

    public void deleteByChatId(Long chatId) {
        sessions.removeSession(chatId);
        repo.deleteByChatId(chatId);
    }

    @Cacheable(value = "sessionCache", key = "{#chatId}")
    public SessionUser getSession(Long chatId) {
        Optional<SessionUser> session = sessions.getSession(chatId);
        if (session.isPresent()) return session.get();
        else {
            AuthUser byChatId = getByChatId(chatId);
            sessions.setSession(chatId, prepareSession(byChatId));
            return getSession(chatId);
        }
    }

    public void registerUser(AuthUser authUser) {
        save(authUser);
        Optional<SessionUser> optional = prepareSession(authUser);
        sessions.setSession(authUser.getChatId(), optional);
    }


    public void decrementDailyLimit(Long chatId) {
        repo.decrementDailyLimit(chatId);
        AuthUser byChatId = getByChatId(chatId);
        sessions.setSession(chatId, prepareSession(byChatId));
    }

    @Cacheable(value = "dailyLimit", key = "{#chatId}")
    public Integer getDailyLimit(Long chatId) {
        return repo.findByChatId(chatId).getDailyRequest();
    }

    @Cacheable(value = "isRegistered", key = "{#chatId}")
    public Boolean isRegistered(Long chatId) {
        Optional<SessionUser> user = sessions.getSession(chatId);
        if (user.isPresent()) return true;
        else {
            Boolean available = Objects.nonNull((repo.findByChatId(chatId)));
            if (available) sessions.setSession(chatId, prepareSession(repo.findByChatId(chatId)));
            return available;
        }
    }

    @Cacheable(value = "isAdmin", key = "{#chatId}")
    public Boolean isAdmin(Long chatId) {
        if (sessions.getSession(chatId).isPresent())
            return sessions.getSession(chatId).get().getRole().equals(Role.ADMIN.getCode());
        return false;
    }

    public String getStatistics() {
        StringBuilder builder = new StringBuilder();
        builder.append("<b>Bot statistikasi 📊</b>\n")
                .append("––––––––––––––––––––––\n")
                .append("🧑🏻‍💼Foydalanuvchilar\n")
                .append(" ⠸ Bugun:<code> ").append(repo.getJoinedLastDay()).append(" kishi:</code>\n")
                .append(" ⠸ So'nggi hafta:<code> ").append(repo.getJoinedLastWeek()).append(" kishi</code>\n")
                .append(" ⠸ So'nggi oy:<code> ").append(repo.getJoinedLastMonth()).append(" kishi</code>\n")
                .append("––––––––––––––––––––––\n")
                .append("♻️So'rovlar");
        return builder.toString();
    }

    public String preparePhoneNumber(Contact contact) {
        String phoneNumber = contact.getPhoneNumber();
        if (phoneNumber.startsWith("+")) phoneNumber = phoneNumber.substring(1);
        return phoneNumber;
    }

    public Optional<SessionUser> prepareSession(AuthUser authUser) {
        return Optional.of(SessionUser.builder().dailyLimit(authUser.getDailyRequest()).role(authUser.getRole()).language(authUser.getLanguage()).state(authUser.getState()).chatId(authUser.getChatId()).build());
    }

    public String lorem() {
        return "How to enable and use @Async in Spring - from the very simple config and basic usage to the more complex executors and exception handling strategies.";
    }


}
