package com.dili.uap.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * <B>Description 拼音转换工具</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/5/29 17:01
 */
public class PinYinUtil {

    private static HanyuPinyinOutputFormat format = null;

    private static String[] pinyin;

    static {
        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        pinyin = null;
    }

    /**
     * @Description 转换单个字符为全拼，返回多音列表
     * @param c
     * @return
     * @author WangMi
     * @time 2015-1-7 上午11:10:03
     */
    public static String[] getCharacterPinYin(char c) {
        try {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        // 如果c不是汉字，toHanyuPinyinStringArray会返回null
        if (pinyin == null) {
            return null;
        }
        // 只取一个发音，如果是多音字，仅取第一个发音
        return pinyin;
    }

    /**
     * @Description 转换一个字符串为全拼
     * @param str 字符串
     * @return
     * @author WangMi
     * @time 2015-1-7 上午11:10:28
     */
    public static String getFullPinYin(String str) {
        return getFullPinYin(str, "", false);
    }

    /**
     * @Description 转换一个字符串为全拼
     * @param str 字符串
     * @param separator 分隔符
     * @param firstCharUpperCase 是否首字母大写
     * @return
     * @author WangMi
     * @time 2015-1-7 上午11:10:28
     */
    public static String getFullPinYin(String str, String separator, boolean firstCharUpperCase) {
        if(null == str) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String[] tempPinyin = null;
        for (int i = 0; i < str.length(); ++i){
            tempPinyin = getCharacterPinYin(str.charAt(i));
            if (tempPinyin == null){
                // 如果str.charAt(i)非汉字，则保持原样
                sb.append(str.charAt(i));
            }else{
                String py = firstCharUpperCase ? firstCharUpperCase(tempPinyin[0]) : tempPinyin[0];
                sb.append(separator+py);
            }
        }
        return sb.toString();
    }

    /**
     * @Description 转换一个字符串为简拼
     * @param str
     * @return
     * @author WangMi
     * @time 2015-1-7 上午11:47:51
     */
    public static String getSimplePinYin(String str) {
        if(null == str) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String[] tempPinyin = null;
        for (int i = 0; i < str.length(); ++i){
            tempPinyin = getCharacterPinYin(str.charAt(i));
            if (tempPinyin == null){
                // 如果str.charAt(i)非汉字，则保持原样
                sb.append(str.charAt(i));
            }else{
                sb.append(tempPinyin[0].substring(0, 1));
            }
        }
        return sb.toString();
    }

    /**
     * <b>首字母大写</b><br>
     * 如firstCharUpperCase("abc")="Abc"<br>
     * 如firstCharUpperCase("aBc")="ABc"<br>
     * 如firstCharUpperCase("abc123")="Abc123"<br>
     * @param s String
     * @return String
     */
    public static String firstCharUpperCase(String s)
    {
        if (s == null || "".equals(s)) {
            return ("");
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
