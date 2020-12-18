package com.dermacon.securewebapp.controller.groceryList;

import com.dermacon.securewebapp.controller.admin.SelectedElements;
import com.dermacon.securewebapp.controller.services.ItemPresetService;
import com.dermacon.securewebapp.controller.services.ItemService;
import com.dermacon.securewebapp.data.Flatmate;
import com.dermacon.securewebapp.data.FlatmateRepository;
import com.dermacon.securewebapp.data.InputItem;
import com.dermacon.securewebapp.data.Item;
import com.dermacon.securewebapp.data.ItemPreset;
import com.dermacon.securewebapp.data.ItemPresetRepository;
import com.dermacon.securewebapp.data.ItemRepository;
import com.dermacon.securewebapp.data.LivingSpace;
import com.dermacon.securewebapp.data.LivingSpaceRepository;
import com.dermacon.securewebapp.data.Room;
import com.dermacon.securewebapp.data.SupplyCategory;
import com.dermacon.securewebapp.data.TaskRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Controller for the grocery list endpoint
 */
@Transactional
@Controller
@RequestMapping("groceryList")
public class GroceryListController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlatmateRepository flatmateRepository;

    @Autowired
    private LivingSpaceRepository livingSpaceRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ItemPresetRepository itemPresetRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemPresetService itemPresetService;


    /**
     * Initializes model with
     * - item instance that will overwritten when a new item will be added
     * - all items that were previously selected
     * - selected items which were clicked with the checkboxes
     * @param model model provided by the framework
     * @return grocery list thymeleaf template
     */
    @RequestMapping(value = "/", method= RequestMethod.GET)
    public String displayGroceryList(Model model) {

        // adding item which will be set in the thymeleaf form and used
        // and overwritten when a new item will be added
        model.addAttribute("item", new InputItem());

        model.addAttribute("preset", new ItemPreset());

        // add list of active and inactive elements, will be used to display
        // what is currently in the grocery list and what was bought at the
        // last shopping trip
        model.addAttribute("newItems", itemService.getSortedItems_nextPurchase());
        model.addAttribute("oldItems", itemService.getSortedItems_prevPurchase());
        model.addAttribute("dateLastPurchase", itemService.getLastPurchase());
        model.addAttribute("selectedItems", new SelectedItems());

        // used in header to select which of the title segments should be highlighted
        model.addAttribute("selectedDomain", "groceryList");

        // used to display preset options
        // todo put into service
        model.addAttribute("saved_presets", sort(itemPresetRepository.findAll()));

        // used when adding a new preset to determine the category type of new preset
        Iterable<SupplyCategory> categories = Arrays.asList(SupplyCategory.values());
        model.addAttribute("available_categories", categories);
        model.addAttribute("new_preset", new ItemPreset());

        model.addAttribute("selectedItemPresets", new SelectedElements());

        return "groceryList";
    }

    // todo remove this
    private <T> Iterable<T> sort(Iterable<T> it) {
        Stream<T> stream = StreamSupport.stream(it.spliterator(), false);
        return stream.sorted().collect(Collectors.toList());
    }

    @RequestMapping(value = "/processForm", method=RequestMethod.POST, params = "updateAll")
    public String checkAllItems() {
        itemService.checkAllItems();
        return "redirect:/groceryList/";
    }

    /**
     * Removes the selected items from the database
     * @param selectedItems object which holds a list of item ids which should be deleted
     * @return grocery list thymeleaf template
     */
    @RequestMapping(value = "/processForm", method=RequestMethod.POST, params = "update")
    public String processCheckboxForm(@ModelAttribute(value="selectedItems") SelectedItems selectedItems) {

        Iterable<Long> itemIds = selectedItems.getCheckedItems();
        boolean ableToSelectAllItems = !itemService.shopSelectedItems(itemIds);
        if (!ableToSelectAllItems) {
            // todo handle error
        }

        return "redirect:/groceryList/";
    }


    @RequestMapping(value = "/processForm", method=RequestMethod.POST, params = "remove")
    public String removeItems(@ModelAttribute(value="selectedItems") SelectedItems selectedItems) {

        Iterable<Long> itemIds = selectedItems.getCheckedItems();
        itemService.removeSelectedItems(itemIds);

        return "redirect:/groceryList/";
    }


    /**
     * Adds a new Item to the database.
     * @param item item provided by the html form
     * @return grocery list thymeleaf template
     */
    // todo update mapping
    @PostMapping("/addItem")
    public String addNewItem(@ModelAttribute("item") InputItem item) {
        itemService.addItem(item);
        return "redirect:/groceryList/";
    }


    @PostMapping("/addNewPreset")
    public String addNewPreset(@ModelAttribute("new_preset") ItemPreset itemPreset) {
        itemPresetService.addNewPreset(itemPreset);
        return "redirect:/groceryList/";
    }

    @RequestMapping(value = "/removePreset", method = RequestMethod.POST)
    public String removePreset_post(@ModelAttribute(value = "selectedItemPreset") SelectedElements selectedPresets) {
        selectedPresets.getCheckedElements()
                .stream()
                .forEach(itemPresetRepository::deleteByPresetId);
        return "redirect:/groceryList/";
    }
}
