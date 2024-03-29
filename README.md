Spring-Boot+Kotlin+jooq+PostgreSQL project 
実行方法
IDEのSDKはJava17

Dockerを起動

docker-compose up -d

JOOQコードの生成

./gradlew generateJooq
アプリケーションの実行

./gradlew bootRun

Test実行

./gradlew test
