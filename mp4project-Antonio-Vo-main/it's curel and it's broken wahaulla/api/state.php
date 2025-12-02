<?php
header('Content-Type: application/json');

$stateFile = __DIR__ . '/state.json';
$state = json_decode(file_get_contents($stateFile), true);

$input = json_decode(file_get_contents('php://input'), true);
$playerId = $input['playerId'] ?? null;

$now = time();
$timeout = 30; // seconds

foreach ($state['players'] as $i => $player) {
  // Remove disconnected players
  if ($player['lastSeen'] !== null && ($now - $player['lastSeen']) > $timeout) {
    $state['log'][] = $player['name'] . " timed out";
    $state['players'][$i] = [
      'name' => null,
      'id' => null,
      'lastSeen' => null,
      'coins' => null,
      'cards' => [],
      'alive' => null
    ];
  }

  // Update current player's timestamp
  if ($player['id'] === $playerId) {
    $state['players'][$i]['lastSeen'] = $now;
  }
}

file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
echo json_encode($state);
?>
