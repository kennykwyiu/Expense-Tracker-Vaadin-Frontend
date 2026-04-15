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
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.BigDecimalField;

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
}
