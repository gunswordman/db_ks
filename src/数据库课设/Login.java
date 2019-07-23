package 数据库课设;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;
import java.lang.Object.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.*;
 

public class Login extends JFrame implements ActionListener {
    private JPanel pan = new JPanel();
    private JLabel namelab = new JLabel("用户名");
    private JLabel passlab = new JLabel("密    码");
    private JTextField nametext = new JTextField();
    private JPasswordField passtext = new JPasswordField();
 
    public JButton denglu = new JButton("登录");
 
    public Login() {
        Font font = new Font("宋体", Font.BOLD, 12);
        super.setTitle("欢迎登录本系统");
        pan.setLayout(null);
        namelab.setBounds(20, 20, 60, 30);
        nametext.setBounds(90, 20, 140, 30);
        passlab.setBounds(20, 60, 60, 30);
        passtext.setBounds(90, 60, 140, 30);
        denglu.setBounds(100, 100, 90, 20);
        
        pan.add(namelab);
        pan.add(nametext);
        pan.add(passlab);
        pan.add(passtext);
        pan.add(denglu);
        
        passtext.setFont(font);        
 
        denglu.addActionListener(this);
 
        super.add(pan);
        super.setSize(300, 200);
        super.setVisible(true);
    }
 
    public static void main(String[] args) {
        new Login();
    }
 
    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == denglu) {
            denglu();
        }
 
    }
    
    Connection con = null;
    Statement statement = null;
    ResultSet res = null;
	String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/ks?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    String name = "public";
    String passwd = "123";
    
    //登录按钮的事件处理函数
    public void denglu() {
    	jdbc();
        String username = nametext.getText();
        String password = String.copyValueOf(passtext.getPassword());
        int sign=compare(username, password);
        if (sign!=0) {
            JOptionPane.showMessageDialog(null, "登录成功！");
            super.setVisible(false);
            if(sign==1) {
            	manager a=new manager(con);
            }
            else if(sign==2){
            	teacher b=new teacher(con,username);
            }
            else if(sign==3){
            	student c=new student(con,username);
            }
            else {
            	
            }
        }    
    } 
    public void close() {
    	try{
			if(statement!=null)
				statement.close();
			if(con!=null)
				con.close();
		}catch(SQLException ex)
		{
			System.out.println("/nSQL操作异常/n");
			System.out.println("异常信息："+ex.getMessage());
			System.out.println("SQL状态:"+ex.getSQLState());
		}
    }
    //jdbc连接数据库
    public void jdbc() {
    	try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, name, passwd);
            statement = con.createStatement();
 
        } catch (ClassNotFoundException e) {
            System.out.println("对不起，找不到这个Driver");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //对比用户名和密码是不匹配
    public int compare(String username, String password) {
        int m = 0;
        String sql = "select * from user where user=\"" + username + "\"";
        try {
            res = statement.executeQuery(sql);
            if (res.next()) {
                String pa = res.getString("password");
                //System.out.println(pa + " " + password);
                if (pa.equals(password)) {
                    m = res.getInt("type");
                } else {
                    JOptionPane.showMessageDialog(null, "密码错误！");
                    res.close();
                    con.close();
                    statement.close();
                }
            	} else {
            		JOptionPane.showMessageDialog(null, "用户名不存在！");
                	res.close();
                	con.close();
                	statement.close();
            	}         
        	} catch (SQLException e) {
        			e.printStackTrace();
        	}        
        return m;
    } 
}

class manager
{
    boolean packOk=false;

    public manager(Connection con)
    {
        ManagerFrame frame=new ManagerFrame(con);
        if(packOk)
            frame.pack();
        else
            frame.validate();

        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize=frame.getSize();
        if(frameSize.height>screenSize.height)
            frameSize.height=screenSize.height-100;

        if(frameSize.width>screenSize.width)
            frameSize.width=screenSize.width;

        frame.setLocation((screenSize.width-frameSize.width)/2,(screenSize.height-frameSize.height)/2);
        frame.setVisible(true);
    }
}

class teacher
{
    boolean packOk=false;
    
    public teacher(Connection con,String username)
    {
        TeacherFrame frame=new TeacherFrame(con,username);
        if(packOk)
            frame.pack();
        else
            frame.validate();

        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize=frame.getSize();
        if(frameSize.height>screenSize.height)
            frameSize.height=screenSize.height-100;

        if(frameSize.width>screenSize.width)
            frameSize.width=screenSize.width;

        frame.setLocation((screenSize.width-frameSize.width)/2,(screenSize.height-frameSize.height)/2);
        frame.setVisible(true);
    }
}

class student
{
    boolean packOk=false;

    public student(Connection con,String username)
    {
        StudentFrame frame=new StudentFrame(con,username);
        if(packOk)
            frame.pack();
        else
            frame.validate();

        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize=frame.getSize();
        if(frameSize.height>screenSize.height)
            frameSize.height=screenSize.height-100;

        if(frameSize.width>screenSize.width)
            frameSize.width=screenSize.width;

        frame.setLocation((screenSize.width-frameSize.width)/2,(screenSize.height-frameSize.height)/2);
        frame.setVisible(true);
    }
}

class TeacherFrame extends JFrame
{
    private JPanel contentPane;
    private FlowLayout myLayout=new FlowLayout( );  //构造XYLayout布局管理器
    
    String username;
	//创建显示信息使用的组件
    private JLabel label2=new JLabel("课程");
    private JTextField CourseField=new JTextField(4);  
    private JLabel label1=new JLabel("学号");
    private JTextField noField=new JTextField(8);
    private JLabel label4=new JLabel("分数");
    private JTextField GradeField=new JTextField(4);     
    private JButton updateButton=new JButton("录入");
    private JButton quitButton=new JButton("退出");
    private JButton queryByNoButton=new JButton("学号查询");
    private JButton allRecordButton=new JButton("全部记录");
    
    Vector vector;
    String title[] ={ "学号", "姓名",  "课程", "分数"};   

    Connection connect=null; //声明Connection接口对象connect
    ResultSet rSet=null;        //定义数据库查询的结果集
    Statement stat=null;   //定义查询数据库的Statement对象
    AbstractTableModel tm;      //声明一个AbstractTableModel类对象tm

    public TeacherFrame(Connection con,String username)
    {
    	this.username=username;
    	this.connect=con;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try{
            jbInit();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        contentPane=(JPanel) this.getContentPane();

        //初始化组件
        contentPane.setLayout(myLayout);   //设置容器的布局管理对象
        setSize(new Dimension(850,350));   //设置容器窗口的大小
        setTitle("学生选课成绩管理系统");
        
        updateButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                updateButton_actionPerformed(e);
            }
        });

        queryByNoButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	queryByNoButton_actionPerformed(e);
            }
        });

        allRecordButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	allRecordButton_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
            }
        });
        quitButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                quitButton_actionPerformed(e);
            }
        });
        
        createTable();  //在初始化函数中调用createtable()函数显示表格
        contentPane.add(label2);
        contentPane.add(CourseField);
        contentPane.add(label1);
        contentPane.add(noField);
        contentPane.add(label4);
        contentPane.add(GradeField);

        contentPane.add(updateButton);
        contentPane.add(queryByNoButton);
        contentPane.add(allRecordButton);
        contentPane.add(quitButton);
       }

    void createTable()
    {
    	JTable table;
    	JScrollPane scroll;
    	vector=new Vector();

    	tm=new AbstractTableModel()
        {
            public int getColumnCount()
            {
            	return title.length;
            }

            public int getRowCount()
            {
            	return vector.size();
            }

            public Object getValueAt(int row,int column)
            {
                if(!vector.isEmpty())
                    return ((Vector)vector.elementAt(row)).elementAt(column);
                else return null;
            }

            public void setValueAt(Object value,int row,int column)
            {
            	//数据模型不可编辑，该方法设置为空
            }

            public String getColumnName(int column)
            {
            	return title[column];
            }

            public Class getColumnClass(int c)
            {
            	return getValueAt(0,c).getClass();
            }

            public boolean isCellEditable(int row,int column)
            {
            	    //设置显示的单元格不可编辑
            	return false;
            }
        };

        table=new JTable(tm);   //生成数据表
        table.setToolTipText("Display Query Result");   //设置帮助提示
        table.setAutoResizeMode(table.AUTO_RESIZE_ALL_COLUMNS); //设置表格调整尺寸模式
        table.setCellSelectionEnabled(false);   //设置单元格选择方式
        table.setShowHorizontalLines(true); //设置是否显示单元格之间的分割线
        table.setShowVerticalLines(true);
        scroll=new JScrollPane(table);  //给表格加上滚动杠
        scroll.setPreferredSize(new Dimension(800,200));
        contentPane.add(scroll);
    }
    
    protected void processWindowEvent(WindowEvent e)
    {
    	super.processWindowEvent(e);
    	if(e.getID()==WindowEvent.WINDOW_CLOSING)
        {
    		System.exit(0);
        }
    }

	//对表learned中的记录根据在各文本框中的输入值进行修改
    void updateButton_actionPerformed(ActionEvent e)
    {
	try{
		stat=connect.createStatement();
		String sql="select * from toperation where sno='"+ noField.getText()+"'";
		rSet=stat.executeQuery(sql);
		if(rSet.next()==false)
                {
			JOptionPane.showMessageDialog(TeacherFrame.this, "修改的记录不存在！","修改记录",1);
                }
		else if(noField.getText().equals("")||GradeField.getText().equals(""))
        {
        	JOptionPane.showMessageDialog(TeacherFrame.this, "未填写分数","添加记录",1);
        }else
                {            
               	 	float Grade=Float.parseFloat(GradeField.getText());                             
                    String sqlstr="update toperation set grade="+
               	 	Float.parseFloat( GradeField.getText())+
               	 	" where sno='"+
               	 	noField.getText()+
               	 	"'AND cname='"+
               	 	CourseField.getText()+
               	 	"' And ctea='"+
               	 	username+
               	 	"'";
                    stat.executeUpdate(sqlstr);
                    JOptionPane.showMessageDialog(TeacherFrame.this, "修改完成!","修改信息",1);
                    allRecordButton_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
                }

		noField.setText("");//清空信息框
		GradeField.setText("");
        }catch(SQLException ex)
        {
        	System.out.println("/nSQL操作异常/n");
        	while(ex!=null)
         	{
                    System.out.println("异常信息："+ex.getMessage());
                    System.out.println("SQL状态:"+ex.getSQLState());
                    
                    ex=ex.getNextException();
         	}
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }
    void quitButton_actionPerformed(ActionEvent e)
    {   	
		try{
			stat=connect.createStatement();
			if(stat!=null)
				stat.close();
			if(connect!=null)
				connect.close();
		}catch(SQLException ex)
		{
			System.out.println("/nSQL操作异常/n");
			System.out.println("异常信息："+ex.getMessage());
			System.out.println("SQL状态:"+ex.getSQLState());
		}
		super.setVisible(false);
	}			
	//按照no，执行表student的学号查询
    void queryByNoButton_actionPerformed(ActionEvent e)
    {
    	try{
    		stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    		String sql="select sno,sname,cname,grade from toperation where sno='"+noField.getText()+"'AND ctea='"+
    		username+"'";
    		rSet=stat.executeQuery(sql);
    		if(rSet.next()==false)
         	{
    			JOptionPane.showMessageDialog(TeacherFrame.this, "数据库中没有您查询的学号！","学号查询",1);
         	}
    		else
    		{
    			vector.removeAllElements();
    			tm.fireTableStructureChanged();
    			rSet.previous();
    			while(rSet.next())
	        	{
    				Vector rec_vector=new Vector();
    				rec_vector.addElement(rSet.getString(1));
    				rec_vector.addElement(rSet.getString(2));
    				rec_vector.addElement(rSet.getString(3));
                    rec_vector.addElement(rSet.getFloat(4));    		
			        rec_vector.addElement("");
			        rec_vector.addElement("");
    				vector.addElement(rec_vector);//向量rec_vector加入向量vector中
	        	}
    		}
    	}catch(SQLException ex)
        {
            System.out.println("/nSQL操作异常/n");
            while(ex!=null)
            {
            	System.out.println("异常信息："+ex.getMessage());
            	System.out.println("SQL状态:"+ex.getSQLState());            	
            	ex=ex.getNextException();
            }
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }

	//执行表student表的所有记录
    void allRecordButton_actionPerformed(ActionEvent e)
    {
    	try{   
            stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String sql="select sno,sname,cname,grade from toperation where cname='"+CourseField.getText()+
            		"' AND ctea='"+username+"'";
            if(CourseField.getText().equals("")) {
        		JOptionPane.showMessageDialog(TeacherFrame.this, "没有输入要查询的课程！",null,1);
        	}
            else {
            	rSet=stat.executeQuery(sql);
            	vector.removeAllElements();
            	tm.fireTableStructureChanged();
            	while(rSet.next())
            	{
            		Vector rec_vector=new Vector();
            		rec_vector.addElement(rSet.getString(1));
            		rec_vector.addElement(rSet.getString(2));
            		rec_vector.addElement(rSet.getString(3));
            		rec_vector.addElement(rSet.getFloat(4));
            		rec_vector.addElement("");
            		rec_vector.addElement("");              
            		vector.addElement(rec_vector);//向量rec_vector加入向量vector中
            	}
            }
             tm.fireTableStructureChanged();
	}catch(SQLException ex)
	{
        	System.out.println("/nSQL操作异常/n");
        	while(ex!=null)
        	{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());
        		ex=ex.getNextException();
        	}
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
    }
}



class StudentFrame extends JFrame
{
    private JPanel contentPane;
    private FlowLayout myLayout=new FlowLayout( );  //构造XYLayout布局管理器
    String username;
	//创建显示信息使用的组件
    private JLabel label1=new JLabel("课程编号");    
    private JLabel label2=new JLabel("课程名");
    private JLabel label3=new JLabel("先行课程");
    private JLabel label4=new JLabel("先行课程编号");
    private JLabel label5=new JLabel("已修课程");
    private JLabel label6=new JLabel("成绩");
    private JLabel label7=new JLabel("已选课程号");
    private JLabel label8=new JLabel("已选课程");
    private JLabel label9=new JLabel("选课");
    private JTextField SeField=new JTextField(4);
    
    private JButton indicateButton1 =new JButton("显示");
    private JButton indicateButton2 =new JButton("显示");
    private JButton indicateButton3 =new JButton("显示");
    private JButton addButton =new JButton("添加");
    private JButton deleteButton=new JButton("删除");
    private JButton quitButton=new JButton("退出");
    
    Vector vector;
    String title1[] ={ "课程编号", "课程名",  "先行课程", "先行课编号"};
    String title2[] ={ "已修课程", "成绩"};
    String title3[] ={"已选课程号","已选课程"};

    // 表头
    Connection connect=null; //声明Connection接口对象connect
    ResultSet rSet=null;        //定义数据库查询的结果集
    Statement stat=null;   //定义查询数据库的Statement对象
    AbstractTableModel tm1;      //声明一个AbstractTableModel类对象tm
    AbstractTableModel tm2;
    AbstractTableModel tm3; 
    
    public StudentFrame(Connection con,String username)
    {
    	this.username=username;
    	this.connect=con;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try{
            jbInit();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        contentPane=(JPanel) this.getContentPane();
        //初始化组件
        contentPane.setLayout(myLayout);   //设置容器的布局管理对象
        setSize(new Dimension(400,700));   //设置容器窗口的大小
        setTitle("学生选课成绩管理系统");

    	addButton.addActionListener(new java.awt.event.ActionListener( )
        {
        	//注册按钮事件监听对象,实现ActionListener接口的actionPerformed方法
            public void actionPerformed(ActionEvent e)
            {
                addButton_actionPerformed(e);
            }
    	});

    	deleteButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                deleteButton_actionPerformed(e);
            }
        });

        indicateButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	indicateButton1_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
            }
        });
        
        indicateButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	indicateButton2_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
            }
        });
   
        indicateButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	indicateButton3_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
            }
        });
        quitButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                quitButton_actionPerformed(e);
            }
        });
        
        createTable1();  //在初始化函数中调用createtable()函数显示表格
        contentPane.add(indicateButton1);

        createTable2();  //在初始化函数中调用createtable()函数显示表格
        contentPane.add(indicateButton2);
        
        createTable3();
        contentPane.add(SeField);
        contentPane.add(addButton);
        contentPane.add(deleteButton);
        contentPane.add(indicateButton3);
        contentPane.add(quitButton);
       }

    void createTable1()
    {
    	JTable table;
    	JScrollPane scroll;
    	vector=new Vector();

    	tm1=new AbstractTableModel()
        {
            public int getColumnCount()
            {
            	return title1.length;
            }

            public int getRowCount()
            {
            	return vector.size();
            }

            public Object getValueAt(int row,int column)
            {
                if(!vector.isEmpty())
                    return ((Vector)vector.elementAt(row)).elementAt(column);
                else return null;
            }

            public void setValueAt(Object value,int row,int column)
            {
            	//数据模型不可编辑，该方法设置为空
            }

            public String getColumnName(int column)
            {
            	return title1[column];
            }

            public Class getColumnClass(int c)
            {
            	return getValueAt(0,c).getClass();
            }

            public boolean isCellEditable(int row,int column)
            {
            	    //设置显示的单元格不可编辑
            	return false;
            }
        };
        
        table=new JTable(tm1);   //生成数据表
        table.setToolTipText("Display Query Result");   //设置帮助提示
        table.setAutoResizeMode(table.AUTO_RESIZE_ALL_COLUMNS); //设置表格调整尺寸模式
        table.setCellSelectionEnabled(false);   //设置单元格选择方式
        table.setShowHorizontalLines(true); //设置是否显示单元格之间的分割线
        table.setShowVerticalLines(true);
        scroll=new JScrollPane(table);  //给表格加上滚动杠
        scroll.setPreferredSize(new Dimension(200,200));
        contentPane.add(scroll);
    }
    
    void createTable2()
    {
    	JTable table;
    	JScrollPane scroll;
    	vector=new Vector();

    	tm2=new AbstractTableModel()
        {
            public int getColumnCount()
            {
            	return title2.length;
            }

            public int getRowCount()
            {
            	return vector.size();
            }

            public Object getValueAt(int row,int column)
            {
                if(!vector.isEmpty())
                    return ((Vector)vector.elementAt(row)).elementAt(column);
                else return null;
            }

            public void setValueAt(Object value,int row,int column)
            {
            	//数据模型不可编辑，该方法设置为空
            }

            public String getColumnName(int column)
            {
            	return title2[column];
            }

            public Class getColumnClass(int c)
            {
            	return getValueAt(0,c).getClass();
            }

            public boolean isCellEditable(int row,int column)
            {
            	    //设置显示的单元格不可编辑
            	return false;
            }
        };
        
        table=new JTable(tm2);   //生成数据表
        table.setToolTipText("Display Query Result");   //设置帮助提示
        table.setAutoResizeMode(table.AUTO_RESIZE_ALL_COLUMNS); //设置表格调整尺寸模式
        table.setCellSelectionEnabled(false);   //设置单元格选择方式
        table.setShowHorizontalLines(true); //设置是否显示单元格之间的分割线
        table.setShowVerticalLines(true);
        scroll=new JScrollPane(table);  //给表格加上滚动杠
        scroll.setPreferredSize(new Dimension(200,200));
        contentPane.add(scroll);
    }
    
    void createTable3()
    {
    	JTable table;
    	JScrollPane scroll;
    	vector=new Vector();

    	tm3=new AbstractTableModel()
        {
            public int getColumnCount()
            {
            	return title3.length;
            }

            public int getRowCount()
            {
            	return vector.size();
            }

            public Object getValueAt(int row,int column)
            {
                if(!vector.isEmpty())
                    return ((Vector)vector.elementAt(row)).elementAt(column);
                else return null;
            }

            public void setValueAt(Object value,int row,int column)
            {
            	//数据模型不可编辑，该方法设置为空
            }

            public String getColumnName(int column)
            {
            	return title3[column];
            }

            public Class getColumnClass(int c)
            {
            	return getValueAt(0,c).getClass();
            }

            public boolean isCellEditable(int row,int column)
            {
            	    //设置显示的单元格不可编辑
            	return false;
            }
        };
        
        table=new JTable(tm3);   //生成数据表
        table.setToolTipText("Display Query Result");   //设置帮助提示
        table.setAutoResizeMode(table.AUTO_RESIZE_ALL_COLUMNS); //设置表格调整尺寸模式
        table.setCellSelectionEnabled(false);   //设置单元格选择方式
        table.setShowHorizontalLines(true); //设置是否显示单元格之间的分割线
        table.setShowVerticalLines(true);
        scroll=new JScrollPane(table);  //给表格加上滚动杠
        scroll.setPreferredSize(new Dimension(200,200));
        contentPane.add(scroll);
    }
    
    protected void processWindowEvent(WindowEvent e)
    {
    	super.processWindowEvent(e);
    	if(e.getID()==WindowEvent.WINDOW_CLOSING)
        {
    		System.exit(0);
        }
    }
    void addButton_actionPerformed(ActionEvent e)
    {
    	    //处理addrecord-JButton(添加按钮)的ActionEvent
    	try{    
            stat=connect.createStatement();
            String sql="select cno,cname from student2 where sno='"+username+"'";
            rSet=stat.executeQuery(sql);
            if(rSet.next()==true)
            {
                JOptionPane.showMessageDialog(StudentFrame.this, "已预选课程","添加记录",1);
            }
              else if(SeField.getText().equals(""))
              {
              	JOptionPane.showMessageDialog(StudentFrame.this, "未填写课程号","添加记录",1);
              }
            else{
            	 String Se = SeField.getText();
            	 String sqlstr = "insert into student2(sno,cno)"+ "values('" +
                         username + "','" + SeField.getText() + "')";
                         stat.executeUpdate(sqlstr);//向表中添加记录
                 JOptionPane.showMessageDialog(StudentFrame.this, "添加成功！","添加记录",1);
                 indicateButton3_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
            } 
            SeField.setText("");//清空信息框
    	}catch(SQLException ex)
        {
        	System.out.println("/nSQL操作异常1/n");
        	while(ex!=null)
        	{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());       		
        		ex=ex.getNextException();
        	}
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }

	//对表student中的记录根据输入的学号进行删除
    void deleteButton_actionPerformed(ActionEvent e)
    {
    	try{
	
    		stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    		String sql="select * from student2 where cno='"+ SeField.getText()+
    				"' and sno='"+username+"'";
    		rSet=stat.executeQuery(sql);
    		if(rSet.next()==false)
        	{
    			JOptionPane.showMessageDialog(StudentFrame.this, "没有您删除的课程",
					"删除记录",1);
        	}else
         	{
        		String sqlstr="delete from learning where sno='"+username+"' and cno='"+
        		SeField.getText()+"'";
        		stat.executeUpdate(sqlstr);  //删除student表中对应no的数据记录	   
                SeField.setText("");//清空信息框

        		JOptionPane.showMessageDialog(StudentFrame.this, "删除成功！","删除记录",1);
        		indicateButton3_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
        	}
        }catch(SQLException ex)
        {
        	System.out.println("/nSQL操作异常/n");
        	while(ex!=null)
        	{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());
        		ex=ex.getNextException();
        	}
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }

	//执行表student表的所有记录
    void indicateButton1_actionPerformed(ActionEvent e)
    {
    	try{          
            stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String sql="select * from student1 ";
            rSet=stat.executeQuery(sql);
            vector.removeAllElements();
            tm1.fireTableStructureChanged();
            while(rSet.next())
            {
    		Vector rec_vector=new Vector();
    		rec_vector.addElement(rSet.getString(1));
    		rec_vector.addElement(rSet.getString(2));
            rec_vector.addElement(rSet.getString(3));
            rec_vector.addElement(rSet.getString(4));
            rec_vector.addElement("");
            rec_vector.addElement("");
                
    		vector.addElement(rec_vector);//向量rec_vector加入向量vector中
            }
             tm1.fireTableStructureChanged();
    		}catch(SQLException ex)
    		{
    			System.out.println("/nSQL操作异常/n");
    			while(ex!=null)
    			{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());
        		ex=ex.getNextException();
    			}
    		}catch(Exception ex)
    		{
    			ex.printStackTrace();
    		}
    }
    void indicateButton2_actionPerformed(ActionEvent e)
    {
    	try{          
            stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String sql="select cname,grade from student3 where sno='"+username+"'";
            rSet=stat.executeQuery(sql);
            vector.removeAllElements();
            tm2.fireTableStructureChanged();
            while(rSet.next())
            {
    		Vector rec_vector=new Vector();
    		rec_vector.addElement(rSet.getString(1));
    		rec_vector.addElement(rSet.getString(2));
            rec_vector.addElement("");
            rec_vector.addElement("");  
            
    		vector.addElement(rec_vector);//向量rec_vector加入向量vector中
            }
             tm2.fireTableStructureChanged();
    		}catch(SQLException ex)
    		{
    			System.out.println("/nSQL操作异常/n");
    			while(ex!=null)
    			{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());
        		ex=ex.getNextException();
    			}
    		}catch(Exception ex)
    	{
    			ex.printStackTrace();
    	}    		
    }
    void indicateButton3_actionPerformed(ActionEvent e)
    {
    	try{          
            stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String sql="select cno,cname from student2 ";
            rSet=stat.executeQuery(sql);
            vector.removeAllElements();
            tm3.fireTableStructureChanged();
            while(rSet.next())
            {
    		Vector rec_vector=new Vector();
    		rec_vector.addElement(rSet.getString(1));
    		rec_vector.addElement(rSet.getString(2));
            rec_vector.addElement("");
            rec_vector.addElement("");
                
    		vector.addElement(rec_vector);//向量rec_vector加入向量vector中
            }
             tm3.fireTableStructureChanged();
    		}catch(SQLException ex)
    		{
    			System.out.println("/nSQL操作异常/n");
    			while(ex!=null)
    			{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());
        		ex=ex.getNextException();
    			}
    		}catch(Exception ex)
    	{
    			ex.printStackTrace();
    	}
    }
    void quitButton_actionPerformed(ActionEvent e)
    {   	
		try{
			stat=connect.createStatement();
			if(stat!=null)
				stat.close();
			if(connect!=null)
				connect.close();
		}catch(SQLException ex)
		{
			System.out.println("/nSQL操作异常/n");
			System.out.println("异常信息："+ex.getMessage());
			System.out.println("SQL状态:"+ex.getSQLState());
		}
		super.setVisible(false);
	}			
}

class ManagerFrame extends JFrame
{
    private JPanel contentPane;
    private FlowLayout myLayout=new FlowLayout( );  //构造XYLayout布局管理器
	//创建显示信息使用的组件
    private JLabel label1=new JLabel("学号");
    private JTextField noField=new JTextField(8);
    private JLabel label2=new JLabel("姓名");
    private JTextField nameField=new JTextField(6);
    private JLabel label3=new JLabel("性别");
    private JTextField sexField=new JTextField(4);
    private JLabel label4=new JLabel("年龄");
    private JTextField ageField=new JTextField(4);
    private JLabel label5=new JLabel("专业");
    private JTextField deptField=new JTextField(4);
    private JLabel label6 = new JLabel("密码");
    private JTextField pwField = new JTextField(8);
    private JLabel label7 = new JLabel("其他");
    private JTextField otherField = new JTextField(4);     
    private JButton addRecordButton1 =new JButton("添加");
    private JButton deleteButton1=new JButton("删除");
    private JButton updateButton1=new JButton("修改");
    private JButton queryByNoButton1=new JButton("学号查询");
    private JButton allRecordButton1=new JButton("全部记录");
    private JButton quitButton1=new JButton("退出");

    
    private JLabel label21=new JLabel("编号");
    private JTextField tnoField=new JTextField(8);
    private JLabel label22=new JLabel("姓名");
    private JTextField tnameField=new JTextField(6);
    private JLabel label23=new JLabel("性别");
    private JTextField tsexField=new JTextField(4);
    private JLabel label24=new JLabel("学院");
    private JTextField tcollField=new JTextField(4);
    private JLabel label25=new JLabel("密码");
    private JTextField tpwField=new JTextField(8);
    private JLabel label26=new JLabel("其他");
    private JTextField totherField=new JTextField(8);
       
    private JButton addRecordButton2 =new JButton("添加");
    private JButton deleteButton2=new JButton("删除");
    private JButton updateButton2=new JButton("修改");
    private JButton queryByNoButton2=new JButton("学号查询");
    private JButton allRecordButton2=new JButton("全部记录");
    private JButton quitButton2=new JButton("退出");
    Vector vector;
    String title1[] ={ "学号", "姓名",  "性别", "年龄", "专业", "密码", "其他", };   
    String title2[]	= {"编号","姓名","性别","学院","密码","其他"};
    // 表头
    Connection connect=null; //声明Connection接口对象connect
    ResultSet rSet=null;        //定义数据库查询的结果集
    Statement stat=null;   //定义查询数据库的Statement对象
    AbstractTableModel tm1;      //声明一个AbstractTableModel类对象tm
    AbstractTableModel tm2;
    
    public ManagerFrame(Connection con)
    {
    	this.connect=con;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try{
            jbInit();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        contentPane=(JPanel) this.getContentPane();

        //初始化组件
        contentPane.setLayout(myLayout);   //设置容器的布局管理对象
        setSize(new Dimension(850,700));   //设置容器窗口的大小
        setTitle("管理员");

    	addRecordButton1.addActionListener(new java.awt.event.ActionListener( )
        {
        	//注册按钮事件监听对象,实现ActionListener接口的actionPerformed方法
            public void actionPerformed(ActionEvent e)
            {
                addRecordButton1_actionPerformed(e);
            }
    	});

    	deleteButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                deleteButton1_actionPerformed(e);
            }
        });

        updateButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                updateButton1_actionPerformed(e);
            }
        });

        queryByNoButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	queryByNoButton1_actionPerformed(e);
            }
        });

        allRecordButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	allRecordButton1_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
            }
        });
        quitButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                quitButton1_actionPerformed(e);
            }
        });
        
        addRecordButton2.addActionListener(new java.awt.event.ActionListener( )
        {
        	//注册按钮事件监听对象,实现ActionListener接口的actionPerformed方法
            public void actionPerformed(ActionEvent e)
            {
                addRecordButton2_actionPerformed(e);
            }
    	});

    	deleteButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                deleteButton2_actionPerformed(e);
            }
        });

        updateButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                updateButton2_actionPerformed(e);
            }
        });

        queryByNoButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	queryByNoButton2_actionPerformed(e);
            }
        });

        allRecordButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	allRecordButton2_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
            }
        });
        quitButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                quitButton2_actionPerformed(e);
            }
        });



        createTable1();  //在初始化函数中调用createtable()函数显示表格
        contentPane.add(label1);
        contentPane.add(noField);
        contentPane.add(label2);
        contentPane.add(nameField);
        contentPane.add(label3);
        contentPane.add(sexField);
        contentPane.add(label4);
        contentPane.add(ageField);
        contentPane.add(label5);
        contentPane.add(deptField);
        contentPane.add(label6);
        contentPane.add(pwField);
        contentPane.add(label7);
        contentPane.add(otherField);

        contentPane.add(addRecordButton1);
        contentPane.add(deleteButton1);
        contentPane.add(updateButton1);
        contentPane.add(queryByNoButton1);
        contentPane.add(allRecordButton1);
        contentPane.add(quitButton1);
        
        
        createTable2();  //在初始化函数中调用createtable()函数显示表格
        contentPane.add(label21);
        contentPane.add(tnoField);
        contentPane.add(label22);
        contentPane.add(tnameField);
        contentPane.add(label23);
        contentPane.add(tsexField);
        contentPane.add(label24);
        contentPane.add(tcollField);
        contentPane.add(label25);
        contentPane.add(tpwField);
        contentPane.add(label26);
        contentPane.add(totherField);

        contentPane.add(addRecordButton2);
        contentPane.add(deleteButton2);
        contentPane.add(updateButton2);
        contentPane.add(queryByNoButton2);
        contentPane.add(allRecordButton2);
        contentPane.add(quitButton2);
       }

    void createTable1()
    {
    	JTable table;
    	JScrollPane scroll;
    	vector=new Vector();

    	tm1=new AbstractTableModel()
        {
            public int getColumnCount()
            {
            	return title1.length;
            }

            public int getRowCount()
            {
            	return vector.size();
            }

            public Object getValueAt(int row,int column)
            {
                if(!vector.isEmpty())
                    return ((Vector)vector.elementAt(row)).elementAt(column);
                else return null;
            }

            public void setValueAt(Object value,int row,int column)
            {
            	//数据模型不可编辑，该方法设置为空
            }

            public String getColumnName(int column)
            {
            	return title1[column];
            }

            public Class getColumnClass(int c)
            {
            	return getValueAt(0,c).getClass();
            }

            public boolean isCellEditable(int row,int column)
            {
            	    //设置显示的单元格不可编辑
            	return false;
            }
        };

        table=new JTable(tm1);   //生成数据表
        table.setToolTipText("Display Query Result");   //设置帮助提示
        table.setAutoResizeMode(table.AUTO_RESIZE_ALL_COLUMNS); //设置表格调整尺寸模式
        table.setCellSelectionEnabled(false);   //设置单元格选择方式
        table.setShowHorizontalLines(true); //设置是否显示单元格之间的分割线
        table.setShowVerticalLines(true);
        scroll=new JScrollPane(table);  //给表格加上滚动杠
        scroll.setPreferredSize(new Dimension(800,200));
        contentPane.add(scroll);
    }
    void createTable2()
    {
    	JTable table;
    	JScrollPane scroll;
    	vector=new Vector();

    	tm2=new AbstractTableModel()
        {
            public int getColumnCount()
            {
            	return title2.length;
            }

            public int getRowCount()
            {
            	return vector.size();
            }

            public Object getValueAt(int row,int column)
            {
                if(!vector.isEmpty())
                    return ((Vector)vector.elementAt(row)).elementAt(column);
                else return null;
            }

            public void setValueAt(Object value,int row,int column)
            {
            	//数据模型不可编辑，该方法设置为空
            }

            public String getColumnName(int column)
            {
            	return title2[column];
            }

            public Class getColumnClass(int c)
            {
            	return getValueAt(0,c).getClass();
            }

            public boolean isCellEditable(int row,int column)
            {
            	    //设置显示的单元格不可编辑
            	return false;
            }
        };

        table=new JTable(tm2);   //生成数据表
        table.setToolTipText("Display Query Result");   //设置帮助提示
        table.setAutoResizeMode(table.AUTO_RESIZE_ALL_COLUMNS); //设置表格调整尺寸模式
        table.setCellSelectionEnabled(false);   //设置单元格选择方式
        table.setShowHorizontalLines(true); //设置是否显示单元格之间的分割线
        table.setShowVerticalLines(true);
        scroll=new JScrollPane(table);  //给表格加上滚动杠
        scroll.setPreferredSize(new Dimension(800,200));
        contentPane.add(scroll);
    }
    
    
    protected void processWindowEvent(WindowEvent e)
    {
    	super.processWindowEvent(e);
    	if(e.getID()==WindowEvent.WINDOW_CLOSING)
        {
    		System.exit(0);
        }
    }

    void addRecordButton1_actionPerformed(ActionEvent e)
    {
    	    //处理addrecord-JButton(添加按钮)的ActionEvent
    	try{
            stat=connect.createStatement();
            String sql="select * from student where sno='"+ noField.getText()+"'";
            rSet=stat.executeQuery(sql);
            if(rSet.next()==true)
            {
                JOptionPane.showMessageDialog(ManagerFrame.this, "学号已经存在","添加记录",1);
            }
              else if(noField.getText().equals("")||pwField.getText().equals(""))
              {
              	JOptionPane.showMessageDialog(ManagerFrame.this, "学号、密码不得为空！","添加记录",1);
              }
            else{	
            	String no=noField.getText();
            	String name=nameField.getText();
            	String sex=sexField.getText();
            	int age=Integer.parseInt(ageField.getText());
            	String dept=deptField.getText();
            	String pw=pwField.getText();
            	String comment=otherField.getText();
            	
            	PreparedStatement pst =null;
            	String sqlstr = "insert into student values(?,?,?,?,?,?,?)"; 
            	pst = connect.prepareStatement(sqlstr);
            	pst.setString(1, no);
            	pst.setString(2, name);
            	pst.setString(3,sex);
            	pst.setInt(4, age);
            	pst.setString(5,dept);
            	pst.setString(6, pw);
            	pst.setString(7,comment);            
                JOptionPane.showMessageDialog(ManagerFrame.this, "添加成功！","添加记录",1);
                allRecordButton1_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
            } 

            noField.setText("");//清空信息框
            nameField.setText("");
            sexField.setText("");
            ageField.setText("");
            deptField.setText("");
            pwField.setText("");
            otherField.setText("");
    	}catch(SQLException ex)
        {
        	System.out.println("/nSQL操作异常1/n");
        	while(ex!=null)
        	{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());       		
        		ex=ex.getNextException();
        	}
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }

	//对表student中的记录根据输入的学号进行删除
    void deleteButton1_actionPerformed(ActionEvent e)
    {
    	try{
    		stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    		String sql="select * from student where sno='"+ noField.getText()+"'";
    		rSet=stat.executeQuery(sql);
    		if(rSet.next()==false)
        	{
    			JOptionPane.showMessageDialog(ManagerFrame.this, "数据库中没有您删除的学号",
					"删除记录",1);
        	}else
         	{
        		String sqlstr="delete from student where sno='"+noField.getText()+"'";
        		stat.executeUpdate(sqlstr);  //删除student表中对应no的数据记录
        		     //清空信息框
        		noField.setText("");
        		nameField.setText("");
        		sexField.setText("");
        		ageField.setText("");
        		deptField.setText("");
        		pwField.setText("");
        		otherField.setText("");
        		JOptionPane.showMessageDialog(ManagerFrame.this, "删除成功！","删除记录",1);
        		allRecordButton1_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
        	}
        }catch(SQLException ex)
        {
        	System.out.println("/nSQL操作异常/n");
        	while(ex!=null)
        	{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());
        		ex=ex.getNextException();
        	}
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }

	//对表student和studentaddress中的记录根据在各文本框中的输入值进行修改
    void updateButton1_actionPerformed(ActionEvent e)
    {
	try{		
		stat=connect.createStatement();
		String sql="select * from student where sno='"+ noField.getText()+"'";
		rSet=stat.executeQuery(sql);
		if(rSet.next()==false)
                {
			JOptionPane.showMessageDialog(ManagerFrame.this, "修改的记录不存在！","修改记录",1);
                }
		else if(noField.getText().equals("")||pwField.getText().equals(""))
        {
        	JOptionPane.showMessageDialog(ManagerFrame.this, "学号、密码不能为空","添加记录",1);
        }else
                {
        			String sqlstr="update student set sname='"+nameField.getText()+
        					"',ssex='"+sexField+
        					"',sage='"+ageField+
        					"',sdept='"+deptField+
        					"',pw='"+pwField+
        					",comment='"+otherField+
        					"'where sno='"+noField.getText()+"'";
                    stat.executeUpdate(sqlstr);
                    JOptionPane.showMessageDialog(ManagerFrame.this, "修改完成!","修改信息",1);
                    allRecordButton1_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
                }

		noField.setText("");//清空信息框
		nameField.setText("");
		sexField.setText("");
		ageField.setText("");
		deptField.setText("");
		pwField.setText("");
		otherField.setText("");
        }catch(SQLException ex)
        {
        	System.out.println("/nSQL操作异常/n");
        	while(ex!=null)
         	{
                    System.out.println("异常信息："+ex.getMessage());
                    System.out.println("SQL状态:"+ex.getSQLState());
                    
                    ex=ex.getNextException();
         	}
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }  
    }

	//按照no，执行表student的学号查询
    void queryByNoButton1_actionPerformed(ActionEvent e)
    {
    	try{   		
    		stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    		String sql="select * from student where sno='"+noField.getText()+"'";
    		rSet=stat.executeQuery(sql);
    		if(rSet.next()==false)
         	{
    			JOptionPane.showMessageDialog(ManagerFrame.this, "数据库中没有您查询的学号！","学号查询",1);
         	}
    		else
    		{
    			vector.removeAllElements();
    			tm1.fireTableStructureChanged();
    			rSet.previous();
    			while(rSet.next())
	        	{
    				Vector rec_vector=new Vector();
    				rec_vector.addElement(rSet.getString(1));
    				rec_vector.addElement(rSet.getString(2));
    			    rec_vector.addElement(rSet.getString(3));
                    rec_vector.addElement(rSet.getString(4));
    		        rec_vector.addElement(rSet.getString(5));
			        rec_vector.addElement(rSet.getString(6));
			        rec_vector.addElement(rSet.getString(7));
			        rec_vector.addElement("");
			        rec_vector.addElement("");
    				vector.addElement(rec_vector);//向量rec_vector加入向量vector中
	        	}
    		}
    	}catch(SQLException ex)
        {
            System.out.println("/nSQL操作异常/n");
            while(ex!=null)
            {
            	System.out.println("异常信息："+ex.getMessage());
            	System.out.println("SQL状态:"+ex.getSQLState());            	
            	ex=ex.getNextException();
            }
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }

	//执行表student表的所有记录
    void allRecordButton1_actionPerformed(ActionEvent e)
    {
    	try{          
            stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String sql="select sno,sname,ssex,sage,sdept,pw,comment from student ";
            rSet=stat.executeQuery(sql);
            vector.removeAllElements();
            tm1.fireTableStructureChanged();
            while(rSet.next())
            {
    		Vector rec_vector=new Vector();
    		rec_vector.addElement(rSet.getString(1));
    		rec_vector.addElement(rSet.getString(2));
    		rec_vector.addElement(rSet.getString(3));
    		rec_vector.addElement(rSet.getInt(4));
    		rec_vector.addElement(rSet.getString(5));
    		rec_vector.addElement(rSet.getString(6));
    		rec_vector.addElement(rSet.getString(7));
            rec_vector.addElement("");
            rec_vector.addElement("");
                
    		vector.addElement(rec_vector);//向量rec_vector加入向量vector中
            }
             tm1.fireTableStructureChanged();
    		}catch(SQLException ex)
    		{
    			System.out.println("/nSQL操作异常/n");
    			while(ex!=null)
    			{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());
        		ex=ex.getNextException();
    			}
    		}catch(Exception ex)
    	{
    			ex.printStackTrace();
    	}
    }
    void quitButton1_actionPerformed(ActionEvent e)
    {   	
		try{
			stat=connect.createStatement();
			if(stat!=null)
				stat.close();
			if(connect!=null)
				connect.close();
		}catch(SQLException ex)
		{
			System.out.println("/nSQL操作异常/n");
			System.out.println("异常信息："+ex.getMessage());
			System.out.println("SQL状态:"+ex.getSQLState());
		}
		super.setVisible(false);
	}	
    
    void addRecordButton2_actionPerformed(ActionEvent e)
    {
    	    //处理addrecord-JButton(添加按钮)的ActionEvent
    	try{
            stat=connect.createStatement();
            String sql="select * from student where sno='"+ noField.getText()+"'";
            rSet=stat.executeQuery(sql);
            if(rSet.next()==true)
            {
                JOptionPane.showMessageDialog(ManagerFrame.this, "学号已经存在","添加记录",2);
            }
              else if(noField.getText().equals("")||pwField.getText().equals(""))
              {
              	JOptionPane.showMessageDialog(ManagerFrame.this, "学号、密码不得为空！","添加记录",2);
              }
            else{	
            	String no=tnoField.getText();
            	String name=tnameField.getText();
            	String sex=tsexField.getText();          
            	String coll=tcollField.getText();
            	String pw=tpwField.getText();
            	String comment=totherField.getText();
            	
            	PreparedStatement pst =null;
            	String sqlstr = "insert into tracher values(?,?,?,?,?,?)"; 
            	pst = connect.prepareStatement(sqlstr);
            	pst.setString(1, no);
            	pst.setString(2, name);
            	pst.setString(3,sex);
            	pst.setString(4,coll);
            	pst.setString(5, pw);
            	pst.setString(6,comment);            
                JOptionPane.showMessageDialog(ManagerFrame.this, "添加成功！","添加记录",2);
                allRecordButton2_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
            } 

            tnoField.setText("");//清空信息框
            tnameField.setText("");
            tsexField.setText("");         
            tcollField.setText("");
            tpwField.setText("");
            totherField.setText("");
    	}catch(SQLException ex)
        {
        	System.out.println("/nSQL操作异常2/n");
        	while(ex!=null)
        	{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());       		
        		ex=ex.getNextException();
        	}
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }

	//对表student中的记录根据输入的学号进行删除
    void deleteButton2_actionPerformed(ActionEvent e)
    {
    	try{
    		stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    		String sql="select * from teacher where sno='"+ noField.getText()+"'";
    		rSet=stat.executeQuery(sql);
    		if(rSet.next()==false)
        	{
    			JOptionPane.showMessageDialog(ManagerFrame.this, "数据库中没有您删除的学号",
					"删除记录",2);
        	}else
         	{
        		String sqlstr="delete from teacher where sno='"+tnoField.getText()+"'";
        		stat.executeUpdate(sqlstr);  //删除student表中对应no的数据记录
        		     //清空信息框
        		tnoField.setText("");
        		tnameField.setText("");
        		tsexField.setText("");        		
        		tcollField.setText("");
        		tpwField.setText("");
        		totherField.setText("");
        		JOptionPane.showMessageDialog(ManagerFrame.this, "删除成功！","删除记录",2);
        		allRecordButton2_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
        	}
        }catch(SQLException ex)
        {
        	System.out.println("/nSQL操作异常/n");
        	while(ex!=null)
        	{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());
        		ex=ex.getNextException();
        	}
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }

	//对表student和studentaddress中的记录根据在各文本框中的输入值进行修改
    void updateButton2_actionPerformed(ActionEvent e)
    {
	try{		
		stat=connect.createStatement();
		String sql="select * from teacher where sno='"+ noField.getText()+"'";
		rSet=stat.executeQuery(sql);
		if(rSet.next()==false)
                {
			JOptionPane.showMessageDialog(ManagerFrame.this, "修改的记录不存在！","修改记录",2);
                }
		else if(noField.getText().equals("")||pwField.getText().equals(""))
        {
        	JOptionPane.showMessageDialog(ManagerFrame.this, "学号、密码不能为空","添加记录",2);
        }else
                {
        			String sqlstr="update student set sname='"+tnameField.getText()+
        					"',ssex='"+tsexField+
        					"',sdept='"+tcollField+
        					"',pw='"+tpwField+
        					",comment='"+totherField+
        					"'where sno='"+tnoField.getText()+"'";
                    stat.executeUpdate(sqlstr);
                    JOptionPane.showMessageDialog(ManagerFrame.this, "修改完成!","修改信息",2);
                    allRecordButton2_actionPerformed(e);   // 触发显示所有记录的按钮，显示更新后的结果
                }

		tnoField.setText("");//清空信息框
		tnameField.setText("");
		tsexField.setText("");	
		tcollField.setText("");
		tpwField.setText("");
		totherField.setText("");
        }catch(SQLException ex)
        {
        	System.out.println("/nSQL操作异常/n");
        	while(ex!=null)
         	{
                    System.out.println("异常信息："+ex.getMessage());
                    System.out.println("SQL状态:"+ex.getSQLState());
                    
                    ex=ex.getNextException();
         	}
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }  
    }

	//按照no，执行表student的学号查询
    void queryByNoButton2_actionPerformed(ActionEvent e)
    {
    	try{   		
    		stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    		String sql="select * from teacher where tno='"+tnoField.getText()+"'";
    		rSet=stat.executeQuery(sql);
    		if(rSet.next()==false)
         	{
    			JOptionPane.showMessageDialog(ManagerFrame.this, "数据库中没有您查询的学号！","学号查询",2);
         	}
    		else
    		{
    			vector.removeAllElements();
    			tm2.fireTableStructureChanged();
    			rSet.previous();
    			while(rSet.next())
	        	{
    				Vector rec_vector=new Vector();
    				rec_vector.addElement(rSet.getString(1));
    				rec_vector.addElement(rSet.getString(2));
    			    rec_vector.addElement(rSet.getString(3));
                    rec_vector.addElement(rSet.getString(4));
    		        rec_vector.addElement(rSet.getString(5));
			        rec_vector.addElement(rSet.getString(6));			      
			        rec_vector.addElement("");
			        rec_vector.addElement("");
    				vector.addElement(rec_vector);//向量rec_vector加入向量vector中
	        	}
    		}
    	}catch(SQLException ex)
        {
            System.out.println("/nSQL操作异常/n");
            while(ex!=null)
            {
            	System.out.println("异常信息："+ex.getMessage());
            	System.out.println("SQL状态:"+ex.getSQLState());            	
            	ex=ex.getNextException();
            }
        }catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }

	//执行表student表的所有记录
    void allRecordButton2_actionPerformed(ActionEvent e)
    {
    	try{          
            stat=connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String sql="select tno,tname,tsex,tcoll,pw,comment from teacher ";
            rSet=stat.executeQuery(sql);
            vector.removeAllElements();
            tm2.fireTableStructureChanged();
            while(rSet.next())
            {
    		Vector rec_vector=new Vector();
    		rec_vector.addElement(rSet.getString(1));
    		rec_vector.addElement(rSet.getString(2));
    		rec_vector.addElement(rSet.getString(3));
    		rec_vector.addElement(rSet.getString(4));
    		rec_vector.addElement(rSet.getString(5));
    		rec_vector.addElement(rSet.getString(6));
            rec_vector.addElement("");
            rec_vector.addElement("");
                
    		vector.addElement(rec_vector);//向量rec_vector加入向量vector中
            }
             tm2.fireTableStructureChanged();
    		}catch(SQLException ex)
    		{
    			System.out.println("/nSQL操作异常/n");
    			while(ex!=null)
    			{
        		System.out.println("异常信息："+ex.getMessage());
        		System.out.println("SQL状态:"+ex.getSQLState());
        		ex=ex.getNextException();
    			}
    		}catch(Exception ex)
    	{
    			ex.printStackTrace();
    	}
    }
    void quitButton2_actionPerformed(ActionEvent e)
    {   	
		try{
			stat=connect.createStatement();
			if(stat!=null)
				stat.close();
			if(connect!=null)
				connect.close();
		}catch(SQLException ex)
		{
			System.out.println("/nSQL操作异常/n");
			System.out.println("异常信息："+ex.getMessage());
			System.out.println("SQL状态:"+ex.getSQLState());
		}
		super.setVisible(false);
	}	
}


 









 




 


