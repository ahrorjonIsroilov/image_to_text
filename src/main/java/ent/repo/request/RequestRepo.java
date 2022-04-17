package ent.repo.request;

import ent.entity.request.Request;
import ent.repo.BaseRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepo extends BaseRepo, JpaRepository<Request, Long> {

    @Query(nativeQuery = true, value = "select get_daily_requests()")
    Long getDailyRequests();

    @Query(nativeQuery = true, value = "select get_weekly_requests()")
    Long getWeeklyRequests();

    @Query(nativeQuery = true, value = "select get_monthly_requests()")
    Long getMonthlyRequests();
}
