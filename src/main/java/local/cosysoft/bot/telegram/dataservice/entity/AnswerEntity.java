package local.cosysoft.bot.telegram.dataservice.entity;

import java.time.LocalDateTime;
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
    private String pollId;
    private String questionId;
    private LocalDateTime createDate;
}
