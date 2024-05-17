package com.ftn.sbnz.model.core.visitor;

import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class VisitorEntity extends UserEntity {

    @Column(nullable = false)
    private String name;

    @ElementCollection(targetClass = VisitorEventPreference.class)
    @Builder.Default
    private Collection<VisitorEventPreference> preferences = new HashSet<>();

    private double money;

    @ManyToOne
    private CountryEntity country;
}
