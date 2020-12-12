package local.cosysoft.bot.telegram.dataservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "polls")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserHistoryEntity extends BaseUUIDEntity {
    private String name;
    private String pollId;

}
