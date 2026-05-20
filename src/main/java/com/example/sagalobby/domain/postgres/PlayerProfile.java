package com.example.sagalobby.domain.postgres;

import com.example.sagalobby.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;
}
