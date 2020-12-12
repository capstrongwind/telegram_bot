package local.cosysoft.bot.telegram.dataservice.repository;

import java.util.UUID;
import local.cosysoft.bot.telegram.dataservice.entity.PollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends JpaRepository<PollEntity, UUID> {
}
