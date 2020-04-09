//package com.dili.uap.boot;
//
//import org.kie.api.KieServices;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
///**
// * @Author: WangMi
// * @Date: 2019/10/29 15:31
// * @Description:
// */
//@Component
//public class DroolsConfiguration implements ApplicationListener<ContextRefreshedEvent> {
//
//    @Bean
//    @ConditionalOnMissingBean(KieServices.class)
//    public KieServices getKieServices() {
//        return KieServices.Factory.get();
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        DroolsFactory.deploy(DroolsFactory.CHARGING_RULES, DroolsFactory.chargingRules);
//    }
//
//}
