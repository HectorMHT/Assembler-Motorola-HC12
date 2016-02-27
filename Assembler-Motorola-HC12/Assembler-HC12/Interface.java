
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
 //@Author Héctor Huerta Toledo
	public class Interface extends JFrame implements ActionListener{
		
		private static final long serialVersionUID = 1L;
		Assembler mainx;
		String rasm="",rerr="",rval="";
		
		JFileChooser expasm = new JFileChooser();
		
		
		DefaultTableModel Main_tab = new DefaultTableModel();
		JTable Main_table = new JTable(Main_tab);
		JScrollPane scrolled_main_tab = new JScrollPane(Main_table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		DefaultTableModel Mess_tab = new DefaultTableModel();
		JTable Mess_table = new JTable(Mess_tab);
		JScrollPane scrolled_mess_tab = new JScrollPane(Mess_table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JButton cmdopenasm = new JButton("Load asm file");
		JButton cmdgo = new JButton("All ready");
		
		FileNameExtensionFilter filtroasm=new FileNameExtensionFilter("ASM","asm");

	

		// File Access field
		private static Interface main;
		
		public Interface() {
			Window_config();
		}

		// Frame view configuration
		public void Window_config() {
  
			// Configuration frame
			this.setTitle("Assembler HC12 Beta");
			expasm.setDialogTitle("Open ...");
			this.setBounds(0,0,610, 600);
			this.setLocationRelativeTo(null);
			this.setLayout(null);
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Disable tlable resize mode
			Main_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			// Components positions on the frame
			scrolled_main_tab.setBounds(25, 40, 560, 350);
			cmdopenasm.setBounds(25, 10, 112, 20);
			cmdgo.setBounds(471, 10, 112, 20);
			scrolled_mess_tab.setBounds(25, 400, 560, 150);
			
			// Add Columns to Main Table
			Main_tab.addColumn("Line");
			Main_tab.addColumn("Label");
			Main_tab.addColumn("Operation code");
			Main_tab.addColumn("Addressing");
			
			Mess_tab.addColumn("Event");
			Mess_tab.addColumn("Line");
			Mess_tab.addColumn("Message");
			

			
			
			// Add components to frame
			this.add(scrolled_main_tab);
			this.add(cmdopenasm);

			this.add(cmdgo);
			this.add(scrolled_mess_tab);
			

			// Set Width for the columns of the Main table
			Main_table.getColumnModel().getColumn(0).setPreferredWidth(35);
			Main_table.getColumnModel().getColumn(1).setPreferredWidth(125);
			Main_table.getColumnModel().getColumn(2).setPreferredWidth(200);
			Main_table.getColumnModel().getColumn(3).setPreferredWidth(200);


			// Default Main table first Row

			// Add components to the action listener
			cmdopenasm.addActionListener(this);
			cmdgo.addActionListener(this);

			// Set content into the table at right
			align_data();

			// Set frame visible
			this.setVisible(true);
		}
		public void Add_row(String row[]) {

			Main_tab.addRow(row);

		}
		public void Add_mess_row(String mess[]) {
			Mess_tab.addRow(mess);

		}
		@Override
		public void actionPerformed(ActionEvent evt) {
			Object pressed = evt.getSource();
			if (cmdopenasm == pressed) {
				expasm.showOpenDialog(getParent());
					File Fasm = expasm.getSelectedFile();
					rasm = Fasm.getPath();
					rerr=rasm.substring(0, rasm.length()-4)+".err";
					rval=rasm.substring(0, rasm.length()-4)+".inst"; 
						
					}
			
			if(cmdgo == pressed && !rasm.equals("") && !rerr.equals("") && !rval.equals("") ){
				clear(Main_table, Mess_table);
				Assembler Nuevo=new Assembler();
				Nuevo.ensamblar(this,rasm,rerr,rval);
			}else if(cmdgo == pressed){
				 JOptionPane.showMessageDialog(this, "One or more files aren't specified",null,JOptionPane.ERROR_MESSAGE);
			}
		}

		public static void main(String []args) throws IOException{
			 try{
				 javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		        }catch (Exception ex) {
					ex.printStackTrace();
		        }
			setMain(new Interface());
		}
		public void align_data() {

			DefaultTableCellRenderer Alg_R = new DefaultTableCellRenderer();
			Alg_R.setHorizontalAlignment(SwingConstants.RIGHT);
			Main_table.getColumnModel().getColumn(0).setCellRenderer(Alg_R);
			Main_table.getColumnModel().getColumn(1).setCellRenderer(Alg_R);
			Main_table.getColumnModel().getColumn(2).setCellRenderer(Alg_R);
			Main_table.getColumnModel().getColumn(3).setCellRenderer(Alg_R);
		}
		public void clear(JTable table, JTable table2) {
			try {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				int filas = table.getRowCount();
				for (int i = 0; filas > i; i++) {
					model.removeRow(0);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Cleaning table Error",null,JOptionPane.ERROR_MESSAGE);
			}
			try {
				DefaultTableModel model1 = (DefaultTableModel) table2.getModel();
				int filas = table2.getRowCount();
				for (int i = 0; filas > i; i++) {
					model1.removeRow(0);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Cleaning table Error",null,JOptionPane.ERROR_MESSAGE);
			}
		}

		public static Interface getMain() {
			return main;
		}

		public static void setMain(Interface main) {
			Interface.main = main;
		}
	}


