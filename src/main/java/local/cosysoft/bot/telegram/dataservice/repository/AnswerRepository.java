package local.cosysoft.bot.telegram.dataservice.repository;

import java.util.Collection;
import java.util.UUID;
import local.cosysoft.bot.telegram.dataservice.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, UUID> {
    Collection<AnswerEntity> getAnswerEntitiesByPollId(String pullId);
}
