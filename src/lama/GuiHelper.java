package lama;

import javafx.scene.control.Alert;

class GuiHelper {
    /**
     * Erzeigt ein Popup mit gewünschten Texten
     * @param alertType Art des Popups
     * @param title Titel des Popups
     * @param headerText Header des Popups
     * @param contentText Content des Popups
     */
    static void showErrorOrWarningAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.setHeight(500);
        alert.setWidth(400);
        alert.showAndWait();
    }
}
