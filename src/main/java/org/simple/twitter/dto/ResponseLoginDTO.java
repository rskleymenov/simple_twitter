package org.simple.twitter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseLoginDTO {
    private Integer loginedUserId;
    private String username;
    private Boolean enabled;
}
