package local.cosysoft.bot.telegram.dataservice.repository;

import java.util.Collection;
import java.util.UUID;
import local.cosysoft.bot.telegram.dataservice.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, UUID> {
    Collection<QuestionEntity> findQuestionEntitiesByPollId(String pullId);
}
