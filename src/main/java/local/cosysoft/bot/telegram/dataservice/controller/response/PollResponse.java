package local.cosysoft.bot.telegram.dataservice.controller.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PollResponse {
    private String id;
    private String name;
    private String status;
    private List<QuestionResponse> questions = new ArrayList<>();
}
