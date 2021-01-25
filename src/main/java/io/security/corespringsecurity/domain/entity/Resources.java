package io.security.corespringsecurity.domain.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

@Entity
//@Table(name = "RESOURCES")
@Data
@ToString(exclude = {"roleSet"})
@Builder
//@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Resources {

    @Id
    @GeneratedValue
    @Column(name = "resource_id")
    private Long id;

    @Column(name = "resource_name")
    private String resourceName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_resources",
                joinColumns = @JoinColumn(name = "resource_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roleSet;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "order_num")
    private int orderNum;

}
