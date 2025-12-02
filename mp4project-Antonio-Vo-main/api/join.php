<?php
// having player join a game took WEEKS to figure out
$data = json_decode(file_get_contents('php://input'), true);
$stateFile = __DIR__ . '/state.json';
$state = json_decode(file_get_contents($stateFile), true);

$slot = $data['slot'];
$name = htmlspecialchars($data['name']);
$playerId = $data['playerId'] ?? null;
$now = time();

// Prevent joining if player has already joined.
foreach ($state['players'] as $player) {
  if (isset($player['id']) && $player['id'] === $playerId) {
    http_response_code(400);
    echo json_encode(['error' => 'You are already in the game.']);
    exit;
  }
}

if (isset($state['players'][$slot]) && $state['players'][$slot]['name'] === null) {
  $state['players'][$slot] = [
    'name' => $name,
    'id' => $playerId,
    'lastSeen' => $now
  ];
  $state['log'][] = "$name joined slot " . ($slot + 1);
  file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
  echo json_encode(['success' => true]);
  exit;
}

http_response_code(400);
echo json_encode(['error' => 'Slot already taken.']);
exit;
?>
