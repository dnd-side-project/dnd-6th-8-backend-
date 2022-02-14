package com.travel.domain.user.entity;

import com.travel.domain.archive.entity.Archives;
import com.travel.domain.archive.entity.EArchivingStyle;
import com.travel.domain.archive.entity.EBudget;
import com.travel.domain.emoji.entity.Emoji;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @Type(type = "uuid-char")
    @Column(columnDefinition = "BINARY(16)")
    private String id;

    @Column()
    private String userName;

    @OneToOne(optional = true)
    @JoinColumn(name = "SURVEY_ID")
    private Survey survey;

    @Column()
    @OneToMany(mappedBy = "user")
    private List<Archives> archives;

    @OneToMany(mappedBy = "board")
    private List<Emoji> emojis;


    @Builder
    public User(String userName) {
        this.userName = userName;
    }

}
