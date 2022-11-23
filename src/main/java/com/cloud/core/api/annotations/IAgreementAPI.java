package com.cloud.core.api.annotations;

import com.cloud.core.annotations.BaseUrlTypeName;
import com.cloud.core.annotations.DataParam;
import com.cloud.core.annotations.GET;
import com.cloud.core.annotations.POST;
import com.cloud.core.annotations.Param;
import com.cloud.core.annotations.Path;
import com.cloud.core.beans.BaseDataBean;
import com.cloud.core.beans.RedirectionConfigItem;
import com.cloud.core.beans.RetrofitParams;
import com.cloud.core.constants.ServiceAPI;
import com.cloud.core.enums.RequestContentType;

import java.io.File;
import java.io.FileInputStream;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/7
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
@BaseUrlTypeName(value = ServiceAPI.Normal, contentType = RequestContentType.Json)
public interface IAgreementAPI {

    @GET(value = "/common/getAppRedirectionConfigByKey")
    @DataParam(RedirectionConfigItem.class)
    RetrofitParams requestRedirectionConfigByKV(
            @Param("key") String key,
            @Param("versionCode") int versionCode,
            @Param("group") String group
    );
    /**
     * 上传图片
     */
    @POST(value = "/aliFileUpload/imgFileUpload/{type}")
    @DataParam(BaseDataBean.class)
    RetrofitParams requestImgFileUpload(
            @Path("type") String type,
            @Param("file") File file
    );
    /**
     * 上传用户补充资料图片
     */
    @POST(value = "/users/idCardUpload/{type}",isPrintApiLog = true)
    @DataParam(BaseDataBean.class)
    RetrofitParams requestIdImgFileUpload(
            @Path("type") String type,
            @Param("file") File file
    );
    /**
     * 上传用户补充资料图片
     */
    @POST(value = "/users/idCardUpload/{type}",isPrintApiLog = true)
    @DataParam(BaseDataBean.class)
    RetrofitParams requestVideoFileUpload(
            @Path("type") String type,
            @Param("file") byte[] videoFile
    );
}
