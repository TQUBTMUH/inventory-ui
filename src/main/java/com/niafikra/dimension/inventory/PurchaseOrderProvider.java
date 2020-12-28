package com.niafikra.dimension.inventory;

import com.niafikra.dimension.inventory.entity.PurchaseOrder;
import com.niafikra.dimension.inventory.service.PurchaseOrderService;
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
public class PurchaseOrderProvider extends PageableDataProvider<PurchaseOrder,Void> {

    private PurchaseOrderService purchaseOrderService;

    public PurchaseOrderProvider(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @Override
    protected Page<PurchaseOrder> fetchFromBackEnd(Query<PurchaseOrder, Void> query, Pageable pageable) {
        return purchaseOrderService.findAll(pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return QuerySortOrder.desc("orderDate").build();
    }

    @Override
    protected int sizeInBackEnd(Query<PurchaseOrder, Void> query) {
        return purchaseOrderService.count().intValue();
    }
}
