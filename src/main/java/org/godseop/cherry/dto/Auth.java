package org.godseop.cherry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;

@Alias("user")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Auth implements GrantedAuthority {

    private String userId;

    private String roleName;

    @Override
    public String getAuthority() {
        return this.roleName;
    }
}
