package com.niafikra.dimension.inventory;

import com.niafikra.dimension.core.navigation.MainNavigationLayout;
import com.niafikra.dimension.inventory.entity.POItem;
import com.niafikra.dimension.inventory.entity.PurchaseOrder;
import com.niafikra.dimension.inventory.entity.Supplier;
import com.niafikra.dimension.inventory.service.POItemService;
import com.niafikra.dimension.inventory.service.PurchaseOrderService;
import com.niafikra.dimension.inventory.service.StockService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.List;

@Route(value = "list-orders", layout = MainNavigationLayout.class)
public class OrdersManagementView extends VerticalLayout {

    private Grid<PurchaseOrder> orders = new Grid<PurchaseOrder>();

    private PurchaseOrderService purchaseOrderService;
    private StockService stockService;
    private POItemService poItemService;
    private PurchaseOrderProvider provider;


    public OrdersManagementView(PurchaseOrderService purchaseOrderService, StockService stockService,
                                POItemService poItemService, PurchaseOrderProvider provider) {
        this.purchaseOrderService = purchaseOrderService;
        this.stockService = stockService;
        this.poItemService = poItemService;
        this.provider = provider;

        orders.setDataProvider(provider);
        // configure grid
        configureOrdersGrid();

        // Header
        H3 header = new H3("ORDER LIST");

        // Links
        RouterLink stockLink = new RouterLink("Back to stock", StockView.class);

        add(header, orders, stockLink);
        updateList();
    }

    private void configureOrdersGrid() {
        orders.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_ROW_BORDERS);

        orders.addColumn(order -> {
            return order.getOrderDate();
        }).setHeader("Date");
        orders.addColumn(order -> {
            Supplier supplier = order.getSupplier();
            return supplier == null ? "-" : supplier.getName();
        }).setHeader("Supplier");
        orders.addColumn(order -> {
            List<POItem> orderList = order.getItems();
            return orderList;
        }).setHeader("Item");


        // Receive button configuration
        orders.addComponentColumn(purchaseOrder -> {
            Button receiveBtn = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_DOWN));
            receiveBtn.addClickListener(event -> {
                // update stock and delete item in purchaseOrder
                Long orderId = purchaseOrder.getId();
                stockService.customeStockUpdate(orderId);

                // delete items from PO table and POItem table
                purchaseOrderService.deleteById(purchaseOrder.getId());
                purchaseOrder.getItems().forEach(
                        poItem -> poItemService.deleteById(poItem.getId())
                );

                // notification

                updateList();
            });

            return receiveBtn;
        }).setHeader("Receive");


        // Delete button configuration
        orders.addComponentColumn(purchaseOrder -> {
            Button deleteBtn = new Button(new Icon(VaadinIcon.CLOSE));
            deleteBtn.addClickListener(event -> {
                Dialog confirm = new Dialog();
                confirm.setCloseOnEsc(false);
                confirm.setCloseOnOutsideClick(false);

                Button confirmBtn = new Button("Yes", Yes -> {
                    purchaseOrderService.deleteById(purchaseOrder.getId());

                    // delete items from poItem table
                    purchaseOrder.getItems().forEach(
                            poItem -> poItemService.deleteById(poItem.getId())
                    );

                    confirm.close();
                    updateList();
                });

                Button denyBtn = new Button("No", No -> {
                    confirm.close();
                });

                confirm.add(new VerticalLayout(new Text("Are you sure you want to delete this order?"),
                        new HorizontalLayout(confirmBtn, denyBtn)));
                confirm.open();
            });

            return deleteBtn;
        }).setHeader("Delete");

    }

    public void updateList() {
        provider.refreshAll();
    }
}

