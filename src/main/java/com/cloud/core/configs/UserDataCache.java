package com.cloud.core.configs;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.beans.user.UserCacheInfo;
import com.cloud.core.cache.RxCache;
import com.cloud.core.encrypts.DESEncrypt;
import com.cloud.core.enums.EncryptType;
import com.cloud.core.enums.EncryptVersion;
import com.cloud.core.events.Action1;
import com.cloud.core.logger.Logger;
import com.cloud.core.rzzbbr;
import com.cloud.core.utils.JsonUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/7/1
 * @Description:用户数据缓存(在主线程中调用)
 * @Modifier:
 * @ModifyContent:
 */
public class UserDataCache {

    private static UserDataCache userDataCache = null;
    private static UserCacheInfo userCacheInfo = null;
    private String USER_CACHE_INFO_KEY = "4c829e023c034c5093a66dba4a549fb7";

    public static UserDataCache getDefault() {
        if (userDataCache == null) {
            userDataCache = new UserDataCache();
        }
        return userDataCache;
    }

    public UserCacheInfo getCacheUserInfo(Context context) {
        try {
            if (isEmptyCache(context, userCacheInfo)) {
                String cacheData = RxCache.getCacheData(context, USER_CACHE_INFO_KEY);
                if (!TextUtils.isEmpty(cacheData)) {
                    String result = parsingEncrypt(cacheData);
                    userCacheInfo = JsonUtils.parseT(result, UserCacheInfo.class);
                }
            }
        } catch (Exception e) {
            Logger.L.error(e);
        } finally {
            if (userCacheInfo == null) {
                userCacheInfo = new UserCacheInfo();
            }
        }
        return userCacheInfo;
    }

    /**
     * 判断登录信息是否为空
     *
     * @param context       上下文
     * @param cacheUserInfo 用户缓存信息
     * @return
     */
    public boolean isEmptyCache(Context context, UserCacheInfo cacheUserInfo) {
        if (cacheUserInfo == null || TextUtils.isEmpty(cacheUserInfo.getToken())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断登录信息是否为空
     *
     * @param context 上下文
     * @return
     */
    public boolean isEmptyCache(Context context) {
        UserCacheInfo cacheUserInfo = getCacheUserInfo(context);
        return isEmptyCache(context, cacheUserInfo);
    }

    /**
     * 设置缓存信息
     *
     * @param context   上下文
     * @param cacheInfo 缓存信息
     */
    public void setCacheUserInfo(Context context, final UserCacheInfo cacheInfo) {
        String cachejson = JsonUtils.toStr(cacheInfo);
        String enstr = DESEncrypt.encode(rzzbbr.DES_KV, cachejson);
        enstr = String.format("rx%s%s%s", EncryptType.DES.getValue(), EncryptVersion.V1.getValue(), enstr);
        RxCache.setCacheData(context, USER_CACHE_INFO_KEY, enstr);
    }

    /**
     * 清空缓存信息
     *
     * @param context 上下文
     * @param action  可以在回调中清空其它缓存信息
     */
    public void clearCacheUserInfo(Context context, Action1<UserCacheInfo> action) {
        UserCacheInfo userCacheInfo = getCacheUserInfo(context);
        userCacheInfo.setIsBind(false);
        userCacheInfo.setLitpic("");
        userCacheInfo.setNickname("");
        userCacheInfo.setRealName("");
        userCacheInfo.setToken("");
        userCacheInfo.setUserId(0);
        userCacheInfo.setUsername("");
        userCacheInfo.setBirthday(0);
        userCacheInfo.setGender("");
        userCacheInfo.setPersonalIntro("");
        userCacheInfo.setVipStatus(0);
        userCacheInfo.setUserPlaceName("");
        userCacheInfo.setAccount("");
        if (action != null) {
            action.call(userCacheInfo);
        }
        this.userCacheInfo = userCacheInfo;
        setCacheUserInfo(context, userCacheInfo);
    }

    /**
     * 缓存帐号信息
     *
     * @param context 上下文
     * @param account 登录账号
     */
    public void setCacheAccount(Context context, String account) {
        UserCacheInfo cacheUserInfo = getCacheUserInfo(context);
        cacheUserInfo.setAccount(account);
        setCacheUserInfo(context, cacheUserInfo);
    }

    private static String parsingEncrypt(String entext) {
        int prefixlen = rzzbbr.EN_PREFIX.length();
        if (entext.length() > prefixlen) {
            String tempentext = entext;
            tempentext = tempentext.substring(prefixlen);
            //排除类型和版本标识
            if (tempentext.length() > 12) {
                String typestr = tempentext.substring(0, 6);
                String vstr = tempentext.substring(6, 12);
                tempentext = tempentext.substring(12);
                List<String> entypes = Arrays.asList(new String[]{EncryptType.DES.getValue()});
                if (entypes.contains(typestr)) {
                    List<String> versions = Arrays.asList(new String[]{EncryptVersion.V1.getValue()});
                    if (versions.contains(vstr)) {
                        if (TextUtils.equals(typestr, EncryptType.DES.getValue())) {
                            if (TextUtils.equals(vstr, EncryptVersion.V1.getValue())) {
                                return DESEncrypt.decode(rzzbbr.DES_KV, tempentext);
                            }
                        }
                    }
                }
            }
        }
        return "";
    }
}
