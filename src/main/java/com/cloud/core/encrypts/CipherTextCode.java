package com.cloud.core.encrypts;

import android.text.TextUtils;

import com.cloud.core.logger.Logger;
import com.cloud.core.utils.ConvertUtils;

import java.util.Arrays;
import java.util.Random;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/10/17
 * Description:密文编码
 * Modifier:
 * ModifyContent:
 */
public class CipherTextCode {

    public static CipherHashKey getHashKey(String enkey) {
        int minNumCount = 8;
        int minCharCount = 8;
        Random random = new Random();
        CipherHashKey hashKey = new CipherHashKey();
        if (TextUtils.isEmpty(enkey)) {
            hashKey.setNums(getHashKey(minNumCount, hashKey.getNums(), random));
            hashKey.setChars(getHashKey(minCharCount, hashKey.getChars(), random));
        } else {
            String md5 = MD5Encrypt.md5(enkey).replace("-", "");
            String nums = "";
            String chars = "";
            for (int i = 0; i < md5.length(); i++) {
                if (Character.isDigit(md5.charAt(i))) {
                    if (nums.indexOf(md5.charAt(i)) < 0) {
                        nums += md5.charAt(i);
                    }
                } else {
                    if (chars.indexOf(md5.charAt(i)) < 0) {
                        chars += md5.charAt(i);
                    }
                }
            }
            hashKey.setNums(nums.toCharArray());
            hashKey.setChars(chars.toCharArray());
        }
        return hashKey;
    }

    private static char[] getHashKey(int count, char[] raw, Random random) {
        char[] nums2 = new char[count];
        for (int i = 0; i < count; i++) {
            int pos = random.nextInt(raw.length);
            nums2[i] = raw[pos];
        }
        return nums2;
    }

    /**
     * 获取加密key
     * 根据参考值第一位和最后一位ASCII码值相加的结果(R1)插入参考值中,R1长度
     * 与参考值取余的位置;
     *
     * @param refValue 参考值
     * @return
     */
    public static String getEnkey(String refValue) {
        if (TextUtils.isEmpty(refValue)) {
            return "";
        }
        refValue = refValue.trim();
        char first = refValue.charAt(0);
        char end = refValue.charAt(refValue.length() - 1);
        int firstAscii = (int) first;
        int endAscii = (int) end;
        String rvalue = String.valueOf(firstAscii + endAscii);
        int yu = rvalue.length() % refValue.length();
        return refValue.substring(0, yu) + rvalue + refValue.substring(yu);
    }

    /**
     * 密文编码
     *
     * @param text 密文
     *             return
     */
    public static String codingCiphertext(String enkey, String text) {
        try {
            if (TextUtils.isEmpty(text)) {
                return "";
            }
            CipherHashKey hashKey = getHashKey(enkey);
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (Character.isDigit(c)) {
                    int pos = Arrays.binarySearch(hashKey.getNums(), c);
                    if (pos < 0) {
                        sb.append(c);
                        sb.append("1" + hashKey.getCiv()[random.nextInt(hashKey.getCiv().length)]);
                    } else {
                        sb.append("0" + pos);
                        int index = random.nextInt(hashKey.getChars().length);
                        sb.append(hashKey.getChars()[index] + "0" + hashKey.getCiv()[random.nextInt(hashKey.getCiv().length)]);
                    }
                } else {
                    int pos = Arrays.binarySearch(hashKey.getChars(), c);
                    if (pos < 0) {
                        sb.append(c);
                        sb.append("1" + hashKey.getCiv()[random.nextInt(hashKey.getCiv().length)]);
                    } else {
                        if (pos < 10) {
                            sb.append("0" + pos);
                        } else {
                            sb.append(pos);
                        }
                        int index = random.nextInt(hashKey.getNums().length);
                        sb.append(hashKey.getNums()[index] + "0" + hashKey.getCiv()[random.nextInt(hashKey.getCiv().length)]);
                    }
                }
            }
            int rpos = random.nextInt(sb.length());
            return sb.substring(rpos) + sb.substring(0, rpos) + "▥▥" + rpos;
        } catch (Exception e) {
            Logger.L.error(e);
        }
        return "";
    }

    public static String decodingCiphertext(String enkey, String text) {
        try {
            if (TextUtils.isEmpty(text)) {
                return "";
            }
            String[] slist = text.split("▥▥");
            if (slist.length != 2) {
                return "";
            }
            if (TextUtils.isEmpty(enkey)) {
                enkey = slist.length > 2 ? slist[2] : "";
            }
            CipherHashKey hashKey = getHashKey(enkey);
            if (TextUtils.isEmpty(slist[0])) {
                return "";
            }
            int rpos = slist[0].length() - ConvertUtils.toInt(slist[1]);
            String result = slist[0].substring(rpos) + slist[0].substring(0, rpos);
            String splits = "";
            for (String splitItem : hashKey.getCiv()) {
                splits += String.format("|%s", splitItem);
            }
            if (splits.length() > 0) {
                splits = splits.substring(1);
            }
            String[] stringItems = result.split(splits);
            StringBuffer sb = new StringBuffer();
            for (String item : stringItems) {
                int placePos = ConvertUtils.toInt(item.substring(item.length() - 1));
                String m = item.substring(0, item.length() - 1);
                if (placePos == 1) {
                    sb.append(m);
                } else {
                    int pos = ConvertUtils.toInt(m.substring(0, 2));
                    if (Character.isDigit(m.charAt(2))) {
                        sb.append(hashKey.getChars()[pos]);
                    } else {
                        sb.append(hashKey.getNums()[pos]);
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            Logger.L.error(e);
        }
        return "";
    }
}
