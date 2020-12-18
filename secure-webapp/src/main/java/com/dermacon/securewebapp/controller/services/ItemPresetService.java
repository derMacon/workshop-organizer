package com.dermacon.securewebapp.controller.services;

import com.dermacon.securewebapp.data.InputItem;
import com.dermacon.securewebapp.data.Item;
import com.dermacon.securewebapp.data.ItemPreset;
import com.dermacon.securewebapp.data.ItemPresetRepository;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemPresetService {

    @Autowired
    private ItemPresetRepository itemPresetRepository;


    public void addNewPreset(ItemPreset itemPreset) {
        ItemPreset alreadySavedPreset = itemPresetRepository
                .findItemPresetsByPresetName(itemPreset.getPresetName());

        // preset name must be unique
        if (alreadySavedPreset == null) {
            // todo fix logger
            LoggerSingleton.getInstance().info("add new item preset: " + itemPreset);
            // todo throws java.sql.SQLIntegrityConstraintViolationException
            // when supply_category is null, this should be displayed to the error page
            itemPresetRepository.save(itemPreset);
        }
    }

    public ItemPreset getPreset(InputItem item) {
        return itemPresetRepository.findItemPresetsByPresetName(item.getItemName());
    }



}
