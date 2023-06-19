<?php
// Inicia a sessão
session_start();

$login = $_POST['login'];
$senha = $_POST['senha'];

$data = array("login" => $login, "senha" => $senha);
$data_string = json_encode($data);

$curl = curl_init();

curl_setopt_array($curl, array(
    CURLOPT_URL => "http://localhost:8080/auth/user",
    CURLOPT_RETURNTRANSFER => true,
    CURLOPT_ENCODING => "",
    CURLOPT_MAXREDIRS => 10,
    CURLOPT_TIMEOUT => 30,
    CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
    CURLOPT_CUSTOMREQUEST => "POST",
    CURLOPT_POSTFIELDS => $data_string,
    CURLOPT_HTTPHEADER => array(
        "Content-Type: application/json",
        "Content-Length: " . strlen($data_string)
    ),
));

$response = curl_exec($curl);
$httpCode = curl_getinfo($curl, CURLINFO_HTTP_CODE);
$err = curl_error($curl);

curl_close($curl);

if ($err) {
    echo "cURL Error #:" . $err;
} else {
    if ($httpCode == 200) {
        $response_data = json_decode($response);

        // Guarda o login e userType na sessão
        $_SESSION['login'] = $login;
        $_SESSION['userType'] = $response_data->userType;

        // Guarda o cookie na sessão
        if (isset($_COOKIE[session_name()])) {
            setcookie(session_name(), '', time()-42000, '/');
        }
        session_regenerate_id(true);

        // Redireciona para a página correta com base no userType
        if ($response_data->userType == 'USER_ADMINISTRATOR') {
            header('Location: admin_page.php');
        } else if ($response_data->userType == 'USER') {
            header('Location: user_page.php');
        } else if ($response_data->userType == 'USER_READ_ONLY') {
            header('Location: read_only_page.php');
        } else {
            // tipo de usuário desconhecido, redirecionar para algum lugar padrão
            header('Location: default_page.php');
        }

        exit;
    } else {
        echo "<script>";
        echo "alert('Erro ao logar');";
        echo "window.location.href = 'index.php';";
        echo "</script>";
    }
}
?>