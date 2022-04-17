package ent.entity.request;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "request")
public class Request {
    @Id
    @SequenceGenerator(name = "seqqq", sequenceName = "request_pk_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqqq")
    private Long id;
    @Column(name = "request_by")
    private Long requestBy;
    @Column(name = "cкeated_at", updatable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime created_at;
}
