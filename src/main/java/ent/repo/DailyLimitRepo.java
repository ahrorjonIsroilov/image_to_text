package ent.repo;

import ent.entity.DailyLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DailyLimitRepo extends BaseRepo, JpaRepository<DailyLimit, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE DailyLimit set limit =:d_limit where id>-1")
    void changeDailyLimit(@Param("d_limit") Integer d_limit);

}
