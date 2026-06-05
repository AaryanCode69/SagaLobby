package com.example.sagalobby.domain.postgres.matchrecord;

import com.example.sagalobby.common.BaseEntity;
import com.example.sagalobby.domain.postgres.playerprofile.PlayerProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "match_record")
@AllArgsConstructor
@NoArgsConstructor
public class MatchRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToMany(mappedBy = "matchRecords")
    private Set<PlayerProfile> playerProfiles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
