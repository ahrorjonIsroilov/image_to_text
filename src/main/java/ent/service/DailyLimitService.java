package ent.service;

import ent.entity.DailyLimit;
import ent.repo.DailyLimitRepo;
import org.springframework.stereotype.Service;

@Service
public class DailyLimitService extends AbstractService<DailyLimitRepo> implements BaseService {

    public DailyLimitService(DailyLimitRepo repo) {
        super(repo);
    }

    public void save(DailyLimit limit) {
        repo.save(limit);
    }

    public void updateDailyLimit(Integer limit) {
        repo.changeDailyLimit(limit);
    }

    public Integer getLimitNum() {
        return repo.findAll().get(0).getLimit();
    }
}
