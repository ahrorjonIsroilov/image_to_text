package ent.entity.auth;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionUser {
    private Long chatId;
    private String role;
    private String state;
    private String language;
    private Integer dailyLimit;
}
