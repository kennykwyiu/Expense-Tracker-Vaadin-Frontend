package com.expensetracker.components;

import com.expensetracker.dto.CreateExpenseRequest;
import com.expensetracker.dto.UpdateExpenseRequest;
import com.expensetracker.util.Logger;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Expense form dialog for creating and editing expenses.
 * Supports single and batch expense entry with toggleable description.
 */
public class ExpenseFormDialog extends Dialog {
    private final Logger logger = new Logger(ExpenseFormDialog.class);

    private final DatePicker datePicker;
    private final BigDecimalField amountField;
    private final ComboBox<String> categoryCombo;
    private final TextArea descriptionArea;
    private final VerticalLayout itemsContainer;
    private final List<ExpenseItem> expenseItems = new ArrayList<>();
    private final Button toggleDescriptionBtn;

    private Consumer<List<CreateExpenseRequest>> onSave;
    private Consumer<UpdateExpenseRequest> onUpdate;
    private Integer editingExpenseId;
    private boolean isEditMode = false;
    private boolean descriptionVisible = false;

    /**
     * Set callback for update action
     */
    public void setOnUpdate(Consumer<UpdateExpenseRequest> callback) {
        this.onUpdate = callback;
    }

    /**
     * Set the date for the form
     */
    public void setDate(LocalDate date) {
        datePicker.setValue(date);
    }

    /**
     * Populate form with expense data for editing
     */
    public void setExpenseData(LocalDate date, BigDecimal amount, String category, String description) {
        logger.info("Setting expense data for edit");
        datePicker.setValue(date);
        amountField.setValue(amount);
        categoryCombo.setValue(category);
        descriptionArea.setValue(description != null ? description : "");

        // Show description if it has content
        if (description != null && !description.isEmpty()) {
            descriptionVisible = true;
            descriptionArea.setVisible(true);
            toggleDescriptionBtn.setText("Hide Note");
            toggleDescriptionBtn.setIcon(VaadinIcon.MINUS.create());
        }
    }

    /**
     * Inner class to hold expense item data
     */
    private static class ExpenseItem {
        LocalDate date;
        BigDecimal amount;
        String category;
        String description;

        ExpenseItem(LocalDate date, BigDecimal amount, String category, String description) {
            this.date = date;
            this.amount = amount;
            this.category = category;
            this.description = description;
        }
    }
}
