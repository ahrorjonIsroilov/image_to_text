package ent.repo.auth;

import ent.entity.auth.AuthUser;
import ent.repo.BaseRepo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AuthRepo extends BaseRepo, CrudRepository<AuthUser, Long> {

    void deleteByChatId(Long chatId);

    AuthUser findByChatId(Long chatId);

    @Query(nativeQuery = true, value = "select get_joined_last_day()")
    Long getJoinedLastDay();

    @Query(nativeQuery = true, value = "select get_joined_last_week()")
    Long getJoinedLastWeek();

    @Query(nativeQuery = true, value = "select get_joined_last_month()")
    Long getJoinedLastMonth();

    @Query(nativeQuery = true, value = "select public.auth_user.chat_id from public.auth_user where auth_user.role<>'admin'")
    List<Long> getAllChatId();

    @Modifying
    @Transactional
    @Query(value = "UPDATE public.auth_user set daily_request= (select public.auth_user.daily_request where chat_id=:PChatId)-1 where chat_id=:PChatId",nativeQuery = true)
    void decrementDailyLimit(@Param("PChatId") Long PChatId);
}
