package ent.service.request;

import ent.entity.request.Request;
import ent.repo.request.RequestRepo;
import ent.service.AbstractService;
import ent.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class RequestService extends AbstractService<RequestRepo> implements BaseService {
    public RequestService(RequestRepo repo) {
        super(repo);
    }

    public void save(Request request){
        repo.save(request);
    }
}
