package com.niafikra.dimension.inventory;

import com.niafikra.dimension.inventory.entity.Item;
import com.niafikra.dimension.inventory.service.ItemService;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vaadin.artur.spring.dataprovider.PageableDataProvider;

import java.util.List;

@Component
@Scope("prototype")
public class POItemsProvider extends PageableDataProvider<Item, Void> {

    private ItemService itemService;

    public POItemsProvider(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    protected Page<Item> fetchFromBackEnd(Query<Item, Void> query, Pageable pageable) {
        return itemService.findAll(pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return QuerySortOrder.asc("name").build();
    }

    @Override
    protected int sizeInBackEnd(Query<Item, Void> query) {
        return itemService.count().intValue();
    }
}
