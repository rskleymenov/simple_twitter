package org.simple.twitter.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {

    private String id;
    private String username;
    private String password;
    private boolean enabled;
    private Timestamp creationDate;
}
