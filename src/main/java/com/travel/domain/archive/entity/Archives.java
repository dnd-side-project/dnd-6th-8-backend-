package com.travel.domain.archive.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.travel.domain.day.entity.Days;

import com.travel.domain.emoji.entity.UserEmojiSelected;
import com.travel.domain.scrap.entity.Scraps;
import com.travel.domain.common.BaseTimeEntity;
import com.travel.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Archives extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String title;

    @Column()
    private boolean isShare;

    @Column()
    private String coverImage;

    @Column()
    private LocalDate firstDay;

    @Column()
    private LocalDate lastDay;

    @Column()
    @Enumerated(EnumType.STRING)
    private EBudget budget;

    @Column()
    @Enumerated(EnumType.STRING)
    private EArchivingStyle archivingStyle;

    @Column()
    private Boolean haveCompanion;

    @OneToMany(mappedBy = "archives")
    private List<Days> days = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name="PLACE_ID")
    private Place place;

    @Enumerated(EnumType.STRING)
    private EBadges badges;


    @OneToMany(mappedBy = "archives")
    private List<UserEmojiSelected> userEmojiSelected;

    @OneToMany(mappedBy = "archives")
    private List<Scraps> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "archives")
    private List<Days> dayFeeds = new ArrayList<>();


    @Builder
    public Archives(String title, boolean isShare, String coverImage,
                    Place place, LocalDate firstDay, LocalDate lastDay, EBudget budget, EPlaces places,
                    EArchivingStyle archivingStyle, Boolean haveCompanion, User user) {
        this.title = title;
        this.isShare = isShare;
        this.coverImage = coverImage;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.haveCompanion = haveCompanion;
        this.budget = budget;
        this.archivingStyle = archivingStyle;
        setUser(user);
        setPlace(place);
    }

    public void setUser(User user) {
        System.out.println("setting user");
        if(this.user == null){
            System.out.println("user ");
            this.user = user;
        }
    }

    public void setPlace(Place place) {
        System.out.println(place.getId());
        this.place = place;
    }

    public int countScrapsOfArchive() {
        return this.scraps.size();
    }

    public int countDaysFeedsOfArchive() { return this.dayFeeds.size(); }
}

