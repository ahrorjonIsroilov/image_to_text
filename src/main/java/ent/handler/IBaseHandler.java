package ent.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public interface IBaseHandler {
    void handle(Update update) throws IOException;
}
