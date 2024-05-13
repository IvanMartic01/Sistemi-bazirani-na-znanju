package com.ftn.sbnz.model.examples_2;

import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

import java.io.Serializable;

@NoArgsConstructor
@Expires("10m")
@Role(Role.Type.EVENT)
public class HeartBeatEvent implements Serializable {

    private static final long serialVersionUID = 1L;

}
