package com.expensetracker.views;

import com.expensetracker.components.CalendarComponent;
import com.expensetracker.components.ExpenseFormDialog;
import com.expensetracker.dto.CreateExpenseRequest;
import com.expensetracker.dto.ExpenseResponse;
import com.expensetracker.dto.ListExpensesResponse;
import com.expensetracker.dto.UpdateExpenseRequest;
import com.expensetracker.service.ApiClient;
import com.expensetracker.util.Logger;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main expenses view with calendar and list tabs.
 */
@Route(value = "expense", layout = MainView.class)
@PageTitle("Expenses")
public class ExpensesView extends VerticalLayout {
    private final ApiClient apiClient;
    private final Logger logger = new Logger(ExpensesView.class);

    private YearMonth currentMonth;
    private ListExpensesResponse currentData;

    private final Grid<ExpenseResponse> expenseGrid;
    private final Span totalSpan;
    private VerticalLayout calendarContainer;
    private CalendarComponent calendarComponent;


    private void updateTotal() {
        if (currentData != null && currentData.getTotal() != null) {
            totalSpan.setText("Total: " + currentData.getTotal());
        }
    }

    private void openExpenseForm(LocalDate date) {
        logger.info("Opening expense form");
        ExpenseFormDialog dialog = new ExpenseFormDialog();
        if (date != null) {
            dialog.setDate(date);
        }
        dialog.setOnSave(this::saveExpenses);
        dialog.open();
    }

    private void onDaySelected(LocalDate date) {
        logger.info("Day selected: " + date);
        openExpenseForm(date);
    }


    private void showNotification(String message) {
        com.vaadin.flow.component.notification.Notification.show(message);
    }
}
