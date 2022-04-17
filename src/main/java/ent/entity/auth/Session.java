package ent.entity.auth;

import ent.enums.State;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class Session {

    protected Map<Long, Optional<SessionUser>> sessions = new HashMap<>();

    public Optional<SessionUser> getSession(Long chatId) {
        for (Map.Entry<Long, Optional<SessionUser>> entry : sessions.entrySet()) {
            if (entry.getKey().equals(chatId)) return entry.getValue();
        }
        return Optional.empty();
    }

    public void setSession(Long chatId, Optional<SessionUser> user) {
        sessions.put(chatId, user);
    }

    public void updateLimit(SessionUser user){

    }

    public void removeSession(Long chatId) {
        sessions.remove(chatId);
    }

    public void setState(State state, Long chatId) {
        Optional<SessionUser> session = getSession(chatId);
        if (session.isPresent()) {
            SessionUser user = session.get();
            user.setState(state.getCode());
            setSession(chatId, Optional.of(user));
        }
    }

    public Boolean checkState(State state, Long chatId) {
        Optional<SessionUser> session = getSession(chatId);
        return session.map(sessionUser -> sessionUser.getState().equals(state.getCode())).orElse(false);
    }

}
