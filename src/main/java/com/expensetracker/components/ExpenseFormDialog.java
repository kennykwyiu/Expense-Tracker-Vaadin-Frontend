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
     * Add expense item to batch list
     */
    private void addExpenseItem() {
        logger.info("Adding expense item");

        // Validate current form
        if (amountField.getValue() == null || amountField.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Invalid amount");
            return;
        }

        // Create current item
        ExpenseItem currentItem = new ExpenseItem(
                datePicker.getValue(),
                amountField.getValue(),
                categoryCombo.getValue(),
                descriptionArea.getValue()
        );
        expenseItems.add(0, currentItem); // Add to top

        // Display added item
        Div itemDiv = createItemDisplay(currentItem, expenseItems.indexOf(currentItem));
        itemsContainer.addComponentAsFirst(itemDiv);
        itemsContainer.setVisible(true);

        logger.info("Item added. Total items: " + expenseItems.size());

        // Reset form
//        datePicker.setValue(LocalDate.now());
        amountField.clear();
        amountField.focus();
        categoryCombo.setValue("Food");
        descriptionArea.clear();
        descriptionVisible = false;
        descriptionArea.setVisible(false);
        toggleDescriptionBtn.setText("Add Note");
        toggleDescriptionBtn.setIcon(VaadinIcon.PLUS.create());
    }

    /**
     * Create display for added item with delete button
     */
    private Div createItemDisplay(ExpenseItem item, int index) {
        Div itemDiv = new Div();
        itemDiv.addClassNames(
                LumoUtility.Padding.SMALL,
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.MEDIUM
        );
        itemDiv.getStyle().set("display", "flex");
        itemDiv.getStyle().set("justify-content", "space-between");
        itemDiv.getStyle().set("align-items", "center");

        StringBuilder itemText = new StringBuilder();
        itemText.append(String.format("%s - %s ($%.2f)",
                item.date,
                item.category,
                item.amount));

        if (item.description != null && !item.description.isEmpty()) {
            itemText.append(" - ").append(item.description);
        }

        Span itemSpan = new Span(itemText.toString());

        Button deleteBtn = new Button(VaadinIcon.TRASH.create());
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
        deleteBtn.addClickListener(e -> {
            expenseItems.remove(index);
            itemsContainer.remove(itemDiv);
            if (expenseItems.isEmpty()) {
                itemsContainer.setVisible(false);
            }
            logger.info("Item removed. Total items: " + expenseItems.size());
        });

        itemDiv.add(itemSpan, deleteBtn);
        return itemDiv;
    }

    /**
     * Save all expenses
     */
    private void save() {
        logger.info("Saving expenses");

        // Handle edit mode
        if (isEditMode && editingExpenseId != null) {
            if (amountField.getValue() != null && amountField.getValue().compareTo(BigDecimal.ZERO) > 0) {
                UpdateExpenseRequest request = new UpdateExpenseRequest();
                request.setDate(datePicker.getValue());
                request.setAmount(amountField.getValue());
                request.setCategory(categoryCombo.getValue());
                request.setDescription(descriptionArea.getValue());

                if (onUpdate != null) {
                    logger.info("Updating expense ID: " + editingExpenseId);
                    onUpdate.accept(request);
                }
            }
        } else {
            // Handle create mode - add current form data if valid
            if (amountField.getValue() != null && amountField.getValue().compareTo(BigDecimal.ZERO) > 0) {
                ExpenseItem currentItem = new ExpenseItem(
                        datePicker.getValue(),
                        amountField.getValue(),
                        categoryCombo.getValue(),
                        descriptionArea.getValue()
                );
                expenseItems.add(currentItem);
            }

            if (expenseItems.isEmpty()) {
                logger.warn("No expenses to save");
                return;
            }

            if (onSave != null) {
                List<CreateExpenseRequest> requests = new ArrayList<>();
                for (ExpenseItem item : expenseItems) {
                    CreateExpenseRequest request = new CreateExpenseRequest();
                    request.setDate(item.date);
                    request.setAmount(item.amount);
                    request.setCategory(item.category);
                    request.setDescription(item.description);
                    requests.add(request);
                }
                logger.info("Saving " + requests.size() + " expenses");
                onSave.accept(requests);
            }
        }

        close();
    }

    /**
     * Set callback for save action
     */
    public void setOnSave(Consumer<List<CreateExpenseRequest>> callback) {
        this.onSave = callback;
    }

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
