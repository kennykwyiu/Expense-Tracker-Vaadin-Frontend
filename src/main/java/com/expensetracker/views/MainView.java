package com.expensetracker.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Main application layout with navigation sidebar.
 */
public class MainView extends AppLayout {

    public MainView() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        var toggle = new DrawerToggle();
        var title = new H1("Daily Expense Tracker");
        title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM);

        addToNavbar(toggle, title);
    }

    private void createDrawer() {
        var nav = new SideNav();

        var expensesItem = new SideNavItem("Expenses", ExpensesView.class, VaadinIcon.WALLET.create());
        nav.addItem(expensesItem);

        addToDrawer(nav);
    }
}
