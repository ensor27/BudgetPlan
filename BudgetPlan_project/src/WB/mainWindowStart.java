package WB;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import PDF.PDF_Godziny_wgGodzin;
import PDF.BudgetPlan;

public class mainWindowStart extends JFrame {

	private JPanel contentPane;
	private JTextField dataRozpoczecia;
	private JTextField dataZakonczenia;
	private JLabel lblWprowadDzieRozpoczcia;
	private JLabel lblWprowadDzieZakoczenia;
	private JLabel lblWprowadOczekiwanLiczb;

	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindowStart frame = new mainWindowStart();
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
	public mainWindowStart() {
		setResizable(false);
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 420, 420);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		dataRozpoczecia = new JTextField();
		dataRozpoczecia.setBounds(132, 48, 138, 38);
		contentPane.add(dataRozpoczecia);
		dataRozpoczecia.setColumns(10);
		
		dataZakonczenia = new JTextField();
		dataZakonczenia.setBounds(132, 146, 138, 38);
		contentPane.add(dataZakonczenia);
		dataZakonczenia.setColumns(10);
		
		lblWprowadDzieRozpoczcia = new JLabel("Wprowad\u017A dzie\u0144 rozpocz\u0119cia analizy, format: rrrr-MM-dd");
		lblWprowadDzieRozpoczcia.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblWprowadDzieRozpoczcia.setBounds(24, 0, 369, 57);
		contentPane.add(lblWprowadDzieRozpoczcia);
		
		lblWprowadDzieZakoczenia = new JLabel("Wprowad\u017A dzie\u0144 zako\u0144czenia analizy, format: rrrr-MM-dd");
		lblWprowadDzieZakoczenia.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblWprowadDzieZakoczenia.setBounds(24, 97, 369, 38);
		contentPane.add(lblWprowadDzieZakoczenia);
		
		JButton analizaGodzin = new JButton("Rozpocznij analiz\u0119 pracownik\u00F3w");
		analizaGodzin.setFont(new Font("Tahoma", Font.PLAIN, 11));
		analizaGodzin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				String dataRozp = dataRozpoczecia.getText();
				String dataZak = dataZakonczenia.getText();
				
				//sprawdzenie daty rozpoczecia
				if(!checkDatePattern(dataRozp)){
					JOptionPane.showMessageDialog(null, "Wprowadz poprawny format daty rrrr-mm-dd");
					dataRozpoczecia.setText("");
				}
				
				//sprawdzenie daty zakonczenia
				else if(!checkDatePattern(dataZak)){
					JOptionPane.showMessageDialog(null, "Wprowadz poprawny format daty rrrr-mm-dd");
					dataZakonczenia.setText("");
				}
				else{
					try {
						System.out.println("Robi analize");
						BudgetPlan.createRaport(dataRozp, dataZak);
						System.exit(0);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		analizaGodzin.setBounds(89, 287, 232, 38);
		contentPane.add(analizaGodzin);
		//Image img = new ImageIcon(this.getClass().getResource("/BackgroundImage.jpg")).getImage();
		
		
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(-383, -402, 812, 812);
		contentPane.add(lblNewLabel);
	}
	
	private static boolean checkDatePattern(String data) {
	    try {
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        format.parse(data);
	        return true;
	    } catch (ParseException e) {
	        return false;
	    }
	}
}