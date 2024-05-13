package com.ftn.sbnz.service.examples_1;

import com.ftn.sbnz.kjar.util.KnowledgeSessionHelper;
import com.ftn.sbnz.model.examples_1.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class HelloWorldTest {

    public static void main() {
        KieContainer kc = KnowledgeSessionHelper.createRuleBase();
        KieSession kSession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kc, "k-session");

        Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);
        kSession.insert(message);
        kSession.fireAllRules();
    }
}
