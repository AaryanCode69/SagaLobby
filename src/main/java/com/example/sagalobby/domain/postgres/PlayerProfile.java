package com.example.sagalobby.domain.postgres;

import com.example.sagalobby.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "player_profile")
@AllArgsConstructor
@NoArgsConstructor
public class PlayerProfile extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private Integer mmr_rating;

    @Setter(AccessLevel.NONE)
    @JoinTable(
            name = "player_match_record",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "match_id")
    )
    private Set<MatchRecord> matchRecords = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;

    public void addMatchRecord(MatchRecord matchRecord) {
        this.matchRecords.add(matchRecord);
        matchRecord.getPlayerProfiles().add(this);
    }

    public void removeMatchRecord(MatchRecord matchRecord) {
        this.matchRecords.remove(matchRecord);
        matchRecord.getPlayerProfiles().remove(this);
    }
}
