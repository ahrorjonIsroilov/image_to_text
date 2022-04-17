package ent.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "daily_limit")
public class DailyLimit {
    @Id
    @SequenceGenerator(name = "seqq", sequenceName = "daily_limit_pk_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqq")
    private Long id;
    @Column(name = "request_limit")
    private Integer limit;
}
