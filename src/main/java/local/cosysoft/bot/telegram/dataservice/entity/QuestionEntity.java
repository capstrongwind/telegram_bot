package local.cosysoft.bot.telegram.dataservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "questions")
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionEntity extends BaseUUIDEntity {
    private String content;
    private String pollId;
}
