package com.expensetracker.components;

import com.expensetracker.dto.MonthlyBalanceResponse;
import com.expensetracker.util.Logger;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.progressbar.ProgressBar;

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
}
