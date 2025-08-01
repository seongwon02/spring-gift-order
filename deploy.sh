#!/bin/bash

# --- 1. 변수 설정 ---
JAR_PATH=$(ls /home/ubuntu/*[^plain].jar)
JAR_NAME=$(basename $JAR_PATH)

echo ">>> 실행할 파일: $JAR_NAME"

# --- 2. 기존 애플리케이션 종료 ---
# 실행 중인 애플리케이션의 프로세스 ID(PID)를 찾습니다.
CURRENT_PID=$(pgrep -f $JAR_NAME)

# 프로세스 ID가 존재하면(실행 중이면) 프로세스를 종료합니다.
if [ -z "$CURRENT_PID" ]; then
  echo ">>> 현재 실행 중인 애플리케이션이 없습니다."
else
  echo ">>> 기존 애플리케이션을 종료합니다. (PID: $CURRENT_PID)"
  kill -15 $CURRENT_PID
  sleep 5
fi

# --- 3. 새 애플리케이션 실행 ---
echo ">>> 새 애플리케이션을 배포합니다: $JAR_PATH"
nohup java -jar $JAR_PATH > ~/deploy.log 2>&1 &