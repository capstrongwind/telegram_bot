package local.cosysoft.bot.telegram.dataservice.repository;

import java.util.Collection;
import java.util.UUID;
import local.cosysoft.bot.telegram.dataservice.entity.AnswerToUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerToUserRepository extends JpaRepository<AnswerToUserEntity, UUID> {
    Collection<AnswerToUserEntity> findAnswerToUserEntitiesByUserId(String userId);
}
