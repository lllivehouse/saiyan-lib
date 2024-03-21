package co.saiyan.common.security.exception;

import co.saiyan.common.model.R;
import co.saiyan.common.model.exception.ErrorCodeEnum;
import co.saiyan.common.utils.GsonFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author larry
 * @createTime 17/08/2023
 * @description DefaultAccessDeniedHandler
 */
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter()
                .println(GsonFactory.getGson().toJson(R.failed(ErrorCodeEnum.FORBIDDEN, accessDeniedException.getMessage())));
        response.getWriter().flush();
    }
}