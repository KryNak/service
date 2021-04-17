package zad1.components;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CustomDialog {

    public static Dialog<ButtonType> createFromComponents(ComboBox<String> countryComboBox, ComboBox<String> currencyCodesComboBox, TextField cityTextField){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setHeaderText("Please type in demanded information right under this text.");
        dialog.setGraphic(new ImageView("/zad1/resources/baseline_info_black_36dp.png"));
        ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image("zad1/resources/baseline_smart_toy_black_48dp.png"));

        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(5);
        pane.add(new Label("Country:"), 1, 1);
        pane.add(countryComboBox, 2, 1);
        pane.add(new Label("City:"), 1, 2);
        pane.add(cityTextField, 2, 2);
        pane.add(new Label("Currency Code:"), 1, 3);
        pane.add(currencyCodesComboBox, 2, 3);

        dialog.getDialogPane().setContent(pane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        return dialog;
    }

}
