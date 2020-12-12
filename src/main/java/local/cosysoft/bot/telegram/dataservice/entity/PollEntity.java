package local.cosysoft.bot.telegram.dataservice.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "polls")
@Data
@EqualsAndHashCode(callSuper = true)
public class PollEntity extends BaseUUIDEntity {
    private String name;
    private LocalDateTime createDate;
    private String status;
    private Boolean isRunnable;
}
