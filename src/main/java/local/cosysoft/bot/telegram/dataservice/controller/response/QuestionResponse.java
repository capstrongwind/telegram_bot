package local.cosysoft.bot.telegram.dataservice.controller.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionResponse {
    private String id;
    private String content;
    private List<BasicAnswerResponse> answers = new ArrayList<>();
}
