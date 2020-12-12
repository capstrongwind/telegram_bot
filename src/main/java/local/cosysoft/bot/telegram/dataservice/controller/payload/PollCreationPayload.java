package local.cosysoft.bot.telegram.dataservice.controller.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PollCreationPayload {
    private String pollName;
}
