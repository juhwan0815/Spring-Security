package io.security.corespringsecurity.domain.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "ACCESS_IP")
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessIp {

    @Id
    @GeneratedValue
    @Column(name = "IP_ID",unique = true,nullable = false)
    private Long id;

    @Column(name = "IP_ADDRESS",nullable = false)
    private String ipAddress;
}
