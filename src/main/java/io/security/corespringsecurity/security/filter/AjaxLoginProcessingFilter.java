package io.security.corespringsecurity.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.security.corespringsecurity.domain.dto.AccountDto;
import io.security.corespringsecurity.security.token.AjaxAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    // 여기에 설정한 정보와 URL이 매칭이 되면 필터가 작동
    public AjaxLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        if(!isAjax(request)){
            throw new IllegalStateException("Authentication is not supported");
        }

        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);

        if(StringUtils.isEmpty(accountDto.getUsername()) || StringUtils.isEmpty(accountDto.getPassword())){
            throw new IllegalArgumentException("Username or Passoword is empty");
        }

        AjaxAuthenticationToken ajaxAuthenticationToken =
                new AjaxAuthenticationToken(accountDto.getUsername(),accountDto.getPassword());

        return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
    }

    // Ajax 방식인지 확인
    private boolean isAjax(HttpServletRequest request) {
        // 이렇게 약속했다고 가정!
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            return true;
        }

        return false;
    }
}
