const express = require("express"); // Import del modulo express.js
const app = express();

const port = process.argv[2];
const server = app.listen(port, () => console.log(`Server is running on port ${server.address().port}`));
const io = require("socket.io")(server); // Import del modulo socket.io

const nickname = new Map(); // ID : Nickname -> Valor : SocketID
const clientes = new Map(); // ID : SocketID -> Valor : Nickname 

// Se establece el path de los archivos estaticos (css, imagenes, etc.)
app.use(express.static(__dirname +'/'));

// Cuando se accede a la pagina envia el html
app.get("/", (req, res) => res.sendFile(__dirname + "/index.html"));

// Cuando hay una conexion...
io.on("connection", (socket) => {

  socket.on("name", (name) => {
    //Almaceno información ( socket.id, nickname)
    nickname.set(name, socket.id);
    clientes.set(socket.id, name);
    console.log(`Se conecto muevo cliente nickname -> ${name}`);
  });

  socket.on("destinatarios", () => {
    //Envio al emisor los clientes cargador
    clientes.forEach( (aNickname, aSocketId) => {
      //console.log(clientes.get(valor));
      socket.broadcast.emit("destC", aNickname);
      //socket.emit("destC", aNickname);
    });
  });

  socket.on("avisarATodos", () => socket.broadcast.emit("alguienEscribe"));

  // Al recibir un mensaje...
  socket.on("messageTo", (msg) => {
    
    // Muestra por consolada el mensaje y destinatario
    console.log(msg);
    const destinatario = nickname.get(msg.to);
    const emisor = clientes.get(socket.id);

    // Enviar mensaje a todos los nombres menos al emisor en caso de no tener un destinatario 
    if (destinatario) {
      socket.to(destinatario).emit('message', { "message": msg.message, "from": emisor });
    } else {
      // Enviar a todos los clientes excepto al emisor
      socket.broadcast.emit('message', { "message": msg.message, "from": emisor });
    }
  });

  socket.on("disconnect", () => {
    // Dar de baja al cerrar la sesion
    const userName = clientes.get(socket.id);
    socket.broadcast.emit("destDesc");
    socket.emit("destDesc");
    console.log(`${userName} cerro la sesion`);
    nickname.delete(clientes.get(socket.id));
    clientes.delete(socket.id);
  });
});
