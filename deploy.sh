#!/bin/bash

# --- 1. 변수 설정 ---
JAR_PATH=$(ls /home/ubuntu/*[^plain].jar)
JAR_NAME=$(basename $JAR_PATH)

echo ">>> 실행할 파일: $JAR_NAME"