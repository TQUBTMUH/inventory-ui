package com.niafikra.dimension.inventory;

import com.niafikra.dimension.inventory.entity.Item;
import com.niafikra.dimension.inventory.entity.POItem;
import com.niafikra.dimension.inventory.service.ItemService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class POItemForm extends VerticalLayout {

    // Fields
    Select<Item> item = new Select<>();
    IntegerField quantity = new IntegerField("Quantity");
    Button addBtn = new Button("Add Item");

    Binder<POItem> binder = new BeanValidationBinder<>(POItem.class);

    private OnSaveHandler onSaveHandler;
    private POItemsProvider poItemsProvider;

    public POItemForm(POItemsProvider poItemsProvider) {

        this.poItemsProvider = poItemsProvider;

        binder.bindInstanceFields(this);
        // Form Fields
        item.setLabel("Item");
        item.setItemLabelGenerator(Item::getName);


//        List<Item> itemList = itemService.findAll();
//        item.setItemLabelGenerator(Item::getName);
//        item.setItems(itemList);
        item.setDataProvider(poItemsProvider);
        item.setItemLabelGenerator(Item::getName);

        addBtn.addClickListener (add -> {
            try {
                // Create empty bean to store the item and quantity
                POItem newPOitem = new POItem();

                // Run validators and write the values to the bean
                binder.writeBean(newPOitem);
                onSaveHandler.onSave(newPOitem);

                // clear fields
                clearFormFields();

                // update grid or cause a redirect into grid


            } catch (ValidationException e) {
                throw  new RuntimeException("Failed to add item into poItem grid " + e);
            }
        });

        binder.addStatusChangeListener(event -> addBtn.setEnabled(binder.isValid()));

        H3 header = new H3("Form");

        add(header, item, quantity, addBtn);
    }

    private void clearFormFields() {
        binder.readBean(null);
    }

    public void setOnSaveHandler(OnSaveHandler onSaveHandler) {
        this.onSaveHandler = onSaveHandler;
    }

    public interface OnSaveHandler{

        void onSave(POItem poItem);
    }
}


