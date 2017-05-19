package com.sailing.demo.springcloud.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yunan on 2017/4/22.
 * jwt 验证过滤
 */
public class JWTFilter extends ZuulFilter {

    /**
     * filterType：返回一个字符串代表本过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
     * pre：可以在请求被路由之前调用
     * routing：在路由请求时候被调用
     * post：在routing和error过滤器之后被调用
     * error：处理请求时发生错误时被调用
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 定义过滤器的顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 过滤器开关量 控制过滤器是否执行
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器逻辑
     * @return
     */
    @Override
    public Object run() {
        // 获取请求上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 获取请求  HttpServlet 对象
        HttpServletRequest httpServletRequest = requestContext.getRequest();

        // // 模拟jwt 从头部获取jwt信息
        // String jwttoken = httpServletRequest.getHeader("jwt");
        // if(null==jwttoken){
        //     // 阻止调用
        //     requestContext.setSendZuulResponse(false);
        //     // 设定返回内容
        //     requestContext.setResponseBody("no jwt header");
        //     // 设定返回state
        //     requestContext.setResponseStatusCode(401);
        //     return null;
        // }
        return null;
    }
}
