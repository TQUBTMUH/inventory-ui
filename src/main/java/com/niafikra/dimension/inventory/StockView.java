package com.niafikra.dimension.inventory;

import com.niafikra.dimension.core.navigation.MainNavigationLayout;
import com.niafikra.dimension.core.navigation.Nav;
import com.niafikra.dimension.inventory.entity.Item;
import com.niafikra.dimension.inventory.entity.Stock;
import com.niafikra.dimension.inventory.service.StockService;
import com.niafikra.dimension.inventory.service.StockServiceImp;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

import static com.niafikra.dimension.core.navigation.Nav.Parent.ROOT;
import static com.niafikra.dimension.core.navigation.Nav.Parent.SETTING;
import static com.vaadin.flow.component.icon.VaadinIcon.HOME;

@Nav(value = "Stocks",
        icon = HOME,
        tree = {@Nav.Parent(ROOT)})
@Route(value = "stocks-view", layout = MainNavigationLayout.class)
public class StockView extends VerticalLayout {

    private StockService stockService;
    private StocksProvider stocksProvider;

    private Grid<Stock> stockGrid = new Grid<Stock>();
    private TextField itemNameFilter;
    private IntegerField quantityFilter;

    private StockServiceImp.StockFilter filter;
    private ConfigurableFilterDataProvider<Stock, Void, StockServiceImp.StockFilter> filterConfigurableProvider;

    public StockView(StockService stockService, StocksProvider stocksProvider) {
        this.stockService = stockService;
        this.stocksProvider = stocksProvider;

        filter = new StockServiceImp.StockFilter();
        filterConfigurableProvider = stocksProvider.withConfigurableFilter();
        filterConfigurableProvider.setFilter(filter);

        add(stockGrid);
        updateList();
    }

    @PostConstruct
    private void configureStockGrid() {
        stockGrid.setDataProvider(stocksProvider);

        stockGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_ROW_BORDERS);

        stockGrid.addColumn(stock -> {
            Item item = stock.getItem();
            return item == null ? "-" : item.getName();
        }).setHeader("Item Name").setKey("name");
        stockGrid.addColumn(Stock::getQuantity).setHeader("Quantity").setKey("quantity");

        stockGrid.addComponentColumn(stock -> {
            Button deleteBtn = new Button(new Icon(VaadinIcon.CLOSE));
            deleteBtn.addClickListener(event -> {
                Dialog alert = new Dialog();
                alert.setCloseOnEsc(false);
                alert.setCloseOnOutsideClick(false);
                Text confirm = new Text("Are you sure you want to alert this stock");
                Button confirmButton = new Button("Yes", yes -> {
                    deleteStock(stock.getId());
                    alert.close();
                });

                Button denyButton = new Button("No", no -> {
                    alert.close();
                });

                HorizontalLayout buttons = new HorizontalLayout(confirmButton, denyButton);
                VerticalLayout layout = new VerticalLayout(confirm, buttons);

                alert.add(layout);
                alert.open();
            });

            return deleteBtn;
        }).setHeader("Delete");


        // filter row
        HeaderRow filterRow = stockGrid.appendHeaderRow();

        // itemName filter
        itemNameFilter = new TextField();
        itemNameFilter.addValueChangeListener(event -> {
            filter.setItem(stockService.findByItemName(event.getValue()));
            filterConfigurableProvider.refreshAll();
        });
        itemNameFilter.setValueChangeMode(ValueChangeMode.LAZY);
        filterRow.getCell(stockGrid.getColumnByKey("name"))
                .setComponent(itemNameFilter);
        itemNameFilter.setSizeFull();
        itemNameFilter.setPlaceholder("Filter");

        // quantity filter
        quantityFilter = new IntegerField();
        quantityFilter.addValueChangeListener(event -> {
            filter.setQuantity(event.getValue());
            filterConfigurableProvider.refreshAll();
        });
        quantityFilter.setValueChangeMode(ValueChangeMode.LAZY);
        filterRow.getCell(stockGrid.getColumnByKey("quantity"))
                .setComponent(quantityFilter);
        quantityFilter.setSizeFull();
        quantityFilter.setPlaceholder("Filter");
    }

    // update stock list
    private void updateList() {
        filterConfigurableProvider.refreshAll();
    }

    // refresh stock list, used by filters
//    private void refresh() {
//        if (itemNameFilter.isEmpty() && quantityFilter.isEmpty()) {
//            updateList();
//        } else if (!(itemNameFilter.isEmpty()) && quantityFilter.isEmpty()) {
//            stockService.findAll(filter);
//        } else if (itemNameFilter.isEmpty() && !(quantityFilter.isEmpty())) {
//            stockService.findAll(filter);
//            // all filter fields have value
//        } else {
//            stockService.findAll(filter);
//        }
//    }

    // Delete stock item
    private void deleteStock(Long theId) {
        stockService.deleteById(theId);
        updateList();
    }

}

