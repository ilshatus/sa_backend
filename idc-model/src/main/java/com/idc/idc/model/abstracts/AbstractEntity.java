package com.idc.idc.model.abstracts;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Comparator;

@Setter
@Getter
@MappedSuperclass
public abstract class AbstractEntity {
    public static final Comparator<AbstractEntity> ID_COMPARATOR = Comparator.comparing(AbstractEntity::getId);
    public static final String ID_COLUMN_NAME = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
    @Column(name = ID_COLUMN_NAME)
    @TableGenerator(name = "generator", allocationSize = 1)
    private Long id;
}
