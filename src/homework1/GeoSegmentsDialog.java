package homework1;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A JDailog GUI for choosing a GeoSegemnt and adding it to the route shown
 * by RoutDirectionGUI.
 * <p>
 * A figure showing this GUI can be found in homework assignment #1.
 */
public class GeoSegmentsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	// the RouteDirectionsGUI that this JDialog was opened from
	private RouteFormatterGUI parent;
	
	// a control contained in this 
	private JList<GeoSegment> lstSegments;
	
	/**
	 * Creates a new GeoSegmentsDialog JDialog.
	 * @effects Creates a new GeoSegmentsDialog JDialog with owner-frame
	 * 			owner and parent pnlParent
	 */
	public GeoSegmentsDialog(Frame owner, RouteFormatterGUI pnlParent) {
		// create a modal JDialog with the an owner Frame (a modal window
		// in one that doesn't allow other windows to be active at the
		// same time).
		super(owner, "Please choose a GeoSegment", true);
		
		this.parent = pnlParent;

		GeoSegmentsDialog dialog = this;

		// Add Segments List
		GeoSegment[] list = ExampleGeoSegments.segments;
		lstSegments = new JList<GeoSegment>(list);
		lstSegments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrlSegments = new JScrollPane(lstSegments);
		scrlSegments.setPreferredSize(new Dimension(600, 200));

		// Add Cancel Buttom
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		// Add "Add" Buttom
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnlParent.addSegment(lstSegments.getSelectedValue());
				dialog.setVisible(false);
			}
		});




		// arrange components on grid
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridbag);


		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 5;
		c.insets = new Insets(20,20,20,20);
		gridbag.setConstraints(scrlSegments, c);
		this.add(scrlSegments);


		c.gridx = 0;
		c.gridy = 20;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.insets = new Insets(20,0,20,20);
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(btnCancel, c);
		this.add(btnCancel);

		c.gridx = 0;
		c.gridy = 20;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(20,20,20,0);
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(btnAdd, c);
		this.add(btnAdd);



		// TODO Write the body of this method
	}
}
