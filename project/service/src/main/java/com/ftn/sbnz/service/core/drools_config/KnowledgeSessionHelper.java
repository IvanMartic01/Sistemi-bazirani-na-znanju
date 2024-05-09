package com.ftn.sbnz.service.core.drools_config;


import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

@Slf4j
public class KnowledgeSessionHelper {
	
	public static KieContainer createRuleBase() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kieContainer = ks.getKieClasspathContainer();
		return kieContainer;
	}
	
	public static StatelessKieSession getStatlessKnowledgeSession(KieContainer kieContainer, String sessionName) {
		StatelessKieSession kSession = kieContainer.newStatelessKieSession(sessionName);
		return kSession;
	}
	
	public static KieSession getStatefulKnowledgeSession(KieContainer kieContainer, String sessionName) {
		log.info("Initialising a new example session.");
		KieSession kSession = kieContainer.newKieSession(sessionName);
		return kSession;
	}

}
