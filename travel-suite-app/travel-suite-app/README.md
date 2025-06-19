# Travel Suite Web Services

## Mô tả dự án
Travel Suite là một ứng dụng web services được phát triển bằng Java 8, Spring Framework, Hibernate và MySQL. Ứng dụng cho phép:

- **Guides**: Thêm địa điểm du lịch, upload hình ảnh và quản lý thông tin
- **Travelers**: Tìm kiếm địa điểm, đánh giá và bình luận về các địa điểm và hình ảnh
- **Unregistered users**: Đăng ký tài khoản để sử dụng các tính năng

## Công nghệ sử dụng
- Java 8 (JDK 1.8)
- Spring Framework 5.3.21
- Spring MVC
- Hibernate 5.6.9
- MySQL 8.0
- Maven 3.6.3
- Apache Tomcat

## Cấu trúc dự án
```
src/
├── main/
│   ├── java/com/travelsuite/
│   │   ├── config/          # Cấu hình Spring và Hibernate
│   │   ├── controller/      # REST Controllers
│   │   ├── dao/            # Data Access Objects
│   │   ├── model/          # Entity Models
│   │   └── service/        # Business Logic Services
│   ├── resources/
│   │   └── application.properties
│   └── webapp/
│       ├── WEB-INF/
│       └── index.html      # API Documentation
```

## User Stories được triển khai

### 1. Authentication & Authorization
- **Login**: POST `/api/auth/login`
- **Logout**: POST `/api/auth/logout`
- **Register**: POST `/api/auth/register`
- **Get Current User**: GET `/api/auth/current`

### 2. Place Management (Guides)
- **Create Place**: POST `/api/places`
- **Update Place**: PUT `/api/places/{id}`
- **Delete Place**: DELETE `/api/places/{id}`
- **Get My Places**: GET `/api/places/my`

### 3. Image Management (Guides)
- **Upload Image**: POST `/api/images/upload/{placeId}`
- **Update Image**: PUT `/api/images/{id}`
- **Delete Image**: DELETE `/api/images/{id}`

### 4. Search Functionality (Travelers)
- **Search Places**: GET `/api/places/search?keyword={keyword}`
- **Search Images**: GET `/api/images/search?keyword={keyword}`
- **Get All Places**: GET `/api/places`
- **Get All Images**: GET `/api/images`

### 5. Rating & Comment System (Travelers)
- **Rate Place**: POST `/api/ratings/place/{placeId}`
- **Rate Image**: POST `/api/ratings/image/{imageId}`
- **Comment on Place**: POST `/api/comments/place/{placeId}`
- **Comment on Image**: POST `/api/comments/image/{imageId}`

## Cài đặt và chạy ứng dụng

### 1. Yêu cầu hệ thống
- JDK 1.8
- Maven 3.6+
- MySQL 8.0+
- Apache Tomcat 9.0+

### 2. Cấu hình Database
```sql
CREATE DATABASE travel_suite;
CREATE USER 'travel_user'@'localhost' IDENTIFIED BY 'travel_pass';
GRANT ALL PRIVILEGES ON travel_suite.* TO 'travel_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Build và Deploy
```bash
# Build project
mvn clean package

# Deploy WAR file to Tomcat
cp target/travel-suite-app.war $TOMCAT_HOME/webapps/
```

### 4. Truy cập ứng dụng
- API Base URL: `http://localhost:8080/travel-suite-app/api`
- Documentation: `http://localhost:8080/travel-suite-app/`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Đăng ký user mới
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/logout` - Đăng xuất
- `GET /api/auth/current` - Lấy thông tin user hiện tại

### Places
- `GET /api/places` - Lấy tất cả địa điểm
- `GET /api/places/{id}` - Lấy địa điểm theo ID
- `GET /api/places/search?keyword={keyword}` - Tìm kiếm địa điểm
- `POST /api/places` - Tạo địa điểm mới (Guide only)
- `PUT /api/places/{id}` - Cập nhật địa điểm (Owner only)
- `DELETE /api/places/{id}` - Xóa địa điểm (Owner only)

### Images
- `GET /api/images` - Lấy tất cả hình ảnh
- `GET /api/images/{id}` - Lấy hình ảnh theo ID
- `GET /api/images/place/{placeId}` - Lấy hình ảnh của địa điểm
- `POST /api/images/upload/{placeId}` - Upload hình ảnh (Owner only)
- `PUT /api/images/{id}` - Cập nhật hình ảnh (Owner only)
- `DELETE /api/images/{id}` - Xóa hình ảnh (Owner only)

### Ratings
- `POST /api/ratings/place/{placeId}` - Đánh giá địa điểm
- `POST /api/ratings/image/{imageId}` - Đánh giá hình ảnh
- `GET /api/ratings/place/{placeId}` - Lấy đánh giá của địa điểm
- `GET /api/ratings/image/{imageId}` - Lấy đánh giá của hình ảnh

### Comments
- `POST /api/comments/place/{placeId}` - Bình luận về địa điểm
- `POST /api/comments/image/{imageId}` - Bình luận về hình ảnh
- `GET /api/comments/place/{placeId}` - Lấy bình luận của địa điểm
- `GET /api/comments/image/{imageId}` - Lấy bình luận của hình ảnh
- `PUT /api/comments/{id}` - Cập nhật bình luận (Owner only)
- `DELETE /api/comments/{id}` - Xóa bình luận (Owner only)

## Database Schema

### Users Table
- id (Primary Key)
- username (Unique)
- password
- email (Unique)
- role (GUIDE/TRAVELER)
- created_at

### Places Table
- id (Primary Key)
- name
- description
- location
- user_id (Foreign Key)
- created_at

### Place_Images Table
- id (Primary Key)
- filename
- original_name
- description
- place_id (Foreign Key)
- uploaded_at

### Ratings Table
- id (Primary Key)
- score (1-5)
- user_id (Foreign Key)
- place_id (Foreign Key, nullable)
- image_id (Foreign Key, nullable)
- created_at

### Comments Table
- id (Primary Key)
- content
- user_id (Foreign Key)
- place_id (Foreign Key, nullable)
- image_id (Foreign Key, nullable)
- created_at

## Tính năng bảo mật
- Session-based authentication
- Role-based authorization (GUIDE/TRAVELER)
- Owner-only access for CRUD operations
- Input validation và error handling

## Testing
Ứng dụng đã được test với các scenario:
- User registration và login
- CRUD operations cho places và images
- Search functionality
- Rating và comment system
- Authorization và access control

## Deployment
Ứng dụng có thể được deploy trên:
- Apache Tomcat 9.0+
- Any Java EE compatible application server
- Cloud platforms (AWS, Azure, GCP)

## Liên hệ
Dự án được phát triển cho môn học Web Services với Java.

