package io.security.corespringsecurity.security.voter;

import io.security.corespringsecurity.service.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;
import java.util.List;

public class IpAddressVoter implements AccessDecisionVoter<Object> {

    private SecurityResourceService securityResourceService;

    public IpAddressVoter(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {

        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = details.getRemoteAddress(); // IP 주소

        List<String> accessIpList = securityResourceService.getAccessIpList();

        int result = ACCESS_DENIED;

        for (String ipAddress : accessIpList) {
            if(remoteAddress.equals(ipAddress)){
                return ACCESS_ABSTAIN; // 허용하면 즉시 인가처리 끝나기 때문에 보류를 줘야한다.
            }
        }

        if(result == ACCESS_DENIED){
            throw new AccessDeniedException("Invalid IpAddress");
        }

        return result;
    }
}
