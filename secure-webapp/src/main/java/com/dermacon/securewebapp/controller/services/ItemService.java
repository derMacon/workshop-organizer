package com.dermacon.securewebapp.controller.services;

import com.dermacon.securewebapp.data.Flatmate;
import com.dermacon.securewebapp.data.InputItem;
import com.dermacon.securewebapp.data.Item;
import com.dermacon.securewebapp.data.ItemPreset;
import com.dermacon.securewebapp.data.ItemRepository;
import com.dermacon.securewebapp.data.LivingSpace;
import com.dermacon.securewebapp.data.Room;
import com.dermacon.securewebapp.data.RoomRepository;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FlatmateService flatmateService;

    @Autowired
    private ItemPresetService itemPresetService;


    private Date lastPurchase = new Date(System.currentTimeMillis());

    /* ---------- Item Info ---------- */

    /**
     * Get a diff between two dates
     * https://stackoverflow.com/questions/1555262/calculating-the-difference-between-two-java-date-instances
     *
     * @param date1    the oldest date
     * @param date2    the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public Date getLastPurchase() {
        return lastPurchase;
    }

    /**
     * Returns the items in the current shopping cart
     *
     * @return the items in the current shopping cart
     */
    public Iterable<Item> getSortedItems_nextPurchase() {
        return sort(itemRepository.findAllByStatus(false));
    }

    /**
     * the items in the last shopping cart
     *
     * @return the items in the last shopping cart
     */
    public Iterable<Item> getSortedItems_prevPurchase() {
        return sort(itemRepository.findAllByStatus(true));
    }


    /* ---------- add new Items ---------- */

    /**
     * Sorts a given iterable instance
     *
     * @param it iterable instance to sort
     * @return sorted Iterable instance
     */
    private Iterable<Item> sort(Iterable<Item> it) {
        Stream<Item> stream = StreamSupport.stream(it.spliterator(), false);
        return stream.sorted().collect(Collectors.toList());
    }

    /**
     * Add new Item to the current shopping list
     *
     * @param inputItem new Item to add
     * @return false if destination wasn't present, else true
     */
    public boolean addItem(InputItem inputItem) {
        boolean validItem = inputItem != null && inputItem.isValid();
        if (validItem) {
            // todo implement builder
            Item newItem = new Item(
                    inputItem.getItemCount(),
                    inputItem.getItemName(),
                    getDestination(inputItem),
                    false
            );
            persistItem(newItem);

            // todo fix logger
            LoggerSingleton.getInstance().info("added new item: " + inputItem);
        } else {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("can't add item: " + inputItem);
        }

        return validItem;
    }

//    public boolean addItem(Item item) {
//        boolean validItem = item != null && item.isValid();
//        if (validItem) {
//            Flatmate loggedInFlatmate = flatmateService.getLoggedInFlatmate();
//            item.setStatus(false);
//            getDestination(item, loggedInFlatmate);
//            persistItem(item);
//
//            LoggerSingleton.getInstance().info("added new item: " + item);
//        } else {
//            // todo fix logger
////            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("can't add item: " + item);
//        }
//
//        return validItem;
//    }

    /**
     * Checks if an item with the same name, destination and shipping status already exists,
     * if so the appropriate entity will be updated otherwise the given entity will be saved
     * to the database as it is.
     *
     * @param item item to persist
     */
    private void persistItem(Item item) {
        Item alreadySavedItem = getItemWithSameName_and_Destination_and_status(item);
        // overwrite item if necessary
        if (alreadySavedItem != null) {
            alreadySavedItem.setItemCount(item.getItemCount() + alreadySavedItem.getItemCount());
            LoggerSingleton.getInstance().info("overwrites already saved item: " + item);
        } else {
            itemRepository.save(item);
            LoggerSingleton.getInstance().info("no existing item entity, persist new: " + item);
        }
    }

    /**
     * Get equivalent item to given input
     *
     * @param inputItem input item to check
     * @return equivalent item to given input
     */
    private Item getItemWithSameName_and_Destination_and_status(Item inputItem) {
        Item out = null;

        for (Item currItem : itemRepository.findAll()) {
            if (currItem.equals(inputItem)
                    && currItem.getItemId() != inputItem.getItemId()) {
                out = currItem;
            }
        }

        return out;
    }


    /* ---------- shop items ---------- */

    /**
     * The destination field of the item will be filled.
     * <p>
     * Depending where the item is neede (e.g. kitchen vs. bathroom supply)
     * the
     *
     * @param item input item (containing name and quantity)
     */
    private Room getDestination(InputItem item) {
        Flatmate loggedInFlatmate = flatmateService.getLoggedInFlatmate();
        LivingSpace livingSpace = loggedInFlatmate.getLivingSpace();
        Room destination;

        ItemPreset preset = itemPresetService.getPreset(item);
        switch (preset.getSupplyCategory()) {
            case KITCHEN_SUPPLY:
                destination = livingSpace.getKitchen();
                break;
            case BATHROOM_SUPPLY:
                destination = livingSpace.getBathroom();
                break;
            case CLEANING_SUPPLY:
                destination = livingSpace.getStorage();
                break;
            default:
                destination = livingSpace.getBedroom();
        }

        LoggerSingleton.getInstance().info("updated item with destination: " + item);
        return destination;
    }

    /**
     * Selects all given items in the current shopping list.
     * All the selected items are already in the database. No
     * need to further persist the data.
     *
     * @param ids item ids to shop
     * @return true if all Items ware selectable
     */
    public boolean shopSelectedItems(Iterable<Long> ids) {
        removeOldItems();
        Iterable<Item> items = itemRepository.findAllById(ids);
        return moveItemsToOldCart(items);
    }

    /**
     * When the user wants to move new items to the right column the old items
     * will be removed, given that the last move action was at the last day
     * <p>
     * Necessary when keeping the old items column up to date
     */
    public void removeOldItems() {
        Date curr = new Date(System.currentTimeMillis());
        if (getDateDiff(lastPurchase, curr, TimeUnit.HOURS) > 0) {

            LoggerSingleton.getInstance().info("latest purchase too old, will be removed. Last " +
                    "Purchase (" + lastPurchase + "), current date (" + curr + ")");

            lastPurchase = curr;

            for (Item item : itemRepository.findAllByStatus(true)) {
                item.setDestination(null);
                // delete entity from database
                itemRepository.delete(item);

                LoggerSingleton.getInstance().info("removed item: " + item);
            }

        }
    }

    /**
     * Iterates over a given list of items and
     *
     * @return
     */
    public boolean moveItemsToOldCart(Iterable<Item> items) {
        boolean allItemsSelectable = true;

        for (Item item : items) {
            allItemsSelectable = allItemsSelectable && item != null;

            // todo fix logger
            LoggerSingleton.getInstance().info("persist item: " + item);

            moveToOldShoppingList(item);
            // todo fix logger
            LoggerSingleton.getInstance().info("moving item to old items table: " + item);
        }

        return allItemsSelectable;
    }

    /**
     * Checks if a given is already present in the last shopping trip list
     * - if not the status of the item will be simply be put to true (to signal that this item
     * was already bought)
     * - if the item already exists in the last shopping trip list, the input item's count will
     * be summed up with the already existing item's count. Then the input Item will be deleted
     * from the database
     *
     * @param inputItem selected Item in the current shopping list (must have a status == false)
     */
    private void moveToOldShoppingList(Item inputItem) {
        assert (inputItem != null && !inputItem.getStatus());

        Item alreadySavedItem = null;

        for (Item currItem : itemRepository.findAll()) {
            if (currItem.getItemName().toLowerCase().equals(inputItem.getItemName().toLowerCase())
                    && currItem.getDestination().equals(inputItem.getDestination())
                    && currItem.getItemId() != inputItem.getItemId()) {

                alreadySavedItem = currItem;

            }
        }

        if (alreadySavedItem == null) {
            inputItem.setStatus(true);
        } else {
            alreadySavedItem.setItemCount(alreadySavedItem.getItemCount() + inputItem.getItemCount());
            // delete entity from database
            itemRepository.delete(inputItem);
        }
    }



    /* ---------- remove items ---------- */

    public void removeSelectedItems(Iterable<Long> checkedItems) {
        for (Long curr : checkedItems) {
            Item item = itemRepository.findByItemId(curr);
            item.setDestination(null);
            LoggerSingleton.getInstance().info("persist item: " + item);
            itemRepository.delete(item);
        }
    }




    /* ---------- other stuff ---------- */

    /**
     * Finds all unchecked items in the shopping cart and checks them by setting their status to
     * true
     */
    public void checkAllItems() {
        // select all non selected checkboxes
        for (Item item : itemRepository.findAllByStatus(false)) {
            item.setStatus(true);
        }
    }


}
