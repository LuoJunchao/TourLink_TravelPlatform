# 使用 OpenJDK 17 作为基础镜像
FROM eclipse-temurin:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制构建好的 jar 包到容器中
COPY target/attraction-service-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口 9080
EXPOSE 9080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]