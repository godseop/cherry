package org.godseop.cherry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Alias("user")
@Getter @Setter
@NoArgsConstructor
@ToString
public class User {

    private String userId;

    private String userName;

    private String password;

    private String mobile;

    private String memo;

    private String smsYn;

}
