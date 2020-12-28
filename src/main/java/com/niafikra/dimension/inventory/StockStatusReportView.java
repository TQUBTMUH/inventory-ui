package com.niafikra.dimension.inventory;

import com.niafikra.dimension.core.navigation.MainNavigationLayout;
import com.niafikra.dimension.core.navigation.Nav;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Nav(value = "Stock Status Report",tree = {@Nav.Parent("Report")})
@Route(value = "stock-status-view",layout = MainNavigationLayout.class)
public class StockStatusReportView extends VerticalLayout {
}
