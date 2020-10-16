package cn.pertech.healthwalk.spring.resolver;

import cn.pertech.healthwalk.base.data.UserSession;
import cn.pertech.healthwalk.spring.annotation.LoginedAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * @Date 2020/6/24 16:38
 * @Author mawkun
 */
@Slf4j
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 判断是否需要解析的参数类型
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("---------------开始参数解析");
        Annotation[] annotions = parameter.getParameterAnnotations();
        boolean flag = false;
        for(Annotation annotation : annotions) {
            if(LoginedAuth.class.isInstance(annotation)) {
                flag = true;
            }
        }
        if(!flag) return false;
        Class<?> cla = parameter.getParameterType();
        if(cla.isAssignableFrom(UserSession.class)) return true;
        return false;
    }

    /**
     * 真正的解析方法
     * @param parameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        log.info("---------------参数解析中");
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        Class<?> cla = parameter.getParameterType();
        if(cla.isAssignableFrom(UserSession.class)) {
            Object obj = request.getAttribute("session");
            ObjectMapper objectMapper = new ObjectMapper();
            UserSession session = objectMapper.convertValue(obj, UserSession.class);
            if(session != null) return session;
        }
        return null;
    }
}
