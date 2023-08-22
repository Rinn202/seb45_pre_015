# 기반 이미지
FROM openjdk:11

# COPY를 위한 경로 변수 설정
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar

# 빌드된 jar파일을 도커 컨테이너의 app.jar로 복사
COPY ${JAR_FILE} app.jar

# jar 파일 실행
ENTRYPOINT ["java","-jar","/app.jar"]