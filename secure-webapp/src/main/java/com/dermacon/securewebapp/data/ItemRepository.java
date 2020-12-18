package com.dermacon.securewebapp.data;

import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {
    Item findByItemId(Long id);
    Iterable<Item> findAllByStatus(Boolean status);
}
