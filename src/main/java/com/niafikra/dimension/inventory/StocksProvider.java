package com.niafikra.dimension.inventory;

import com.niafikra.dimension.inventory.entity.Stock;
import com.niafikra.dimension.inventory.service.StockService;
import com.niafikra.dimension.inventory.service.StockServiceImp.StockFilter;
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
public class StocksProvider extends PageableDataProvider<Stock, StockFilter> {

    StockService stockService;

    public StocksProvider(StockService stockService) {
        this.stockService = stockService;
    }

    @Override
    protected Page<Stock> fetchFromBackEnd(Query<Stock, StockFilter> query, Pageable pageable) {
        return stockService.findAll(
                query.getFilter().orElse(new StockFilter()),
                pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return QuerySortOrder.desc("quantity").build();
    }

    @Override
    protected int sizeInBackEnd(Query<Stock, StockFilter> query) {
        return stockService.count(query.getFilter().orElse(new StockFilter())).intValue();
    }
}

