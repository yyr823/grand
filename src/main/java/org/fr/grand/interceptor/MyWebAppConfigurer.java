/**
 * @ClassName：MyWebAppConfigurer
 * @Author：yyr
 * @Date：2019-04-28 14:58
 * @Description：拦截器配置类.
 */
package org.fr.grand.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {

    @Autowired
    MyInterceptor myInterceptor1;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //选择过滤的url请求
        registry.addInterceptor(myInterceptor1).addPathPatterns("/**");
        //registry.addInterceptor(new LoginInterceptor());
    }


}

