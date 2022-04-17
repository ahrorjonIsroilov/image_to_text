package ent.entity.auth;

import ent.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "auth_user")
public class AuthUser implements BaseEntity {
    @Id
    @SequenceGenerator(name = "seq", sequenceName = "auth_user_pk_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;
    @Column(name = "chat_id", unique = true)
    private Long chatId;
    @Column(unique = true)
    private String username;
    private String firstname;
    private String lastname;
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;
    @Column(nullable = false, columnDefinition = "varchar default 'user'")
    private String role;
    @Column(nullable = false, columnDefinition = "varchar default 'DEFAULT'")
    private String state;
    @Column(nullable = false, columnDefinition = "varchar default 'uz'")
    private String language;
    @Column(name = "daily_request", nullable = false)
    private Integer dailyRequest;
    @Column(name = "created_at", updatable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime created_at;
}
