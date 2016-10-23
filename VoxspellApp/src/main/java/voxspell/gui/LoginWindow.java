package voxspell.gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import voxspell.app.VoxModel;
import voxspell.toolbox.VoxDatabase;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class LoginWindow extends JFrame {

	private LoginWindow itself = this;
	
	private JPanel contentPane;
	
	private JTextField usrName;
	private JTextField password;


	/**
	 * Create the frame.
	 */
	public LoginWindow() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 221);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogIn = new JLabel("Log In!");
		lblLogIn.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
		lblLogIn.setForeground(new Color(102, 0, 255));
		lblLogIn.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogIn.setBounds(34, 11, 320, 38);
		contentPane.add(lblLogIn);
		
		JLabel lblUserName = new JLabel("User name:");
		lblUserName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUserName.setBounds(26, 60, 96, 20);
		contentPane.add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(26, 91, 96, 20);
		contentPane.add(lblPassword);
		
		usrName = new JTextField();
		usrName.setBounds(145, 60, 232, 20);
		usrName.setText("");
		contentPane.add(usrName);
		usrName.setColumns(10);
		
		password = new JTextField();
		password.setBounds(145, 91, 232, 20);
		password.setText("");
		contentPane.add(password);
		password.setColumns(10);
		
		final JCheckBox rememberMe = new JCheckBox("Remember me next time");
		rememberMe.setBackground(new Color(204, 255, 255));
		rememberMe.setBounds(100, 118, 240, 20);
		contentPane.add(rememberMe);
		
		
		JButton btnLogin = new JButton("Login!");
		btnLogin.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				if (usrName.getText().matches("\\s*")){
					JOptionPane.showMessageDialog(itself, "Sorry. Username cannot be blank.\n");
					return;
				}
				
				if (password.getText().matches("\\s*")){
					JOptionPane.showMessageDialog(itself, "Sorry. Password cannot be blank.\n");
					return;
				}
				
				
				boolean userExists = checkIfUsrExists();
				if (!userExists){
					JOptionPane.showMessageDialog(itself, "Sorry. Your supplied username is invalid.\n"
							+ "Please try once more or register first.");
					return;
				}
				
				boolean isPasswordValid = checkPassword();
				if (!isPasswordValid){
					return;
				}
				
				if (rememberMe.isSelected()){
					VoxDatabase.writeToSysFile("rememberedUsr", usrName.getText(), false);
				}
				
				login(usrName.getText());
				
				
				itself.dispose();
				
			}
			
		});
		btnLogin.setBackground(new Color(51, 102, 255));
		btnLogin.setForeground(new Color(204, 255, 255));
		btnLogin.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		btnLogin.setBounds(202, 145, 131, 26);
		contentPane.add(btnLogin);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				

				if (usrName.getText().matches("\\s*")){
					JOptionPane.showMessageDialog(itself, "Sorry. Username cannot be blank.\n");
					return;
				}
				
				if (password.getText().matches("\\s*")){
					JOptionPane.showMessageDialog(itself, "Sorry. Password cannot be blank.\n");
					return;
				}
				
				
				boolean userExists = checkIfUsrExists();
				
				if (userExists){
					JOptionPane.showMessageDialog(itself, "Sorry. This username has already been registered.\n"
							+ "Please try another one.");
					return;
				}
				
				createUsr(usrName.getText(), password.getText());
				
				if (rememberMe.isSelected()){
					VoxDatabase.writeToSysFile("rememberedUsr", usrName.getText(), false);
				}
				
				login(usrName.getText());
				
				
				itself.dispose();
				
			}
			
		});
		btnRegister.setBackground(new Color(51, 102, 255));
		btnRegister.setForeground(new Color(204, 255, 255));
		btnRegister.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		btnRegister.setBounds(69, 145, 118, 26);
		contentPane.add(btnRegister);
	}
	
	private boolean checkIfUsrExists(){

		String name = usrName.getText();
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<String> registeredUsrs = VoxDatabase.getContents().get(1);

		if (!(registeredUsrs.isEmpty())){
			
			for (String record : registeredUsrs){
				String[] temp = record.split(" ");
				users.add(temp[0]);
			}

			if (!(users.contains(name))){
				
				return false;
			}

			return true;

		}

		return false;

	}

	private boolean checkPassword(){

		String name = usrName.getText();
		String pw = password.getText();
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<String> passwords = new ArrayList<String>();
		ArrayList<String> registeredUsrs = VoxDatabase.getContents().get(1);

		if (!(registeredUsrs.isEmpty())){
			
			for (String record : registeredUsrs){
				String[] temp = record.split(" ");
				users.add(temp[0]);
				passwords.add(temp[1]);
			}

			for (int i=0; i<users.size(); i++){
				if (name.equals(users.get(i))){
					if (pw.equals(passwords.get(i))){
						return true;
					}
				}
			}

			JOptionPane.showMessageDialog(itself, "Sorry. Your supplied password is incorrect.\n"
					+ "Please try once more.");
			return false;

		}

		JOptionPane.showMessageDialog(itself, "Sorry. Cannot find any registered user at this moment.\n"
				+ "Please register first.");
		return false;

	}
	
	private void createUsr(String usrName, String password){
		
		VoxDatabase.writeToSysFile("registeredUsr", usrName+" "+password+"\n", true);
		
	}
	
	private void login(String usrName){
		
		VoxDatabase.loadUsrInfo(usrName, true);
		
		for (WindowPattern wp : VoxModel.getVoxModel().getGuis()){
			wp.getLogin().setEnabled(false);
			wp.getLogoff().setEnabled(true);
			wp.updateUsr(usrName);
		}
		
		JOptionPane.showMessageDialog(itself, "Welcome! You have successfully logged in as "+usrName+" :)");
		
	}
	
}
