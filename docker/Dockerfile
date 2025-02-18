# 1. Ubuntu 20.04 기반 이미지 사용
FROM ubuntu:20.04

# 2. 필수 패키지 설치 후 불필요한 파일 정리
RUN apt update && apt install -y openjdk-21-jdk \
    && apt clean && rm -rf /var/lib/apt/lists/*

# 3. 작업 디렉토리 명확히 설정
WORKDIR /app

# 4. JAR 파일 복사
COPY api-server.jar .

# 5. 실행 명령어 개선 (ENTRYPOINT 사용)
ENTRYPOINT ["java", "-XshowSettings:vm", "-XX:MinRAMPercentage=50", "-XX:MaxRAMPercentage=80", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5000", "-jar", "api-server.jar"]
