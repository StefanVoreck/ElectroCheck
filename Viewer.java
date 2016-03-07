import javax.swing.JList;
import javax.swing.JFrame;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

@SuppressWarnings("unchecked")
public class Viewer extends JFrame implements ActionListener{
	private Library lib;
	private JList itemList;
	private JLabel header;
	private JScrollPane itemPanel;
	private JPanel buttonPanel;
	private JButton newItemButton, editButton, removeButton;

	public Viewer(Library lib){
		// Frame
		super("ElectroCheck | Last update: " + (new Item()).calendarToInt());
		this.lib = lib;
		setSize(900,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - getWidth())/2, (dim.height - getHeight())/2);
		
		// Header
		JLabel header = new JLabel(Item.HEADER);
		header.setFont(new Font(Font.MONOSPACED,Font.BOLD,13));
		
		// Item list and panel
		itemList = new JList(lib);
		itemList.setFont(new Font(Font.MONOSPACED,Font.PLAIN,13));
		itemPanel = new JScrollPane(itemList);
		itemPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		// Buttons and buttonpanel
		newItemButton = new JButton("New item");
		newItemButton.addActionListener(this);
		editButton = new JButton("Edit selected item");
		editButton.addActionListener(this);
		removeButton = new JButton("Remove selected item");
		removeButton.addActionListener(this);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout());
		buttonPanel.add(newItemButton);
		buttonPanel.add(editButton);
		buttonPanel.add(removeButton);
		
		// Layout
		setLayout(new BorderLayout());
		add(header, BorderLayout.NORTH);
		add(itemPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setVisible(true);
		getRootPane().setDefaultButton(newItemButton);
	}
	
	public void exportInit(){
		new ImExStream<Library>().exportObject("lib.bin", lib);
		lib.refresh();
		setTitle("ElectroCheck | Last update: " + (new Item()).calendarToInt());
	}
	
	public void actionPerformed(ActionEvent event){
		String input = event.getActionCommand();
		
		// New item
		if(input.equals(newItemButton.getText())){
			Item temp = new Item();
			new EditItem(temp, "New item");
			
		// Edit existing item
		}else if(input.equals(editButton.getText())){
			try{
				Item tempItem = lib.get(itemList.getSelectedIndex());
				new EditItem(tempItem, "Edit item: " + tempItem.getName());
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Choose an item to edit");
			}
			
		// Remove item
		}else if(input.equals(removeButton.getText())){
			try{
				lib.remove(itemList.getSelectedIndex());
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Choose an item to remove");
			}
			
		// Should never occur
		}else{
			JOptionPane.showMessageDialog(null, "Error 0x45");
		}
		
		exportInit();
	}
	
	class EditItem extends JFrame implements ActionListener{
		private Item item;
		private String title;
		private JPanel panel;
		private JTextField newNameField, newQuantityField, newCostField, newOrderDateField, newEstimatedDeliveryField;
		private JLabel newNameLabel, newQuantityLabel, newCostLabel, newStatusLabel, newOrderDateLabel, newEstimatedDeliveryLabel;
		private JComboBox statusCombo;
		private JButton submitButton, cancelButton;
		private String[] statuses = {"PENDING", "ORDERED", "ARRIVED"};
		
		private EditItem(Item item, String title){
			super(title);
			this.item = item;
			this.title = title;
			
			setSize(350,200);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLocation((dim.width - getWidth())/2, (dim.height - getHeight())/2);
			
			// JButtons
			submitButton = new JButton("Submit");
			submitButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);
			
			// JFields
			newNameField = new JTextField(15);
			newNameField.setText(item.getName());
			newNameField.addActionListener(this);
			newNameField.setActionCommand(submitButton.getActionCommand());
			newQuantityField = new JTextField(15);
			newQuantityField.setText("" + item.getQuantity());
			newQuantityField.addActionListener(this);
			newQuantityField.setActionCommand(submitButton.getActionCommand());
			newCostField = new JTextField(15);
			newCostField.setText("" + item.getCost());
			newCostField.addActionListener(this);
			newCostField.setActionCommand(submitButton.getActionCommand());
			newOrderDateField = new JTextField(15);
			newOrderDateField.setText("" + item.getOrderDate());
			newOrderDateField.addActionListener(this);
			newOrderDateField.setActionCommand(submitButton.getActionCommand());
			newEstimatedDeliveryField = new JTextField(15);
			newEstimatedDeliveryField.setText("" + item.getEstimatedDelivery());
			newEstimatedDeliveryField.addActionListener(this);
			newEstimatedDeliveryField.setActionCommand(submitButton.getActionCommand());
				
			// JLabels
			Dimension labelDimension = new Dimension(160, 10);			
			newNameLabel = new JLabel("Name: ", SwingConstants.RIGHT);
			newNameLabel.setPreferredSize(labelDimension);
			newQuantityLabel = new JLabel("Quantity: ", SwingConstants.RIGHT);
			newQuantityLabel.setPreferredSize(labelDimension);
			newCostLabel = new JLabel("Cost: ", SwingConstants.RIGHT);
			newCostLabel.setPreferredSize(labelDimension);
			newStatusLabel = new JLabel("Status: ", SwingConstants.RIGHT);
			newStatusLabel.setPreferredSize(labelDimension);
			newOrderDateLabel = new JLabel("Order date: ", SwingConstants.RIGHT);
			newOrderDateLabel.setPreferredSize(labelDimension);
			newEstimatedDeliveryLabel = new JLabel("Estimated delivery: ", SwingConstants.RIGHT);
			newEstimatedDeliveryLabel.setPreferredSize(labelDimension);
		
			// JComboBox
			statusCombo = new JComboBox(statuses);
			
			// Layout
			panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 0.1;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = 0;
			panel.add(newNameLabel, c);
			c.gridy = 1;
			panel.add(newQuantityLabel, c);
			c.gridy = 2;
			panel.add(newCostLabel, c);
			c.gridy = 3;
			panel.add(newStatusLabel, c);
			c.gridy = 4;
			panel.add(newOrderDateLabel, c);
			c.gridy = 5;
			panel.add(newEstimatedDeliveryLabel, c);
			
			c.weightx = 0.9;
			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 2;
			panel.add(newNameField, c);
			c.gridy = 1;
			panel.add(newQuantityField, c);
			c.gridy = 2;
			panel.add(newCostField, c);
			c.gridy = 3;
			panel.add(statusCombo, c);
			c.gridy = 4;
			panel.add(newOrderDateField, c);
			c.gridy = 5;
			panel.add(newEstimatedDeliveryField, c);
			
			c.gridx = 0;
			c.gridy = 6;
			panel.add(submitButton, c);
			c.gridx = 1;
			panel.add(cancelButton, c);
		
			add(panel);
			setVisible(true);
			
		}
		
		public void actionPerformed(ActionEvent event){
			String input = event.getActionCommand();
			
			if(input.equals(submitButton.getActionCommand())){
				try{
					item.setName(newNameField.getText());
					item.setQuantity(Integer.parseInt(newQuantityField.getText()));
					item.setCost(Float.parseFloat(newCostField.getText()));
					int status = statusCombo.getSelectedIndex();
					item.setStatus(statusCombo.getSelectedIndex());
					item.setOrderDate(Integer.parseInt(newOrderDateField.getText()));
					item.setEstimatedDelivery(Integer.parseInt(newEstimatedDeliveryField.getText()));
					
					if(!item.isEmpty()){
						if(title.equals("New item")){
							lib.add(item);
						}
						lib.sortLibrary();
						dispose();
					}else{
						JOptionPane.showMessageDialog(this, "Fill all fields");
					}
					
				}catch(Exception e){
					JOptionPane.showMessageDialog(this, "Use numbers only");
				}
			}else if(input.equals(cancelButton.getActionCommand())){
				dispose();
			}else{
				JOptionPane.showMessageDialog(this, "Error 0x734");
			}
			
			exportInit();
		}
	}
}








































