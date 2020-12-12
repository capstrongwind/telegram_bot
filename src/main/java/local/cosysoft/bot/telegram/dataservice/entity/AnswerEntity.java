package local.cosysoft.bot.telegram.dataservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "answers")
@Data
@EqualsAndHashCode(callSuper = true)
public class AnswerEntity extends BaseUUIDEntity {
    private String content;
    private String questionId;
}
