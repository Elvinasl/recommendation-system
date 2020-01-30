package recommendator.models.entities.shared;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This class can be added to each entity requiring weight. Weight defines how strong a entity should be
 * weighted in the algorithm (predicting recommendations).
 */
@Data
@MappedSuperclass
public abstract class Weight {
    @Column(nullable = false)
    @Min(1)
    @Max(100)
    @NotNull
    private int weight = 50;
}
