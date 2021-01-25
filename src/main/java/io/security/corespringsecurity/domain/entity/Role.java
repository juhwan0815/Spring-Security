package io.security.corespringsecurity.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
//@Table(name = "ROLE")
@Builder
@Getter
@Setter
@ToString(exclude = {"user","resourceSet"})
@NoArgsConstructor()
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDesc;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "roleSet")
    private Set<Resources> resourcesSet = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "userRoles")
    private Set<Account> accounts = new HashSet<>();
}
