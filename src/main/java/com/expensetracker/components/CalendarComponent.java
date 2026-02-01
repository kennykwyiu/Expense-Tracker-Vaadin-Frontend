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


    private HorizontalLayout createDayNamesHeader() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        layout.setSpacing(true);

        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : dayNames) {
            Span daySpan = new Span(dayName);
            daySpan.addClassNames(
                    LumoUtility.FontWeight.BOLD,
                    LumoUtility.TextAlignment.CENTER,
                    LumoUtility.Width.FULL
            );
            layout.add(daySpan);
        }

        return layout;
    }

    private Div createDayCell(int day) {
        Div cell = new Div();
        cell.setWidth("14.28%");
        cell.setHeight("120px");
        cell.addClassNames(
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST_10,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderRadius.MEDIUM
        );

        // Styling
        cell.getStyle().set("cursor", "pointer");
        cell.getStyle().set("transition", "all 0.2s ease");
        cell.getStyle().set("display", "flex");
        cell.getStyle().set("flex-direction", "column");
        cell.getStyle().set("align-items", "center");
        cell.getStyle().set("justify-content", "center");
        cell.getStyle().set("gap", "8px");

        // Day number
        Span dayNumber = new Span(String.valueOf(day));
        dayNumber.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.LARGE);

        // Daily total amount
        Span totalAmount = new Span();
        BigDecimal dayTotal = dailyTotals.get(day);

        if (dayTotal != null && dayTotal.compareTo(BigDecimal.ZERO) > 0) {
            totalAmount.setText("$" + String.format("%.2f", dayTotal));
            totalAmount.addClassNames(
                    LumoUtility.FontWeight.SEMIBOLD,
                    LumoUtility.FontSize.MEDIUM,
                    LumoUtility.TextColor.SUCCESS
            );
        }

        // Add components to cell
        cell.add(dayNumber);
        if (!totalAmount.getText().isEmpty()) {
            cell.add(totalAmount);
        }

        // Click handler
        cell.addClickListener(e -> {
            LocalDate selectedDate = yearMonth.atDay(day);
            if (onDaySelected != null) {
                onDaySelected.accept(selectedDate);
            }
        });

        // Hover effect
        cell.getElement().addEventListener("mouseenter", e -> {
            cell.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
            cell.getStyle().set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
        });

        cell.getElement().addEventListener("mouseleave", e -> {
            cell.getStyle().set("background-color", "transparent");
            cell.getStyle().set("box-shadow", "none");
        });

        return cell;
    }

    private Div createEmptyCell() {
        Div cell = new Div();
        cell.setWidth("14.28%");
        cell.setHeight("120px");
        cell.addClassNames(LumoUtility.BorderRadius.MEDIUM);
        return cell;
    }

    /**
     * Get daily total for a specific day.
     */
    public BigDecimal getDailyTotal(int day) {
        return dailyTotals.getOrDefault(day, BigDecimal.ZERO);
    }
}
