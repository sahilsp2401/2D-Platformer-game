package Login;

import Main.Game;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class LoginForm extends JFrame implements ActionListener{
	JLabel uname,pass,create,pic,title;
	JButton login,cancel;
	ImageIcon bg_login;
	JTextField t1;
	ImageIcon img;
	JPasswordField tpass;
	
LoginForm()
{
	bg_login = new ImageIcon(this.getClass().getResource("/Login_BG.jpg"));
	pic = new JLabel(bg_login);
	pic.setSize(400,600);

	img = new ImageIcon(this.getClass().getResource("/ninja.png"));
	title = new JLabel(img);
	title.setSize(100,100);
	uname = new JLabel("Username");
	pass = new JLabel("Password");
	create = new JLabel("Create New Account");
	login = new JButton("Login");
	cancel = new JButton("Cancel");
	login.addActionListener(this);
	cancel.addActionListener(this);
	t1 = new JTextField(30);
	tpass = new JPasswordField(30);



	create.setForeground(Color.black);
	Font f4 = new Font("arial",Font.PLAIN,16);
	Font f2 = new Font("Comic Sans MS", Font.BOLD, 20);
	Font f3 = new Font("Bell MT",Font.BOLD,16);

	create.setForeground(Color.CYAN);
	uname.setForeground(Color.MAGENTA);
	pass.setForeground(Color.MAGENTA);
	create.setFont(f3);
	uname.setFont(f2);
	t1.setFont(f4);
	tpass.setFont(f4);
	pass.setFont(f2);

	pic.add(uname);
	pic.add(pass);
	pic.add(create);
	pic.add(login);
	pic.add(cancel);
	pic.add(t1);
	pic.add(tpass);
	pic.add(title);



	
	t1.requestFocus();
	uname.setBounds(70,200,150,30);
	t1.setBounds(230,200,100,30);
	pass.setBounds(70,260,150,30);
	tpass.setBounds(230,260,100,30);
	login.setBounds(100,380,80,30);
	cancel.setBounds(200,380,80,30);
	create.setBounds(120,320,150,30);
	title.setBounds(65,75,260,75);

	add(pic);
	setSize(400,600);
	setVisible(true);
	setTitle("Login");
	setLayout(null);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	setResizable(false);
	
	create.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	create.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			new Registration();
			setVisible(false);
		}
	});
	}
	public void setVisibility(boolean visibility){
		setVisible(visibility);
	}
public void actionPerformed(ActionEvent ae)
{
	if(ae.getSource()==cancel)
		dispose();
	if(ae.getSource()==login) {
		authenticateUser();
	}
		
}
public static void main(String[] args) {
	new LoginForm();
	}



public void authenticateUser() {
    
    String enteredUsername = t1.getText();
    char[] enteredPasswordChars = tpass.getPassword();
    String enteredPassword = new String(enteredPasswordChars);

    try  {
    	Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con= DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/game","root","Sahil@2401");
		if (con!=null)
			System.out.println("Connection Successfull");
		
        String selectQuery = "SELECT * FROM user WHERE name = ? AND pass = ?";

        PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
	
	
            preparedStatement.setString(1, enteredUsername);
            preparedStatement.setString(2, enteredPassword);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
				JOptionPane.showMessageDialog(null, "Login successful!!!");
				new Game();
				setVisibility(false);
			}
             else 
                JOptionPane.showMessageDialog(null, "Login failed. Please check your credentials.");
            
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    	}catch (Exception e){
    			System.out.println(e);
		}
	}
}

