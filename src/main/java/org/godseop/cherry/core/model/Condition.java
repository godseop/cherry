package org.godseop.cherry.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Condition implements Serializable {

    private String userId;

    private String password;

}
