package cn.com.hsh.platform.util;

import java.util.List;

import cn.com.hsh.platform.model.HrUser;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {

    // 注解里传入 网络请求 的部分URL地址
    // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里

    /**
     * hr登录验证
     * */
    @GET("api/hrcontroller/checkuser")
    Call<HrUser> hrLogin(@Query("appkey")String appkey,@Query("usercode")String usercode, @Query("password")String password);

}
