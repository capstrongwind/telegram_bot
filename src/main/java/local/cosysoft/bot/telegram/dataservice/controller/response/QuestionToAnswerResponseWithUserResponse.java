package local.cosysoft.bot.telegram.dataservice.controller.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QuestionToAnswerResponseWithUserResponse extends QuestionToAnswerResponse {
    private String userId;
}
