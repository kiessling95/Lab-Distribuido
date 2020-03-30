@ECHO OFF

ECHO Borrando bytecodes...
DEL /S /Q *.class 2>nul
ECHO Listo!

ECHO Compilando...
javac -d ../out cliente/*.java
javac -d ../out server/*.java
ECHO Listo!
ECHO *****************
ECHO Entrar a carpeta out y ejecutar:
ECHO ---
ECHO java server.ServidorHoroscopo
ECHO java server.ServidorPronostico
ECHO java server.ServidorCentral
ECHO java cliente.MainCliente
ECHO ---
ECHO Tantos clientes como sean necesarios

