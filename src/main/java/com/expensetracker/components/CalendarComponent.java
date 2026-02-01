package com.expensetracker.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Custom calendar component built with Vaadin Flow (no addons).
 * Displays a calendar grid with clickable days and daily expense totals.
 */
public class CalendarComponent extends VerticalLayout {
    private final YearMonth yearMonth;
    private Consumer<LocalDate> onDaySelected;
    private final Map<Integer, BigDecimal> dailyTotals = new HashMap<>();
    private VerticalLayout calendarGrid;

    /**
     * Get daily total for a specific day.
     */
    public BigDecimal getDailyTotal(int day) {
        return dailyTotals.getOrDefault(day, BigDecimal.ZERO);
    }
}
