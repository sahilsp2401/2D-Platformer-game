package Login;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;

public class Registration extends JFrame implements ActionListener{

	JLabel title2,name,no,email,pass,pic,title;
	JButton regi,cancel;
	JTextField tname,temail,tno;
	JPasswordField tpass;
	ImageIcon bg_login,img;
	
	
	Registration()
	{

		bg_login = new ImageIcon(this.getClass().getResource("/Login_BG.jpg"));
		pic = new JLabel(bg_login);
		pic.setSize(400,600);

		img = new ImageIcon(this.getClass().getResource("/ninja.png"));
		title = new JLabel(img);

	    title2 = new JLabel("Registration");
	    name = new JLabel("Name");
	    no = new JLabel("Mobile No.");
	    email = new JLabel("Email ID");
	    pass = new JLabel("Password");
	    
	    regi = new JButton("Register");
	    cancel = new JButton("Cancel");
	    regi.addActionListener(this);
		cancel.addActionListener(this);
		
		tname = new JTextField(30);
		temail = new JTextField(30);
		tno = new JTextField(30);
		tpass = new JPasswordField(30);

		Font f3=new Font("times new roman", Font.PLAIN, 30);
		Font f2 = new Font("Comic Sans MS", Font.BOLD, 20);
		Font f4 = new Font("arial",Font.PLAIN,16);

		title2.setFont(f3);
		title2.setForeground(Color.cyan);
		name.setFont(f2);
		name.setForeground(Color.MAGENTA);
		no.setFont(f2);
		no.setForeground(Color.MAGENTA);
		email.setFont(f2);
		email.setForeground(Color.MAGENTA);
		pass.setFont(f2);
		pass.setForeground(Color.MAGENTA);
		tname.setFont(f4);
		tno.setFont(f4);
		temail.setFont(f4);
		tpass.setFont(f4);


		pic.add(title2);
		pic.add(name);
		pic.add(no);
		pic.add(email);
		pic.add(pass);
		pic.add(regi);
		pic.add(cancel);
		pic.add(tname);
		pic.add(tno);
		pic.add(temail);
		pic.add(tpass);
		pic.add(title);

		title2.setBounds(120,110,180,50);
		name.setBounds(60,170,100,30);
		tname.setBounds(200,170,150,30);
		email.setBounds(60,220,100,30);
		temail.setBounds(200,220,150,30);
		no.setBounds(60,270,120,30);
		tno.setBounds(200,270,150,30);
		pass.setBounds(60,320,100,30);
		tpass.setBounds(200,320,150,30);
		regi.setBounds(80,400,100,30);
		cancel.setBounds(200,400,100,30);
		title.setBounds(65,30,260,75);

		add(pic);
		setSize(400,600);
		setVisible(true);
		setTitle("Registration");
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);


		tname.requestFocus();
		tname.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent ke) {
                if (tname.getText().length() >= 30)
                    ke.consume();
                if (ke.getKeyChar() >= 'A' && ke.getKeyChar() <= 'Z')
                    return;
                else if (ke.getKeyChar() >= 'a' && ke.getKeyChar() <= 'z')
                    return;
                else if (ke.getKeyChar() == ' ')
                    return;
                else
                    ke.consume();
            }
        });
		 tno.addKeyListener(new KeyAdapter() {
	            public void keyTyped(KeyEvent ke) {
	                if (tno.getText().length() >= 10)
	                    ke.consume();
	                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9')
	                    return;
	                else
	                    ke.consume();
	            }
	        });
		
		 
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		LoginForm obj = new LoginForm();
		if(ae.getSource()==cancel) {
			dispose();
			obj.setVisibility(true);
		}
		if(ae.getSource()==regi)
		{
			if(tname.getText().length()==0)
				JOptionPane.showMessageDialog(this,"Enter Name");
			else if(temail.getText().length()==0)
				JOptionPane.showMessageDialog(this,"Enter Email ID");
			else if(tno.getText().length()==0)
				JOptionPane.showMessageDialog(this,"Enter Mobile Number");
			else if(tpass.getText().length()==0)
				JOptionPane.showMessageDialog(this,"Enter Password");
			else {
				InsertData();
				dispose();
				obj.setVisible(true);
			}
		}
	}

	
	public void InsertData()
	{
		try {
			String dname,demail,dno,dpass;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con= DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/game","root","Sahil@2401");
			if (con!=null)
				System.out.println("Connection Successfull");
			dname=tname.getText();
			demail=temail.getText();
			dno=tno.getText();
			dpass=tpass.getText();
			
			String insertQuery = "INSERT INTO user (name, email, no, pass) VALUES (?, ?, ?, ?)";
			PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, dname);
            preparedStatement.setString(2, demail);
            preparedStatement.setString(3, dno);
            preparedStatement.setString(4, dpass);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Account created successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to create account.");
            }
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
