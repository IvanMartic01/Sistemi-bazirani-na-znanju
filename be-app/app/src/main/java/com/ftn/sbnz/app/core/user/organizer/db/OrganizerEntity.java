package com.ftn.sbnz.app.core.user.organizer.db;


import com.ftn.sbnz.app.core.user.abstract_user.db.model.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrganizerEntity extends UserEntity {

    @Column(nullable = false)
    private String name;
}
