package com.ftn.sbnz.service.examples_2_cep;

import com.ftn.sbnz.model.examples_2.HeartAttackEvent;
import com.ftn.sbnz.model.examples_2.HeartBeatEvent;
import org.drools.core.ClockType;
import org.drools.core.time.SessionPseudoClock;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.io.ResourceFactory;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class CEPConfigTest {

    @Test
    public void testCEPConfigThroughCode() {
        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();

        kfs.write(ResourceFactory.newClassPathResource("droll-examples-1/kjar/src/main/resources/examples_2/cep_2/heart-monitor-rules-code-generated.drl"));
       
        KieBuilder kbuilder = ks.newKieBuilder(kfs);
        kbuilder.buildAll();
        if (kbuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new IllegalArgumentException("Coudln't build knowledge module" + kbuilder.getResults());
        }
        KieContainer kContainer = ks.newKieContainer(kbuilder.getKieModule().getReleaseId());
        KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
        kbconf.setOption(EventProcessingOption.STREAM);
        KieBase kbase = kContainer.newKieBase(kbconf);
        
        KieSessionConfiguration ksconf1 = ks.newKieSessionConfiguration();
        ksconf1.setOption(ClockTypeOption.get(ClockType.REALTIME_CLOCK.getId()));
        KieSession ksession1 = kbase.newKieSession(ksconf1, null);
        
        KieSessionConfiguration ksconf2 = ks.newKieSessionConfiguration();
        ksconf2.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
        KieSession ksession2 = kbase.newKieSession(ksconf2, null);
        
        runRealtimeClockExample(ksession1);
        runPseudoClockExample(ksession2);
    }
    
    @Test
    public void testCEPConfigThroughKModuleXML() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer(); 
        KieSession ksession1 = kContainer.newKieSession("cepConfigKsessionRealtimeClock");
        KieSession ksession2 = kContainer.newKieSession("cepConfigKsessionPseudoClock");
        
//        runRealtimeClockExample(ksession1);
        runPseudoClockExample(ksession2);
    }

    private void runPseudoClockExample(KieSession ksession) {
        SessionPseudoClock clock = ksession.getSessionClock();
        for (int i = 0; i < 100; i++) {

            HeartBeatEvent beep = new HeartBeatEvent();
            ksession.insert(beep);
            clock.advanceTime(1, TimeUnit.SECONDS);
            int ruleCount = ksession.fireAllRules();

            //As long as there is a steady heart beat, no rule will fire
            assertThat(ruleCount, equalTo(0));
        }
        //We manually advance time 1 minute, without a heart beat
        clock.advanceTime(1, TimeUnit.MINUTES);
        int ruleCount = ksession.fireAllRules();
        assertThat(ruleCount, equalTo(1));
        Collection<?> newEvents = ksession.getObjects(new ClassObjectFilter(HeartAttackEvent.class));
        assertThat(newEvents.size(), equalTo(1));
    }
    
    private void runRealtimeClockExample(KieSession ksession) {
        Thread t = new Thread(() -> {
            for (int index = 0; index < 4; index++) {
                HeartBeatEvent beep = new HeartBeatEvent();
                ksession.insert(beep);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //do nothing
                }
            }
        });
        t.setDaemon(true);
        t.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            //do nothing
        }

        ksession.fireUntilHalt();
        Collection<?> newEvents = ksession.getObjects(new ClassObjectFilter(HeartAttackEvent.class));
        assertThat(newEvents.size(), equalTo(1));
    }
}
