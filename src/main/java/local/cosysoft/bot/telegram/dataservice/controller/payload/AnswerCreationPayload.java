package local.cosysoft.bot.telegram.dataservice.controller.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerCreationPayload {
    private String content;
    private String pollId;
    private String questionId;
    private String userTelegramId;
}
