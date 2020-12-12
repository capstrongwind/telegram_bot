package local.cosysoft.bot.telegram.dataservice.controller.response;

import java.util.Collection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerResponse {
    private String pollId;
    private String userId;
    private Collection<QuestionToAnswerResponse> questionToAnswerResponses;
}
