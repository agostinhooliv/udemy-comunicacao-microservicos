package br.com.curso.udemy.product.api.config.interceptor;

import br.com.curso.udemy.product.api.config.exception.ValidationException;
import com.sun.net.httpserver.HttpServer;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION = "Authorization";

    @Override
    public void apply(RequestTemplate template) {

        var currentRequest = getCurrentRequest();
        template
                .header(AUTHORIZATION, currentRequest.getHeader(AUTHORIZATION));
    }

    private HttpServletRequest getCurrentRequest(){
        try {
            return ((ServletRequestAttributes)RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();

        } catch (Exception ex){
            log.error(ex.getMessage());
            throw new ValidationException("The current request could not be accessed.");
        }
    }
}
