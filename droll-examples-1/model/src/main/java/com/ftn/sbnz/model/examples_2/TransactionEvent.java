package com.ftn.sbnz.model.examples_2;

import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Timestamp("executionTime") // ovo je polje gde skladistimo timestamp
@Expires("2h30m") // Za koliko vremena event istice, brise se iz memorije
@Role(Role.Type.EVENT)
public class TransactionEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private Date executionTime; // timestamp
    private Long customerId;
    private Double totalAmount;

    public TransactionEvent() {
        super();
    }
    
    public TransactionEvent(Long customerId, Double totalAmount) {
        super();
        this.executionTime = new Date();
        this.customerId = customerId;
        this.totalAmount = totalAmount;
    }
}

