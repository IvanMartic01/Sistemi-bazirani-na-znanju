package com.ftn.sbnz.service.core.user.admin;

import com.ftn.sbnz.service.core.user.abstract_user.db.model.UserEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AdminEntity extends UserEntity {

}
