package local.cosysoft.bot.telegram.dataservice.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import local.cosysoft.bot.telegram.dataservice.controller.payload.PollCreationPayload;
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

    public Collection<PollEntity> getPolls() {
        return pollRepository.findAll();
    }

    public Collection<PollEntity> getPollById(final String id) {
        Optional<PollEntity> pollEntity = pollRepository.findById(UUID.fromString(id));
        return pollEntity.map(Collections::singletonList).orElse(Collections.emptyList());
    }

    public String createAnswer(final AnswerEntity answerEntity) {
        UUID answerId = UUID.randomUUID();
        answerEntity.setId(answerId);
        answerRepository.save(answerEntity);
        return answerId.toString();
    }

    public String createPoll(final PollCreationPayload payload) {
        final PollEntity pollEntity = new PollEntity();
        UUID pollId = UUID.randomUUID();
        pollEntity.setId(pollId);
        pollEntity.setName(payload.getPollName());

        List<QuestionEntity> questionEntities = payload.getQuestions().stream().map(q -> {
            QuestionEntity questionEntity = new QuestionEntity();
            UUID questionId = UUID.randomUUID();
            questionEntity.setPollId(pollId.toString());
            questionEntity.setContent(q.getContent());
            questionEntity.setId(questionId);
            return questionEntity;
        }).collect(Collectors.toList());

        pollRepository.save(pollEntity);
        questionRepository.saveAll(questionEntities);

        return pollId.toString();
    }

    public AnswerEntity getLastAnswer(final String pullId) {
        Collection<AnswerEntity> answerEntitiesByPollId = answerRepository.getAnswerEntitiesByPollId(pullId);
        return answerEntitiesByPollId.stream().max(Comparator.comparing(AnswerEntity::getCreateDate)).orElse(new AnswerEntity());
    }
}
