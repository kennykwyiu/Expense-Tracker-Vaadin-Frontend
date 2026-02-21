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

    public ExpensesView(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.currentMonth = YearMonth.now();

        setSpacing(true);
        setPadding(true);

        // Header
        add(createHeader());

        // Month/Year Picker
        add(createMonthPicker());

        // Tabs for Calendar and List views
        Tabs tabs = createTabs();
        add(tabs);

        // Calendar Container (will hold the calendar component)
        calendarContainer = new VerticalLayout();
        calendarContainer.setSpacing(true);
        calendarContainer.setPadding(false);
        calendarContainer.setWidth("100%");
        add(calendarContainer);

        // Grid for list view
        expenseGrid = createExpenseGrid();
        expenseGrid.setVisible(false);
        add(expenseGrid);

        // Total Span
        totalSpan = new Span();
        totalSpan.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);
        add(totalSpan);

        // Add Expense Button
        Button addExpenseBtn = new Button("Add Expense", VaadinIcon.PLUS.create());
        addExpenseBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addExpenseBtn.addClickListener(e -> openExpenseForm(null));
        add(addExpenseBtn);

        // Load initial data
        loadExpenses();
    }

    private H2 createHeader() {
        H2 title = new H2("Daily Expenses");
        title.addClassNames(LumoUtility.Margin.MEDIUM);
        return title;
    }

    private HorizontalLayout createMonthPicker() {
        ComboBox<String> monthCombo = new ComboBox<>();
        monthCombo.setLabel("Month");
        monthCombo.setItems("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
        monthCombo.setValue(currentMonth.getMonth().toString());
        monthCombo.addValueChangeListener(e -> {
            int monthIndex = monthCombo.getListDataView().getItems().collect(Collectors.toList()).indexOf(e.getValue()) + 1;
            currentMonth = YearMonth.of(currentMonth.getYear(), monthIndex);
            loadExpenses();
        });

        ComboBox<Integer> yearCombo = new ComboBox<>();
        yearCombo.setLabel("Year");
        List<Integer> years = new ArrayList<>();
        for (int i = 2020; i <= 2030; i++) {
            years.add(i);
        }
        yearCombo.setItems(years);
        yearCombo.setValue(currentMonth.getYear());
        yearCombo.addValueChangeListener(e -> {
            currentMonth = YearMonth.of(e.getValue(), currentMonth.getMonthValue());
            loadExpenses();
        });

        HorizontalLayout layout = new HorizontalLayout(monthCombo, yearCombo);
        layout.setSpacing(true);
        return layout;
    }

    private Tabs createTabs() {
        Tab calendarTab = new Tab("Calendar");
        Tab listTab = new Tab("List");

        Tabs tabs = new Tabs(calendarTab, listTab);
        tabs.addSelectedChangeListener(e -> {
            if (e.getSelectedTab() == calendarTab) {
                calendarContainer.setVisible(true);
                expenseGrid.setVisible(false);
            } else {
                calendarContainer.setVisible(false);
                expenseGrid.setVisible(true);
            }
        });

        return tabs;
    }

    private Grid<ExpenseResponse> createExpenseGrid() {
        Grid<ExpenseResponse> grid = new Grid<>(ExpenseResponse.class, false);
        grid.setWidth("100%");

        grid.addColumn(expense -> expense.getDate().toString()).setHeader("Date").setFlexGrow(1);
        grid.addColumn(expense -> "$" + String.format("%.2f", expense.getAmount())).setHeader("Amount").setFlexGrow(1);
        grid.addColumn(ExpenseResponse::getCategory).setHeader("Category").setFlexGrow(1);
        grid.addColumn(ExpenseResponse::getDescription).setHeader("Description").setFlexGrow(1);

        grid.addComponentColumn(expense -> {
            Button editBtn = new Button("Edit", VaadinIcon.EDIT.create());
            editBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            editBtn.addClickListener(e -> editExpense(expense));

            Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create());
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(e -> deleteExpense(expense));

            HorizontalLayout actions = new HorizontalLayout(editBtn, deleteBtn);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Actions").setWidth("150px");

        return grid;
    }

    private void loadExpenses() {
        try {
            logger.info("Loading expenses for " + currentMonth);
            currentData = apiClient.listExpenses(currentMonth.getYear(), currentMonth.getMonthValue());

            if (currentData != null) {
                expenseGrid.setItems(currentData.getExpenses());
                updateTotal();
                renderCalendar();
            }
        } catch (Exception e) {
            logger.error("Error loading expenses: " + e.getMessage());
            showNotification("Error loading expenses: " + e.getMessage());
        }
    }

    private void renderCalendar() {
        // Remove old calendar component
        calendarContainer.removeAll();

        // Create new calendar component for the current month
        calendarComponent = new CalendarComponent(currentMonth);
        calendarComponent.setOnDaySelected(this::onDaySelected);

        if (currentData != null && currentData.getExpenses() != null && !currentData.getExpenses().isEmpty()) {
            // Calculate daily totals from expenses
            java.util.Map<Integer, BigDecimal> dayTotals = new java.util.HashMap<>();
            for (ExpenseResponse expense : currentData.getExpenses()) {
                int day = expense.getDate().getDayOfMonth();
                BigDecimal currentTotal = dayTotals.getOrDefault(day, BigDecimal.ZERO);
                BigDecimal newTotal = currentTotal.add(expense.getAmount());
                dayTotals.put(day, newTotal);
                logger.info("Day " + day + ": " + expense.getAmount() + " -> Total: " + newTotal);
            }

            // Mark days with expenses and their totals
            logger.info("Marking " + dayTotals.size() + " days with expenses");
            for (java.util.Map.Entry<Integer, BigDecimal> entry : dayTotals.entrySet()) {
                logger.info("  Day " + entry.getKey() + ": $" + entry.getValue());
            }
            calendarComponent.markDaysWithExpenses(dayTotals);
        } else {
            logger.info("No expenses found for " + currentMonth);
        }

        // Add the calendar to the container
        calendarContainer.add(calendarComponent);
    }

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

    private void saveExpenses(List<CreateExpenseRequest> expenses) {
        try {
            logger.info("Saving " + expenses.size() + " expenses");
            if (expenses.size() == 1) {
                apiClient.createExpense(
                        expenses.get(0).getDate(),
                        expenses.get(0).getAmount(),
                        expenses.get(0).getCategory(),
                        expenses.get(0).getDescription()
                );
            } else {
                apiClient.batchCreateExpenses(expenses);
            }
            loadExpenses();
            showNotification("Expense(s) saved successfully");
        } catch (Exception e) {
            logger.error("Error saving expenses: " + e.getMessage());
            showNotification("Error saving expenses: " + e.getMessage());
        }
    }

    private void editExpense(ExpenseResponse expense) {
        logger.info("Editing expense ID: " + expense.getId());
        try {
            ExpenseFormDialog dialog = new ExpenseFormDialog(expense.getId());
            dialog.setExpenseData(
                    expense.getDate(),
                    expense.getAmount(),
                    expense.getCategory(),
                    expense.getDescription()
            );
            dialog.setOnSave(this::saveExpenses);
            dialog.open();
        } catch (Exception e) {
            logger.error("Error opening edit form: " + e.getMessage());
            showNotification("Error opening edit form: " + e.getMessage());
        }
    }

    private void deleteExpense(ExpenseResponse expense) {
        logger.info("Deleting expense ID: " + expense.getId());
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Delete Expense");
        confirmDialog.add(new Span("Are you sure you want to delete this expense?"));

        Button deleteBtn = new Button("Delete", e -> {
            try {
                apiClient.deleteExpense(expense.getId());
                loadExpenses();
                showNotification("Expense deleted successfully");
                confirmDialog.close();
            } catch (Exception ex) {
                logger.error("Error deleting expense: " + ex.getMessage());
                showNotification("Error deleting expense: " + ex.getMessage());
            }
        });
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelBtn = new Button("Cancel", e -> confirmDialog.close());

        confirmDialog.getFooter().add(deleteBtn, cancelBtn);
        confirmDialog.open();
    }

    private void showNotification(String message) {
        com.vaadin.flow.component.notification.Notification.show(message);
    }
}
