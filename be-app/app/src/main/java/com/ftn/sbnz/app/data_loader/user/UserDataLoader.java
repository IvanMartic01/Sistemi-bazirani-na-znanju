package com.ftn.sbnz.app.data_loader.user;

import com.ftn.sbnz.app.core.user.admin.service.AdminService;
import com.ftn.sbnz.app.core.user.organizer.service.OrganizerService;
import com.ftn.sbnz.app.core.user.visitor.service.VisitorService;
import com.ftn.sbnz.model.core.AdminEntity;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.Role;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import com.ftn.sbnz.model.core.visitor.VisitorEventPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

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
        HashSet<VisitorEventPreference> preferences = new HashSet<>();
        preferences.add(VisitorEventPreference.SPORTING_EVENTS);
        preferences.add(VisitorEventPreference.CULTURAL_EVENTS);

        return VisitorEntity.builder()
                .id(VISITOR_ID)
                .email(VISITOR_EMAIL)
                .password(passwordEncoder.encode(PASSWORD))
                .role(Role.VISITOR)
                .name(VISITOR_NAME)
                .preferences(preferences)
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
