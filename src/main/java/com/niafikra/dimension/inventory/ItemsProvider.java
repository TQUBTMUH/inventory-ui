package com.niafikra.dimension.inventory;

import com.niafikra.dimension.inventory.entity.Item;
import com.niafikra.dimension.inventory.service.ItemService;
import com.niafikra.dimension.inventory.service.ItemServiceImp;
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
public class ItemsProvider extends PageableDataProvider<Item, ItemServiceImp.ItemFilter> {

    private final ItemService itemService;

    public ItemsProvider(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    protected Page<Item> fetchFromBackEnd(Query<Item, ItemServiceImp.ItemFilter> query, Pageable pageable) {

        return itemService.findAll(
                query.getFilter().orElse(new ItemServiceImp.ItemFilter()),
                pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return QuerySortOrder.asc("name").build();
    }

    @Override
    protected int sizeInBackEnd(Query<Item, ItemServiceImp.ItemFilter> query) {

        return itemService.count(
                query.getFilter().orElse(new ItemServiceImp.ItemFilter())).intValue();
    }
}

