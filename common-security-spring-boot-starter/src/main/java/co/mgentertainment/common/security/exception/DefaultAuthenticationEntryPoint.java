package co.mgentertainment.common.security.exception;

import co.mgentertainment.common.model.R;
import co.mgentertainment.common.model.exception.ErrorCodeEnum;
import co.mgentertainment.common.utils.GsonFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author larry
 * @createTime 17/08/2023
 * @description DefaultAuthenticationEntryPoint
 */
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ErrorCodeEnum errorCode = parseErrCodeByAuthenticationException(authException);
        response.getWriter().println(GsonFactory.getGson().toJson(R.failed(errorCode, authException.getMessage())));
        response.getWriter().flush();
    }

    private ErrorCodeEnum parseErrCodeByAuthenticationException(AuthenticationException authException) {
        if (authException instanceof InsufficientAuthenticationException) {
            return ErrorCodeEnum.INVALID_TOKEN;
        } else if (authException instanceof BadCredentialsException) {
            return ErrorCodeEnum.INVALID_TOKEN;
        } else if (authException instanceof CredentialsExpiredException) {
            return ErrorCodeEnum.INVALID_TOKEN;
        } else {
            return ErrorCodeEnum.UNAUTHORIZED;
        }
    }
}