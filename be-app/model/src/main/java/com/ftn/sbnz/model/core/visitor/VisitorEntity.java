package com.ftn.sbnz.model.core.visitor;

import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.HashSet;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class VisitorEntity extends UserEntity {

    @Column(nullable = false)
    private String name;

    @ElementCollection(targetClass = VisitorEventPreference.class)
    @Builder.Default
    private Collection<VisitorEventPreference> preferences = new HashSet<>();

    @ManyToOne
    private CountryEntity country;
}
