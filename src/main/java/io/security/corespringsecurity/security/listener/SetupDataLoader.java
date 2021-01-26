package io.security.corespringsecurity.security.listener;

import io.security.corespringsecurity.domain.entity.*;
import io.security.corespringsecurity.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourcesRepository resourcesRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccessIpRepository accessIpRepository;

    @Autowired
    private RoleHierarchyRepository roleHierarchyRepository;

    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(alreadySetup){
            return;
        }

        setupSecurityResources();
        setupAccessIpData();

        alreadySetup = true;
    }

    @Transactional
    public void setupSecurityResources() {
        Set<Role> roles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN","관리자");
        roles.add(adminRole);
        createResourceIfNotFound("/admin/**","",roles,"url");
        Account account = createUserIfNotFound("admin","pass","admin@gmail.com",10,roles);
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저권한");
        Role userRole = createRoleIfNotFound("ROLE_USER", "사용자권한");
        createRoleHierarchyIfNotFound(managerRole, adminRole);
        createRoleHierarchyIfNotFound(userRole, managerRole);

    }


    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc) {

        Role role = roleRepository.findByRoleName(roleName);

        if(role == null){
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .build();
        }

        return roleRepository.save(role);
    }

    @Transactional
    public Account createUserIfNotFound(String username,
                                         String password,
                                         String email,
                                         int age,
                                         Set<Role> roleSet) {
        Account account = userRepository.findByUsername(username);

        if(account == null){
            account = Account.builder()
                    .username(username)
                    .email(email)
                    .age(age)
                    .password(passwordEncoder.encode(password))
                    .userRoles(roleSet)
                    .build();
        }
        return userRepository.save(account);
    }

    @Transactional
    public Resources createResourceIfNotFound(String resourceName,
                                              String httpMethod,
                                              Set<Role> roleSet,
                                              String resourceType) {

        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if(resources == null){
            resources = Resources.builder()
                    .resourceName(resourceName)
                    .roleSet(roleSet)
                    .httpMethod(httpMethod)
                    .resourceType(resourceType)
                    .orderNum(count.incrementAndGet())
                    .build();
        }

        return resourcesRepository.save(resources);
    }

    @Transactional
    public void createRoleHierarchyIfNotFound(Role childRole,Role parentRole){

        RoleHierarchy roleHierarchy = roleHierarchyRepository.findByChildName(parentRole.getRoleName());
        if( roleHierarchy == null){
            roleHierarchy = RoleHierarchy.builder()
                    .childName(parentRole.getRoleName())
                    .build();
        }

        RoleHierarchy parentRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

        RoleHierarchy roleHierarchy2 = roleHierarchyRepository.findByChildName(childRole.getRoleName());

        if(roleHierarchy2 == null){
            roleHierarchy2 = RoleHierarchy.builder()
                    .childName(childRole.getRoleName())
                    .build();
        }

        RoleHierarchy childRoleHierarchy = roleHierarchyRepository.save(roleHierarchy2);
        childRoleHierarchy.setParentName(parentRoleHierarchy);
        roleHierarchyRepository.save(childRoleHierarchy);
    }

    private void setupAccessIpData(){
        AccessIp byIpAddress = accessIpRepository.findByIpAddress("0:0:0:0:0:0:0:1");

        if (byIpAddress == null){
            AccessIp accessIp = AccessIp.builder()
                    .ipAddress("0:0:0:0:0:0:0:1")
                    .build();
            accessIpRepository.save(accessIp);
        }
    }



}
