import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;

public class KayitFormu extends  JDialog {
    private JTextField txtAdi;
    private JTextField txtEmail;
    private JTextField txtTelefon;
    private JTextField txtAdres;
    private JPasswordField txtSifre;
    private JPasswordField txtSifreTekrar;
    private JButton btnKayıt;
    private JButton txtGeri;
    private JPanel kayitPanel;

    public KayitFormu(JFrame parent){
        super(parent);
        setTitle("Yeni Kayıt Oluştur");
        setContentPane(kayitPanel);
        setMinimumSize(new Dimension(500,574));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnKayıt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kayitUser();
            }
        });
        txtGeri.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void kayitUser() {
        String adi=txtAdi.getText();
        String email=txtEmail.getText();
        String telefon=txtTelefon.getText();
        String adres=txtAdres.getText();
        String sifre=txtSifre.getText();
        String sifretekrar=txtSifreTekrar.getText();

        if (adi.isEmpty()||email.isEmpty()||telefon.isEmpty()||adres.isEmpty()||sifre.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Lütfen Tüm Bilgileri Giriniz",
                    "Tekrar Deneyin",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!sifre.equals(sifretekrar)){
            JOptionPane.showMessageDialog(this,
                    "Şifre Eşleşmiyor",
                    "Tekrar Deneyin",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

       user= addUserToDatabase(adi,email,telefon,adres,sifre);
        if (user !=null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Yeni Kullanıcı Kayıt Edilemedi",
                    "Tekrar Deneyin",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public User user;
    private User addUserToDatabase(String adi, String email, String telefon, String adres, String sifre) {
        User user=null;
        final String DB_URL="jdbc:mysql://localhost:3306/kayitdata?serverTimezone=UTC";
        final String USERNAME="root";
        final String PASSWORD="";

        try {
            Connection conn= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement stmt=conn.createStatement();
            String sql="INSERT INTO user(adi,email,telefon,adres,sifre)"+
                    "VALUES(?,?,?,?,?)";

            PreparedStatement preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setString(1,adi);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,telefon);
            preparedStatement.setString(4,adres);
            preparedStatement.setString(5,sifre);

            int addedRows=preparedStatement.executeUpdate();
            if (addedRows>0){
                user=new User();
                user.adi=adi;
                user.email=email;
                user.telefon=telefon;
                user.adres=adres;
                user.sifre=sifre;

            }
            stmt.close();
            conn.close();


        } catch (Exception e) {
           e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        KayitFormu kayitForm=new KayitFormu(null);
        User user=kayitForm.user;
        if (user !=null){
            System.out.println("Kayıt Başarılı = " + user.adi);
        }
        else {
            System.out.println("Kayıt İptal Edildi = ");
        }
    }
}
