package ent.service;

import ent.repo.BaseRepo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AbstractService<T extends BaseRepo> {
    public T repo;
}
