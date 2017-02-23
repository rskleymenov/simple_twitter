package org.simple.twitter.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {

    @Id
    @Column(name = "user_id")
    private int id;

    @Column(name = "name")
    private String name;

}
