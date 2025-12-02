<?php
require_once __DIR__ . '/actions.php';

header('Content-Type: application/json');
try {
    echo json_encode(startGame());
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['error' => $e->getMessage()]);
}