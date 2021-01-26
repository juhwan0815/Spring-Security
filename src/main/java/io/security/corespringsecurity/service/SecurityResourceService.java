package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.entity.Resources;
import io.security.corespringsecurity.repository.AccessIpRepository;
import io.security.corespringsecurity.repository.ResourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityResourceService {

    private ResourcesRepository resourcesRepository;

    private AccessIpRepository accessIpRepository;

    public SecurityResourceService(ResourcesRepository resourcesRepository,
                                   AccessIpRepository accessIpRepository) {
        this.resourcesRepository = resourcesRepository;
        this.accessIpRepository = accessIpRepository;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAllResources();
        resourcesList.forEach(resources -> {
            // 리소스 하나당 권한 정보 리스트를 갖는다.
            List<ConfigAttribute> configAttributeList = new ArrayList<>();
            // 권한 정보를 계속 담는다.
            resources.getRoleSet().forEach(role -> {
                configAttributeList.add(new SecurityConfig(role.getRoleName()));
                result.put(new AntPathRequestMatcher(resources.getResourceName()), configAttributeList);
            });
        });
        return result;
    }

    public List<String> getAccessIpList() {
        List<String> accessIpList = accessIpRepository.findAll()
                .stream()
                .map(accessIp -> accessIp.getIpAddress())
                .collect(Collectors.toList());
        return accessIpList;
    }
}
