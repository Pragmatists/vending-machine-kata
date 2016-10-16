package vendingmachine.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.core.VendingMachineInitializer;
import vendingmachine.model.Product;
import vendingmachine.model.VendingMachine;

public class VendingMachineMainWindow {

	private static final Logger LOG = LogManager.getLogger(VendingMachineMainWindow.class);

	private JFrame frame;
	private JTextField textFieldDisplay;

	public static void main(String[] args) {

		if (args.length != 1) {
			LOG.info("Application should be given one parameter: path to configration file. Quitting...");
			System.exit(1);
		} else {
			if (VendingMachineInitializer.init(args[0])) {
				launchGUI();
			}
		}

	}

	private static void launchGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				trySettingNimubsLookAndFeel();
				VendingMachineMainWindow window = new VendingMachineMainWindow();
				window.frame.setVisible(true);
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

	public VendingMachineMainWindow() {
		initialize();
	}

	private void initialize() {

		VendingMachine vendingMachine = VendingMachine.getInstance();
		Product[][] products = VendingMachine.getInstance().getProducts();

		frame = new JFrame();
		frame.getContentPane().setPreferredSize(new Dimension(0, 22));
		frame.setBounds(100, 100, 1174, 625);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnInsertdomination5 = new JButton("5");
		btnInsertdomination5.setEnabled(false);
		btnInsertdomination5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vendingMachine.addMoney(new BigDecimal("5.0"));
				int currentlySelectedShelf = vendingMachine.getCurrentlySelectedShelf();
				textFieldDisplay.setText("Money needed to be inserted to buy selected product: "
						+ products[currentlySelectedShelf - 1][0].getPrice().subtract(vendingMachine.getMoneyInVendingMachine()));
			}
		});
		btnInsertdomination5.setBounds(652, 56, 39, 23);
		frame.getContentPane().add(btnInsertdomination5);

		JButton btnInsertdomination2 = new JButton("2");
		btnInsertdomination2.setEnabled(false);
		btnInsertdomination2.setBounds(701, 56, 39, 23);
		frame.getContentPane().add(btnInsertdomination2);

		JButton btnInsertdomination1 = new JButton("1");
		btnInsertdomination1.setEnabled(false);
		btnInsertdomination1.setBounds(750, 56, 39, 23);
		frame.getContentPane().add(btnInsertdomination1);

		JButton btnInsertdomination05 = new JButton("0.5");
		btnInsertdomination05.setEnabled(false);
		btnInsertdomination05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnInsertdomination05.setBounds(639, 87, 52, 23);
		frame.getContentPane().add(btnInsertdomination05);

		JButton btnInsertdomination02 = new JButton("0.2");
		btnInsertdomination02.setEnabled(false);
		btnInsertdomination02.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnInsertdomination02.setBounds(701, 87, 52, 23);
		frame.getContentPane().add(btnInsertdomination02);

		JButton btnInsertdomination01 = new JButton("0.1");
		btnInsertdomination01.setEnabled(false);
		btnInsertdomination01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vendingMachine.addMoney(new BigDecimal("0.1"));
				int currentlySelectedShelf = vendingMachine.getCurrentlySelectedShelf();
				textFieldDisplay.setText("Money needed to be inserted to buy selected product: "
						+ products[currentlySelectedShelf - 1][0].getPrice().subtract(vendingMachine.getMoneyInVendingMachine()));
			}
		});
		btnInsertdomination01.setBounds(763, 87, 52, 23);
		frame.getContentPane().add(btnInsertdomination01);

		JLabel lblSelectingShelve = new JLabel("Selecting shelve");
		lblSelectingShelve.setBounds(26, 30, 106, 14);
		frame.getContentPane().add(lblSelectingShelve);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(142, 30, 46, 14);
		frame.getContentPane().add(lblPrice);

		JLabel lblInsertingCoins = new JLabel("Inserting coins");
		lblInsertingCoins.setBounds(682, 30, 89, 14);
		frame.getContentPane().add(lblInsertingCoins);

		JLabel lblProductY4X1 = new JLabel("PY4X1");
		lblProductY4X1.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X1.setBounds(203, 62, 60, 14);
		frame.getContentPane().add(lblProductY4X1);

		JLabel lblProductY4X2 = new JLabel("PY4X2");
		lblProductY4X2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X2.setBounds(273, 62, 60, 14);
		frame.getContentPane().add(lblProductY4X2);

		JLabel lblProductY4X3 = new JLabel("PY4X3");
		lblProductY4X3.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X3.setBounds(346, 62, 60, 14);
		frame.getContentPane().add(lblProductY4X3);

		JLabel lblProductY4X4 = new JLabel("PY4X4");
		lblProductY4X4.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X4.setBounds(416, 62, 60, 14);
		frame.getContentPane().add(lblProductY4X4);

		JLabel lblProductY4X5 = new JLabel("PY4X5");
		lblProductY4X5.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X5.setBounds(485, 62, 60, 14);
		frame.getContentPane().add(lblProductY4X5);

		JLabel lblProductY4X6 = new JLabel("PY4X6");
		lblProductY4X6.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY4X6.setBounds(555, 62, 60, 14);
		frame.getContentPane().add(lblProductY4X6);

		JRadioButton rdbtnSelectShelve4 = new JRadioButton("");
		rdbtnSelectShelve4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDisplay.setText("Product price: " + products[3][0].getPrice());
				activateInsertCoinButtons(btnInsertdomination5, btnInsertdomination2, btnInsertdomination1, btnInsertdomination05, btnInsertdomination02, btnInsertdomination01);
				VendingMachine.getInstance().setCurrentlySelectedShelf(4);
			}
		});
		rdbtnSelectShelve4.setBounds(65, 60, 21, 18);
		frame.getContentPane().add(rdbtnSelectShelve4);

		JLabel lblPricePTY4 = new JLabel("0");
		lblPricePTY4.setHorizontalAlignment(SwingConstants.CENTER);
		lblPricePTY4.setBounds(126, 62, 62, 14);
		frame.getContentPane().add(lblPricePTY4);

		JLabel lblProductY3X1 = new JLabel("PY3X1");
		lblProductY3X1.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X1.setBounds(203, 89, 60, 14);
		frame.getContentPane().add(lblProductY3X1);

		JLabel lblProductY3X2 = new JLabel("PY3X2");
		lblProductY3X2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X2.setBounds(273, 89, 60, 14);
		frame.getContentPane().add(lblProductY3X2);

		JLabel lblProductY3X3 = new JLabel("PY3X3");
		lblProductY3X3.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X3.setBounds(346, 89, 60, 14);
		frame.getContentPane().add(lblProductY3X3);

		JLabel lblProductY3X4 = new JLabel("PY3X4");
		lblProductY3X4.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X4.setBounds(416, 89, 60, 14);
		frame.getContentPane().add(lblProductY3X4);

		JLabel lblProductY3X5 = new JLabel("PY3X5");
		lblProductY3X5.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X5.setBounds(485, 89, 60, 14);
		frame.getContentPane().add(lblProductY3X5);

		JLabel lblProductY3X6 = new JLabel("PY3X6");
		lblProductY3X6.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY3X6.setBounds(555, 89, 60, 14);
		frame.getContentPane().add(lblProductY3X6);

		JRadioButton rdbtnSelectShelve3 = new JRadioButton("");
		rdbtnSelectShelve3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDisplay.setText("Product price: " + products[2][0].getPrice());
				activateInsertCoinButtons(btnInsertdomination5, btnInsertdomination2, btnInsertdomination1, btnInsertdomination05, btnInsertdomination02, btnInsertdomination01);
				VendingMachine.getInstance().setCurrentlySelectedShelf(3);
			}
		});
		rdbtnSelectShelve3.setBounds(65, 87, 21, 18);
		frame.getContentPane().add(rdbtnSelectShelve3);

		JLabel lblPricePTY3 = new JLabel("0");
		lblPricePTY3.setHorizontalAlignment(SwingConstants.CENTER);
		lblPricePTY3.setBounds(126, 89, 62, 14);
		frame.getContentPane().add(lblPricePTY3);

		JLabel lblProductY2X1 = new JLabel("PY2X1");
		lblProductY2X1.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X1.setBounds(203, 122, 60, 14);
		frame.getContentPane().add(lblProductY2X1);

		JLabel lblProductY2X2 = new JLabel("PY2X2");
		lblProductY2X2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X2.setBounds(273, 122, 60, 14);
		frame.getContentPane().add(lblProductY2X2);

		JLabel lblProductY2X3 = new JLabel("PY2X3");
		lblProductY2X3.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X3.setBounds(346, 122, 60, 14);
		frame.getContentPane().add(lblProductY2X3);

		JLabel lblProductY2X4 = new JLabel("PY2X4");
		lblProductY2X4.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X4.setBounds(416, 122, 60, 14);
		frame.getContentPane().add(lblProductY2X4);

		JLabel lblProductY2X5 = new JLabel("PY2X5");
		lblProductY2X5.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X5.setBounds(485, 122, 60, 14);
		frame.getContentPane().add(lblProductY2X5);

		JLabel lblProductY2X6 = new JLabel("PY2X6");
		lblProductY2X6.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY2X6.setBounds(555, 122, 60, 14);
		frame.getContentPane().add(lblProductY2X6);

		JRadioButton rdbtnSelectShelve2 = new JRadioButton("");
		rdbtnSelectShelve2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDisplay.setText("Product price: " + products[1][0].getPrice());
				activateInsertCoinButtons(btnInsertdomination5, btnInsertdomination2, btnInsertdomination1, btnInsertdomination05, btnInsertdomination02, btnInsertdomination01);
				VendingMachine.getInstance().setCurrentlySelectedShelf(2);
			}
		});
		rdbtnSelectShelve2.setBounds(65, 120, 21, 18);
		frame.getContentPane().add(rdbtnSelectShelve2);

		JLabel lblPricePTY2 = new JLabel("0");
		lblPricePTY2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPricePTY2.setBounds(126, 122, 62, 14);
		frame.getContentPane().add(lblPricePTY2);

		JLabel lblProductY1X1 = new JLabel("PY1X1");
		lblProductY1X1.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X1.setBounds(203, 155, 60, 14);
		frame.getContentPane().add(lblProductY1X1);

		JLabel lblProductY1X2 = new JLabel("PY1X2");
		lblProductY1X2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X2.setBounds(273, 155, 60, 14);
		frame.getContentPane().add(lblProductY1X2);

		JLabel lblProductY1X3 = new JLabel("PY1X3");
		lblProductY1X3.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X3.setBounds(346, 155, 60, 14);
		frame.getContentPane().add(lblProductY1X3);

		JLabel lblProductY1X4 = new JLabel("PY1X4");
		lblProductY1X4.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X4.setBounds(416, 155, 60, 14);
		frame.getContentPane().add(lblProductY1X4);

		JLabel lblProductY1X5 = new JLabel("PY1X5");
		lblProductY1X5.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X5.setBounds(485, 155, 60, 14);
		frame.getContentPane().add(lblProductY1X5);

		JLabel lblProductY1X6 = new JLabel("PY1X6");
		lblProductY1X6.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProductY1X6.setBounds(555, 155, 60, 14);
		frame.getContentPane().add(lblProductY1X6);

		JRadioButton rdbtnSelectShelve1 = new JRadioButton("");
		rdbtnSelectShelve1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDisplay.setText("Product price: " + products[0][0].getPrice());
				activateInsertCoinButtons(btnInsertdomination5, btnInsertdomination2, btnInsertdomination1, btnInsertdomination05, btnInsertdomination02, btnInsertdomination01);
				VendingMachine.getInstance().setCurrentlySelectedShelf(1);
			}
		});
		rdbtnSelectShelve1.setBounds(65, 153, 21, 18);
		frame.getContentPane().add(rdbtnSelectShelve1);

		JLabel lblPricePTY1 = new JLabel("0");
		lblPricePTY1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPricePTY1.setBounds(126, 155, 62, 14);
		frame.getContentPane().add(lblPricePTY1);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(0, 24));
		separator.setBounds(126, 30, 6, 164);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setPreferredSize(new Dimension(0, 24));
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(187, 30, 6, 164);
		frame.getContentPane().add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setPreferredSize(new Dimension(0, 24));
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(623, 30, 6, 164);
		frame.getContentPane().add(separator_2);

		JSeparator separator_3 = new JSeparator();
		separator_3.setPreferredSize(new Dimension(250, 0));
		separator_3.setBounds(21, 229, 811, 14);
		frame.getContentPane().add(separator_3);

		JSeparator separator_4 = new JSeparator();
		separator_4.setPreferredSize(new Dimension(0, 34));
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setBounds(21, 30, 10, 206);
		frame.getContentPane().add(separator_4);

		JSeparator separator_5 = new JSeparator();
		separator_5.setPreferredSize(new Dimension(0, 24));
		separator_5.setOrientation(SwingConstants.VERTICAL);
		separator_5.setBounds(825, 21, 6, 215);
		frame.getContentPane().add(separator_5);

		JSeparator separator_6 = new JSeparator();
		separator_6.setPreferredSize(new Dimension(250, 0));
		separator_6.setBounds(21, 26, 811, 5);
		frame.getContentPane().add(separator_6);

		JSeparator separator_7 = new JSeparator();
		separator_7.setPreferredSize(new Dimension(250, 0));
		separator_7.setBounds(21, 45, 811, 5);
		frame.getContentPane().add(separator_7);

		textFieldDisplay = new JTextField();
		textFieldDisplay.setEditable(false);
		textFieldDisplay.setBounds(652, 155, 163, 63);
		frame.getContentPane().add(textFieldDisplay);
		textFieldDisplay.setColumns(10);

		JLabel lblDisplay = new JLabel("Display:");
		lblDisplay.setBounds(652, 139, 61, 14);
		frame.getContentPane().add(lblDisplay);

		ButtonGroup selectingShelvesButtonGroup = new ButtonGroup();
		selectingShelvesButtonGroup.add(rdbtnSelectShelve1);
		selectingShelvesButtonGroup.add(rdbtnSelectShelve2);
		selectingShelvesButtonGroup.add(rdbtnSelectShelve3);
		selectingShelvesButtonGroup.add(rdbtnSelectShelve4);

		lblProductY1X1.setText(products[0][0].getName());
		lblProductY1X2.setText(products[0][0].getName());
		lblProductY1X3.setText(products[0][0].getName());
		lblProductY1X4.setText(products[0][0].getName());
		lblProductY1X5.setText(products[0][0].getName());
		lblProductY1X6.setText(products[0][0].getName());
		lblPricePTY1.setText(products[0][0].getPrice().toString());

		lblProductY2X1.setText(products[1][0].getName());
		lblProductY2X2.setText(products[1][0].getName());
		lblProductY2X3.setText(products[1][0].getName());
		lblProductY2X4.setText(products[1][0].getName());
		lblProductY2X5.setText(products[1][0].getName());
		lblProductY2X6.setText(products[1][0].getName());
		lblPricePTY2.setText(products[1][0].getPrice().toString());

		lblProductY3X1.setText(products[2][0].getName());
		lblProductY3X2.setText(products[2][0].getName());
		lblProductY3X3.setText(products[2][0].getName());
		lblProductY3X4.setText(products[2][0].getName());
		lblProductY3X5.setText(products[2][0].getName());
		lblProductY3X6.setText(products[2][0].getName());
		lblPricePTY3.setText(products[2][0].getPrice().toString());

		lblProductY4X1.setText(products[3][0].getName());
		lblProductY4X2.setText(products[3][0].getName());
		lblProductY4X3.setText(products[3][0].getName());
		lblProductY4X4.setText(products[3][0].getName());
		lblProductY4X5.setText(products[3][0].getName());
		lblProductY4X6.setText(products[3][0].getName());
		lblPricePTY4.setText(products[3][0].getPrice().toString());

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
