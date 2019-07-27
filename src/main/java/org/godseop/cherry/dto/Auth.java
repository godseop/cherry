package org.godseop.cherry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;

@Alias("auth")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Auth implements GrantedAuthority {

    private String authCode;

    private String authName;

    private String authDescription;

    @Override
    public String getAuthority() {
        return this.authCode;
    }
}
