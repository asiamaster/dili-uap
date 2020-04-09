//package com.dili.uap.boot;
//
//import org.kie.api.KieBase;
//import org.kie.api.builder.Results;
//import org.kie.api.io.ResourceType;
//import org.kie.internal.utils.KieHelper;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Drools工厂类，用于获取KieBase
// * @Author: WangMi
// * @Date: 2019/11/4 11:02
// * @Description:
// */
//public class DroolsFactory {
//
//    public static String chargingRules = "package com.rules\n"
//            + "import com.dili.uap.domain.CustomizeBeanImpl;\n"
//            + "rule \"rules1\"\n"
//            + "    when\n" + "        bean : CustomizeBeanImpl();\n"
//            + "    then\n" + "        bean.setResult(true);\n"
//            + "        System.out.println(\"规则中打印日志：校验通过1!\");\n"
//            + "end";
//
//    /**
//     * KieBase缓存，key为kbaseName
//     * 启动时初始化，用于获取kieSession
//     */
//    public static Map<String, KieBase> cachedKieBase = new HashMap<>(2);
//
//    //计费规则
//    public static final String CHARGING_RULES = "charging_rules";
//
//    public static KieBase getKieBase(String kBaseName){
//        return cachedKieBase.get(kBaseName);
//    }
//
//    /**
//     * 获取计费规则KieBase
//     * @return
//     */
//    public static KieBase getChargingRulesKieBase(){
//        return cachedKieBase.get(CHARGING_RULES);
//    }
//
//    /**
//     * 部署规则，并缓存
//     */
//    public static KieBase deploy(String kBaseName, String rules) {
//        KieBase kieBase = deploy(rules);
//        cachedKieBase.put(kBaseName, kieBase);
//        return kieBase;
//    }
//
//    /**
//     * 部署规则
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
//}
