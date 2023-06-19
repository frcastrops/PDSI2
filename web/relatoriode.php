<?php
session_start();

// Verifica se o usuário já está logado
if(!isset($_SESSION['login'])) {
    header('Location: index.php');  // Redireciona para a página de login
    exit;
}

// Passa o tipo de usuário para uma variável PHP
$userType = $_SESSION['userType'];
?>

<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Atributos</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css"
          integrity="sha512-MV7K8+y+gLIBoVD59lQIYicR65iaqukzvf/nwasF0nqhPay5w/9lJmVM2hMDcnK1OnMGCdVK+iQrJ7lzPJQd1w=="
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link href='https://unpkg.com/boxicons@2.1.1/css/boxicons.min.css' rel='stylesheet'>
</head>

<body>
  <div class="sidebar">
    <div class="logo_menu">
      <img src="imagens/logo2.png" alt="">
      <br>
      <span class="m">
        <i class="fas fa-user-tie teste"></i>Sistema de Evasão de Alunos</span>
      <p class="p"></p>
    </div>
    <nav>
      <ul>
        <li>
          <a href="usuarios.php">
            <i class="fas fa-users"></i>
            <span class="nav-item">Usuário</span>
          </a>
        </li>
        <li>
          <a href="alunos.php">
            <i class="fas fa-users"></i>
            <span class="nav-item">Alunos</span>
          </a>
        </li>
        <li>
          <a href="#">
            <i class="fas fa-user"></i>
            <span class="nav-item">Perfil de acesso</span>
          </a>
        </li>
        <li>
          <a href="#">
            <i class="fas fa-task"></i>
            <span class="nav-item">Apredizagem de máquina</span>
          </a>
        </li>
        <li>
          <a href="relatoriode.php">
            <i class="fas fa-chart-bar"></i>
            <span class="nav-item">Relatório de atributos</span>
          </a>
        </li>
        <li>
          <a href="relatorioevasao.php" class="active">
            <i class="fas fa-chart-bar"></i>
            <span class="nav-item">Relatório de evasão</span>
          </a>
        </li>
        <li>
          <a href="suporte.php" class="suporte">
            <i class="fas fa-question-circle"></i>
            <span class="nav-item">Suporte</span>
          </a>
        </li>
        <p class="versao">versão 1.111</p>
      </ul>
    </nav>
  </div>

  <div class="container">
    <div class="header">
      <div class="editar">
        <span class="red">Atributos que mais colaboram para a evasão</span>
        <button onclick="gerarPdf()" id="new">Baixar Relatório</button>
        <button onclick="openModal()" id="new" class="pls">Gerar novo Relatório</button>
        <i class="fa fa-bell"></i>
      </div>
    </div>

    <div class="divTable">
      <div class="tests">
        <div class="sub">
          <p>Curso</p>
          <select id="tipo" name="tipo" class="he">
            <option value="1">BSI</option>
            <option value="2">BCC</option>
          </select>
          <p>Período</p>
          <select id="number" name="5" class="he">
            <option value="1">Todos</option>
            <option value="2">1</option>
            <option value="3">2</option>
            <option value="4">3</option>
          </select>
          <i class="fa fa-search plss"></i>
        </div>
      </div>

      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Atributos</th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table>
      <div class="testess">
        <span><a>Anterior</a></span>
        <p>1</p>
        <span><a>Próximo</a></span>
      </div>
    </div>

    <div class="modal-container2">
      <div class="modal2">
        <form>
          <label for="m-nome">Notas</label>
          <input id="m-nome" type="text" required>

          <label for="m-funcao">Faltas</label>
          <input id="m-funcao" type="text" required>

          <label for="m-nomes">Situação financeira</label>
          <input id="m-nomes" type="text" required>

          <button id="btnSalvar">Salvar</button>
        </form>
      </div>
    </div>
  </div>

  <script src="script.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.4.0/jspdf.umd.min.js"></script>
  <script>
    function gerarPdf() {
      var doc = new jsPDF();
      doc.text("Atributos que mais colaboram para a evasão", 15, 15);
      doc.autoTable({ html: 'table' });
      doc.save('Relatório_De_Atributos.pdf');
    }

    // Passa a variável PHP para uma variável JavaScript
    var userType = "<?php echo $userType; ?>";

    // Quando a página é carregada
    window.onload = function() {
        // Seleciona o botão
        var button = document.getElementById('new');

        // Se o tipo de usuário não for 'USER_ADMINISTRATOR', desativa o botão e mostra uma mensagem quando clicado
        if(userType != 'USER_ADMINISTRATOR') {
            button.disabled = true;
            button.onclick = function() {
                alert('Apenas usuários administradores podem gerar um novo relatório');
            };
        }
    };
  </script>
</body>

</html>
