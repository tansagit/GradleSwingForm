package forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import actions.downloadAttachments;

import javax.swing.JButton;
import java.awt.TextArea;
import javax.swing.SwingConstants;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.io.IOException;

public class mainForm extends JFrame {

	private JPanel contentPane;
	private JTextField txtEmail;
	private JPasswordField txtPassword;
	private JLabel lblSum;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainForm frame = new mainForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public mainForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		String host = "pop.gmail.com";
		String port = "995";
		String saveDirectory = "E:\\Attachments";

		JButton btnNewButton = new JButton("Scan");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null, "Hi there!");
				String userName = txtEmail.getText().toString();
				String password = new String(txtPassword.getPassword());

				downloadAttachments dl = new downloadAttachments();
				dl.setSaveDirectory(saveDirectory);
				try {
					dl.downloadEmailAttachments(host, port, userName, password);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				lblSum.setText(password);

			}
		});
		btnNewButton.setBounds(502, 42, 119, 48);
		contentPane.add(btnNewButton);

		txtEmail = new JTextField();
		txtEmail.setBounds(36, 49, 205, 35);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);

		JLabel lblInputEmailTo = new JLabel("Input email to download attachments");
		lblInputEmailTo.setBounds(36, 13, 275, 16);
		contentPane.add(lblInputEmailTo);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(265, 49, 181, 35);
		contentPane.add(txtPassword);

		lblSum = new JLabel("");
		lblSum.setBounds(76, 132, 322, 35);
		contentPane.add(lblSum);
	}

}
