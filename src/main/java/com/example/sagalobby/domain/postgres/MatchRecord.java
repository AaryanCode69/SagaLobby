package com.example.sagalobby.domain.postgres;

import com.example.sagalobby.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "match_record")
@AllArgsConstructor
@NoArgsConstructor
public class MatchRecord extends BaseEntity {

    @ManyToMany(mappedBy = "matchRecords")
    private Set<PlayerProfile> playerProfiles;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
