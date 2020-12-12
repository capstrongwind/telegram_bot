package local.cosysoft.bot.telegram.dataservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseUUIDEntity implements DbEntity {

    @Id
    @Type(type="pg-uuid")
    @Column(name = "id", updatable = false, nullable = false)
    @JsonIgnore
    private UUID id;
}
