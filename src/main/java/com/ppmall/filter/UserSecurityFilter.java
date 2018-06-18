package com.ppmall.filter;

import com.ppmall.common.Const;
import com.ppmall.common.ResponseCode;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;
import com.ppmall.service.IUserService;
import com.ppmall.service.impl.UserServiceImpl;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class UserSecurityFilter implements Filter {

    private IUserService iUserService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.iUserService = new UserServiceImpl();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        /**
         * 設置返回格式
         */
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");

        HttpSession httpSession = httpServletRequest.getSession();

        String requestUrl = httpServletRequest.getRequestURI();
        String resultString = ServerResponse.createErrorStatus(ResponseCode.NOT_LOGIN.getCode(), "未登录").toString();

        User currentUser = (User) httpSession.getAttribute(Const.CURRENT_USER);
        /**
         * 包含login为登陆url不过滤，不包含login,register將进入以下
         */
        if (!requestUrl.contains("login") && !requestUrl.contains("register") && !requestUrl.contains("alipay_callback.do") && !requestUrl.contains("check_valid") && !requestUrl.contains("kill")) {
            if (currentUser == null) {
                httpServletResponse.getWriter().print(resultString);
            } else {
                /**
                 * 已经登陆，转发请求
                 */
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        } else if (requestUrl.contains("manage") && currentUser != null) {
            /**
             * 如果url包含manage即为后台管理，需要验证管理员权限
             */
            ServerResponse responseUser = iUserService.checkAdmin(currentUser);
            if (!responseUser.isSuccess()) {
                resultString = ServerResponse.createErrorMessage("需要管理员权限").toString();
                httpServletResponse.getWriter().print(resultString);
            } else {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }


    }

    @Override
    public void destroy() {

    }
}
