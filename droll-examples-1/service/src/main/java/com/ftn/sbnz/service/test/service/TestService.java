package com.ftn.sbnz.service.test.service;

import com.ftn.sbnz.app.core.drools.KnowledgeSessionHelper;
import com.ftn.sbnz.model.test.TestModel;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestService {

    public void fireAllRules() {
        KieContainer kieContainer = KnowledgeSessionHelper.createRuleBase();
        KieSession kSession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "test-k-session-1"); // TODO promeniti

        kSession.insert(TestModel.builder().id(null).name("Test").age(20).build());
        kSession.insert(TestModel.builder().id(null).name("Test").age(8).build());
        kSession.insert(TestModel.builder().id(null).name("Test").age(18).build());
        kSession.insert(TestModel.builder().id(null).name("Test").age(7).build());
        kSession.insert(TestModel.builder().id(null).name("Test").age(17).build());
        kSession.insert(TestModel.builder().id(null).name("Test").age(11).build());
        kSession.insert(TestModel.builder().id(null).name("Test").age(1).build());
        kSession.insert(TestModel.builder().id(null).name("Test").age(2).build());

        kSession.insert( "go" );
        kSession.fireAllRules();
        System.out.println("---");

        kSession.insert( "go1" );
        kSession.fireAllRules();
        System.out.println("---");
    }
}

