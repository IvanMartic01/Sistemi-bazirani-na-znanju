package com.ftn.sbnz.service.examples_2_cep;

import com.ftn.sbnz.model.examples_2.TransactionEvent;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class CEPRulesTest {

    @Test
    public void testCEPRules() {
    	KieServices ks = KieServices.Factory.get();
    	KieContainer kc = ks.newKieClasspathContainer();
        KieSession ksession = kc.newKieSession("cepKsession");

        Long customerId = 1L;
        ksession.insert(new TransactionEvent(customerId, 10.00));
        ksession.insert(new TransactionEvent(customerId, 10.00));
        ksession.insert(new TransactionEvent(customerId, 14.00));
        ksession.insert(new TransactionEvent(customerId, 10.50));
        ksession.insert(new TransactionEvent(customerId, 10.99));
        ksession.insert(new TransactionEvent(customerId, 9.00));
        ksession.insert(new TransactionEvent(customerId, 11.00));
        ksession.insert(new TransactionEvent(customerId, 15.00));
        ksession.insert(new TransactionEvent(customerId, 18.00));
        ksession.insert(new TransactionEvent(customerId, 201.00));

        // NECE se okinuti pravilo "More than 10 transactions in an hour from one client" iz <cep-rules.drl>
        // Razlog zato sto imamo 10 transakcija a treba nam 11 jer jedna transakcija nam je referentna i ona sluzi za poredjenje

        long ruleFireCount = ksession.fireAllRules();
        System.out.println(ruleFireCount);

        ksession.insert(new TransactionEvent(customerId, 202.00));
        ruleFireCount = ksession.fireAllRules();

        System.out.println(ruleFireCount);
    }
}
