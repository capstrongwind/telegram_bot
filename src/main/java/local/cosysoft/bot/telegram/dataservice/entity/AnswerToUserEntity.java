package local.cosysoft.bot.telegram.dataservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "answer_to_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class AnswerToUserEntity extends BaseUUIDEntity {
    private String answerId;
    private String userId;
}
