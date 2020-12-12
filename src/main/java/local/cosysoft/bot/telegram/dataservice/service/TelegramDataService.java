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
import local.cosysoft.bot.telegram.dataservice.controller.payload.AnswerToUserBindingPayload;
import local.cosysoft.bot.telegram.dataservice.controller.payload.PollCreationPayload;
import local.cosysoft.bot.telegram.dataservice.controller.payload.QuestionCreationPayload;
import local.cosysoft.bot.telegram.dataservice.controller.response.AllAnswerResponse;
import local.cosysoft.bot.telegram.dataservice.controller.response.AnswerResponse;
import local.cosysoft.bot.telegram.dataservice.controller.response.BasicAnswerResponse;
import local.cosysoft.bot.telegram.dataservice.controller.response.PollResponse;
import local.cosysoft.bot.telegram.dataservice.controller.response.QuestionResponse;
import local.cosysoft.bot.telegram.dataservice.controller.response.QuestionToAnswerResponse;
import local.cosysoft.bot.telegram.dataservice.controller.response.QuestionToAnswerResponseWithUserResponse;
import local.cosysoft.bot.telegram.dataservice.entity.AnswerEntity;
import local.cosysoft.bot.telegram.dataservice.entity.AnswerToUserEntity;
import local.cosysoft.bot.telegram.dataservice.entity.PollEntity;
import local.cosysoft.bot.telegram.dataservice.entity.QuestionEntity;
import local.cosysoft.bot.telegram.dataservice.repository.AnswerRepository;
import local.cosysoft.bot.telegram.dataservice.repository.AnswerToUserRepository;
import local.cosysoft.bot.telegram.dataservice.repository.PollRepository;
import local.cosysoft.bot.telegram.dataservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class TelegramDataService {
    private final PollRepository pollRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerToUserRepository answerToUserRepository;

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
        answerEntity.setCreateDate(LocalDateTime.now());
        answerRepository.save(answerEntity);
        return answerId.toString();
    }

    public String createPoll(final PollCreationPayload payload) {
        final PollEntity pollEntity = new PollEntity();
        UUID pollId = UUID.randomUUID();
        pollEntity.setId(pollId);
        pollEntity.setName(payload.getPollName());
        pollEntity.setCreateDate(LocalDateTime.now());
        pollEntity.setStatus("CREATED");
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

    public Optional<PollResponse> getCurrentPoll() {
        List<PollEntity> pollEntities = pollRepository.findAll();
        Optional<PollEntity> pollEntity =
            pollEntities.stream().max(Comparator.comparing(PollEntity::getCreateDate));

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
                Collection<AnswerEntity> answerEntities = answerRepository.getAnswerEntitiesByQuestionId(questionEntity.getId().toString());
                List<BasicAnswerResponse> answerResponses = answerEntities.stream().map(entity -> {
                    BasicAnswerResponse basicAnswerResponse = new BasicAnswerResponse();
                    basicAnswerResponse.setContent(entity.getContent());
                    basicAnswerResponse.setId(entity.getId().toString());
                    return basicAnswerResponse;
                }).collect(Collectors.toList());
                questionResponse.setAnswers(answerResponses);
                return questionResponse;
            }).collect(Collectors.toList());

            pollResponse.setQuestions(questionResponses);
            return Optional.of(pollResponse);
        }

        return Optional.empty();

    }

    public String startPoll() {
        List<PollEntity> polls = pollRepository.findAll();
        if (CollectionUtils.isEmpty(polls)) {
            return "";
        }
        Optional<PollEntity> pollEntityOpt =
            polls.stream().max(Comparator.comparing(PollEntity::getCreateDate));
        if (!pollEntityOpt.isPresent()) {
            return "";
        }
        PollEntity pollEntity = pollEntityOpt.get();
        pollEntity.setStatus("ACTIVE");
        pollEntity.setIsRunnable(Boolean.TRUE);

        polls.stream().filter(p -> !p.getId().equals(pollEntity.getId())).forEach(p -> p.setStatus("CREATED"));
        pollRepository.saveAll(polls);
        return pollEntity.getId().toString();
    }

    public String stopPoll() {
        List<PollEntity> polls = pollRepository.findAll();
        Optional<PollEntity> pollEntityOpt =
            polls.stream().max(Comparator.comparing(PollEntity::getCreateDate));
        if (!pollEntityOpt.isPresent()) {
            return "";
        }
        PollEntity pollEntity = pollEntityOpt.get();
        pollEntity.setIsRunnable(Boolean.FALSE);
        pollEntity.setStatus("CLOSED");
        pollRepository.save(pollEntity);
        return pollEntity.getId().toString();
    }

    public String bindAnswerWithUser(final AnswerToUserBindingPayload payload) {
        final AnswerToUserEntity answerToUserEntity = new AnswerToUserEntity();
        UUID answerToUseId = UUID.randomUUID();
        answerToUserEntity.setId(answerToUseId);
        answerToUserEntity.setAnswerId(payload.getAnswerId());
        answerToUserEntity.setUserId(payload.getUserId());
        answerToUserRepository.save(answerToUserEntity);
        return answerToUseId.toString();
    }

    public Optional<AnswerResponse> showByUserId(final String id) {
        List<PollEntity> pollEntities = pollRepository.findAll();
        Optional<PollEntity> pollEntity = pollEntities.stream().max(Comparator.comparing(PollEntity::getCreateDate));
        if (pollEntity.isPresent()) {
            AnswerResponse answerResponse = new AnswerResponse();
            answerResponse.setUserId(id);
            answerResponse.setPollId(pollEntity.get().getId().toString());

            Collection<AnswerToUserEntity> answerToUserEntities = answerToUserRepository.findAnswerToUserEntitiesByUserId(id);
            List<QuestionToAnswerResponse> questionToAnswerResponses = answerToUserEntities.stream().map(answerToUserEntity -> {
                QuestionToAnswerResponse questionToAnswerResponse = new QuestionToAnswerResponse();
                questionToAnswerResponse.setAnswerId(answerToUserEntity.getAnswerId());
                Optional<AnswerEntity> answerEntity = answerRepository.findById(UUID.fromString(answerToUserEntity.getAnswerId()));
                answerEntity.ifPresent(entity -> questionToAnswerResponse.setQuestionId(entity.getQuestionId()));
                return questionToAnswerResponse;
            }).collect(Collectors.toList());

            answerResponse.setQuestionToAnswerResponses(questionToAnswerResponses);
            return Optional.of(answerResponse);
        }

        return Optional.empty();
    }

    public Optional<QuestionResponse> getQuestionById(final String id) {
        Optional<QuestionEntity> questionEntity = questionRepository.findById(UUID.fromString(id));
        if (questionEntity.isPresent()) {
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setId(questionEntity.get().getId().toString());
            questionResponse.setContent(questionEntity.get().getContent());
            Collection<AnswerEntity> answerEntities = answerRepository.getAnswerEntitiesByQuestionId(id);
            List<BasicAnswerResponse> answerResponses = answerEntities.stream().map(entity -> {
                BasicAnswerResponse basicAnswerResponse = new BasicAnswerResponse();
                basicAnswerResponse.setContent(entity.getContent());
                basicAnswerResponse.setId(entity.getId().toString());
                return basicAnswerResponse;
            }).collect(Collectors.toList());
            questionResponse.setAnswers(answerResponses);
            return Optional.of(questionResponse);
        }
        return Optional.empty();
    }

    public Optional<AnswerEntity> getAnswerById(final String id) {
        return answerRepository.findById(UUID.fromString(id));
    }

    public Optional<AllAnswerResponse> show() {
        List<PollEntity> pollEntities = pollRepository.findAll();
        Optional<PollEntity> pollEntity = pollEntities.stream().max(Comparator.comparing(PollEntity::getCreateDate));
        if (pollEntity.isPresent()) {
            AllAnswerResponse answerResponse = new AllAnswerResponse();
            answerResponse.setPollId(pollEntity.get().getId().toString());
            Collection<AnswerEntity> answerEntities = answerRepository.getAnswerEntitiesByPollId(pollEntity.get().getId().toString());
            List<QuestionToAnswerResponseWithUserResponse> questionToAnswerResponses = answerEntities.stream().map(answerEntity -> {
                Collection<AnswerToUserEntity> answerToUserEntities = answerToUserRepository.findAnswerToUserEntitiesByAnswerId(answerEntity.getId().toString());
                return answerToUserEntities.stream().map(answerToUserEntity -> {
                    QuestionToAnswerResponseWithUserResponse questionToAnswerResponse = new QuestionToAnswerResponseWithUserResponse();
                    questionToAnswerResponse.setAnswerId(answerToUserEntity.getAnswerId());
                    questionToAnswerResponse.setQuestionId(answerEntity.getQuestionId());
                    questionToAnswerResponse.setUserId(answerToUserEntity.getUserId());
                    return questionToAnswerResponse;
                }).collect(Collectors.toList());
            }).flatMap(Collection::stream).collect(Collectors.toList());

            answerResponse.setQuestionToAnswerResponses(questionToAnswerResponses);
            return Optional.of(answerResponse);
        }

        return Optional.empty();
    }
}
