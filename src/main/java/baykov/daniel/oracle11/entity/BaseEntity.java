package baykov.daniel.oracle11.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entity_sequence")
    @SequenceGenerator(name = "entity_sequence", sequenceName = "your_sequence_name", allocationSize = 1)
    private Long id;
}
