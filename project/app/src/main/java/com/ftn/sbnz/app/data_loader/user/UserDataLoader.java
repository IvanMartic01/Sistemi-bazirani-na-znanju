package com.ftn.sbnz.app.data_loader.user;

import com.ftn.sbnz.app.core.user.abstract_user.db.model.Role;
import com.ftn.sbnz.app.core.user.admin.db.AdminEntity;
import com.ftn.sbnz.app.core.user.admin.service.AdminService;
import com.ftn.sbnz.app.core.user.organizer.db.OrganizerEntity;
import com.ftn.sbnz.app.core.user.organizer.service.OrganizerService;
import com.ftn.sbnz.app.core.user.visitor.db.VisitorEntity;
import com.ftn.sbnz.app.core.user.visitor.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.ftn.sbnz.app.data_loader.user.UserDataConstants.*;

@RequiredArgsConstructor
@Component
public class UserDataLoader {

    private final PasswordEncoder passwordEncoder;

    private final VisitorService visitorService;
    private final OrganizerService organizerService;
    private final AdminService adminService;

    public void load() {
        visitorService.save(getVisitor_1());
        organizerService.save(getOrganizer_1());
        adminService.save(getAdmin_1());
    }

    private VisitorEntity getVisitor_1() {
        return VisitorEntity.builder()
                .id(VISITOR_ID)
                .email(VISITOR_EMAIL)
                .password(passwordEncoder.encode(PASSWORD))
                .role(Role.VISITOR)
                .name(VISITOR_NAME)
                .enabled(true)
                .build();
    }

    // TODO: Add image if needed
    private OrganizerEntity getOrganizer_1() {
        return OrganizerEntity.builder()
                .id(ORGANIZER_ID)
                .email(ORGANIZER_EMAIL)
                .password(passwordEncoder.encode(PASSWORD))
                .role(Role.ORGANIZER)
                .name(ORGANIZER_NAME)
                .enabled(true)
                .build();
    }

    private AdminEntity getAdmin_1() {
        return AdminEntity.builder()
                .id(ADMIN_ID)
                .email(ADMIN_EMAIL)
                .password(passwordEncoder.encode(PASSWORD))
                .role(Role.ADMIN)
                .enabled(true)
                .build();
    }
}
