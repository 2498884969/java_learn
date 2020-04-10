package com.imooc.controller.intercepter;

import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class UserTokenInterceptor implements HandlerInterceptor {

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    @Autowired
    RedisOperator redisOperator;

    public static String getRedisUserToken() {
        return REDIS_USER_TOKEN;
    }

    public RedisOperator getRedisOperator() {
        return redisOperator;
    }

    public void setRedisOperator(RedisOperator redisOperator) {
        this.redisOperator = redisOperator;
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @return  false 请求被拦截， true放行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 从headers中获取用户id和token headerUserId headerUserToken
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");
        String key = REDIS_USER_TOKEN + ":" + userId;
        // 2. 对于用户id和token进行非空判定
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {
            // 3. 从redis中获取用户token
            String uniqueToken = redisOperator.get(key);
            if (StringUtils.isBlank(uniqueToken)) {
                // 4. token为空，请登录
                returnErrorResponse(response,IMOOCJSONResult.errorMsg("请登录"));
                return false;
            } else {
                // 5. token不为空但不相等 账号在别处登录
                if (!uniqueToken.equals(userToken)) {
                    returnErrorResponse(response, IMOOCJSONResult.errorMsg("账号已经在别处登录"));
                    return false;
                }
            }

        } else {
            // 6. 用户id或者token为空----请登录
            returnErrorResponse(response,IMOOCJSONResult.errorMsg("请登录"));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


    public void returnErrorResponse(HttpServletResponse response,
                                    IMOOCJSONResult result) {
        // OutputStream out res.charset content-type out out.write out.flush
        OutputStream out = null;

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");

        try {
            out = response.getOutputStream();
            out.write(Objects.requireNonNull(JsonUtils.objectToJson(result)).getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
