package org.simple.twitter.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User implements ModelEntity<Long> {
    Long id;
    String login;
    String password = "";
    Boolean enabled = true;    
}
