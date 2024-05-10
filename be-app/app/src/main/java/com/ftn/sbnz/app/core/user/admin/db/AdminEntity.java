package com.ftn.sbnz.app.core.user.admin.db;

import com.ftn.sbnz.app.core.user.abstract_user.db.model.UserEntity;
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
