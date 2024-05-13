package com.ftn.sbnz.service.examples_2_cep;

import com.ftn.sbnz.model.examples_2.TransactionEvent;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CEPEntryPointsTest {

    @Test
    public void testCEPRules() {
    	KieServices ks = KieServices.Factory.get();
    	KieContainer kc = ks.newKieClasspathContainer();
        KieSession ksession = kc.newKieSession("cepKsession");

        Long customer1 = 1L;
        Long customer2 = 2L;

        List<TransactionEvent> smallListOfEvents1 = createTransactionEventList(customer1, 11);
        for (TransactionEvent event : smallListOfEvents1) {
            ksession.getEntryPoint("small-client-portal").insert(event);
        }
        int ruleCount = ksession.fireAllRules();
        assertThat(ruleCount, equalTo(1));
        
        List<TransactionEvent> smallListOfEvents2 = createTransactionEventList(customer2, 11);
        for (TransactionEvent event : smallListOfEvents2) {
            ksession.getEntryPoint("big-client-portal").insert(event);
        }
        ruleCount = ksession.fireAllRules();
        assertThat(ruleCount, equalTo(0));

        List<TransactionEvent> bigListOfEvents2 = createTransactionEventList(customer2, 101);
        for (TransactionEvent event : bigListOfEvents2) {
            ksession.getEntryPoint("big-client-portal").insert(event);
        }
        ruleCount = ksession.fireAllRules();
        assertThat(ruleCount, equalTo(1));
    }
    
    private List<TransactionEvent> createTransactionEventList(Long customerId, int size) {
        List<TransactionEvent> events = new ArrayList<>(size);
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            events.add(new TransactionEvent(customerId, (r.nextDouble() * 10)));
        }
        return events;
    }
}
