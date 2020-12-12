package local.cosysoft.bot.telegram.dataservice.controller.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AllAnswerResponse {
    private String pollId;
    private List<QuestionToAnswerResponseWithUserResponse> questionToAnswerResponses = new ArrayList<>();
}
