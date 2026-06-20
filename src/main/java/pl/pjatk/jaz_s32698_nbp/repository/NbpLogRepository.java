package pl.pjatk.jaz_s32698_nbp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pjatk.jaz_s32698_nbp.model.NbpLogRecord;

@Repository
public interface NbpLogRepository extends JpaRepository<NbpLogRecord, Long> {
}