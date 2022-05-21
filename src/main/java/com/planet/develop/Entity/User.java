package com.planet.develop.Entity;

import com.planet.develop.Security.Enum.MemberRole;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    public void addMemberRole(MemberRole memberRole) {
        roleSet.add(memberRole);
    }

}
