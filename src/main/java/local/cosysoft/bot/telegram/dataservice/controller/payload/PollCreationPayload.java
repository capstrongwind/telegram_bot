package local.cosysoft.bot.telegram.dataservice.controller.payload;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PollCreationPayload {
    private String pollName;
    private List<QuestionCreationPayload> questions = new ArrayList<>();
}
