package com.niafikra.dimension.inventory;

import com.niafikra.dimension.inventory.entity.Supplier;
import com.niafikra.dimension.inventory.service.SupplierService;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vaadin.artur.spring.dataprovider.PageableDataProvider;

import java.util.List;

import static com.niafikra.dimension.inventory.service.SupplierServiceImp.*;


@Component
@Scope("prototype")
public class SuppliersProvider extends PageableDataProvider<Supplier, SupplierFilter> {

    private SupplierService supplierService;

    public SuppliersProvider(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Override
    protected Page<Supplier> fetchFromBackEnd(Query<Supplier, SupplierFilter> query, Pageable pageable) {
        return supplierService.findAll(query.getFilter().orElse(new SupplierFilter()), pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return QuerySortOrder.asc("name").build();
    }

    @Override
    protected int sizeInBackEnd(Query<Supplier, SupplierFilter> query) {
        return supplierService.count(query.getFilter().orElse(new SupplierFilter())).intValue();
    }
}
