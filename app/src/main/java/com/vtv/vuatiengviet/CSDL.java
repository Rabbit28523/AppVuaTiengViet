package com.vtv.vuatiengviet;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSDL {
    DataBase db;
    public CSDL(Context applicationContext) {
        db = new DataBase(applicationContext, "VTV.sql", null, 1);
    }
    public void recreateDatabase() {
        db.deleteAllTables();
        TaoCSDL();
        insertNewAvt();
        insertNewKhung();
        TaoNhanVat("Khach");
    }
    public void TaoNhanVat(String name){
        db.QueryData("CREATE TABLE IF NOT EXISTS ThongTinNguoiChoi (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT, " +
                "ruby INTEGER," +
                "level INTEGER, " +
                "avt_ID INTEGER," +
                "khung_id INTEGER," +
                "damua_khung TEXT," +
                "damua_avt TEXT)"); // Close the parenthesis here
        db.QueryData("INSERT INTO ThongTinNguoiChoi (name, ruby, level, avt_ID, khung_id, damua_khung, damua_avt) VALUES ('" + name + "', 24, 0, 1, 1, '1', '1')");
    }
    private void updatePlayerInfoOnFirebase() {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        //Khởi tạo đối tượng FirebaseDatabase để thực hiện lưu trữ thông tin người dùng
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            ThongTinNguoiChoi thongTinNguoiChoi = HienThongTinNhanVat2();
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", user.getUid());
            map.put("name", thongTinNguoiChoi.getName());
            map.put("ruby", thongTinNguoiChoi.getRuby());
            map.put("level", thongTinNguoiChoi.getLevel());
            map.put("avt_id", thongTinNguoiChoi.getAvt_id());
            map.put("khung_id", thongTinNguoiChoi.getKhung_id());
            map.put("damua_khung", thongTinNguoiChoi.getDamua_khung());
            map.put("damua_avt", thongTinNguoiChoi.getDamua_avt());

            // Cập nhật thông tin người chơi lên Firebase
            firebaseDatabase.getReference().child("users").child(user.getUid()).updateChildren(map);
        }
    }
    public ThongTinNguoiChoi HienThongTinNhanVat2(){
        Cursor dataCV=db.GetData("SELECT * FROM ThongTinNguoiChoi ");
        ThongTinNguoiChoi thongTinNguoiChoi=null;
        if (dataCV != null && dataCV.moveToFirst()) {
            int id = dataCV.getInt(0);
            String name=dataCV.getString(1);
            int ruby = dataCV.getInt(2);
            int level = dataCV.getInt(3);
            int avt_id = dataCV.getInt(4);
            int khung_id = dataCV.getInt(5);
            String khung_damua=dataCV.getString(6);
            String avt_damua=dataCV.getString(7);
            thongTinNguoiChoi= new ThongTinNguoiChoi(name,ruby,level,avt_id,khung_id,khung_damua,avt_damua);
        }
        else {
            int id = -1;
            String name="name";
            int ruby = 0;
            int level = 0;
            int avt_id = -1;
            int khung_id = -1;
            String khung_damua="1";
            String avt_damua="1";
            thongTinNguoiChoi= new ThongTinNguoiChoi(name,ruby,level,avt_id,khung_id,khung_damua,avt_damua);

        }
        return thongTinNguoiChoi;
    }
    public void getPlayerInfoFromFirebase() {  //
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference userRef = firebaseDatabase.getReference().child("users").child(user.getUid());

            // Lấy thông tin người chơi từ Firebase
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // Kiểm tra xem dataSnapshot có tồn tại không
                    if (dataSnapshot.exists()) {
                        ThongTinNguoiChoi thongTinNguoiChoi = dataSnapshot.getValue(ThongTinNguoiChoi.class);
                        if (thongTinNguoiChoi != null) {
                            LoginTroLai(thongTinNguoiChoi,HienThongTinNhanVat2());

                        }
                    } else {
                        System.out.println("User does not exist.");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });
        } else {
            System.out.println("User not logged in.");
        }
    }
    public void UpdateSanPhamLai(String table, int id) {
        // Append id to damua_khung or damua_avt using string concatenation in SQLite

        // Update the 'tinhtrang' field in the specified table
        db.QueryData("UPDATE " + table + " SET tinhtrang = 1 WHERE id = " + id);
    }

    public void LoginTroLai(ThongTinNguoiChoi thongTinNguoiChoi, ThongTinNguoiChoi old){
            SuaThongTinNhanVat(thongTinNguoiChoi.getName(),thongTinNguoiChoi.getAvt_id(),thongTinNguoiChoi.getKhung_id());
            UpdateRuby(thongTinNguoiChoi.getRuby()-old.getRuby());
            System.out.println("level: "+thongTinNguoiChoi.getLevel());
            if(thongTinNguoiChoi.getLevel()>0){
                for(int i=1;i<=thongTinNguoiChoi.getLevel();i++){
                    Update(i);
                }
            }
            UpdateThongTin(thongTinNguoiChoi.getLevel(),0);
            String[] listAvt = thongTinNguoiChoi.getDamua_avt().split(",");
            String[] listKhung = thongTinNguoiChoi.getDamua_khung().split(",");
            for (int i = 0; i < listAvt.length; i++) {
                UpdateSanPham("avt",Integer.parseInt(listAvt[i]));
            }
            for (int i = 0; i < listKhung.length; i++) {
                UpdateSanPham("khung",Integer.parseInt(listKhung[i]));
            }
        }
    public void ChoiLai(Context context){
        db.QueryData("DROP TABLE IF EXISTS CauHoi" );
        TaoCSDL();
        Toast.makeText(context, "Bạn đã chọn chơi lại từ đầu", Toast.LENGTH_SHORT).show();
    }
    public void TaoCSDL() {
//        db.QueryData("DROP TABLE IF EXISTS Ruby" );
        Cursor cursor1 = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='Ruby'");
        if (cursor1 != null && cursor1.getCount() > 0) {
        } else {
            db.QueryData("CREATE TABLE IF NOT EXISTS Ruby (id INTEGER PRIMARY KEY AUTOINCREMENT,SoLuong Integer default 24)");
            db.QueryData("INSERT INTO Ruby  VALUES (null,2500)");
        }


        Cursor cursor = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='CauHoi'");
        if (cursor != null && cursor.getCount() > 0) {
        } else {
            db.QueryData("CREATE TABLE IF NOT EXISTS CauHoi (id INTEGER PRIMARY KEY AUTOINCREMENT, Tu TEXT, DapAn NVARCHAR(100), TinhTrang INTEGER DEFAULT 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'C/h/ị/h/c/t/ủ', 'Chủ tịch', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'c/L/ồ/ọ/n', 'Lọ cồn', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'n/ệ/n/ả/h/Đ/i', 'Điện ảnh', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'c/C/a/o/u/n', 'Con cua', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'s/ú/c/C/ố', 'Cú sốc', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'b/y/L/a/i', 'Ly bia', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'à/Đ/ô/n/g/n', 'Đàn ông', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'â/T/ẻ/t/r/r/u', 'Trẻ trâu', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'p/H/h/ồ/c/i/ụ', 'Hồi phục', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'T/u/t/à/á/o', 'Táo tàu', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'A/n/n/t/à/o', 'An toàn', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'c/C/ắ/h/n/c/h/ắ', 'Chắc chắn', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'á/K/ả/i/h/g', 'Khá giả', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'T/á/h/ệ/p/r/t/i', 'Triệt phá', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'L/a/a/n/n/c', 'Lan can', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'ắ/g/n/ò/Đ/g/n/l', 'Đắng lòng', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'ẻ/G/ở/q/i/u', 'Giở quẻ', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'c/C/u/ỉ/h/n/h', 'Chỉn chu', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'n/P/t/ụ/h/g/o/h', 'Phong thụ', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'i/P/v/g/ò/n/h', 'Phòng vi', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'i/ả/t/ế/G/i/h/t', 'Giả thiết', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'ả/Đ/c/i/g/ộ', 'Độc giả', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'ì/n/N/t/h/ô/n/g', 'Ngôn tình', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'í/h/ồ/n/i/C/u/m', 'Chín muồi', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'u/n/g/T/ự/t/r/u', 'Tựu trung', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'c/h/c/N/ậ/m/ứ/h', 'Nhậm chức', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'n/C/u/ẩ/đ/á/h/o/n', 'Chuẩn đoán', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'a/m/q/u/T/h/n/a', 'Tham quan', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'á/p/n/S/ậ/h/p', 'Sáp nhập', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'ậ/à/n/G/i/h/g/i/t', 'Giành giật', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'ú/c/t/S/c/h/í', 'Súc tích', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'g/ạ/B/t/ạ/n/m', 'Bạt mạng', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'t/á/C/x/ọ', 'Cọ xát', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'ạ/X/n/l/á/n', 'Xán lạn', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'c/ế/c/ụ/K/t', 'Kết cục', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'u/ề/Q/ả/n/h/y/u/n', 'Quản huyền', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'t/Q/á/c/ê/n/y/u', 'Quyên cát', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'ơ/ì/t/S/h/n', 'Sơ tình', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'ầ/p/ư/m/T/h/ơ/g/n', 'Tầm phương', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'â/m/T/a/n/h/k/h/h', 'Thanh khâm', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'h/u/n/X/â/u/y/ê/n', 'Xuân huyên', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'á/Đ/ư/s/n/ờ/g', 'Đường sá', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'d/D/à/h/m/ụ/n', 'Dành dụm', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'g/X/i/ụ/c/ú/i', 'Xúi giục', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'í/B/i/ả/h/c/g/n', 'Bích giản', 0)");
            db.QueryData("INSERT INTO CauHoi  VALUES (null,'ư/C/u/ử/h/n', 'Cửu như', 0)");
        }
        Cursor cursor2 = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='avt'");
        if (cursor2 == null || cursor2.getCount() <= 0) {
            db.QueryData("CREATE TABLE IF NOT EXISTS avt (id INTEGER PRIMARY KEY AUTOINCREMENT, hinhAnh INTEGER,price INTEGER,tinhtrang INTEGER)");
            db.QueryData("INSERT INTO avt  VALUES (null,'avt1',0,1)");
            db.QueryData("INSERT INTO avt  VALUES (null,'avt2',5,0)");
            db.QueryData("INSERT INTO avt  VALUES (null,'avt3',10,0)");
        }
        Cursor cursor3 = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='khung'");
        if (cursor3 == null || cursor3.getCount() <= 0) {
            db.QueryData("CREATE TABLE IF NOT EXISTS khung (id INTEGER PRIMARY KEY AUTOINCREMENT, hinhAnh INTEGER,price INTEGER,tinhtrang INTEGER)");
            db.QueryData("INSERT INTO khung  VALUES (null,'khung1',0,1)");
            db.QueryData("INSERT INTO khung  VALUES (null,'khung2',5,0)");
        }
    }
    public void taoCSDLround2ChinhTa(){
        Cursor cursor1 = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='LoiChinhTa'");
        if (cursor1 == null || cursor1.getCount() <= 0) {
            db.QueryData("CREATE TABLE IF NOT EXISTS LoiChinhTa (id INTEGER PRIMARY KEY AUTOINCREMENT, Tu TEXT, DapAnDung NVARCHAR(100),DapAnSai NVARCHAR(100))");

            String[] TuList = {"bánh ...ày","bơi ...ải","..ầy ..ật","..éo ngoe","... chu","xuôi ... mát mái","...ung công","...ày trông mai đợi","...ãy nảy","...ấu ...iếm","...ở trò","ma ...ơi","thôi ...ao","bạ.. mạng"};
            String[] dapAnDungList = {"gi","tr","tr","tr","chỉn","chèo","x","r","gi","gi-gi","gi","tr","x","t"};
            String[] dapAnSaiList = {"d","ch","ch","ch","chỉnh","chiều","s","d","d","gi-d","d","ch","s","c"};
            // Kiểm tra xem số lượng hình ảnh và câu trả lời có khớp nhau không
            if (TuList.length != dapAnDungList.length || dapAnDungList.length!= dapAnSaiList.length ||TuList.length!= dapAnSaiList.length) {
                // Nếu không khớp, bạn có thể xử lý tùy thuộc vào yêu cầu cụ thể của ứng dụng, ví dụ: thông báo cho người dùng.
                // Ví dụ: Toast.makeText(context, "Số lượng hình ảnh và câu trả lời không khớp nhau.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Duyệt qua từng phần tử trong mảng hinhAnhList và dapAnList
            for (int i = 0; i < dapAnDungList.length; i++) {
                // Kiểm tra xem dữ liệu đã tồn tại trong cơ sở dữ liệu chưa
                Cursor cursor = db.GetData("SELECT * FROM LoiChinhTa WHERE Tu = '" + TuList[i] + "' AND DapAnDung = '" + dapAnDungList[i] + "' and DapAnSai = '"+dapAnSaiList[i]+"'");
                if (cursor == null || cursor.getCount() <= 0) {
                    // Nếu không tìm thấy dữ liệu tương ứng, thực hiện câu lệnh insert dữ liệu mới vào bảng
                    db.QueryData("INSERT INTO LoiChinhTa (Tu,DapAnDung,DapAnSai ) VALUES ('" + TuList[i] + "', '" + dapAnDungList[i] + "','"+dapAnSaiList[i]+"')");
                } else {

                }
            }
        }
    }
    public List<LoiChinhTa> getLoiCTRound2(int max) {
        List<LoiChinhTa> cauHoiList = new ArrayList<>();
        Cursor dataCV = db.GetData("SELECT * FROM LoiChinhTa ORDER BY RANDOM() LIMIT "+max);

        if (dataCV != null && dataCV.moveToFirst()) {
            do {
                int id = dataCV.getInt(0);
                String tu = dataCV.getString(1);
                String dapAnDung = dataCV.getString(2);
                String dapAnSai = dataCV.getString(3);
                LoiChinhTa cauHoi = new LoiChinhTa(id, tu,dapAnDung,dapAnSai);
                cauHoiList.add(cauHoi);
            } while (dataCV.moveToNext());
        } else {
            for (int i = 0; i < 10; i++) {
                int id = -1;
                String hinhAnh = "hinh123";
                String dapAn = "dapan";
                LoiChinhTa cauHoi = new LoiChinhTa(id, "tu","dapAnDung","dapAnSai");
                cauHoiList.add(cauHoi);
            }
        }

        if (dataCV != null) {
            dataCV.close();
        }

        return cauHoiList;
    }
    public List<CaDao> getCauHoiRound2(int max) {
        List<CaDao> cauHoiList = new ArrayList<>();
        Cursor dataCV = db.GetData("SELECT * FROM CaDaoTucNgu ORDER BY RANDOM() LIMIT "+max);

        if (dataCV != null && dataCV.moveToFirst()) {
            do {
                int id = dataCV.getInt(0);
                String hinhAnh = dataCV.getString(1);
                String dapAn = dataCV.getString(2);
                CaDao cauHoi = new CaDao(id, hinhAnh, dapAn);
                cauHoiList.add(cauHoi);
            } while (dataCV.moveToNext());
        } else {
            for (int i = 0; i < 10; i++) {
                int id = -1;
                String hinhAnh = "hinh123";
                String dapAn = "dapan";
                CaDao cauHoi = new CaDao(id, hinhAnh, dapAn);
                cauHoiList.add(cauHoi);
            }
        }

        if (dataCV != null) {
            dataCV.close();
        }

        return cauHoiList;
    }
    public void taoCSDLround2CaDao(){
        Cursor cursor1 = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='CaDaoTucNgu'");
        if (cursor1 == null || cursor1.getCount() <= 0) {
            db.QueryData("CREATE TABLE IF NOT EXISTS CaDaoTucNgu (id INTEGER PRIMARY KEY AUTOINCREMENT, HinhAnh TEXT, DapAn NVARCHAR(100))");

            String[] hinhAnhList = {"an_khong_noi_co","anh_em_nhu_the_chan_tay","ba_mat_mot_loi","binh_chan_nhu_vai",
                    "bit_mat_bat_de","boi_beo_ra_bo","but_sa_ga_chet","cai_kho_lo_cai_khon","cam_can_nay_muc",
                    "cho_treo_meo_day","coc_mo_co_xoi","com_ao_gao_tien","con_sau_lam_rau_noi_canh",
                    "du_long_du_canh","dung_nui_nay_trong_nui_no","gai_muoi_bay_be_gay_sung_trau",
                    "hai_ban_tay_moi_vo_thanh_tieng","hoa_thom_danh_ca_cum","ke_cap_gap_ba_gia",
                    "lay_trung_choi_da","meo_ma_ga_dong","mot_nu_cuoi_bang_muoi_thang_thuoc_bo",
                    "mot_tien_ga_ba_tien_thoc","nhay_bao_bo","nhay_lo_co","nuoi_ong_tay_ao_nuoi_cao_trong_nha",
                    "phu_quy_sinh_le_nghia","ruou_vao_loi_ra","rut_day_dong_rung","thoc_gay_banh_xe",
                    "thung_rong_keu_to","tien_tram_hau_tau","to_gan_lon_mat","tranh_vo_dua_gap_vo_dua",
                    "troi_nong_chong_khat_troi_mat_chong_doi","trong_danh_xuoi_ken_thoi_nguoc",
                    "vat_dau_ca_va_dau_tom","ve_duong_cho_huou_chay",};

            // Thêm các câu trả lời khác tương tự ở đây
            String[] dapAnList = {"ăn không nói có","anh em như thể chân tay","ba mặt một lời","bình chân như vại",
                    "bịt mắt bắt dê","bới bèo ra bọ","bút sa gà chết","cái khó ló cái khôn","cầm cân nảy mực",
                    "chó treo mèo đậy","cốc mò cò xơi","cơm áo gạo tiền","con sâu làm rầu nồi canh",
                    "đủ lông đủ cánh","đứng núi này trông núi nọ","gái mười bảy bẻ gãy sừng trâu",
                    "hai bàn tay mới vỗ thành tiếng","hoa thơm đánh cả cụm","kẻ cắp gặp bà già",
                    "lấy trứng chọi đá","mèo mả gà đồng","một nụ cười bằng mười thang thuốc bổ",
                    "một tiền gà ba tiền thóc","nhảy bao bố","nhảy lò cò","nuôi ong tay áo nuôi cáo trong nhà",
                    "phú quý sinh lễ nghĩa","rượu vào lời ra","rút dây động rừng","thọc gậy bánh xe",
                    "thùng rỗng kêu to","tiền trảm hậu tấu","to gan lớn mật","tránh vỏ dưa gặp vỏ dừa",
                    "trời nóng chóng khát trời mát chóng đói","trống đánh xuôi, kèn thổi ngược",
                    "vặt đầu cá vá đầu tôm","vẽ đường cho hươu chạy"};
            // Kiểm tra xem số lượng hình ảnh và câu trả lời có khớp nhau không
            if (hinhAnhList.length != dapAnList.length) {
                // Nếu không khớp, bạn có thể xử lý tùy thuộc vào yêu cầu cụ thể của ứng dụng, ví dụ: thông báo cho người dùng.
                // Ví dụ: Toast.makeText(context, "Số lượng hình ảnh và câu trả lời không khớp nhau.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Duyệt qua từng phần tử trong mảng hinhAnhList và dapAnList
            for (int i = 0; i < hinhAnhList.length; i++) {
                // Kiểm tra xem dữ liệu đã tồn tại trong cơ sở dữ liệu chưa
                Cursor cursor = db.GetData("SELECT * FROM CaDaoTucNgu WHERE HinhAnh = '" + hinhAnhList[i] + "' AND DapAn = '" + dapAnList[i] + "'");
                if (cursor == null || cursor.getCount() <= 0) {
                    // Nếu không tìm thấy dữ liệu tương ứng, thực hiện câu lệnh insert dữ liệu mới vào bảng
                    db.QueryData("INSERT INTO CaDaoTucNgu (HinhAnh, DapAn) VALUES ('" + hinhAnhList[i] + "', '" + dapAnList[i] + "')");
                } else {
                    // Nếu dữ liệu đã tồn tại, bạn có thể thực hiện các hành động phù hợp, ví dụ: thông báo cho người dùng.
                    // Ví dụ: Toast.makeText(context, "Dữ liệu đã tồn tại trong cơ sở dữ liệu.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void insertNewAvt(){
        // Khởi tạo mảng hình ảnh và giá
        String[] hinhAnhList = new String[15];
        int[] priceList = new int[15];

        // Tạo dữ liệu cho mảng hình ảnh và giá
        for (int i = 0; i < 15; i++) {
            hinhAnhList[i] = "avt" + (i + 1); // Tạo tên hình ảnh theo mẫu "avt1", "avt2",...
            priceList[i] = i * 5; // Giá tăng lên 5 sau mỗi lần
        }

        // Kiểm tra xem số lượng hình ảnh và giá có khớp nhau không
        if (hinhAnhList.length != priceList.length) {
            // Xử lý tùy thuộc vào yêu cầu cụ thể của ứng dụng, ví dụ: thông báo cho người dùng.
            return;
        }

        // Duyệt qua từng phần tử trong mảng hinhAnhList và priceList
        for (int i = 0; i < hinhAnhList.length; i++) {
            // Kiểm tra xem dữ liệu đã tồn tại trong cơ sở dữ liệu chưa
            Cursor cursor = db.GetData("SELECT * FROM avt WHERE hinhAnh = '" + hinhAnhList[i] + "' AND price = " + priceList[i]);
            if (cursor == null || cursor.getCount() <= 0) {
                // Nếu không tìm thấy dữ liệu tương ứng, thực hiện câu lệnh insert dữ liệu mới vào bảng
                db.QueryData("INSERT INTO avt (hinhAnh, price, tinhtrang) VALUES ('" + hinhAnhList[i] + "', " + priceList[i] + ", 0)");
            } else {
                // Nếu dữ liệu đã tồn tại, bạn có thể thực hiện các hành động phù hợp, ví dụ: thông báo cho người dùng.
            }
        }
    }

    public void insertNewKhung(){
        // Dữ liệu bạn muốn thêm vào bảng khung
        // Thêm các hình ảnh khung khác tương tự ở đây
        String[] hinhAnhList = new String[15];
        int[] priceList = new int[15];

        // Khởi tạo dữ liệu cho mảng hình ảnh và giá cả
        for (int i = 0; i < 15; i++) {
            hinhAnhList[i] = "khung" + (i + 1);
            priceList[i] = i * 5;
        }

        // Duyệt qua từng phần tử trong mảng hinhAnhList và priceList
        for (int i = 0; i < hinhAnhList.length; i++) {
            // Kiểm tra xem dữ liệu đã tồn tại trong cơ sở dữ liệu chưa
            Cursor cursor = db.GetData("SELECT * FROM khung WHERE hinhAnh = '" + hinhAnhList[i] + "' AND price = " + priceList[i]);
            if (cursor == null || cursor.getCount() <= 0) {
                // Nếu không tìm thấy dữ liệu tương ứng, thực hiện câu lệnh insert dữ liệu mới vào bảng
                db.QueryData("INSERT INTO khung (hinhAnh, price, tinhtrang) VALUES ('" + hinhAnhList[i] + "', " + priceList[i] + ", 0)");
            } else {
                // Nếu dữ liệu đã tồn tại, bạn có thể thực hiện các hành động phù hợp, ví dụ: thông báo cho người dùng.
            }
        }
    }
    public CauHoi HienCSDL(Context context){
        Cursor dataCV=db.GetData("SELECT * FROM CauHoi WHERE TinhTrang = 0 LIMIT 1");
        CauHoi cauHoi;
        if (dataCV != null && dataCV.moveToFirst()) {
            int id = dataCV.getInt(0);
            String tu = dataCV.getString(1);
            String dapAn = dataCV.getString(2);
            int tinhTrang = dataCV.getInt(3);
            cauHoi= new CauHoi(id, tu, dapAn, tinhTrang);
//            Toast.makeText(context, "id: " + dataCV.getInt(0) + "dapan: " + dataCV.getString(2), Toast.LENGTH_SHORT).show();
        }
        else {
            cauHoi= new CauHoi(-1,"aa","bb",0);
        }
        return cauHoi;
    }
    public int HienRuby(Context context){
        Cursor dataCV=db.GetData("SELECT * FROM Ruby  LIMIT 1");
        int soluong=0;
        if (dataCV != null && dataCV.moveToFirst()) {
            soluong = dataCV.getInt(1);
//            Toast.makeText(context, "id: " + dataCV.getInt(1) , Toast.LENGTH_SHORT).show();

        }
        return soluong;
    }
    //update câu hỏi
    public void Update(int id){
        db.QueryData("update CauHoi set TinhTrang=1 where id="+id);
    }
    public void UpdateRuby( int slg){

//        db.QueryData("update Rubys set SoLuong= SoLuong+"+slg);
        db.QueryData("update ThongTinNguoiChoi set ruby=ruby +"+slg);
        updatePlayerInfoOnFirebase();
    }
    public void UpdateThongTin( int level,int levelMax){
        if(level>levelMax){
            db.QueryData("update ThongTinNguoiChoi set level="+level);
            updatePlayerInfoOnFirebase();
        }

    }
    //update mua sản phẩm
    public void UpdateSanPham(String table, int id) {
        // Append id to damua_khung or damua_avt using string concatenation in SQLite
        if(id != 1) {
            if (!table.equalsIgnoreCase("khung")) {
                db.QueryData("UPDATE ThongTinNguoiChoi SET damua_khung = damua_khung || '," + id + "'");
            } else {
                db.QueryData("UPDATE ThongTinNguoiChoi SET damua_avt = damua_avt || '," + id + "'");
            }
        }

        // Update the 'tinhtrang' field in the specified table
        db.QueryData("UPDATE " + table + " SET tinhtrang = 1 WHERE id = " + id);
        updatePlayerInfoOnFirebase();
    }
    public  boolean KiemTraNhanVat(Context context){
        Cursor cursor1 = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='ThongTinNguoiChoi'");
        if (cursor1 == null || cursor1.getCount() <= 0) {
//            db.QueryData("CREATE TABLE IF NOT EXISTS Rubys (id INTEGER PRIMARY KEY AUTOINCREMENT,SoLuong Integer)");
//            db.QueryData("INSERT INTO Rubys  VALUES (null,9999)");
            return true;
        }
        return false;
    }

    public void SuaThongTinNhanVat(String name, int avt_ID,int khung_id){
        db.QueryData("Update ThongTinNguoiChoi set name='"+name+"', avt_ID="+avt_ID+", khung_id="+khung_id);
        updatePlayerInfoOnFirebase();
    }
    public  CauHoi getCauHoi(int id){
        Cursor dataCV=db.GetData("SELECT * FROM CauHoi WHERE id =" +id+ " LIMIT 1");
        CauHoi cauHoi;
        if (dataCV != null && dataCV.moveToFirst()) {
            int id1 = dataCV.getInt(0);
            String tu = dataCV.getString(1);
            String dapAn = dataCV.getString(2);
            int tinhTrang = dataCV.getInt(3);
            cauHoi= new CauHoi(id1, tu, dapAn, tinhTrang);
//            Toast.makeText(context, "id: " + dataCV.getInt(0) + "dapan: " + dataCV.getString(2), Toast.LENGTH_SHORT).show();
        }
        else {
            cauHoi= new CauHoi(-1,"aa","bb",0);
        }
        return cauHoi;
    }
    public List<CauHoi> getCauHoiRound1(int max) {
        List<CauHoi> cauHoiList = new ArrayList<>();
        Cursor dataCV = db.GetData("SELECT * FROM CauHoi  ORDER BY RANDOM() LIMIT "+max);

        if (dataCV != null && dataCV.moveToFirst()) {
            do {
                int id = dataCV.getInt(0);
                String hinhAnh = dataCV.getString(1);
                String dapAn = dataCV.getString(2);
                int tinhTrang = dataCV.getInt(3);
                CauHoi cauHoi = new CauHoi(id, hinhAnh, dapAn, tinhTrang);
                cauHoiList.add(cauHoi);
            } while (dataCV.moveToNext());
        } else {
            for (int i = 0; i < 10; i++) {
                int id = -1;
                String hinhAnh = "hinh123";
                String dapAn = "dapan";
                int tinhTrang = 0;
                CauHoi cauHoi = new CauHoi(id, hinhAnh, dapAn, tinhTrang);
                cauHoiList.add(cauHoi);
            }
        }

        if (dataCV != null) {
            dataCV.close();
        }

        return cauHoiList;
    }
    public ThongTinNguoiChoi HienThongTinNhanVat(){
        Cursor dataCV=db.GetData("SELECT * FROM ThongTinNguoiChoi ");
        ThongTinNguoiChoi thongTinNguoiChoi=null;
        if (dataCV != null && dataCV.moveToFirst()) {
            int id = dataCV.getInt(0);
            String name=dataCV.getString(1);
            int ruby = dataCV.getInt(2);
            int level = dataCV.getInt(3);
            int avt_id = dataCV.getInt(4);
            int khung_id = dataCV.getInt(5);
            thongTinNguoiChoi= new ThongTinNguoiChoi(name,ruby,level,avt_id,khung_id);
        }
        else {
            int id = -1;
            String name="name";
            int ruby = 0;
            int level = 0;
            int avt_id = -1;
            int khung_id = -1;
            thongTinNguoiChoi= new ThongTinNguoiChoi(name,ruby,level,avt_id,khung_id);
        }
        return thongTinNguoiChoi;
    }
    public ArrayList<SanPham> HienDS_AVT(){
        ArrayList<SanPham> danhSachSanPham = new ArrayList<>();
        Cursor dataCV = db.GetData("SELECT * FROM avt");

        while (dataCV.moveToNext()) {
            int id = dataCV.getInt(0);
            String hinhanh = dataCV.getString(1);
            int price=dataCV.getInt(2);
            int tinhtrang = dataCV.getInt(3);
            // Tạo một đối tượng SanPham từ dữ liệu và thêm vào danh sách
            SanPham sanPham = new SanPham(id, hinhanh,price,tinhtrang); // Cần sửa constructor của SanPham để phù hợp
            danhSachSanPham.add(sanPham);
        }

        // Đóng con trỏ sau khi sử dụng để tránh rò rỉ bộ nhớ
        dataCV.close();

        // Trả về danh sách các sản phẩm
        return danhSachSanPham;
    }

    public ArrayList<SanPham> HienDS_Khung(){
        ArrayList<SanPham> danhSachSanPham = new ArrayList<>();
        Cursor dataCV = db.GetData("SELECT * FROM khung");

        while (dataCV.moveToNext()) {
            int id = dataCV.getInt(0);
            String hinhanh = dataCV.getString(1);
            int price=dataCV.getInt(2);
            int tinhtrang = dataCV.getInt(3);
            // Tạo một đối tượng SanPham từ dữ liệu và thêm vào danh sách
            SanPham sanPham = new SanPham(id, hinhanh,price,tinhtrang); // Cần sửa constructor của SanPham để phù hợp
            danhSachSanPham.add(sanPham);
        }

        // Đóng con trỏ sau khi sử dụng để tránh rò rỉ bộ nhớ
        dataCV.close();

        // Trả về danh sách các sản phẩm
        return danhSachSanPham;
    }
}
class DataBase extends SQLiteOpenHelper {
    public DataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //truy vấn không trả kết quả:insert, update,delete
    public void QueryData (String sql){
        SQLiteDatabase db= getWritableDatabase();
        db.execSQL(sql);
    }
    //truy vấn có trả kết quả
    public Cursor GetData(String sql){
        SQLiteDatabase db= getReadableDatabase();
        return db.rawQuery(sql,null);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    // Phương thức xóa tất cả các bảng
    public void deleteAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Truy vấn danh sách các bảng
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%';", null);
        if (cursor.moveToFirst()) {
            do {
                // Lấy tên bảng
                String tableName = cursor.getString(0);
                // Xóa bảng
                db.execSQL("DROP TABLE IF EXISTS " + tableName);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}