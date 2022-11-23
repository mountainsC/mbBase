package com.cloud.core.utils;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cloud.core.R;
import com.cloud.core.configs.BaseCConfig;
import com.cloud.core.configs.RxCoreConfigItems;
import com.cloud.core.enums.RuleParams;
import com.cloud.core.logger.Logger;
import com.cloud.core.ui.WirelessPromptActivity;

import java.io.File;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/20
 * @Description: 公用配置类
 * @Modifier:
 * @ModifyContent:
 */
public class BaseCommonUtils {

    private static HandlerUtils handlerUtils = new HandlerUtils();

    /**
     * 获取图片url格式(中间imgurl用%s代替)
     *
     * @param baseUrl
     * @param imgUrl
     * @param suffix  图片后缀
     * @return 如:baseurl/imgurl+规则
     */
    public static String getImgUrlFormat(String baseUrl, String imgUrl, String suffix) {
        if (TextUtils.isEmpty(imgUrl)) {
            return "";
        }
        if (ValidUtils.valid(RuleParams.Url.getValue(), imgUrl)) {
            return String.format("%s%s", imgUrl, suffix);
        } else {
            return String.format("%s%s", PathsUtils.combine(baseUrl, imgUrl), suffix);
        }
    }

    public static ViewGroup getRootView(Window win) {
        View rootView = win.getDecorView().findViewById(android.R.id.content);
        ViewGroup vg = (ViewGroup) rootView;
        return vg;
    }

    public static void sendNetworkStateBroadcast(Context context) {
        RxCoreConfigItems configItems = BaseCConfig.getInstance().getConfigItems(context);
        RedirectUtils.sendBroadcast(context, configItems.getReceiveNetworkAction(), null);
    }

    public static void hideNetworkStateView(Window win) {
        ViewGroup rootview = getRootView(win);
        if (rootview != null) {
            View networkstateview = rootview
                    .findViewById(R.id.network_state_rl);
            if (networkstateview != null) {
                networkstateview.setVisibility(View.GONE);
            }
        }
    }

    public static void showNetworkStateView(final Activity activity) {
        if (activity == null) {
            return;
        }
        ViewGroup rootview = getRootView(activity.getWindow());
        if (rootview != null) {
            View networkstateview = rootview
                    .findViewById(R.id.network_state_rl);
            if (networkstateview == null) {
                RelativeLayout.LayoutParams rlparam = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlparam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                View nsview = View.inflate(activity,
                        R.layout.network_state_view, null);
                networkstateview = nsview.findViewById(R.id.network_state_rl);
                networkstateview.setVisibility(View.VISIBLE);
                networkstateview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RedirectUtils.startActivity(activity, WirelessPromptActivity.class);
                    }
                });
                rootview.addView(nsview, rlparam);
            } else {
                networkstateview.setVisibility(View.VISIBLE);
            }
        }
    }

    public static View getMapPopouView(Context context, Bundle extraInfo) {
        TextView tv = (TextView) View.inflate(context,
                R.layout.location_pop_view, null);
        tv.setBackgroundResource(extraInfo.getInt("BACKGROUND_KEY"));
        tv.setText(extraInfo.getString("CONTENT_KEY"));
        return tv;
    }

    /**
     * 绑定Fragment至FragmentLayout容器
     *
     * @param fragmentManager fragment管理器
     * @param containerViewId 容器id
     * @param fragment        添加的fragment对象
     */
    public static void bindFrameLayout(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 绑定Fragment至FragmentLayout容器
     *
     * @param fragmentActivity fragment管理器
     * @param containerViewId  容器id
     * @param fragment         添加的fragment对象
     */
    public static void bindFrameLayout(FragmentActivity fragmentActivity, int containerViewId, Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
            bindFrameLayout(fragmentManager, containerViewId, fragment);
        }
    }

    public static void downApp(Context context, String url, String title) {
        String suffix = GlobalUtils.getSuffixName(url);
        if (ValidUtils.valid(RuleParams.Url.getValue(), url) && TextUtils.equals(suffix, "apk")) {
            //获取显示标题
            if (!TextUtils.isEmpty(title)) {
                String name = FilenameUtils.getName(url);
                title = name == null ? "" : name;
            }
            //下载
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setVisibleInDownloadsUi(true);
            File dir = StorageUtils.getApksDir();
            File downfile = new File(dir, String.format("%s.apk", GlobalUtils.getGuidNoConnect()));
            request.setDestinationUri(Uri.fromFile(downfile));
            request.setTitle(title);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setAllowedOverRoaming(false);
            request.setVisibleInDownloadsUi(true);
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            request.setMimeType(mimeTypeMap.getMimeTypeFromExtension(url));
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
            ToastUtils.showLong(context, R.string.downloading_just);
        } else {
            ToastUtils.showLong(context, R.string.down_address_novalid);
        }
    }

    /**
     * 加载静态库文件是否成功
     *
     * @param libName 静态库名称
     * @return 是否加载成功
     */
    public static boolean loadLibrary(String libName) {
        try {
            try {
                System.loadLibrary(libName);
                return true;
            } catch (Exception e) {
                System.loadLibrary(libName);
            }
            return true;
        } catch (Exception e) {
            Logger.L.error(e);
        }
        return false;
    }

    public static void post(Runnable runnable) {
        if (handlerUtils != null) {
            handlerUtils.post(runnable);
        }
    }

    public static void postDelayed(Runnable runnable, long delayMillis) {
        if (handlerUtils != null) {
            handlerUtils.postDelayed(runnable, delayMillis);
        }
    }

    public void removeMessages(int what, Runnable runnable) {
        if (handlerUtils == null) {
            return;
        }
        handlerUtils.removeMessages(what, runnable);
    }

    public void removeMessages(int what) {
        if (handlerUtils == null) {
            return;
        }
        handlerUtils.removeMessages(what);
    }

    public void removeMessages(Runnable runnable) {
        if (handlerUtils == null) {
            return;
        }
        handlerUtils.removeMessages(runnable);
    }
}
