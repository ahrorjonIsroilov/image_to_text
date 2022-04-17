package ent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {
    SEND_AD("send_ad"),
    ADD_BUTTON("add_button"),
    SET_LIMIT("set_limit"),
    DEFAULT("default");
    private final String code;
}
