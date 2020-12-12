package local.cosysoft.bot.telegram.dataservice.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import local.cosysoft.bot.telegram.dataservice.controller.payload.AnswerCreationPayload;
import local.cosysoft.bot.telegram.dataservice.controller.payload.PollCreationPayload;
import local.cosysoft.bot.telegram.dataservice.controller.payload.QuestionCreationPayload;
import local.cosysoft.bot.telegram.dataservice.controller.response.PollResponse;
import local.cosysoft.bot.telegram.dataservice.controller.response.QuestionResponse;
import local.cosysoft.bot.telegram.dataservice.entity.AnswerEntity;
import local.cosysoft.bot.telegram.dataservice.entity.PollEntity;
import local.cosysoft.bot.telegram.dataservice.entity.QuestionEntity;
import local.cosysoft.bot.telegram.dataservice.repository.AnswerRepository;
import local.cosysoft.bot.telegram.dataservice.repository.PollRepository;
import local.cosysoft.bot.telegram.dataservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramDataService {
    private final PollRepository pollRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public Collection<PollResponse> getPolls() {
        List<PollEntity> pollEntities = pollRepository.findAll();
        return getPollResponses(pollEntities);
    }

    public Collection<PollResponse> getPollById(final String id) {
        Optional<PollEntity> pollEntity = pollRepository.findById(UUID.fromString(id));
        List<PollEntity> pollEntities = pollEntity.map(Collections::singletonList).orElse(Collections.emptyList());
        return getPollResponses(pollEntities);
    }

    private List<PollResponse> getPollResponses(final List<PollEntity> pollEntities) {
        return pollEntities.stream().map(poll -> {
            PollResponse pollResponse = new PollResponse();
            pollResponse.setName(poll.getName());
            pollResponse.setId(poll.getId().toString());

            Collection<QuestionEntity> questionEntities = questionRepository.findQuestionEntitiesByPollId(poll.getId().toString());

            List<QuestionResponse> questionResponses = questionEntities.stream().map(question -> {
                QuestionResponse questionResponse = new QuestionResponse();
                questionResponse.setContent(question.getContent());
                questionResponse.setId(question.getId().toString());
                return questionResponse;
            }).collect(Collectors.toList());

            pollResponse.setQuestions(questionResponses);

            return pollResponse;
        }).collect(Collectors.toList());
    }

    public String createAnswer(final AnswerCreationPayload payload) {
        AnswerEntity answerEntity = new AnswerEntity();
        UUID answerId = UUID.randomUUID();
        answerEntity.setId(answerId);
        answerEntity.setContent(payload.getContent());
        answerEntity.setPollId(payload.getPollId());
        answerEntity.setQuestionId(payload.getQuestionId());
        answerEntity.setIsRunnable(Boolean.TRUE);
        answerEntity.setCreateDate(LocalDateTime.now());
        answerEntity.setUserTelegramId(payload.getUserTelegramId());
        answerRepository.save(answerEntity);
        return answerId.toString();
    }

    public String createPoll(final PollCreationPayload payload) {
        final PollEntity pollEntity = new PollEntity();
        UUID pollId = UUID.randomUUID();
        pollEntity.setId(pollId);
        pollEntity.setName(payload.getPollName());
        pollEntity.setCreateDate(LocalDateTime.now());
        pollRepository.save(pollEntity);
        return pollId.toString();
    }

    public String createQuestion(final QuestionCreationPayload payload) {
        QuestionEntity questionEntity = new QuestionEntity();
        UUID questionId = UUID.randomUUID();
        questionEntity.setPollId(payload.getPollId());
        questionEntity.setContent(payload.getContent());
        questionEntity.setId(questionId);
        questionRepository.save(questionEntity);
        return questionId.toString();
    }

    public PollResponse getLastPoll() {
        List<PollEntity> pollEntities = pollRepository.findAll();
        Optional<PollEntity> pollEntity = pollEntities.stream().max(Comparator.comparing(PollEntity::getCreateDate));

        if (pollEntity.isPresent()) {
            PollEntity poll = pollEntity.get();
            PollResponse pollResponse = new PollResponse();
            pollResponse.setName(poll.getName());
            pollResponse.setId(poll.getId().toString());

            Collection<QuestionEntity> questionEntities =
                questionRepository.findQuestionEntitiesByPollId(poll.getId().toString());

            List<QuestionResponse> questionResponses = questionEntities.stream().map(questionEntity -> {
                QuestionResponse questionResponse = new QuestionResponse();
                questionResponse.setId(questionEntity.getId().toString());
                questionResponse.setContent(questionEntity.getContent());
                return questionResponse;
            }).collect(Collectors.toList());

            pollResponse.setQuestions(questionResponses);
            return pollResponse;
        }

        return new PollResponse();

    }
}
