package vendingmachine.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.enums.CoinDenomination;
import vendingmachine.enums.ShelfSelection;
import vendingmachine.gui.handlers.CancelButtonHandler;
import vendingmachine.gui.handlers.InsertedCoinHandler;
import vendingmachine.model.Product;
import vendingmachine.model.VendingMachine;

@Dependent
public class VendingMachineMainWindow {

	private static final Logger LOG = LogManager.getLogger(VendingMachineMainWindow.class);

	private JFrame frame;

	@Inject
	private VendingMachine vendingMachine;

	@Inject
	private InsertedCoinHandler insertedCoinListener;

	@Inject
	private CancelButtonHandler cancelButtonHandler;

	/**
	 * @wbp.parser.entryPoint
	 */
	public void launchGUI() {
		VendingMachineMainWindow thisRef = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				trySettingNimubsLookAndFeel();
				thisRef.initialize();
				thisRef.frame.setVisible(true);
			}

			private void trySettingNimubsLookAndFeel() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
					LOG.info("Nimbus Look and Feel not found. Staying with default L&F.");
				}
			}
		});
	}

	private void initialize() {

		Product[][] products = vendingMachine.getProducts();

		frame = new JFrame();
		frame.getContentPane().setPreferredSize(new Dimension(0, 22));
		frame.setBounds(100, 100, 870, 393);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JTextPane textPaneDisplay = new JTextPane();
		textPaneDisplay.setEditable(false);
		textPaneDisplay.setBounds(648, 154, 184, 64);
		SimpleAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
		textPaneDisplay.setParagraphAttributes(attribs, true);

		JScrollPane scrollPane = new JScrollPane(textPaneDisplay);
		scrollPane.setBounds(187, 224, 395, 88);
		frame.getContentPane().add(scrollPane);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setEnabled(false);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButtonHandler.handle(textPaneDisplay, btnCancel);
			}
		});
		btnCancel.setBounds(698, 150, 73, 23);
		frame.getContentPane().add(btnCancel);

		JButton btnInsertdomination5 = new JButton("5");
		btnInsertdomination5.setEnabled(false);
		btnInsertdomination5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertedCoinListener.handle(textPaneDisplay, btnCancel, CoinDenomination.DENOMINATION_5);
			}
		});
		btnInsertdomination5.setBounds(663, 56, 39, 23);
		frame.getContentPane().add(btnInsertdomination5);

		JButton btnInsertdomination2 = new JButton("2");
		btnInsertdomination2.setEnabled(false);
		btnInsertdomination2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertedCoinListener.handle(textPaneDisplay, btnCancel, CoinDenomination.DENOMINATION_2);
			}
		});
		btnInsertdomination2.setBounds(712, 56, 39, 23);
		frame.getContentPane().add(btnInsertdomination2);

		JButton btnInsertdomination1 = new JButton("1");
		btnInsertdomination1.setEnabled(false);
		btnInsertdomination1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertedCoinListener.handle(textPaneDisplay, btnCancel, CoinDenomination.DENOMINATION_1);
			}
		});
		btnInsertdomination1.setBounds(761, 56, 39, 23);
		frame.getContentPane().add(btnInsertdomination1);

		JButton btnInsertdomination05 = new JButton("0.5");
		btnInsertdomination05.setEnabled(false);
		btnInsertdomination05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertedCoinListener.handle(textPaneDisplay, btnCancel, CoinDenomination.DENOMINATION_05);
			}
		});
		btnInsertdomination05.setBounds(650, 91, 52, 23);
		frame.getContentPane().add(btnInsertdomination05);

		JButton btnInsertdomination02 = new JButton("0.2");
		btnInsertdomination02.setEnabled(false);
		btnInsertdomination02.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertedCoinListener.handle(textPaneDisplay, btnCancel, CoinDenomination.DENOMINATION_02);
			}
		});
		btnInsertdomination02.setBounds(712, 91, 52, 23);
		frame.getContentPane().add(btnInsertdomination02);

		JButton btnInsertdomination01 = new JButton("0.1");
		btnInsertdomination01.setEnabled(false);
		btnInsertdomination01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertedCoinListener.handle(textPaneDisplay, btnCancel, CoinDenomination.DENOMINATION_01);
			}
		});
		btnInsertdomination01.setBounds(771, 91, 52, 23);
		frame.getContentPane().add(btnInsertdomination01);

		JLabel lblSelectingShelve = new JLabel("Selecting shelve");
		lblSelectingShelve.setBounds(31, 30, 101, 14);
		frame.getContentPane().add(lblSelectingShelve);

		JLabel lblInsertingCoins = new JLabel("Inserting coins");
		lblInsertingCoins.setBounds(682, 30, 89, 14);
		frame.getContentPane().add(lblInsertingCoins);

		JPanel panel = new JPanel();
		panel.setBounds(141, 57, 486, 116);
		frame.getContentPane().add(panel);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(13, 13, 13, 13);
		panel.setLayout(new GridLayout(0, 6, 0, 15));

		JLabel lblProductY4X1 = new JLabel("PY4X1");
		panel.add(lblProductY4X1);
		lblProductY4X1.setVerticalAlignment(SwingConstants.BOTTOM);

		lblProductY4X1.setText(products[3][0].getName() + " [" + products[3][0].getQuantity() + "]");

		JLabel lblProductY4X2 = new JLabel("PY4X2");
		panel.add(lblProductY4X2);
		lblProductY4X2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X2.setText(products[3][0].getName() + " [" + products[3][1].getQuantity() + "]");

		JLabel lblProductY4X3 = new JLabel("PY4X3");
		panel.add(lblProductY4X3);
		lblProductY4X3.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X3.setText(products[3][0].getName() + " [" + products[3][2].getQuantity() + "]");

		JLabel lblProductY4X4 = new JLabel("PY4X4");
		panel.add(lblProductY4X4);
		lblProductY4X4.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X4.setText(products[3][0].getName() + " [" + products[3][3].getQuantity() + "]");

		JLabel lblProductY4X5 = new JLabel("PY4X5");
		panel.add(lblProductY4X5);
		lblProductY4X5.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X5.setText(products[3][0].getName() + " [" + products[3][4].getQuantity() + "]");

		JLabel lblProductY4X6 = new JLabel("PY4X6");
		panel.add(lblProductY4X6);
		lblProductY4X6.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X6.setText(products[3][0].getName() + " [" + products[3][5].getQuantity() + "]");

		JLabel lblProductY3X1 = new JLabel("PY3X1");
		panel.add(lblProductY3X1);
		lblProductY3X1.setVerticalAlignment(SwingConstants.BOTTOM);

		lblProductY3X1.setText(products[2][0].getName() + " [" + products[2][0].getQuantity() + "]");

		JLabel lblProductY3X2 = new JLabel("PY3X2");
		panel.add(lblProductY3X2);
		lblProductY3X2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X2.setText(products[2][0].getName() + " [" + products[2][1].getQuantity() + "]");

		JLabel lblProductY3X3 = new JLabel("PY3X3");
		panel.add(lblProductY3X3);
		lblProductY3X3.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X3.setText(products[2][0].getName() + " [" + products[2][2].getQuantity() + "]");

		JLabel lblProductY3X4 = new JLabel("PY3X4");
		panel.add(lblProductY3X4);
		lblProductY3X4.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X4.setText(products[2][0].getName() + " [" + products[2][3].getQuantity() + "]");

		JLabel lblProductY3X5 = new JLabel("PY3X5");
		panel.add(lblProductY3X5);
		lblProductY3X5.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X5.setText(products[2][0].getName() + " [" + products[2][4].getQuantity() + "]");

		JLabel lblProductY3X6 = new JLabel("PY3X6");
		panel.add(lblProductY3X6);
		lblProductY3X6.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X6.setText(products[2][0].getName() + " [" + products[2][5].getQuantity() + "]");

		JLabel lblProductY2X1 = new JLabel("PY2X1");
		panel.add(lblProductY2X1);
		lblProductY2X1.setVerticalAlignment(SwingConstants.BOTTOM);

		lblProductY2X1.setText(products[1][0].getName() + " [" + products[1][0].getQuantity() + "]");

		JLabel lblProductY2X2 = new JLabel("PY2X2");
		panel.add(lblProductY2X2);
		lblProductY2X2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X2.setText(products[1][0].getName() + " [" + products[1][1].getQuantity() + "]");

		JLabel lblProductY2X3 = new JLabel("PY2X3");
		panel.add(lblProductY2X3);
		lblProductY2X3.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X3.setText(products[1][0].getName() + " [" + products[1][2].getQuantity() + "]");

		JLabel lblProductY2X4 = new JLabel("PY2X4");
		panel.add(lblProductY2X4);
		lblProductY2X4.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X4.setText(products[1][0].getName() + " [" + products[1][3].getQuantity() + "]");

		JLabel lblProductY2X5 = new JLabel("PY2X5");
		panel.add(lblProductY2X5);
		lblProductY2X5.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X5.setText(products[1][0].getName() + " [" + products[1][4].getQuantity() + "]");

		JLabel lblProductY2X6 = new JLabel("PY2X6");
		panel.add(lblProductY2X6);
		lblProductY2X6.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X6.setText(products[1][0].getName() + " [" + products[1][5].getQuantity() + "]");

		JLabel lblProductY1X1 = new JLabel("PY1X1");
		panel.add(lblProductY1X1);
		lblProductY1X1.setVerticalAlignment(SwingConstants.BOTTOM);

		lblProductY1X1.setText(products[0][0].getName() + " [" + products[0][0].getQuantity() + "]");

		JLabel lblProductY1X2 = new JLabel("PY1X2");
		panel.add(lblProductY1X2);
		lblProductY1X2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X2.setText(products[0][0].getName() + " [" + products[0][1].getQuantity() + "]");

		JLabel lblProductY1X3 = new JLabel("PY1X3");
		panel.add(lblProductY1X3);
		lblProductY1X3.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X3.setText(products[0][0].getName() + " [" + products[0][2].getQuantity() + "]");

		JLabel lblProductY1X4 = new JLabel("PY1X4");
		panel.add(lblProductY1X4);
		lblProductY1X4.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X4.setText(products[0][0].getName() + " [" + products[0][3].getQuantity() + "]");

		JLabel lblProductY1X5 = new JLabel("PY1X5");
		panel.add(lblProductY1X5);
		lblProductY1X5.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X5.setText(products[0][0].getName() + " [" + products[0][4].getQuantity() + "]");

		JLabel lblProductY1X6 = new JLabel("PY1X6");
		panel.add(lblProductY1X6);
		lblProductY1X6.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X6.setText(products[0][0].getName() + " [" + products[0][5].getQuantity() + "]");

		JRadioButton rdbtnSelectShelve4 = new JRadioButton("");
		rdbtnSelectShelve4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneDisplay.setText("Product price: " + products[ShelfSelection.FOURTH.getCode()][0].getPrice());
				activateInsertCoinButtons(btnInsertdomination5, btnInsertdomination2, btnInsertdomination1, btnInsertdomination05, btnInsertdomination02, btnInsertdomination01);
				vendingMachine.setSelectedShelf(ShelfSelection.FOURTH);
			}
		});
		rdbtnSelectShelve4.setBounds(65, 60, 21, 18);
		frame.getContentPane().add(rdbtnSelectShelve4);

		JRadioButton rdbtnSelectShelve3 = new JRadioButton("");
		rdbtnSelectShelve3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneDisplay.setText("Product price: " + products[ShelfSelection.THIRD.getCode()][0].getPrice());
				activateInsertCoinButtons(btnInsertdomination5, btnInsertdomination2, btnInsertdomination1, btnInsertdomination05, btnInsertdomination02, btnInsertdomination01);
				vendingMachine.setSelectedShelf(ShelfSelection.THIRD);
			}
		});
		rdbtnSelectShelve3.setBounds(65, 92, 21, 18);
		frame.getContentPane().add(rdbtnSelectShelve3);

		JRadioButton rdbtnSelectShelve2 = new JRadioButton("");
		rdbtnSelectShelve2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneDisplay.setText("Product price: " + products[ShelfSelection.SECOND.getCode()][0].getPrice());
				activateInsertCoinButtons(btnInsertdomination5, btnInsertdomination2, btnInsertdomination1, btnInsertdomination05, btnInsertdomination02, btnInsertdomination01);
				vendingMachine.setSelectedShelf(ShelfSelection.SECOND);
			}
		});
		rdbtnSelectShelve2.setBounds(65, 123, 21, 18);
		frame.getContentPane().add(rdbtnSelectShelve2);

		JRadioButton rdbtnSelectShelve1 = new JRadioButton("");
		rdbtnSelectShelve1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneDisplay.setText("Product price: " + products[ShelfSelection.FIRST.getCode()][0].getPrice());
				activateInsertCoinButtons(btnInsertdomination5, btnInsertdomination2, btnInsertdomination1, btnInsertdomination05, btnInsertdomination02, btnInsertdomination01);
				vendingMachine.setSelectedShelf(ShelfSelection.FIRST);
			}
		});
		rdbtnSelectShelve1.setBounds(65, 153, 21, 18);
		frame.getContentPane().add(rdbtnSelectShelve1);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(0, 24));
		separator.setBounds(126, 27, 6, 167);
		frame.getContentPane().add(separator);

		JSeparator separator_2 = new JSeparator();
		separator_2.setPreferredSize(new Dimension(0, 24));
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(634, 27, 6, 167);
		frame.getContentPane().add(separator_2);

		JSeparator separator_3 = new JSeparator();
		separator_3.setPreferredSize(new Dimension(250, 0));
		separator_3.setBounds(21, 336, 811, 14);
		frame.getContentPane().add(separator_3);

		JSeparator separator_4 = new JSeparator();
		separator_4.setPreferredSize(new Dimension(0, 34));
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setBounds(21, 27, 10, 310);
		frame.getContentPane().add(separator_4);

		JSeparator separator_6 = new JSeparator();
		separator_6.setPreferredSize(new Dimension(250, 0));
		separator_6.setBounds(21, 26, 811, 5);
		frame.getContentPane().add(separator_6);

		JSeparator separator_7 = new JSeparator();
		separator_7.setPreferredSize(new Dimension(250, 0));
		separator_7.setBounds(21, 45, 811, 5);
		frame.getContentPane().add(separator_7);

		JLabel lblDisplay = new JLabel("Display:");
		lblDisplay.setBounds(187, 205, 61, 14);
		frame.getContentPane().add(lblDisplay);

		ButtonGroup selectingShelvesButtonGroup = new ButtonGroup();
		selectingShelvesButtonGroup.add(rdbtnSelectShelve1);
		selectingShelvesButtonGroup.add(rdbtnSelectShelve2);
		selectingShelvesButtonGroup.add(rdbtnSelectShelve3);
		selectingShelvesButtonGroup.add(rdbtnSelectShelve4);

		JSeparator separator_8 = new JSeparator();
		separator_8.setPreferredSize(new Dimension(250, 0));
		separator_8.setBounds(21, 193, 811, 14);
		frame.getContentPane().add(separator_8);

		JSeparator separator_5 = new JSeparator();
		separator_5.setPreferredSize(new Dimension(0, 34));
		separator_5.setOrientation(SwingConstants.VERTICAL);
		separator_5.setBounds(832, 27, 10, 310);
		frame.getContentPane().add(separator_5);

		JLabel lblProducts = new JLabel("Products");
		lblProducts.setBounds(336, 30, 78, 14);
		frame.getContentPane().add(lblProducts);

	}

	private void activateInsertCoinButtons(JButton btnInsertdomination5, JButton btnInsertdomination2, JButton btnInsertdomination1, JButton btnInsertdomination05,
			JButton btnInsertdomination02, JButton btnInsertdomination01) {
		btnInsertdomination5.setEnabled(true);
		btnInsertdomination2.setEnabled(true);
		btnInsertdomination1.setEnabled(true);
		btnInsertdomination05.setEnabled(true);
		btnInsertdomination02.setEnabled(true);
		btnInsertdomination01.setEnabled(true);
	}
}
