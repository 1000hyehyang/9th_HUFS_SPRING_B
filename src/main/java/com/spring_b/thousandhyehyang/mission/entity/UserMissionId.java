package com.spring_b.thousandhyehyang.mission.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMissionId implements Serializable {
    private Long userId;
    private Long missionId;
}
