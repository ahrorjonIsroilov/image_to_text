package ent.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {
    UZ("uz"),
    RU("ru"),
    EN("en");

    private final String code;
}
