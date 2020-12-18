package com.dermacon.securewebapp.data;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TaskRepository extends CrudRepository<Task, Long> {
    Set<Task> findAllByResponsibleFlatmates_flatmateId(Long id);
    Task findAllByTaskStatus(Boolean status);
}
