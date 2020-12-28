package com.niafikra.dimension.inventory;

import com.niafikra.dimension.core.navigation.MainNavigationLayout;
import com.niafikra.dimension.core.navigation.Nav;
import com.niafikra.dimension.inventory.entity.PurchaseOrder;
import com.niafikra.dimension.inventory.entity.Supplier;
import com.niafikra.dimension.inventory.service.ItemService;
import com.niafikra.dimension.inventory.service.POItemService;
import com.niafikra.dimension.inventory.service.PurchaseOrderService;
import com.niafikra.dimension.inventory.service.SupplierService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import static com.niafikra.dimension.core.navigation.Nav.Parent.ROOT;
import static com.vaadin.flow.component.icon.VaadinIcon.HOME;

@Nav(value = "Order",
        icon = HOME,
        tree = {@Nav.Parent(ROOT)})
@Route(value = "order-form", layout = MainNavigationLayout.class)
public class OrderForm extends VerticalLayout {

    private SupplierService supplierService;
    private PurchaseOrderService purchaseOrderService;
    private ItemService itemService;
    private OrderItemsEditor orderItemsEditor;
    private POItemService poItemService;
    private SuppliersProvider suppliersProvider;

    // Global components and properties
    DatePicker orderDate = new DatePicker("Order Date");
    Select<Supplier> supplier = new Select<>();


    Binder<PurchaseOrder> binder = new BeanValidationBinder<>(PurchaseOrder.class);

    public OrderForm(SupplierService supplierService, ItemService itemService,
                     PurchaseOrderService purchaseOrderService, OrderItemsEditor orderItemsEditor,
                     POItemService poItemService, SuppliersProvider suppliersProvider) {

        this.supplierService = supplierService;
        this.itemService = itemService;
        this.purchaseOrderService = purchaseOrderService;
        this.orderItemsEditor = orderItemsEditor;
        this.poItemService = poItemService;
        this.suppliersProvider = suppliersProvider;

        orderItemsEditor.setWidth("80%");


        // Supplier combobox
        supplier.setLabel("Supplier");
        supplier.setDataProvider(suppliersProvider);
        supplier.setItemLabelGenerator(Supplier::getName);

        // Items selector
        OrderItemsEditor itemsList = orderItemsEditor;

        // save button configuration
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        save.addClickListener(click -> {
            try {
                // create empty bean to store the order
                PurchaseOrder newPurchaseOrder = new PurchaseOrder();

                // Run validators and write the values to the bean
                binder.writeBean(newPurchaseOrder);


                // call backend to store
                itemsList.getPOItemsSelected().forEach(poItem -> poItemService.save(poItem));
                newPurchaseOrder.setItems(orderItemsEditor.getPOItemsSelected());
                purchaseOrderService.save(newPurchaseOrder);

                // show success notification


                // clear form fields
                clearForm();

            } catch (ValidationException e) {
                // create custom validation

                e.printStackTrace();
            }
        });

        // binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid());
        binder.addStatusChangeListener(event -> save.setEnabled(event.getBinder().isValid()));

        // Binder
        binder.bindInstanceFields(this);

        // Header
        Div header = new Div(new H3("Create new order"));

        // Links
        RouterLink stockList = new RouterLink("Back to stock", StockView.class);
        RouterLink newSupplier = new RouterLink("New Supplier", SupplierCRUDView.class);

        Div link = new Div(stockList);

        add(header, newSupplier, orderDate, supplier, itemsList, save, link);

    }

    private void clearForm() {
        // clear fields
        binder.readBean(null);

        // clear grid
        orderItemsEditor.clearGrid();
        orderItemsEditor.load();
    }


}

