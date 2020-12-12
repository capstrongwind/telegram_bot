package local.cosysoft.bot.telegram.dataservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.Collection;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import local.cosysoft.bot.telegram.dataservice.controller.payload.AnswerCreationPayload;
import local.cosysoft.bot.telegram.dataservice.controller.payload.AnswerToUserBindingPayload;
import local.cosysoft.bot.telegram.dataservice.controller.payload.PollCreationPayload;
import local.cosysoft.bot.telegram.dataservice.controller.payload.QuestionCreationPayload;
import local.cosysoft.bot.telegram.dataservice.controller.response.AnswerResponse;
import local.cosysoft.bot.telegram.dataservice.controller.response.PollResponse;
import local.cosysoft.bot.telegram.dataservice.controller.response.QuestionResponse;
import local.cosysoft.bot.telegram.dataservice.entity.AnswerEntity;
import local.cosysoft.bot.telegram.dataservice.service.TelegramDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @Operation(summary = "Получение опросов")
    public Collection<PollResponse> getPolls(@RequestParam(required = false) String id) {
        if (id == null) return telegramDataService.getPolls();
        return telegramDataService.getPollById(id);
    }

    @PostMapping(value = "/answer", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Создание ответа")
    public String createAnswer(@RequestBody AnswerCreationPayload payload) {
        return telegramDataService.createAnswer(payload);
    }

    @PostMapping(value = "/poll", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Создание опроса")
    public String createPoll(@RequestBody PollCreationPayload payload) {
        return telegramDataService.createPoll(payload);
    }

    @PostMapping(value = "/question", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Создание вопроса")
    public String createQuestion(@RequestBody QuestionCreationPayload payload) {
        return telegramDataService.createQuestion(payload);
    }

    @GetMapping(value = "/poll/current")
    @Operation(summary = "Получение текущего активного опроса")
    public PollResponse getCurrentPoll(HttpServletResponse response) {
        Optional<PollResponse> pollResponse = telegramDataService.getCurrentPoll();
        if (!pollResponse.isPresent()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new PollResponse();
        }
        return pollResponse.get();
    }

    @PutMapping(value = "/poll/start")
    @Operation(summary = "Активация опроса")
    public String startPoll(HttpServletResponse response) {
        String s = telegramDataService.startPoll();
        if (s.equals("")) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return s;
        }
        return s;
    }

    @PutMapping(value = "/poll/stop")
    @Operation(summary = "Деактивация опроса")
    public String stopPoll() {
        return telegramDataService.stopPoll();
    }

    @PostMapping(value = "/bind/answer", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Установление связи между пользователем и ответом")
    public String bindAnswerWithUser(@RequestBody AnswerToUserBindingPayload payload) {
        return telegramDataService.bindAnswerWithUser(payload);
    }

    @GetMapping(value = "/show/user/{id}")
    @Operation(summary = "Получение ответов")
    public AnswerResponse showByUserId(@PathVariable String id, HttpServletResponse response) {
        Optional<AnswerResponse> answerResponse = telegramDataService.showByUserId(id);
        if (!answerResponse.isPresent()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new AnswerResponse();
        }
        return answerResponse.get();
    }

    @GetMapping(value = "/question/{id}")
    @Operation(summary = "Получение вопроса по id")
    public QuestionResponse getQuestionById(@PathVariable String id, HttpServletResponse response) {
        Optional<QuestionResponse> questionById = telegramDataService.getQuestionById(id);
        if (!questionById.isPresent()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new QuestionResponse();
        }
        return questionById.get();
    }

    @GetMapping(value = "/answer/{id}")
    @Operation(summary = "Получение ответа по id")
    public AnswerEntity getAnswerById(@PathVariable String id, HttpServletResponse response) {
        Optional<AnswerEntity> answerEntity = telegramDataService.getAnswerById(id);
        if (!answerEntity.isPresent()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new AnswerEntity();
        }
        return answerEntity.get();
    }
}
