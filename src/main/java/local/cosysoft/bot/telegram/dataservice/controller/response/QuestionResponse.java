package local.cosysoft.bot.telegram.dataservice.controller.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionResponse {
    private String id;
    private String content;
}
