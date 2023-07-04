@echo off

set JWT_AUDIENCE="http://0.0.0.0:8080/hello"
set JWT_ISSUER="http://localhost/"
set JWT_SECRET="secret"
set JWT_REALM="Access"

gradlew.bat run
