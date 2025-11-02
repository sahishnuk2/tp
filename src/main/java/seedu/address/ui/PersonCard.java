package seedu.address.ui;

import java.util.Comparator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.Trackable;
import seedu.address.model.person.TrackerColour;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label studentId;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane exerciseStatus;
    @FXML
    private Label githubUsername;
    @FXML
    private FlowPane labAttendance;
    @FXML
    private FlowPane grades;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        //render one-based index
        this.person = person;
        id.setText(displayedIndex + ". ");
        //Set student details
        studentId.setText(person.getStudentId().value);
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        githubUsername.setText(person.getGithubUsername().value);

        // Render tags
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        // Render trackable sections
        renderTrackable(exerciseStatus, person.getExerciseTracker(), "exercise");
        renderTrackable(labAttendance, person.getLabAttendanceList(), "lab");
        renderTrackable(grades, person.getGradeTracker(), "exam");
    }

    /**
     * Renders a generic Trackable object (e.g., ExerciseList, LabList, GradeMap)
     * into the provided FlowPane using its labels and tracker colours.
     */
    private void renderTrackable(FlowPane pane, Trackable trackable, String baseClass) {
        List<TrackerColour> colours = trackable.getTrackerColours();
        List<String> labels = trackable.getLabels();
        assert labels.size() == colours.size() : "There must be the same number of labels and colours";
        pane.getChildren().clear();
        for (int i = 0; i < labels.size(); i++) {
            Label label = new Label(labels.get(i));
            String colourClass = switch (colours.get(i)) {
            case GREEN -> baseClass + "-green";
            case RED -> baseClass + "-red";
            case GREY -> baseClass + "-grey";
            };
            label.getStyleClass().addAll("status-label", colourClass);
            pane.getChildren().add(label);
        }
    }
}
