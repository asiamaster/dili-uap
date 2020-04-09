//package com.dili.uap.drools.service;
//
//import com.dili.uap.domain.CustomizeBeanImpl;
//import org.drools.core.base.RuleNameStartsWithAgendaFilter;
//import org.kie.api.KieBase;
//import org.kie.api.KieServices;
//import org.kie.api.builder.Results;
//import org.kie.api.command.Command;
//import org.kie.api.command.KieCommands;
//import org.kie.api.io.ResourceType;
//import org.kie.api.runtime.ExecutionResults;
//import org.kie.api.runtime.KieSession;
//import org.kie.api.runtime.KieSessionsPool;
//import org.kie.api.runtime.StatelessKieSession;
//import org.kie.internal.utils.KieHelper;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Drools工具
// */
//public class DroolsUtils {
//
//    public static String rule1 = "package rules1;" + "rule \"test1\"\n" + "when\n" + "eval(true)\n" + "then \n" + " System.out.println(\"----------你好1\");\n" + "end";
//
//    public static String rule2 = "package com.rules\n" + "import com.dili.uap.domain.CustomizeBeanImpl;\n" + "\n" + "rule \"result should be filled with true1\"\n" + "\n" + "    when\n" + "        bean : CustomizeBeanImpl();\n" + "    then\n" + "        bean.setResult(true);\n" + "        System.out.println(\"规则中打印日志：校验通过1!\");\n" + "end";
//
//    public static String rules = "package com.rules\n" + "import com.dili.uap.domain.CustomizeBeanImpl;\n" + "\n" + "rule test1\n" + "when\n" + "    eval(true)\n" + "then \n" + "    System.out.println(\"----------你好2\");\n" + "end\n" + "\n" + "rule \"result should be filled with true1\"\n" + "\n" + "    when\n" + "        bean : CustomizeBeanImpl();\n" + "    then\n" + "        bean.setResult(true);\n" + "        System.out.println(\"规则中打印日志：校验通过2!\");\n" + "end";
//
//    public static void main(String[] args) {
//        benchmark();
//    }
//
//    public static void testAgendaFilter() {
//        KieBase kBase = deploy(rules);
//        KieSession session = kBase.newKieSession();
//        session.insert(new CustomizeBeanImpl());
//        int count = session.fireAllRules(new RuleNameStartsWithAgendaFilter("result should "));
//        System.out.println("触发了"+count+"条规则");
//    }
//
//    /**
//     * 性能测试
//     */
//    public static void benchmark() {
//        try {
//            KieBase kBase = deploy(rule1);
//            System.out.println("开始部署");
//            long start = System.currentTimeMillis();
//            kBase = deploy(rule1);
//            KieSessionsPool statelessPool = kBase.newKieSessionsPool(10);
//
////            KieSessionsPool statePool = kBase.newKieSessionsPool(10);
//            long end = System.currentTimeMillis();
//            System.out.println("deploy cost:" + (end - start));
//            run(kBase, null);
//            System.out.println("warm up cost:" + (System.currentTimeMillis() - end));
//            System.out.println("启动测试");
//            start = System.currentTimeMillis();
//            for (int i = 0; i < 10; i++) {
//                run(kBase, null);
//            }
//            end = System.currentTimeMillis();
//            System.out.println("state run cost:" + (end - start));
//            KieCommands kieCommands = KieServices.Factory.get().getCommands();
////            pool.newStatelessKieSession().execute(kieCommands.newFireAllRules());
//            //预热
//            statelessPool.newStatelessKieSession().execute(new Object());
//            start = System.currentTimeMillis();
//            for (int i = 0; i < 10; i++) {
////                runStateless(kBase, null);
//                StatelessKieSession statelessKieSession = statelessPool.newStatelessKieSession();
//                statelessKieSession.execute(kieCommands.newFireAllRules());
//            }
//            statelessPool.shutdown();
//            end = System.currentTimeMillis();
//            System.out.println("stateless run cost:" + (end - start));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void testStateless() {
//        KieBase kBase = deploy(rules);
//        CustomizeBeanImpl customizeBean = new CustomizeBeanImpl();
//        customizeBean.setName("name1");
//        CustomizeBeanImpl customizeBean2 = new CustomizeBeanImpl();
//        customizeBean2.setName("name2");
//        runStateless(kBase, customizeBean, customizeBean2);
//    }
//
//    /**
//     * test1 cost:28859ms
//     *
//     * @throws UnsupportedEncodingException
//     */
//    public static KieBase deploy(String rules) {
//        KieHelper helper = new KieHelper();
//        helper.addContent(rules, ResourceType.DRL);
//        Results results = helper.verify();
//        if (!results.getMessages().isEmpty()) {
//            System.out.println(results.getMessages().get(0).toString());
//            return null;
//        }
//        return helper.build();
//    }
//
//    /**
//     * 执行有状态会话
//     *
//     * @param kBase
//     * @return 返回影响规则数
//     */
//    public static int run(KieBase kBase, Object obj) {
//        KieSession session = kBase.newKieSession();
//        if (obj != null) {
//            session.insert(obj);
//        }
//        try {
//            return session.fireAllRules();
//        } finally {
//            session.dispose();
//        }
//    }
//
//    /**
//     * 批量执行无状态会话，并获取返回值
//     *
//     * @param kBase
//     * @param objs
//     * @return 返回执行结果(0 ... N)
//     */
//    public static ExecutionResults runStateless(KieBase kBase, Object... objs) {
//        StatelessKieSession session = kBase.newStatelessKieSession();
//        KieCommands kieCommands = KieServices.Factory.get().getCommands();
//        if(objs != null) {
//            List<Command> cmds = new ArrayList<>();
//            for (int i = 0; i < objs.length; i++) {
//                cmds.add(kieCommands.newInsert(objs[i], String.valueOf(i), true, null));
//            }
//            //        BatchExecutionResults results = session.execute( kieCommands.newBatchExecution( cmds ) );
//            ExecutionResults results = session.execute(kieCommands.newBatchExecution(cmds));
////        for (int i = 0; i < objs.length; i++) {
////            results.getValue("" + i);
////        }
//            return results;
//        }
//        session.execute(kieCommands.newFireAllRules());
//        return null;
//    }
//
//
//}