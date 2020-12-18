package com.dermacon.securewebapp.data;

import org.springframework.data.repository.CrudRepository;

public interface ItemPresetRepository extends CrudRepository<ItemPreset, Long> {
    ItemPreset findItemPresetsByPresetName(String name);
    ItemPreset findItemPresetsByPresetNameAndSupplyCategory(String name,
                                                               SupplyCategory category);
    void deleteByPresetId(long id);
}
