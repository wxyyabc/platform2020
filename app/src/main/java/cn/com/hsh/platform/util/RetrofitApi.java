package cn.com.hsh.platform.util;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.com.hsh.platform.model.HrUser;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApi {

    // 注解里传入 网络请求 的部分URL地址
    // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里

    /**
     * hr登录验证
     * */
    @GET("api/hrcontroller/checkuser")
    Call<HrUser> hrLogin(@Query("appkey")String appkey,@Query("usercode")String usercode, @Query("password")String password);

    /**
     * k3 cloud 登录验证
     * @return
     */
    @POST("K3Cloud/Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc")
    Call<JSONObject> k3Login(@Query("acctID") String acctID,@Query("username") String username,@Query("password") String password,@Query("lcid") int lcid);

}
