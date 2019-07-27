package org.godseop.cherry.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Alias("authDetail")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuthDetail {

    private String authCode;

    private String serviceCode;

    private String serviceName;

    private String serviceType;

    private String selectYn;

    private String insertYn;

    private String updateYn;

    private String deleteYn;

    private String excelYn;
}
