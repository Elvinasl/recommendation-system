package recommendator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This class simplified the return of a list. We don't want to return an array, but an object.
 *
 * @param <T> list of T object is gonna be returned
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReturnObjectDTO<T> {

    private List<T> objects;
}
