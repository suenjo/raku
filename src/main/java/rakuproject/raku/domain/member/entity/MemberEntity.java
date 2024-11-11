package rakuproject.raku.domain.member.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rakuproject.raku.domain.member.entity.enums.MemberRole;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EntityListeners(value = {
        AuditingEntityListener.class
})
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userKey;

    @Column(unique = true, nullable = false)
    private String id;

    @Column(nullable = false)
    private String pwd;

    @Column(unique = true, nullable = false)
    private String nick;

    @Column(nullable = false)
    private int address;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    private int alarm;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate borndate;

    @LastModifiedDate
    private LocalDate accessdate;

    @Column(nullable = true)
    private String bookmark;

    @Column(nullable = true)
    private String recent;
}
