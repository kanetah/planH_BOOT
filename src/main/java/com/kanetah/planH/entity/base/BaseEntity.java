package com.kanetah.planH.entity.base;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.neo4j.ogm.annotation.GraphId;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BaseEntity {

    @GraphId
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id==null?super.hashCode():id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof BaseEntity && ((BaseEntity)other).id.equals(this.id);
    }
}
