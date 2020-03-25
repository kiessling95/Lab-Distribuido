@ECHO OFF

ECHO Borrando bytecodes...
DEL /S /Q *.class 2>nul
ECHO Listo!

ECHO Compilando...
javac -d ../out cliente/*.java
javac -d ../out server/*.java
ECHO Listo!
ECHO *****************
ECHO Entrar a carpeta out
ECHO Ejecutar con java cliente.MainCliente
ECHO Sino con java server.ServidorCentral