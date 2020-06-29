const express = require("express"); // Import del modulo express.js
const app = express();

const port = process.argv[2];
const server = app.listen(port, () => console.log(`Server is running on port ${server.address().port}`));
const io = require("socket.io")(server); // Import del modulo socket.io

const nickname = new Map(); // Nickname -> SocketID
const clientes = new Map(); // SocketID -> Nickname 

// Se establece el path de los archivos estaticos (css, imagenes, etc.)
app.use(express.static(__dirname +'/'));

// Cuando se accede a la pagina envia el html
app.get("/", (req, res) => res.sendFile(__dirname + "/index.html"));

// Cuando hay una conexion...
io.on("connection", (socket) => {

  socket.on("name", (name) => {
    nickname.set(name, socket.id);
    clientes.set(socket.id, name);
    socket.broadcast.emit("nuevoDest", name)
    console.log(`Se conecto ${name}`);
  });

  socket.on("avisarATodos", () => socket.broadcast.emit("alguienEscribe"));

  // Al recibir un mensaje, hacer...
  socket.on("messageTo", (msg) => {
    // Enviar mensaje a todos los nombres menos al emisor
    console.log(msg);
    const destinatario = nickname.get(msg.to);
    const emisor = clientes.get(socket.id);

    // cuando el destinatario no existe hace broadcast
    if (destinatario) {
      socket.to(destinatario).emit('message', { "message": msg.message, "from": emisor });
    } else {
      socket.broadcast.emit('message', { "message": msg.message, "from": emisor });
    }
  });

  socket.on("disconnect", () => {
    // Dar de baja al cerrar la sesion
    const userName = clientes.get(socket.id);
    console.log(`${userName} cerro la sesion`);
    nickname.delete(clientes.get(socket.id));
    clientes.delete(socket.id);
  });
});

// Inicia el servidor http en el puerto designado
//http.listen(port, function () {
//  console.log("Server started on: " + port);
//});

 // sending to individual socketid (private message)
 //io.to('${socketId}').emit('hey', 'I just met you');
