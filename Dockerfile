# Stage 1: Build
# Dùng image có sẵn Maven + JDK 21 để build code
FROM maven:3.9-eclipse-temurin-21 AS build

# Tạo và di chuyển vào thư mục /app bên trong container
WORKDIR /app

# Copy file pom.xml vào trước (để cache dependency, build nhanh hơn lần sau)
COPY pom.xml .

# Copy toàn bộ source code vào
COPY src ./src

# Build JAR, bỏ qua test
RUN mvn clean package -DskipTests

# ─────────────────────────────────────────
# Stage 2: Run
# Dùng image nhẹ hơn, chỉ có JRE (không cần Maven nữa)
FROM eclipse-temurin:21-jre-alpine

# Tạo và di chuyển vào thư mục /app
WORKDIR /app

# Lấy file JAR từ stage 1 đặt tên lại là app.jar
COPY --from=build /app/target/*.jar app.jar

# Khai báo port app sẽ chạy
EXPOSE 8080

# Lệnh chạy khi container start
ENTRYPOINT ["java", "-jar", "app.jar"]