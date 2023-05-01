import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;
import java.io.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.scene.control.cell.*;
import javafx.beans.property.*;

public class FridgeFX extends Application {

	// used as ChoiceBox value for filter
	public enum FILTER_COLUMNS {
		ITEM,
		SECTION,
		BOUGHT_DAYS_AGO
	};

	// the data source controller
	private FridgeDSC fridgeDSC;


	public void init() throws Exception {
		// creating an instance of the data source controller to be used
		// in this application
		fridgeDSC = new FridgeDSC();

		/* TODO 2-01 - TO COMPLETE ****************************************
		 * call the data source controller database connect method
		 * NOTE: that database connect method throws exception
		 */
		try {
			fridgeDSC.connect();
		} catch(Exception exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}

	public void start(Stage stage) throws Exception {

		/* TODO 2-02 - TO COMPLETE ****************************************
		 * - this method is the start method for your application
		 * - set application title
		 * - show the stage
		 */
		build(stage);
		stage.setTitle("What's In My Fridge v1.0");
		stage.show();


		/* TODO 2-03 - TO COMPLETE ****************************************
		 * currentThread uncaught exception handler
		 */
		Thread.currentThread().setUncaughtExceptionHandler((thread, exception) ->
      	{
			System.out.println("ERROR: " + exception);
			Alert alert = new Alert(Alert.AlertType.WARNING); alert.setContentText("Exception thrown: " + exception); alert.showAndWait();
		});
	}

	public void build(Stage stage) throws Exception {

		// Create table data (an observable list of objects)
		ObservableList<Grocery> tableData = FXCollections.observableArrayList(fridgeDSC.getAllGroceries());

		// Define table columns
		TableColumn<Grocery, Integer> idColumn = new TableColumn<Grocery, Integer>("Id");
		TableColumn<Grocery, String> itemNameColumn = new TableColumn<Grocery, String>("Item");
		TableColumn<Grocery, Integer> quantityColumn = new TableColumn<Grocery, Integer>("QTY");
		TableColumn<Grocery, String> sectionColumn = new TableColumn<Grocery, String>("Section");
		TableColumn<Grocery, String> daysAgoColumn = new TableColumn<Grocery, String>("Bought");

		/* TODO 2-04 - TO COMPLETE ****************************************
		 * for each column defined, call their setCellValueFactory method
		 * using an instance of PropertyValueFactory
		 */
		idColumn.setCellValueFactory(
			new PropertyValueFactory<Grocery, Integer>("Id"));
		itemNameColumn.setCellValueFactory(
			new PropertyValueFactory<Grocery, String>("ItemName"));
		quantityColumn.setCellValueFactory(
			new PropertyValueFactory<Grocery, Integer>("Quantity"));
		sectionColumn.setCellValueFactory(
			new PropertyValueFactory<Grocery, String>("Section"));
		daysAgoColumn.setCellValueFactory(
			new PropertyValueFactory<Grocery, String>("DaysAgo"));


		// Create the table view and add table columns to it
		TableView<Grocery> tableView = new TableView<Grocery>();


		/* TODO 2-05 - TO COMPLETE ****************************************
		 * add table columns to the table view create above
		 */
		tableView.getColumns().add(idColumn);
		tableView.getColumns().add(itemNameColumn);
		tableView.getColumns().add(quantityColumn);
		tableView.getColumns().add(sectionColumn);
		tableView.getColumns().add(daysAgoColumn);


		//	Attach table data to the table view
		tableView.setItems(tableData);


		/* TODO 2-06 - TO COMPLETE ****************************************
		 * set minimum and maximum width to the table view and each columns
		 */
		tableView.setMinWidth(1000);
		tableView.setMaxWidth(1200);
		idColumn.setMinWidth(100);
		itemNameColumn.setMinWidth(150);
		quantityColumn.setMinWidth(100);
		sectionColumn.setMinWidth(150);
		daysAgoColumn.setMinWidth(200);


		/* TODO 2-07 - TO COMPLETE ****************************************
		 * call data source controller get all groceries method to add
		 * all groceries to table data observable list
		 */
		//done
		//tableData.add(fridgeDSC.getAllGroceries());
		//tableView.setItems(tableData);

		// =====================================================
		// ADD the remaining UI elements
		// NOTE: the order of the following TODO items can be
		// 		 changed to satisfy your UI implementation goals
		// =====================================================

		/* TODO 2-08 - TO COMPLETE ****************************************
		 * filter container - part 1
		 * add all filter related UI elements you identified
		 */
		TextField filterTF = new TextField();
		Label filterLB = new Label("Filter By: ");
		ChoiceBox<String> choiceBox = new ChoiceBox<String>();
		choiceBox.getItems().addAll("ITEM", "SECTION", "BOUGHT_DAYS_AGO");
		choiceBox.setValue("ITEM");
		CheckBox filterCB = new CheckBox("Show Expire Only");
		filterCB.setSelected(false);
		Button addBT = new Button("ADD");
		Button updateBT = new Button("UPDATE ONE");
		Button deleteBT = new Button("DELETE");

		/* TODO 2-09 - TO COMPLETE ****************************************
		 * filter container - part 2:
		 * - addListener to the "Filter By" ChoiceBox to clear the filter
		 *   text field vlaue and to enable the "Show Expire Only" CheckBox
		 *   if "BOUGHT_DAYS_AGO" is selected
		 */
		choiceBox.valueProperty().addListener((observable, oldValue, newValue) ->
			{
				filterTF.clear();//?
				String filterBy = choiceBox.getValue().trim();
				if(filterBy == "ITEM") {
				}
				else if(filterBy == "SECTION") {
				}
				else if(filterBy == "BOUGHT_DAYS_AGO") {
					filterCB.setSelected(true);
				}
			});

		/* TODO 2-10 - TO COMPLETE ****************************************
		 * filter container - part 2:
		 * - addListener to the "Filter By" ChoiceBox to clear and set focus
		 *   to the filter text field and to enable the "Show Expire Only"
		 *   CheckBox if "BOUGHT_DAYS_AGO" is selected
		 *
		 * - setOnAction on the "Show Expire Only" Checkbox to clear and
		 *   set focus to the filter text field
		 */
		choiceBox.valueProperty().addListener((observable, oldValue, newValue) ->
			{
				//filterTF.valueProperty().set(null);
				filterTF.requestFocus();
				String filterBy = choiceBox.getValue().trim();
				if(filterBy == "ITEM") {
				}
				else if(filterBy == "SECTION") {
				}
				else if(filterBy == "BOUGHT_DAYS_AGO") {
					filterCB.setSelected(true);
				}
			});

		filterCB.setOnAction( (e) -> {
			filterTF.requestFocus();
		});

		/* TODO 2-11 - TO COMPLETE ****************************************
		 * filter container - part 3:
		 * - create a filtered list
		 * - create a sorted list from the filtered list
		 * - bind comparators of sorted list with that of table view
		 * - set items of table view to be sorted list
		 * - set a change listener to text field to set the filter predicate
		 *   of filtered list
		 */
		FilteredList<Grocery> filteredList = new FilteredList<Grocery>(tableView.getItems(), grocery -> true);
		SortedList<Grocery> sortedList = new SortedList<Grocery>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);
		filterTF.textProperty().addListener((observable, oldValue, newValue) ->
   			{
      			filteredList.setPredicate(grocery ->
         			{
         				if (newValue == null || newValue.isEmpty())
						{
							return true;
						}
   						String filterString = newValue.toUpperCase();
   						String filterBy = choiceBox.getValue().toUpperCase().trim();
   						if(filterBy == "ITEM"){
							if (grocery.getItemName().toUpperCase().contains(filterString))
							{
								return true;
							}
							else
							{
								return false;
							}
						} else if(filterBy == "SECTION") {
							if (grocery.getSection().name().toUpperCase().contains(filterString))
							{
								return true;
							}
							else
							{
								return false;
							}
						} else {
							//else if(filterBy == "BOUGHT_DAYS_AGO")
							try {
								Integer filterInt = Integer.parseInt(filterString);
								if (fridgeDSC.calcDaysAgo(grocery.getDate()) >= filterInt)
								{
									if(filterCB.isSelected()) {
										if(grocery.getItem().canExpire())
										{
											return true;
										}
										else
										{
											return false;
										}
									}

									return true;
								}
								else
								{
									return false;
								}
							} catch(Exception e) {
								return false;
							}
						}
					});
            });


		/* TODO 2-12 - TO COMPLETE ****************************************
		 * ACTION buttons: ADD, UPDATE ONE, DELETE
		 * - ADD button sets the add UI elements to visible;
		 *   NOTE: the add input controls and container may have to be
		 * 		   defined before these action controls & container(s)
		 * - UPDATE ONE and DELETE buttons action need to check if a
		 *   table view row has been selected first before doing their
		 *   action; hint: should you also use an Alert confirmation?
		 */
		Label addItemLB = new Label("Item");
		ComboBox<Item> addItemChoice = new ComboBox<Item>();
		addItemChoice.getItems().addAll(fridgeDSC.getAllItems());
		addItemChoice.setVisibleRowCount(6);
		addItemLB.setVisible(false);
		addItemChoice.setVisible(false);

		Label addSectionLB = new Label("Section");
		ChoiceBox<String> addSectionChoiceBox = new ChoiceBox<String>();
		addSectionChoiceBox.getItems().addAll("FREEZER", "MEAT", "COOLING", "CRISPER");
		//addSectionChoiceBox.setValue();
		addSectionLB.setVisible(false);
		addSectionChoiceBox.setVisible(false);

		Label addQuantityLB = new Label("Quantity");
		TextField addQuantityTF = new TextField();
		addQuantityLB.setVisible(false);
		addQuantityTF.setVisible(false);

		Button addClearBT = new Button("CLEAR");
		addClearBT.setVisible(false);

		Button addSaveBT = new Button("SAVE");
		addSaveBT.setVisible(false);

		addBT.setOnAction(e -> {
			addItemLB.setVisible(true);
			addItemChoice.setVisible(true);
			addSectionLB.setVisible(true);
			addSectionChoiceBox.setVisible(true);
			addQuantityLB.setVisible(true);
			addQuantityTF.setVisible(true);
			addClearBT.setVisible(true);
			addSaveBT.setVisible(true);
		});

		addClearBT.setOnAction(e -> {
			addItemChoice.setValue(null);
			addSectionChoiceBox.setValue(null);//?
			addQuantityTF.clear();//?
		});

		addSaveBT.setOnAction(e -> {
			//addItemChoice... should be only name?
			try {
				FridgeDSC.SECTION section = null;
				switch(addSectionChoiceBox.getValue()){
					case "FREEZER": section = FridgeDSC.SECTION.FREEZER; break;
	      			case "MEAT": section = FridgeDSC.SECTION.MEAT; break;
	      			case "COOLING": section = FridgeDSC.SECTION.COOLING; break;
	      			case "CRISPER": section = FridgeDSC.SECTION.CRISPER; break;
				}
				fridgeDSC.addGrocery(addItemChoice.getValue().getName(), Integer.parseInt(addQuantityTF.getText()), section);
				tableData.clear();
   				tableData.addAll(fridgeDSC.getAllGroceries());
   				addItemLB.setVisible(false);
				addItemChoice.setVisible(false);
				addSectionLB.setVisible(false);
				addSectionChoiceBox.setVisible(false);
				addQuantityLB.setVisible(false);
				addQuantityTF.setVisible(false);
				addClearBT.setVisible(false);
				addSaveBT.setVisible(false);
			}
			catch (Exception exception) {
				throw new RuntimeException(exception.getMessage());
			}
		});

		updateBT.setOnAction(e -> {
			Grocery selectedUpdateGrocery = tableView.getSelectionModel().getSelectedItem();

			try
			{
				fridgeDSC.useGrocery(selectedUpdateGrocery.getId());
				//update doesnt work?
				tableData.clear();
   				tableData.addAll(fridgeDSC.getAllGroceries());
			}
			catch(Exception exception)
			{
				//throw new RuntimeException(exception.getMessage());
				Alert error = new Alert(Alert.AlertType.ERROR);
				error.setTitle("ERROR");
				error.setContentText(exception.getMessage() + ": There is only one " + selectedUpdateGrocery.getItemName() +
					"(Bought on " + selectedUpdateGrocery.getDate() + ") -use DELETE instead.");
				error.showAndWait();
			}
		});

		deleteBT.setOnAction(e -> {
			Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
			confirm.setTitle("Comfirmation");
			confirm.setContentText("Are you sure? Delete selected grocery?");

			Optional<ButtonType> result = confirm.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK)
			{
				Grocery selectedDeleteGrocery = tableView.getSelectionModel().getSelectedItem();

				try
				{
					fridgeDSC.removeGrocery(selectedDeleteGrocery.getId());
					tableData.remove(selectedDeleteGrocery);
				}
				catch(Exception exception)
				{
					throw new RuntimeException(exception.getMessage());
				}
			}
		});

		/* TODO 2-13 - TO COMPLETE ****************************************
		 * add input controls and container(s)
		 * - Item will list item data from the data source controller list
		 *   all items method
		 * - Section will list all sections defined in the data source
		 *   controller SECTION enum
		 * - Quantity: a texf field, self descriptive
		 * - CANCEL button: clears all input controls
		 * - SAVE button: sends the new grocery information to the data source
		 *   controller add grocery method; be mindful of exceptions when any
		 *   or all of the input controls are empty upon SAVE button click
		 */
		//done


		// =====================================================================
		// SET UP the Stage
		// =====================================================================
		// Create scene and set stage
		VBox root = new VBox();

		/* TODO 2-14 - TO COMPLETE ****************************************
		 * - add all your containers, controls to the root
		 */
		Pane filterPane1 = new FlowPane(filterTF, filterLB, choiceBox, filterCB);
		Pane filterPane2 = new FlowPane(tableView);
		Pane filterPane3 = new FlowPane(addBT, updateBT, deleteBT);
		//Pane filterPane4 = new FlowPane(addItemLB, addSectionLB, addQuantityLB);//how to make two up and down?
		//Pane filterPane5 = new FlowPane(addItemChoice, addSectionChoiceBox, addQuantityTF);
		VBox h1 = new VBox(addItemLB, addItemChoice);
		VBox h2 = new VBox(addSectionLB, addSectionChoiceBox);
		VBox h3 = new VBox(addQuantityLB, addQuantityTF);
		Pane filterPane4 = new FlowPane(h1, h2, h3);
		Pane filterPane6 = new FlowPane(addClearBT, addSaveBT);

		root.getChildren().add(filterPane1);
		root.getChildren().add(filterPane2);
		root.getChildren().add(filterPane3);
		root.getChildren().add(filterPane4);
		//root.getChildren().add(filterPane5);
		root.getChildren().add(filterPane6);

		root.setStyle(
			"-fx-font-size: 20;" +
			"-fx-alignment: center;"
		);

		Scene scene = new Scene(root);
		stage.setScene(scene);
	}

	public void stop() throws Exception {

		/* TODO 2-15 - TO COMPLETE ****************************************
		 * call the data source controller database disconnect method
		 * NOTE: that database disconnect method throws exception
		 */
		try {
			fridgeDSC.disconnect();
		} catch(Exception exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}
}
