package local.cosysoft.bot.telegram.dataservice.controller;

import java.util.Collection;
import local.cosysoft.bot.telegram.dataservice.controller.payload.PollCreationPayload;
import local.cosysoft.bot.telegram.dataservice.entity.AnswerEntity;
import local.cosysoft.bot.telegram.dataservice.entity.PollEntity;
import local.cosysoft.bot.telegram.dataservice.service.TelegramDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telegram/data")
public class TelegramDataController {
    private final TelegramDataService telegramDataService;

    @GetMapping("/poll")
    public Collection<PollEntity> getPolls(@RequestParam(required = false) String id) {
        if (id == null) return telegramDataService.getPolls();
        return telegramDataService.getPollById(id);
    }

    @PostMapping("/answer")
    public String createAnswer(@RequestBody AnswerEntity answerEntity) {
        return telegramDataService.createAnswer(answerEntity);
    }

    @PostMapping("/poll")
    public String createPoll(@RequestBody PollCreationPayload payload) {
        return telegramDataService.createPoll(payload);
    }

    @GetMapping("/answer/last")
    public AnswerEntity getLastAnswer(@RequestParam String pullId) {
        return telegramDataService.getLastAnswer(pullId);
    }
}
