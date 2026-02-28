-Manage Requests Backend

-Backend được xây dựng bằng Spring Boot.
-Ứng dụng sử dụng MySQL và xác thực bằng JWT.

-Yêu cầu trước khi chạy

Cài đặt Java 17 trở lên

Cài đặt Maven

Cài đặt MySQL

Cấu hình Database

-Đảm bảo MySQL đang chạy.

Kiểm tra lại username và password cho đúng với máy của bạn.

Database fileupload sẽ tự tạo nếu chưa tồn tại.

Nếu MySQL của bạn có mật khẩu, hãy điền vào:

spring.datasource.password=
-Cấu hình JWT (bắt buộc)

Bạn phải tự nhập giá trị cho:

jwt.secretKey=
jwt.refreshKey=

-Cách tải và chạy bằng file ZIP
Bước 1: Tải file ZIP

Vào trang GitHub của project.

Nhấn nút Code.

Chọn Download ZIP.

Giải nén file vừa tải về.

Bước 2: Mở project

Sau khi giải nén:

Mở thư mục project

Vào thư mục backend (ví dụ: Manage-Requests-Backend)

Mở thư mục đó bằng IntelliJ hoặc VS Code

-Chạy ứng dụng bằng cách chạy file DemoApplication.java

-Đăng nhập bằng tài khoản mẫu có tài khoản và mật khẩu là "adminadmin"
