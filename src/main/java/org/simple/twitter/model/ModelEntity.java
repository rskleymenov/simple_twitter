package org.simple.twitter.model;

import java.io.Serializable;

public interface ModelEntity<ID> extends Serializable{
    ID getId();
}
