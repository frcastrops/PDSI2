<?php
session_start();

// Verifica se o usuário já está logado
if (!isset($_SESSION['login'])) {
    header('Location: index.php');  // Se não está logado, redireciona para a página de login
    exit;
} else {
    // Verifica se o usuário tem as permissões necessárias
    if ($_SESSION['userType'] !== 'USER' && $_SESSION['userType'] !== 'USER_ADMINISTRATOR') {
        echo "<script>";
        echo "alert('Você não tem permissão para acessar esta página.');";
        echo "window.location.href = 'relatorioevasao.php';";
        echo "</script>";
        exit;
    }
}
$url = "http://localhost:8080/create/user";
$json = file_get_contents($url);
$data = json_decode($json, true);
?>