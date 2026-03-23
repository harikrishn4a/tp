package seedu.address.ui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;

/**
 * Panel that displays the full profile of a viewed contact including encounter history.
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
    private Label stageLabel;
    @FXML
    private Label riskBadge;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label aliasesLabel;
    @FXML
    private Label notesLabel;
    @FXML
    private FlowPane tagsPane;
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

        String riskStr = person.getRisk().toString().toUpperCase();
        riskBadge.setText(riskStr + " RISK");
        riskBadge.getStyleClass().setAll("view-risk-badge", "view-risk-" + riskStr.toLowerCase());

        phoneLabel.setText(person.getPhone().value);
        emailLabel.setText(person.getEmail().value);
        addressLabel.setText(person.getAddress().value);

        String aliasText = String.join(", ",
                person.getAliases().stream().map(a -> a.value).toList());
        aliasesLabel.setText(aliasText.isEmpty() ? "None" : aliasText);
        if (aliasText.isEmpty()) {
            aliasesLabel.getStyleClass().setAll("view-value-dim");
        } else {
            aliasesLabel.getStyleClass().setAll("view-value");
        }

        String notesText = person.getNotes().value;
        notesLabel.setText(notesText.isEmpty() ? "None" : notesText);
        if (notesText.isEmpty()) {
            notesLabel.getStyleClass().setAll("view-value-dim");
        } else {
            notesLabel.getStyleClass().setAll("view-value");
        }

        tagsPane.getChildren().clear();
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add("tag-label");
            tagsPane.getChildren().add(tagLabel);
        });
        if (person.getTags().isEmpty()) {
            Label noTags = new Label("None");
            noTags.getStyleClass().add("view-value-dim");
            tagsPane.getChildren().add(noTags);
        }

        populateEncounters(person.getEncounters());
    }

    private void populateEncounters(List<Encounter> encounters) {
        encounterBox.getChildren().clear();

        if (encounters.isEmpty()) {
            Label empty = new Label("No encounters logged.");
            empty.getStyleClass().add("view-value-dim");
            empty.setWrapText(true);
            encounterBox.getChildren().add(empty);
            return;
        }

        for (int i = encounters.size() - 1; i >= 0; i--) {
            Encounter enc = encounters.get(i);
            VBox card = buildEncounterCard(enc, encounters.size() - i);
            encounterBox.getChildren().add(card);
        }
    }

    private VBox buildEncounterCard(Encounter enc, int displayIndex) {
        VBox card = new VBox(4);
        card.getStyleClass().add("encounter-card");
        card.setPadding(new Insets(8));

        Label header = new Label("#" + displayIndex + "  " + enc.getFormattedDateTime()
                + "  —  " + enc.location);
        header.getStyleClass().add("encounter-header");
        header.setWrapText(true);

        Label desc = new Label(enc.description);
        desc.getStyleClass().add("view-value");
        desc.setWrapText(true);

        card.getChildren().addAll(header, desc);

        enc.outcome.ifPresent(out -> {
            Label outcomeLabel = new Label("Outcome: " + out);
            outcomeLabel.getStyleClass().add("encounter-outcome");
            outcomeLabel.setWrapText(true);
            card.getChildren().add(outcomeLabel);
        });

        Separator sep = new Separator();
        sep.getStyleClass().add("view-separator");
        card.getChildren().add(sep);

        return card;
    }
}
