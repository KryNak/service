package zad1.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class AutoCompleteComboBox<T> {

    private ComboBox<T> comboBox;
    private String filter = "";
    private ObservableList<T> originalItems;

    public AutoCompleteComboBox(ComboBox<T> comboBox) {
        this.comboBox = comboBox;
        originalItems = FXCollections.observableArrayList(comboBox.getItems());
        comboBox.setOnKeyPressed(this::handleOnKeyPressed);
        comboBox.setOnHidden(this::handleOnHiding);
    }

    public static <T> void attachAutoCompleteFunctionalityTo(ComboBox<T> comboBox){
        new AutoCompleteComboBox<>(comboBox);
    }

    private void handleOnKeyPressed(KeyEvent e) {
        ObservableList<T> filteredList = FXCollections.observableArrayList();
        KeyCode code = e.getCode();

        if (code.isLetterKey()) {
            filter += e.getText();
        }
        if (code == KeyCode.ESCAPE || code == KeyCode.BACK_SPACE) {
            filter = "";
        }
        if (filter.isEmpty()) {
            filteredList = originalItems;
        } else {
            comboBox.getItems().stream()
                    .filter(el -> el.toString().toLowerCase().contains(filter.toLowerCase()))
                    .forEach(filteredList::add);

            comboBox.show();
        }
        comboBox.getItems().setAll(filteredList);
    }

    private void handleOnHiding(Event e) {
        filter = "";
        T s = comboBox.getSelectionModel().getSelectedItem();
        comboBox.getItems().setAll(originalItems);
        comboBox.getSelectionModel().select(s);
    }

}
