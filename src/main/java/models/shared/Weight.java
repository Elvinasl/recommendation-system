package models.shared;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
public abstract class Weight {
    @Column(nullable = false)
    @Min(1)
    @Max(100)
    @NotNull
    private int weight = 50;
}
