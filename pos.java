package JAVA.Convinientstorepos;

import javafx.application.*;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.*;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import oracle.security.o3logon.WorkBench;

//database
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class pos extends Application{
      class connectDB {
        public Connection getConnection(){
            try{
                String urlSQL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1"; 
                String nameSQL  ="POS"; 
                String SQLpass = "123456"; 

                System.out.println("connected to SQL successfully");
                return  DriverManager.getConnection(urlSQL, nameSQL, SQLpass); 
                
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
                return null; //phai them 
            }
        }
    }
    Stage stage; 
    WebEngine engine; 

    public class Bridge {
        //chuyen trang 
        public void choose(String index){
          Platform.runLater(() -> { //Đưa tác vụ load web vào luồng chính
            switch(index) {
                case "0":{
                    System.out.println("Đang đăng xuất!");
                    loadloginpage();
                    break;
                }
                case "1": {
                    System.out.println("Đang chuyển sang Trang Chủ");
                    loadhomepage();
                    break; 
                }
                case "2": {
                    System.out.println("Đang chuyển sang Kết Ca");
                    loadketcapage();
                    break; 
                }
                case "3": {
                    System.out.println(" Đang chuyển sang Chức năng Tiện ích ");
                    loadtienichpage(); 
                    break;
                }
                case "4": {
                    System.out.println("Đang chuyển sang Đăng xuất");
                    loadlogoutpage();
                    break;
                }
            }
        });
        }
        //ktra dang nhap 
        public String checkedLogin(String id, String pass){

            int checkid = 0; 

            try{
               checkid = Integer.parseInt(id);
            }
            catch(NumberFormatException ex){
                System.out.println("Loi: ID phai la so!");
                return "ID format is incorrect!"; 
            }
            
            //create 1 cai database 
            connectDB db = new connectDB(); 
            String sqlcheckaccount = "select * from nhanvien where id = ?"; 
            Connection connection = null; 
            PreparedStatement pstmt = null; 
            ResultSet rs = null; 
            
            String passchecked = null; 

            //kiem tra connected 
            try{
                connection = db.getConnection(); 

                if(connection == null){
                    System.out.println("loi khong ket noi duoc");
                    return "database error!";
                }
                pstmt = connection.prepareStatement(sqlcheckaccount); 

                //truyen id 
                pstmt.setInt(1, checkid);

                rs = pstmt.executeQuery(); //thực thi 

                if(rs.next()){
                    //lay chuoi o cot mk gan vao 
                    passchecked = rs.getString("Password"); 
                }

                rs.close();
                pstmt.close();
                connection.close();
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
            //ktra bang nhau 
            if(passchecked != null && passchecked.equals(pass)){
                System.out.println("đăng nhập thành công bởi nhân viên có ID: " + id); 
                  // chuyen trang 
                Platform.runLater(()-> loadhomepage());
                return "Logined successful";  
            }
            else{
                return "ID or Password can be incorrect!"; 
            }
        }
    }

    @Override
    public void start(Stage stage){
        this.stage = stage; 
        WebView webView = new WebView(); 
        engine = webView.getEngine(); 

        Bridge bridge = new Bridge(); 

        engine.getLoadWorker().stateProperty().addListener((obs, oldstate, newstate)->{
            if(newstate == Worker.State.SUCCEEDED){
                JSObject  window = (JSObject) engine.executeScript("window"); 
                window.setMember("app", bridge);
            }
        });
        loadloginpage();

       // stage.setTitle("Conveniencepos");
        stage.setScene(new Scene(webView, 1480, 1024)); 
        stage.show();
    }
    //load cac trang 
    public void loadloginpage(){
        var resource = getClass().getResource("login.html"); 
        if (resource == null) {
            System.out.println("LỖI: Không tìm thấy file login.html trong package!");
        } 
        else {
            System.out.println("Đang nạp file từ: " + resource.toExternalForm());
            engine.load(resource.toExternalForm());
        }
    }
    public void loadhomepage(){
        var resource = getClass().getResource("home.html"); 
        if (resource == null) {
            System.out.println("LỖI: Không tìm thấy file home.html trong package!");
        } 
        else {
            System.out.println("Đang nạp file từ: " + resource.toExternalForm());
            engine.load(resource.toExternalForm());
        }
    }
     public void loadketcapage(){
        var resource = getClass().getResource("ketca.html"); 
        if (resource == null) {
            System.out.println("LỖI: Không tìm thấy file ketca.html trong package!");
        } 
        else {
            System.out.println("Đang nạp file từ: " + resource.toExternalForm());
            engine.load(resource.toExternalForm());
        }
    }
     public void loadlogoutpage(){
        var resource = getClass().getResource("logout.html"); 
        if (resource == null) {
            System.out.println("LỖI: Không tìm thấy file logout.html trong package!");
        } 
        else {
            System.out.println("Đang nạp file từ: " + resource.toExternalForm());
            engine.load(resource.toExternalForm());
        }
    }

     public void loadtienichpage(){
        var resource = getClass().getResource("tienich.html"); 
        if (resource == null) {
            System.out.println("LỖI: Không tìm thấy file tienich.html trong package!");
        } 
        else {
            System.out.println("Đang nạp file từ: " + resource.toExternalForm());
            engine.load(resource.toExternalForm());
        }
    }

    //xu ly du lieu 
    public static void main(String[] args) {
        launch(args);
    }
}
