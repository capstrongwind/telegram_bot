package local.cosysoft.bot.telegram.dataservice.controller;

import java.util.Collection;
import local.cosysoft.bot.telegram.dataservice.controller.payload.AnswerCreationPayload;
import local.cosysoft.bot.telegram.dataservice.controller.payload.PollCreationPayload;
import local.cosysoft.bot.telegram.dataservice.controller.response.PollResponse;
import local.cosysoft.bot.telegram.dataservice.entity.AnswerEntity;
import local.cosysoft.bot.telegram.dataservice.service.TelegramDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/poll")
    public Collection<PollResponse> getPolls(@RequestParam(required = false) String id) {
        if (id == null) return telegramDataService.getPolls();
        return telegramDataService.getPollById(id);
    }

    @PostMapping(value = "/answer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createAnswer(@RequestBody AnswerCreationPayload payload) {
        return telegramDataService.createAnswer(payload);
    }

    @PostMapping(value = "/poll", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createPoll(@RequestBody PollCreationPayload payload) {
        return telegramDataService.createPoll(payload);
    }

    @GetMapping(value = "/answer/last")
    public AnswerEntity getLastAnswer(@RequestParam String pullId) {
        return telegramDataService.getLastAnswer(pullId);
    }
}
