//package com.dili.uap.boot;
//
//import org.kie.api.KieServices;
//import org.kie.api.builder.KieFileSystem;
//import org.kie.api.runtime.KieContainer;
//import org.kie.internal.io.ResourceFactory;
//import org.kie.spring.KModuleBeanFactoryPostProcessor;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;
//
//import java.io.IOException;
//
///**
// * @Author: WangMi
// * @Date: 2019/10/29 15:31
// * @Description:
// */
////@Configuration
//public class DroolsAutoConfigurationBak {
//
//    /** 特别要注意 rule的存放位置  */
//    private static final String RULES_PATH = "rules/";
//
//    private String[] paths = new String[]{"rules/points/", "rules/charge/"};
//
//    /**
//     * 实现多套规则
//     * @return
//     * @throws IOException
//     */
////    @Bean
////    public Map<String,KieSession> getMapKieSession() throws IOException{
////        Map<String,KieSession> kieSessionMap=new HashMap<String,KieSession>();
////        for(String path : paths){
////            KieServices kieServices = KieServices.Factory.get();
////            KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
////            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
////            Resource[] resource=resourcePatternResolver.getResources("classpath*:" +path+ "**/*.*");
////            for (Resource file : resource) {
////                kieFileSystem.write(ResourceFactory.newClassPathResource(path + file.getFilename(), "UTF-8"));
////            }
////            final KieRepository kieRepository = kieServices.getRepository();
////            kieRepository.addKieModule(new KieModule() {
////                public ReleaseId getReleaseId() {
////                    return kieRepository.getDefaultReleaseId();
////                }
////            });
////            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
////            kieBuilder.buildAll();
////            KieContainer kieContainer=kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
////            KieSession kieSession = kieContainer.newKieSession();
////            kieSessionMap.put(path, kieSession);
////        }
////        return kieSessionMap;
////    }
//
////    @Bean
////    @ConditionalOnMissingBean(KieFileSystem.class)
//    public KieFileSystem kieFileSystem() throws IOException {
//        KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();
//        for (Resource file : getRuleFiles()) {
//            String path = RULES_PATH + file.getFilename();
//            kieFileSystem.write(ResourceFactory.newClassPathResource(path, "UTF-8"));
//        }
//        return kieFileSystem;
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(KieContainer.class)
//    public KieContainer kieContainer() throws IOException {
////        final KieRepository kieRepository = getKieServices().getRepository();
////
////        kieRepository.addKieModule(new KieModule() {
////            @Override
////            public ReleaseId getReleaseId() {
////                return kieRepository.getDefaultReleaseId();
////            }
////        });
////
////        KieBuilder kieBuilder = getKieServices().newKieBuilder(kieFileSystem());
////        kieBuilder.buildAll();
////
////        return getKieServices().newKieContainer(kieRepository.getDefaultReleaseId());
//        return getKieServices().getKieClasspathContainer();
//    }
//
////    @Bean
////    @ConditionalOnMissingBean(KieBase.class)
////    public KieBase kieBase() throws IOException {
////        return kieContainer().getKieBase();
////    }
////
////    @Bean
////    @ConditionalOnMissingBean(KieSession.class)
////    public KieSession kieSession() throws IOException {
////        return kieContainer().newKieSession();
////    }
//
//    @Bean
//    @ConditionalOnMissingBean(KModuleBeanFactoryPostProcessor.class)
//    public KModuleBeanFactoryPostProcessor kiePostProcessor() {
//        return new KModuleBeanFactoryPostProcessor();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(KieServices.class)
//    public KieServices getKieServices() {
//        return KieServices.Factory.get();
//    }
//
//    /**
//     * 这里要引入 org.springframework.core.io.Resource  包
//     *
//     * @return
//     * @throws IOException
//     */
//    private Resource[] getRuleFiles() throws IOException {
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        return resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
//    }
//
//
////    @Bean
////    public KieRepository getKieRepository() {
////        KieServices kieServices = KieServices.Factory.get();
////        return kieServices.getRepository();
////    }
//}
