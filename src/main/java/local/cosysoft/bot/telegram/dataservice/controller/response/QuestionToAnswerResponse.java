package local.cosysoft.bot.telegram.dataservice.controller.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionToAnswerResponse {
    private String questionId;
    private String answerId;
}
