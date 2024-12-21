# 使用官方的 Java 运行时作为父镜像
FROM eclipse-temurin:21-jre-alpine

# 设置工作目录
WORKDIR /app

# 将当前目录的内容复制到容器的工作目录中
COPY qihuis-world-server-*.jar app.jar

# 声明环境变量
ENV JAVA_OPTS="-Dwish_tree_host.mysql=mysql -Dwish_tree_host.elasticsearch=elasticsearch"

# 设置容器启动命令
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]