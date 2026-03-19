package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private Label aliasesLabel;
    @FXML
    private Label stageLabel;
    @FXML
    private Label riskLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label notesLabel;
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
        stageLabel.setText(person.getStage().toString());
        addressLabel.setText(person.getAddress().value);
        aliasesLabel.setText(String.join(", ", person.getAliases().stream().map(a -> a.value).toList()));
        notesLabel.setText(person.getNotes().value);
        riskLabel.setText(person.getRisk().toString());

        noEncountersLabel.setVisible(true);
        noEncountersLabel.setManaged(true);
    }
}
