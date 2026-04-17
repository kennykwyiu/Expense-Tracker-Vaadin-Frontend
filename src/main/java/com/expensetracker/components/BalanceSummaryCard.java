package com.expensetracker.components;

import com.expensetracker.dto.MonthlyBalanceResponse;
import com.expensetracker.util.Logger;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * Balance Summary Card component for displaying monthly financial summary.
 * Shows: last month balance, income, budget, and current balance with progress bar.
 */
public class BalanceSummaryCard extends Div {

    private final Logger logger = new Logger(BalanceSummaryCard.class);

    private final Span lastMonthBalanceLabel = new Span();
    private final Span incomeThisWeekLabel = new Span();
    private final Span expenseBudgetLabel = new Span();
    private final Span currentBalanceLabel = new Span();
    private final ProgressBar budgetProgressBar = new ProgressBar();

    private final Button addIncomeBtn = new Button(VaadinIcon.PLUS.create());
    private final Button editBudgetBtn = new Button(VaadinIcon.EDIT.create());

    private Consumer<BigDecimal> onIncomeUpdate;
    private Consumer<BigDecimal> onBudgetUpdate;

    private MonthlyBalanceResponse currentBalance;


    public BalanceSummaryCard() {
        logger.info("Initializing BalanceSummaryCard");
        addClassNames(
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.MEDIUM
        );
        getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");

        // Title
        Span title = new Span("Monthly Balance Summary");
        title.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.LARGE);

        // Content layout
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(false);
        
        // Last month balance row
        HorizontalLayout lastMonthRow = new HorizontalLayout();
        lastMonthRow.setWidthFull();
        lastMonthRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        Span lastMonthLabel = new Span("Last Month Balance:");
        lastMonthLabel.addClassNames(LumoUtility.FontWeight.SEMIBOLD);
        lastMonthBalanceLabel.setText("$0.00");
        lastMonthBalanceLabel.addClassNames(LumoUtility.TextColor.SUCCESS);
        lastMonthRow.add(lastMonthLabel, lastMonthBalanceLabel);
        
        // Income this week row
        HorizontalLayout incomeRow = new HorizontalLayout();
        incomeRow.setWidthFull();
        incomeRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        incomeRow.setAlignItems(FlexComponent.Alignment.CENTER);
        Span incomeLabel = new Span("Income This Week:");
        incomeLabel.addClassNames(LumoUtility.FontWeight.SEMIBOLD);
        
        HorizontalLayout incomeContent = new HorizontalLayout();
        incomeContent.setAlignItems(FlexComponent.Alignment.CENTER);
        incomeThisWeekLabel.setText("$0.00");
        incomeThisWeekLabel.addClassNames(LumoUtility.TextColor.SUCCESS);
        
        addIncomeBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        addIncomeBtn.setTooltipText("Add income");
        addIncomeBtn.addClickListener(e -> openAddIncomeDialog());
        
        incomeContent.add(incomeThisWeekLabel, addIncomeBtn);
        incomeRow.add(incomeLabel, incomeContent);
        
        // Expense budget row
        HorizontalLayout budgetRow = new HorizontalLayout();
        budgetRow.setWidthFull();
        budgetRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        budgetRow.setAlignItems(FlexComponent.Alignment.CENTER);
        Span budgetLabel = new Span("Expense Budget:");
        budgetLabel.addClassNames(LumoUtility.FontWeight.SEMIBOLD);
        
        HorizontalLayout budgetContent = new HorizontalLayout();
        budgetContent.setAlignItems(FlexComponent.Alignment.CENTER);
        expenseBudgetLabel.setText("$0.00");
        expenseBudgetLabel.addClassNames(LumoUtility.TextColor.WARNING);
        
        editBudgetBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        editBudgetBtn.setTooltipText("Edit budget");
        editBudgetBtn.addClickListener(e -> openEditBudgetDialog());
        
        budgetContent.add(expenseBudgetLabel, editBudgetBtn);
        budgetRow.add(budgetLabel, budgetContent);
        
        // Progress bar
        budgetProgressBar.setWidth("100%");
        budgetProgressBar.setMin(0);
        budgetProgressBar.setMax(100);
        budgetProgressBar.setValue(0);
        
        // Current balance row (highlighted)
        HorizontalLayout balanceRow = new HorizontalLayout();
        balanceRow.setWidthFull();
        balanceRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        balanceRow.addClassNames(
                LumoUtility.Padding.SMALL,
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.BorderRadius.SMALL
        );
        Span balanceLabel = new Span("Current Balance:");
        balanceLabel.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.MEDIUM);
        currentBalanceLabel.setText("$0.00");
        currentBalanceLabel.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.MEDIUM);
        balanceRow.add(balanceLabel, currentBalanceLabel);
        
        // Add all rows to content
        content.add(
                lastMonthRow,
                incomeRow,
                budgetRow,
                budgetProgressBar,
                balanceRow
        );
        
        add(title, content);
    }

    /**
     * Update balance display with data
     */
    public void updateBalance(MonthlyBalanceResponse balance) {
        logger.info("Updating balance display");
        this.currentBalance = balance;

        if (balance == null) {
            logger.warn("Balance is null");
            return;
        }

        // Update labels
        lastMonthBalanceLabel.setText(formatCurrency(balance.getLastMonthBalance()));
        incomeThisWeekLabel.setText(formatCurrency(balance.getIncomeThisWeek()));
        expenseBudgetLabel.setText(formatCurrency(balance.getExpenseBudget()));
        currentBalanceLabel.setText(formatCurrency(balance.getCurrentBalance()));
        // Update progress bar (budget usage percentage)
        if (balance.getExpenseBudget() != null && balance.getExpenseBudget().compareTo(BigDecimal.ZERO) > 0) {
            double percentage = balance.getExpenseBudget().doubleValue() / balance.getExpenseBudget().doubleValue() * 100;
            budgetProgressBar.setValue(Math.min(percentage / 100, 1.0)); // Cap at 100%

            // Update color based on usage
            if (percentage >= 100) {
                budgetProgressBar.getStyle().set("--lumo-progress-value-background-color", "var(--lumo-error-color)");
            } else if (percentage >= 80) {
                budgetProgressBar.getStyle().set("--lumo-progress-value-background-color", "var(--lumo-warning-color)");
            } else {
                budgetProgressBar.getStyle().set("--lumo-progress-value-background-color", "var(--lumo-success-color)");
            }
        } else {
            budgetProgressBar.setValue(0);
        }
        // Update balance color
        if (balance.getCurrentBalance() != null) {
            if (balance.getCurrentBalance().compareTo(BigDecimal.ZERO) < 0) {
                currentBalanceLabel.removeClassNames(LumoUtility.TextColor.SUCCESS);
                currentBalanceLabel.addClassNames(LumoUtility.TextColor.ERROR);
            } else {
                currentBalanceLabel.removeClassNames(LumoUtility.TextColor.ERROR);
                currentBalanceLabel.addClassNames(LumoUtility.TextColor.SUCCESS);
            }
        }
    }

    /**
     * Open dialog to add income
     */
    private void openAddIncomeDialog() {
        logger.info("Opening add income dialog");

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add Income");
        dialog.setWidth("400px");

        FormLayout form = new FormLayout();
        BigDecimalField incomeField = new BigDecimalField("Income Amount");
//        incomeField.setMin(0);
        incomeField.setHelperText("Enter income amount");
        form.add(incomeField);

        Button saveBtn = new Button("Add", e -> {
            if (incomeField.getValue() != null && onIncomeUpdate != null) {
                logger.info("Adding income: " + incomeField.getValue());
                onIncomeUpdate.accept(incomeField.getValue());
                dialog.close();
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(saveBtn, cancelBtn);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        dialog.add(form, buttons);
        dialog.open();
    }

    /**
     * Open dialog to edit budget
     */
    private void openEditBudgetDialog() {
        logger.info("Opening edit budget dialog");

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edit Expense Budget");
        dialog.setWidth("400px");

        FormLayout form = new FormLayout();
        BigDecimalField budgetField = new BigDecimalField("Monthly Budget");
//        budgetField.setMin(0);
        budgetField.setHelperText("Enter monthly expense budget");
        if (currentBalance != null && currentBalance.getExpenseBudget() != null) {
            budgetField.setValue(currentBalance.getExpenseBudget());
        }
        form.add(budgetField);

        Button saveBtn = new Button("Save", e -> {
            if (budgetField.getValue() != null && onBudgetUpdate != null) {
                logger.info("Updating budget: " + budgetField.getValue());
                onBudgetUpdate.accept(budgetField.getValue());
                dialog.close();
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(saveBtn, cancelBtn);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        dialog.add(form, buttons);
        dialog.open();
    }

    /**
     * Format BigDecimal as currency string
     */
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }
        return String.format("$%.2f", amount);
    }
}
