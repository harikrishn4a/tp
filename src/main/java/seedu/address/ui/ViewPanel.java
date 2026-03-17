package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;

/**
 * Panel that displays the full profile of a viewed contact and (in future) their encounter history.
 */
public class ViewPanel extends UiPart<Region> {

    private static final String FXML = "ViewPanel.fxml";

    @FXML
    private VBox viewPanel;
    @FXML
    private Label placeholder;
    @FXML
    private VBox profileBox;
    @FXML
    private Label nameLabel;
    @FXML
    private FlowPane tagsPane;
    @FXML
    private Label aliasLabel;
    @FXML
    private Label stageLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private VBox encounterBox;
    @FXML
    private Label noEncountersLabel;

    public ViewPanel() {
        super(FXML);
    }

    /**
     * Displays the full profile of the given {@code person}.
     */
    public void setPerson(Person person) {
        placeholder.setVisible(false);
        placeholder.setManaged(false);
        profileBox.setVisible(true);
        profileBox.setManaged(true);

        nameLabel.setText(person.getName().fullName);

        tagsPane.getChildren().clear();
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tagsPane.getChildren().add(new Label(tag.tagName)));

        aliasLabel.setText(person.getAlias().toString());
        stageLabel.setText(person.getStage().toString());
        phoneLabel.setText(person.getPhone().value);
        emailLabel.setText(person.getEmail().value);
        addressLabel.setText(person.getAddress().value);

        noEncountersLabel.setVisible(true);
        noEncountersLabel.setManaged(true);
    }
}
