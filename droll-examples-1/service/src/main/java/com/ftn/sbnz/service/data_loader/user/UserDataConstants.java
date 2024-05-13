package com.ftn.sbnz.service.data_loader.user;


import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.UUID;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UserDataConstants {

    public static final UUID VISITOR_ID = UUID.fromString("98e2726d-f405-40ea-9374-89f0e5e3e6d0");
    public static final String VISITOR_EMAIL = "visitor1@email.com";
    public static final String VISITOR_NAME = "Visitor 1";

    public static final UUID ORGANIZER_ID = UUID.fromString("05a40ad0-70e0-45b3-8d2e-2fe443578927");
    public static final String ORGANIZER_EMAIL = "organizer1@email.com";
    public static final String ORGANIZER_NAME = "Organizer 1";

    public static final UUID ADMIN_ID = UUID.fromString("e5726a1b-4731-4754-a386-a6d2c9d82aa3");
    public static final String ADMIN_EMAIL = "admin1@email.com";

    public static final String PASSWORD = "Password!123";
    // TODO change path to relative
    public static final byte[] IMAGE_BASE64 = readFile("/home/martic/d/git/private/diplomski/be-event-app/core/src/main/resources/test_images/organizer.jpg");

    @SneakyThrows
    private static byte[] readFile(String filePath) {
        return FileUtils.readFileToByteArray(new File(filePath));
    }
}
