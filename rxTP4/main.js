// Para que el scroll del chat quede siempre abajo
let chatBox = document.querySelector('#chat');
chatBox.scrollTop = chatBox.scrollHeight - chatBox.clientHeight;

// Socket.io
const socket = io();

//Listado Destinatarios
const listDestinatario = new Array();

// Al recibir un mensaje, crea un elemento en la lista chat mostrando dicho mensaje
socket.on("message", function (msg) {
  
  const liMessageRecibed = document.createElement('li');
  liMessageRecibed.className = "messageReceived";

  const divFrom = document.createElement('div');
  divFrom.className ="formatFrom";

  const divMessage = document.createElement('div');
  divMessage.className = "formatMessage";

  const textMessage = document.createTextNode(msg.message);
  const textFrom = document.createTextNode(" "+msg.from+":");

  divFrom.innerHTML = ('<i class="fas fa-user-alt"></i>')
  divFrom.appendChild(textFrom);
  divMessage.appendChild(textMessage)

  liMessageRecibed.append(divFrom);
  liMessageRecibed.append(divMessage);

  $("#chat").append(liMessageRecibed);
});

// Al conectarse un nuevo destinatario
socket.on("destC", (name) => {
  if ($("#nickActual").text() != name) {
    if (listDestinatario.length == 0) {
      listDestinatario.push(name);
      $("#nicknameDestinatario").append($('<option value="'+name+'">'+name+'</option>'));
    } else {
        const nuevo = true;
        listDestinatario.forEach(element => {
          if (element == name) {
            nuevo = false;
          }
        });
        if (nuevo) {
          listDestinatario.push(name);
          $("#nicknameDestinatario").append($('<option value="'+name+'">'+name+'</option>'));
        }
    }
  }
});

// Cuando alguien se va
socket.on("destDesc", () => {
  listDestinatario.splice(0, listDestinatario.length);
  $("#nicknameDestinatario option").remove();
  $("#nicknameDestinatario").append($('<option value="TODOS"+">Todos</option>'));
});

$("#nicknameDestinatario").on("click", () => {
  socket.emit("destinatarios");
});

// Cuando se presiona el boton de 'Send', se envia el mensaje
$("#sendButton").on("click", () => sendMessage());

// Cuando se apreta 'enter', se envia el mensaje
$("#messageToSend").keypress((key) => {
  if (key.which == 13) {
    sendMessage();
  } else {
    socket.emit("avisarATodos");
  }
});


socket.on("alguienEscribe", () => {
  $("#escribiendo").text("Alguien está escribiendo...");
  setTimeout(() => $("#escribiendo").text(""), 3000);
});


// Cuando se apreta 'enter', se asigna el nuevo nick
$("#nickname").keypress((key) => {
  if (key.which == 13) {
    const to = $("#nickActual").text();
    if (to == "Nick:") {
      const name = $("#nickname").val().trim();
      if (!name) {
        return false;
      }
      socket.emit("name", name);
      $("#nickname").val("");
      $("#nickActual").text(name);
    }
  }
});

// Funcion utilizada para obtener el mensaje escrito y enviarlo al servidor
function sendMessage() {
  const message = $("#messageToSend").val().trim();
  if (!message) {
    return false;
  }
  const name = $("#nicknameDestinatario").val().trim();
  socket.emit("messageTo", { "message": message, "to": name });
  $("#messageToSend").val("").focus();
  $("#chat").append($('<li class="messageSended">').text(message)); 
  

  // Agrega el mensaje a la lista del chat como un mensaje enviado por el cliente, y no recibido del servidor
  $("#contenedor_chat").stop().animate({ scrollTop: $("#contenedor_chat")[0].scrollHeight }, 1000);

  return false;
}
