package ent;

import ent.config.BotRunner;
import ent.service.DailyLimitService;
import ent.service.auth.AuthService;
import ent.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class TasvirniMatngaApplication {

    private final AuthService service;
    private final RequestService RService;
    private final BotRunner runner;
    private final DailyLimitService dlService;

    public static void main(String[] args) {
        SpringApplication.run(TasvirniMatngaApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return (args) -> {
            runner.main();
//            DailyLimit limit = DailyLimit.builder()
//                    .limit(7)
//                    .build();
//            Request request = Request.builder()
//                    .created_at(LocalDateTime.now())
//                    .requestBy(1992137199L)
//                    .build();
//            AuthUser authUser = AuthUser.builder()
//                    .chatId(1992137199L)
//                    .username("AhrorjonIsroilov")
//                    .firstname("Ahrorjon")
//                    .lastname("Isroilov")
//                    .phoneNumber("998903061599")
//                    .dailyRequest(100)
//                    .language(Language.UZ.getCode())
//                    .role(Role.ADMIN.getCode())
//                    .state(State.DEFAULT.getCode())
//                    .created_at(LocalDateTime.now())
//                    .build();
//            service.save(authUser);
//            RService.save(request);
//            dlService.save(limit);
        };
    }
}
