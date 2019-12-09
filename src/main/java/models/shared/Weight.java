package models.shared;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class Weight {
    @Column(nullable = false)
    @Size(min = 1, max = 100)
    @NotNull
    private int weight = 50;
}
