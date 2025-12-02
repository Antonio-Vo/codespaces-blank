<?php
header('Content-Type: application/json');

function income(){
$stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    // Get playerId from POST data
    $input = json_decode(file_get_contents('php://input'), true);
    $playerId = $input['playerId'] ?? null;
    $targetId = $input['target'] ?? null;
    if (!$playerId) {
        return ['error' => 'No playerId provided'];
    }

    foreach ($state['players'] as &$player) {
        if ($player['id'] === $playerId) {
            $player['coins'] += 1; // add 1 coin for income and cannot be blocked
            $state['log'][] = "{$player['name']} recived income (+1 coin)";
            break;
        }
    }
    unset($player);

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
}

function foreignAid($state, $playerId, $stateFile) {
    // Set pendingAction for block
    $blockableBy = [];
    foreach ($state['players'] as $p) {
        if ($p['id'] !== $playerId && $p['alive']) {
            $blockableBy[] = $p['id'];
        }
    }
    $state['pendingAction'] = [
        'type' => 'foreign_aid',
        'actor' => $playerId,
        'blockableBy' => $blockableBy,
        'claimedCard' => null, //I don't think this is needed for foreign aid but might be good to have so the json is consistent
        'status' => 'waiting'
    ];
    foreach ($state['players'] as $p) {
        if ($p['id'] === $playerId) {
            $actorName = $p['name'];
            break;
        }
    }
    $state['log'][] = "{$actorName} attempts to take Foreign Aid (can be blocked)";
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['pending' => true]);
    exit;
}

function Coup(){
    $stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    $input = json_decode(file_get_contents('php://input'), true);
    $playerId = $input['playerId'] ?? null;
    $targetId = $input['target'] ?? null;
    if (!$playerId || !$targetId) {
        return ['error' => 'Missing playerId or target'];
    }

    $actor = null;
    $target = null;
    foreach ($state['players'] as &$player) {
        if ($player['id'] === $playerId) $actor = &$player;
        if ($player['id'] === $targetId) $target = &$player;
    }
    if (!$actor || !$target) {
        return ['error' => 'Invalid player or target'];
    }

    // Must have at least 7 coins to assassinate
    if ($actor['coins'] < 7) {
        return ['error' => 'Not enough coins to assassinate'];
    }

    // Remove 3 coins from actor
    $actor['coins'] -= 7;

    // Remove one card from target (last card for now)
    if (count($target['cards']) > 0) {
        array_pop($target['cards']);
        if (count($target['cards']) < 1) {
            $target['alive'] = false; // Target is dead if no cards left
            $state['log'][] = "{$target['name']} has been overthrown!";
        } else {
            $state['log'][] = "{$target['name']} lost a card due to a coup!";
        }
        $state['log'][] = "{$actor['name']} assassinated {$target['name']} (3 coins spent)";
    } else {
        $state['log'][] = "{$target['name']} has no cards left to lose!";
    }

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
}



function assassinate($state, $playerId, $stateFile) {
    $input = json_decode(file_get_contents('php://input'), true);
    $targetId = $input['target'] ?? null;
    if (!$playerId || !$targetId) {
        http_response_code(400);
        echo json_encode(['error' => 'Missing playerId or target']);
        exit;
    }

    // Find actor and target
    $actor = null;
    $target = null;
    foreach ($state['players'] as $p) {
        if ($p['id'] === $playerId) $actor = $p;
        if ($p['id'] === $targetId) $target = $p;
    }
    if (!$actor || !$target) {
        http_response_code(400);
        echo json_encode(['error' => 'Invalid player or target']);
        exit;
    }
    if ($actor['coins'] < 3) {
        http_response_code(400);
        echo json_encode(['error' => 'Not enough coins to assassinate']);
        exit;
    }
    if (!$target['alive']) {
        http_response_code(400);
        echo json_encode(['error' => 'Target is not alive']);
        exit;
    }

    // Set pendingAction for block/challenge
    $state['pendingAction'] = [
        'type' => 'assassinate',
        'actor' => $playerId,
        'target' => $targetId,
        'blockableBy' => [$targetId], // Only the target can block
        'claimedCard' => 'assassin',
        'status' => 'waiting'
    ];
    $state['log'][] = "{$actor['name']} attempts to assassinate {$target['name']} (can be blocked/challenged)";
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['pending' => true]);
    exit;
}



function steal($state, $playerId, $stateFile) {
    $input = json_decode(file_get_contents('php://input'), true);
    $targetId = $input['target'] ?? null;
    if (!$playerId || !$targetId) {
        http_response_code(400);
        echo json_encode(['error' => 'Missing playerId or target']);
        exit;
    }

    // Find actor and target
    $actor = null;
    $target = null;
    foreach ($state['players'] as $p) {
        if ($p['id'] === $playerId) $actor = $p;
        if ($p['id'] === $targetId) $target = $p;
    }
    if (!$actor || !$target) {
        http_response_code(400);
        echo json_encode(['error' => 'Invalid player or target']);
        exit;
    }
    if (!$target['alive']) {
        http_response_code(400);
        echo json_encode(['error' => 'Target is not alive']);
        exit;
    }

    // Set pendingAction for block/challenge
    $state['pendingAction'] = [
        'type' => 'steal',
        'actor' => $playerId,
        'target' => $targetId,
        'blockableBy' => [$targetId], // Only the target can block
        'claimedCard' => 'captain',
        'status' => 'waiting'
    ];
    $state['log'][] = "{$actor['name']} attempts to steal from {$target['name']} (can be blocked/challenged)";
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['pending' => true]);
    exit;
}



// start game
function startGame() {
    $stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    // Collect joined players
    $joinedPlayers = array_filter($state['players'], fn($p) => $p['name'] !== null);
    $numPlayers = count($joinedPlayers);
    if ($numPlayers < 2 || $numPlayers > 4) {
        return ['error' => 'Need 2-4 players to start'];
    }

    // Build and shuffle deck
    $allCards = array_merge(
        array_fill(0, 3, 'duke'),
        array_fill(0, 3, 'assassin'),
        array_fill(0, 3, 'captain'),
        array_fill(0, 3, 'contessa'),
        array_fill(0, 3, 'ambassador')
    );
    shuffle($allCards);

    // Deal 2 cards and 2 coins to each player
    foreach ($state['players'] as &$player) {
        if ($player['name'] !== null) {
            $player['coins'] = 2;
            $player['cards'] = [array_pop($allCards), array_pop($allCards)];
            $player['alive'] = true;
        } else {
            $player['cards'] = [];
            $player['alive'] = null;
        }
    }
    unset($player);

    $state['deck'] = $allCards;
    $state['status'] = 'playing';
    $state['turn'] = 0; // First player
    $state['log'][] = "Game started!";

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
    }
//

function challenge() {
    $stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    $input = json_decode(file_get_contents('php://input'), true);
    $challengerId = $input['playerId'] ?? null;

    // Check for a pending action
    if (
        !$state['pendingAction'] ||
        !isset($state['pendingAction']['type']) ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        return ['error' => 'No action to challenge'];
    }

    // Who is being challenged?
    $actorId = $state['pendingAction']['actor'] ?? null;
    $claimedCard = $state['pendingAction']['claimedCard'] ?? null; // e.g. 'duke', 'assassin', etc.

    if (!$actorId || !$claimedCard) {
        return ['error' => 'Invalid pending action for challenge'];
    }

    // Find actor and challenger
    $actor = null;
    $challenger = null;
    foreach ($state['players'] as &$player) {
        if ($player['id'] === $actorId) $actor = &$player;
        if ($player['id'] === $challengerId) $challenger = &$player;
    }
    if (!$actor || !$challenger) {
        return ['error' => 'Invalid player(s)'];
    }

    // Does the actor have the claimed card?
    $actorHasCard = in_array($claimedCard, $actor['cards']);

    if ($actorHasCard) {
        // Actor reveals and replaces the card
        $key = array_search($claimedCard, $actor['cards']);
        unset($actor['cards'][$key]);
        $actor['cards'] = array_values($actor['cards']); // reindex

        // Return revealed card to deck and draw a new one
        $state['deck'][] = $claimedCard;
        shuffle($state['deck']);
        $actor['cards'][] = array_pop($state['deck']);

        // Challenger loses a card 
        if (count($challenger['cards']) > 0) {
            array_pop($challenger['cards']);
            if (count($challenger['cards']) < 1) {
                $challenger['alive'] = false;
                $state['log'][] = "{$challenger['name']} lost the challenge and is out!";
            } else {
                $state['log'][] = "{$challenger['name']} lost the challenge and lost a card!";
            }
        }
        $state['log'][] = "{$actor['name']} proved they had the {$claimedCard}!";
        // Action proceeds
        $state['pendingAction']['status'] = 'resolved';
    } else {
        // Actor loses a card 
        if (count($actor['cards']) > 0) {
            array_pop($actor['cards']);
            if (count($actor['cards']) < 1) {
                $actor['alive'] = false;
                $state['log'][] = "{$actor['name']} was caught bluffing and is out!";
            } else {
                $state['log'][] = "{$actor['name']} was caught bluffing and lost a card!";
            }
        }
        $state['log'][] = "{$actor['name']} failed to prove they had the {$claimedCard}!";
        // Action is blocked/canceled
        $state['pendingAction']['status'] = 'blocked';
    }

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
}
function tax($state, $playerId, $stateFile) {
    // Set pendingAction for block/challenge
    $blockableBy = [];
    foreach ($state['players'] as $p) {
        if ($p['id'] !== $playerId && $p['alive']) {
            $blockableBy[] = $p['id'];
        }
    }
    $state['pendingAction'] = [
        'type' => 'tax',
        'actor' => $playerId,
        'blockableBy' => $blockableBy,
        'claimedCard' => 'duke',
        'status' => 'waiting'
    ];
    foreach ($state['players'] as $p) {
        if ($p['id'] === $playerId) {
            $actorName = $p['name'];
            break;
        }
    }
    $state['log'][] = "{$actorName} claims Duke for Tax (can be blocked/challenged)";
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['pending' => true]);
    exit;
}
function exchange($state, $playerId, $stateFile) {
    // Set pendingAction for challenge
    $blockableBy = [];
    foreach ($state['players'] as $p) {
        if ($p['id'] !== $playerId && $p['alive']) {
            $blockableBy[] = $p['id'];
        }
    }
    $state['pendingAction'] = [
        'type' => 'exchange',
        'actor' => $playerId,
        'blockableBy' => $blockableBy,
        'claimedCard' => 'ambassador',
        'status' => 'waiting'
    ];
    foreach ($state['players'] as $p) {
        if ($p['id'] === $playerId) {
            $actorName = $p['name'];
            break;
        }
    }
    $state['log'][] = "{$actorName} claims Ambassador for Exchange (can be challenged)";
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['pending' => true]);
    exit;
}
function resolve_exchange($state, $playerId, $stateFile) {
    if (
        !$state['pendingAction'] ||
        $state['pendingAction']['type'] !== 'exchange' ||
        $state['pendingAction']['actor'] !== $playerId ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        http_response_code(400);
        echo json_encode(['error' => 'No exchange to resolve.']);
        exit;
    }

    // Find the player
    $player = null;
    foreach ($state['players'] as &$p) {
        if ($p['id'] === $playerId) {
            $player = &$p;
            break;
        }
    }
    if (!$player) {
        http_response_code(400);
        echo json_encode(['error' => 'Invalid player']);
        exit;
    }

    // Draw up to 2 cards from the deck
    $drawn = [];
    for ($i = 0; $i < 2 && count($state['deck']) > 0; $i++) {
        $drawn[] = array_pop($state['deck']);
    }

    // Combine player's cards and drawn cards
    $allCards = array_merge($player['cards'], $drawn);

    // Shuffle and keep 2 (random selection)
    shuffle($allCards);
    $player['cards'] = array_slice($allCards, 0, 2);

    // Return the rest to the deck and shuffle
    $returned = array_slice($allCards, 2);
    $state['deck'] = array_merge($state['deck'], $returned);
    shuffle($state['deck']);

    $state['log'][] = "{$player['name']} exchanged cards";
    $state['pendingAction'] = null;

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['success' => true]);
    exit;
}
// handle foreign aid resolution
function resolve_foreign_aid($state, $playerId, $stateFile) {
    if (
        !$state['pendingAction'] ||
        $state['pendingAction']['type'] !== 'foreign_aid' ||
        $state['pendingAction']['actor'] !== $playerId ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        http_response_code(400);
        echo json_encode(['error' => 'No foreign aid to resolve.']);
        exit;
    }
    foreach ($state['players'] as &$player) {
        if ($player['id'] === $playerId) {
            $player['coins'] += 2;
            $state['log'][] = "{$player['name']} received 2 coins from Foreign Aid.";
            break;
        }
    }
    $state['pendingAction'] = null;
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['success' => true]);
    exit;
}
function block_foreign_aid($state, $playerId, $stateFile) {
    if (
        !$state['pendingAction'] ||
        $state['pendingAction']['type'] !== 'foreign_aid' ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        http_response_code(400);
        echo json_encode(['error' => 'No foreign aid to block']);
        exit;
    }
    if (!in_array($playerId, $state['pendingAction']['blockableBy'])) {
        http_response_code(400);
        echo json_encode(['error' => 'You cannot block this action']);
        exit;
    }
    foreach ($state['players'] as $p) {
        if ($p['id'] === $playerId) {
            $blockerName = $p['name'];
            break;
        }
    }
    $state['pendingAction']['status'] = 'blocked';
    $state['log'][] = "Foreign Aid was blocked by $blockerName";
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['success' => true]);
    exit;
}
function block_assassinate($state, $playerId, $stateFile) {
    if (
        !$state['pendingAction'] ||
        $state['pendingAction']['type'] !== 'assassinate' ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        http_response_code(400);
        echo json_encode(['error' => 'No assassination to block']);
        exit;
    }
    if (!in_array($playerId, $state['pendingAction']['blockableBy'])) {
        http_response_code(400);
        echo json_encode(['error' => 'You cannot block this action']);
        exit;
    }
    foreach ($state['players'] as $p) {
        if ($p['id'] === $playerId) {
            $blockerName = $p['name'];
            break;
        }
    }
    $state['pendingAction']['status'] = 'blocked';
    $state['log'][] = "Assassination was blocked by $blockerName";
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['success' => true]);
    exit;
}
function block_steal($state, $playerId, $stateFile) {
    if (
        !$state['pendingAction'] ||
        $state['pendingAction']['type'] !== 'steal' ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        http_response_code(400);
        echo json_encode(['error' => 'No steal to block']);
        exit;
    }
    if (!in_array($playerId, $state['pendingAction']['blockableBy'])) {
        http_response_code(400);
        echo json_encode(['error' => 'You cannot block this action']);
        exit;
    }
    foreach ($state['players'] as $p) {
        if ($p['id'] === $playerId) {
            $blockerName = $p['name'];
            break;
        }
    }
    $state['pendingAction']['status'] = 'blocked';
    $state['log'][] = "Steal was blocked by $blockerName";
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['success' => true]);
    exit;
}

// Resolves below handles action after it has been blocked or challenged
function resolve_assassinate($state, $playerId, $stateFile) {
    if (
        !$state['pendingAction'] ||
        $state['pendingAction']['type'] !== 'assassinate' ||
        $state['pendingAction']['actor'] !== $playerId ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        http_response_code(400);
        echo json_encode(['error' => 'No assassination to resolve.']);
        exit;
    }

    $targetId = $state['pendingAction']['target'];
    $actor = null;
    $target = null;
    foreach ($state['players'] as &$player) {
        if ($player['id'] === $playerId) $actor = &$player;
        if ($player['id'] === $targetId) $target = &$player;
    }
    if (!$actor || !$target) {
        http_response_code(400);
        echo json_encode(['error' => 'Invalid player or target']);
        exit;
    }
    if ($actor['coins'] < 3) {
        http_response_code(400);
        echo json_encode(['error' => 'Not enough coins to assassinate']);
        exit;
    }

    // Remove 3 coins from actor
    $actor['coins'] -= 3;

    // Remove one card from target 
    if (count($target['cards']) > 0) {
        array_pop($target['cards']);
        if (count($target['cards']) < 1) {
            $target['alive'] = false;
            $state['log'][] = "{$target['name']} has been assassinated!";
        } else {
            $state['log'][] = "{$target['name']} lost a card due to assassination!";
        }
        $state['log'][] = "{$actor['name']} assassinated {$target['name']} (3 coins spent)";
    } else {
        $state['log'][] = "{$target['name']} has no cards left to lose!";
    }

    $state['pendingAction'] = null;
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['success' => true]);
    exit;
}
function resolve_steal($state, $playerId, $stateFile) {
    if (
        !$state['pendingAction'] ||
        $state['pendingAction']['type'] !== 'steal' ||
        $state['pendingAction']['actor'] !== $playerId ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        http_response_code(400);
        echo json_encode(['error' => 'No steal to resolve.']);
        exit;
    }

    $targetId = $state['pendingAction']['target'];
    $actor = null;
    $target = null;
    foreach ($state['players'] as &$player) {
        if ($player['id'] === $playerId) $actor = &$player;
        if ($player['id'] === $targetId) $target = &$player;
    }
    if (!$actor || !$target) {
        http_response_code(400);
        echo json_encode(['error' => 'Invalid player or target']);
        exit;
    }

    // Take up to 2 coins from target, give to actor
    $amount = min(2, $target['coins']);
    $target['coins'] -= $amount;
    $actor['coins'] += $amount;
    $state['log'][] = "{$actor['name']} stole from {$target['name']} for $amount coins";

    $state['pendingAction'] = null;
    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    echo json_encode(['success' => true]);
    exit;
}

// Dispatcher
$input = json_decode(file_get_contents('php://input'), true);
$action = $input['action'] ?? null;
$playerId = $input['playerId'] ?? null;
$stateFile = __DIR__ . '/state.json';
$state = json_decode(file_get_contents($stateFile), true);

switch ($action) {
    case 'income':
        income($state, $playerId, $stateFile);
        break;
    case 'foreign_aid':
        foreignAid($state, $playerId, $stateFile);
        break;
    case 'resolve_foreign_aid':
        resolve_foreign_aid($state, $playerId, $stateFile);
        break;
    case 'block_foreign_aid':
        block_foreign_aid($state, $playerId, $stateFile);
        break;
    case 'tax':
        tax($state, $playerId, $stateFile);
        break;
    case 'assassinate':
        assassinate($state, $playerId, $stateFile);
        break;
    case 'resolve_assassinate':
        resolve_assassinate($state, $playerId, $stateFile);
        break;
    case 'block_assassinate':
        block_assassinate($state, $playerId, $stateFile);
        break;
    case 'steal':
        steal($state, $playerId, $stateFile);
        break;
    case 'resolve_steal':
        resolve_steal($state, $playerId, $stateFile);
        break;
    case 'block_steal':
        block_steal($state, $playerId, $stateFile);
        break;
    case 'exchange':
        exchange($state, $playerId, $stateFile);
        break;
    case 'resolve_exchange':
        resolve_exchange($state, $playerId, $stateFile);
        break;
    case 'challenge':
        challenge();
        break;
    case 'coup':
        Coup();
        break;
    case 'start_game':
        startGame();
        break;
    default:
        http_response_code(400);
        echo json_encode(['error' => 'Unknown or missing action']);
        exit;
}