//package com.dili.uap.drools.service;
//
//import org.drools.core.impl.KnowledgeBaseFactory;
//import org.drools.core.impl.KnowledgeBaseImpl;
//import org.kie.api.builder.Results;
//import org.kie.api.io.ResourceType;
//import org.kie.api.runtime.KieSession;
//import org.kie.internal.builder.KnowledgeBuilder;
//import org.kie.internal.builder.KnowledgeBuilderError;
//import org.kie.internal.builder.KnowledgeBuilderErrors;
//import org.kie.internal.builder.KnowledgeBuilderFactory;
//import org.kie.internal.io.ResourceFactory;
//import org.kie.internal.utils.KieHelper;
//
//import java.io.UnsupportedEncodingException;
//
//public class DroolsService{
//
//    public static KnowledgeBaseImpl kBase = null;
//    public static KnowledgeBaseImpl kBase2 = null;
//    public static String rule1 = "package rules1;"
//            + "\n"
//            + "rule \"test1\"\n"
//            + "when\n"
//            + "eval(true)\n"
//            + "then \n"
//            + " System.out.println(\"----------你好1\");\n"
//            + "end";
//    public static String rule2 = "package rules2;"
//            + "\n"
//            + "rule \"test2\"\n"
//            + "when\n"
//            + "eval(true)\n"
//            + "then \n"
//            + " System.out.println(\"----------你好2\");\n"
//            + "end";
//
////    public static KieHelper helper = new KieHelper();
//
//    public static void main(String[] args) {
//        try {
//
//            System.out.println("开始预热");
//            test();
//            System.out.println("---------------------------");
//            test2();
//
//
//            System.out.println("启动测试");
//            long start = System.currentTimeMillis();
//            for(int i=0; i<10; i++) {
//                test();
//            }
//            long end = System.currentTimeMillis();
//            System.out.println("test1 cost:"+(end - start));
//            System.out.println("---------------------------");
//            start = System.currentTimeMillis();
//            for(int i=0; i<10; i++) {
//                test2();
//            }
//            end = System.currentTimeMillis();
//            System.out.println("test2 cost:"+(end - start));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * test1 cost:28859ms
//     * @throws UnsupportedEncodingException
//     */
//    public static void test() throws UnsupportedEncodingException {
//        //重新添加规则
////        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
////        //装入规则，可以装入多个
////        kb.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
////        // 检查规则正确性
////        KnowledgeBuilderErrors errors = kb.getErrors();
////        for (KnowledgeBuilderError error : errors) {
////            System.out.println(String.format("规则文件正确性有误：%s", error));
////            return;
////        }
//        KieHelper helper = new KieHelper();
//        helper.addContent(rule1, ResourceType.DRL);
//        helper.addContent(rule2, ResourceType.DRL);
//        Results results = helper.verify();
//        if(!results.getMessages().isEmpty()) {
//            System.out.println(results.getMessages().get(0).toString());
//            return;
//        }
//        if(kBase == null){
//            kBase =(KnowledgeBaseImpl) helper.build();
//        }
//        //移除规则
////        kBase.removeRule("rules","test");
//        KieSession session = kBase.newKieSession();
////        kBase.addPackages(kb.getKnowledgePackages());
//        session.fireAllRules();
//    }
//
//    /**
//     * test2 cost:12155ms
//     */
//    public static void test2(){
//        KieSession kSession = null;
//        try {
//            KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
//            kb.add(ResourceFactory.newByteArrayResource(rule1.getBytes("utf-8")), ResourceType.DRL);
//            kb.add(ResourceFactory.newByteArrayResource(rule2.getBytes("utf-8")), ResourceType.DRL);
//            // 检查规则正确性
//            KnowledgeBuilderErrors errors = kb.getErrors();
//            for (KnowledgeBuilderError error : errors) {
//                System.out.println(String.format("规则文件正确性有误：%s", error));
//                return;
//            }
//            if(kBase2 == null){
//                kBase2 = (KnowledgeBaseImpl)KnowledgeBaseFactory.newKnowledgeBase();
//            }
////            if(kBase2 != null && kBase2.getRule("rules1", "test") != null){
////                kBase2.removeRule("rules1", "test");
////            }
//            kBase2.addPackages(kb.getKnowledgePackages());
//            // 执行规则
//            kSession = kBase2.newKieSession();
////            kSession.insert(fact);
//            kSession.fireAllRules();
//        } catch (Exception e) {
//            System.out.println(String.format("规则执行异常：%s", e));
//        } finally {
//            if (null != kSession) {
//                kSession.dispose();
//            }
//        }
//    }
//}