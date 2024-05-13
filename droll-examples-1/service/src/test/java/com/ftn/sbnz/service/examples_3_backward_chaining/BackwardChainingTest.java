package com.ftn.sbnz.service.examples_3_backward_chaining;

import com.ftn.sbnz.kjar.util.KnowledgeSessionHelper;
import com.ftn.sbnz.model.examples_3_backward_chaining.Location;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class BackwardChainingTest {

    @Test
    public void fireRules() {
        KieContainer kieContainer = KnowledgeSessionHelper.createRuleBase();
        KieSession kSession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "k-session-backward-chaining");

//        kSession.insert( new Location("Office", "House") );
//        kSession.insert( new Location("Kitchen", "House") );
//        kSession.insert( new Location("Knife", "Kitchen") );
//        kSession.insert( new Location("Cheese", "Kitchen") );
        kSession.insert( new Location("Desk", "Office") );
//        kSession.insert( new Location("Chair", "Office") );
//        kSession.insert( new Location("Computer", "Desk") );
        kSession.insert( new Location("Draw", "Desk") );

//        kSession.insert( "go1" );
//        kSession.fireAllRules();
//        System.out.println("---");
//
//        kSession.insert( "go2" );
//        kSession.fireAllRules();
//        System.out.println("---");
//
//        kSession.insert( "go3" );
//        kSession.fireAllRules();
//        System.out.println("---");
//
//        kSession.insert( new Location("Key", "Draw") );
//        kSession.fireAllRules();
//        System.out.println("---");
//
//        kSession.insert( "go4" );
//        kSession.fireAllRules();
//        System.out.println("---");
//
        kSession.insert( "go5" );
        kSession.fireAllRules();
    }
}
