package client_gui;

import java.util.function.Function;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class ShowButtonTableCell<S> extends TableCell<S, Button> {

    private final Button actionButton;

    public ShowButtonTableCell(String label, Function<S,S> function) {
        //this.getStyleClass().add("action-button-table-cell");

        this.actionButton = new Button();
		Image image1 = new Image("/javafx_images/details.png", 24, 24, true, true);
		ImageView imageView1 = new ImageView(image1);
		imageView1.setImage(image1);
		actionButton.setGraphic(imageView1);
		actionButton.setStyle("-fx-cursor:hand;");
        this.actionButton.setOnAction((ActionEvent e) -> {
            function.apply(getCurrentItem());
        });
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
    }

    public S getCurrentItem() {
        return (S) getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Function<S,S> function) {
        return param -> new ShowButtonTableCell<>(label, function);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {                
            setGraphic(actionButton);
        }
    }
}