<?php
session_start();

// Verifica se o usuário já está logado
if (!isset($_SESSION['login'])) {
    header('Location: index.php');  // Se não está logado, redireciona para a página de login
    exit;
} else {
    // Verifica se o usuário tem as permissões necessárias
    if ($_SESSION['userType'] !== 'USER_ADMINISTRATOR') {
        echo "<script>";
        echo "alert('Você não tem permissão para acessar esta página.');";
        echo "window.location.href = 'relatorioevasao.php';";
        echo "</script>";
        exit;
    }
}
$data = array(
  "userEmail" => $_POST['userEmail'],
  "userName" => $_POST['userName'],
  "userPassword" => $_POST['userPassword'],
  "userType" => $_POST['userType'],
);

$data_json = json_encode($data);

$curl = curl_init();

curl_setopt_array($curl, array(
  CURLOPT_URL => "http://localhost:8080/create/user",
  CURLOPT_RETURNTRANSFER => true,
  CURLOPT_ENCODING => "",
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "POST",
  CURLOPT_POSTFIELDS => $data_json,
  CURLOPT_HTTPHEADER => array(
    "Content-Type: application/json",
    "Content-Length: " . strlen($data_json)
  ),
));

$response = curl_exec($curl);
$httpCode = curl_getinfo($curl, CURLINFO_HTTP_CODE);
$err = curl_error($curl);

curl_close($curl);

if ($err) {
  echo "cURL Error #:" . $err;
} else {
    if($httpCode == 200) {
        header('Location: usuarios.php');
        exit;
    } else {
        echo "<script>";
        echo "alert('Erro ao cadastrar usuário');";
        echo "window.location.href = 'usuarios.php';";
        echo "</script>";
    }
}
?>
