<?php
// main actions
function income(){
$stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    // Get playerId from POST data
    $input = json_decode(file_get_contents('php://input'), true);
    $playerId = $input['playerId'] ?? null;
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

function foreignAid() {
    $stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    // Get playerId from POST data
    $input = json_decode(file_get_contents('php://input'), true);
    $playerId = $input['playerId'] ?? null;
    if (!$playerId) {
        return ['error' => 'No playerId provided'];
    }

    foreach ($state['players'] as &$player) {
        if ($player['id'] === $playerId) {
            $player['coins'] += 2; // Foreign Aid gives 2 coins
            $state['log'][] = "{$player['name']} took Foreign Aid (+2 coins)";
            break;
        }
    }
    unset($player);

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
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

function tax() {
$stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    // Get playerId from POST data
    $input = json_decode(file_get_contents('php://input'), true);
    $playerId = $input['playerId'] ?? null;
    if (!$playerId) {
        return ['error' => 'No playerId provided'];
    }

    foreach ($state['players'] as &$player) {
        if ($player['id'] === $playerId) {
            $player['coins'] += 3; // add 3 coin for income and cannot be blocked
            $state['log'][] = "{$player['name']} took tax (+3 coin)";
            break;
        }
    }
    unset($player);

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
}

function assassinate(){
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

    // Must have at least 3 coins to assassinate
    if ($actor['coins'] < 3) {
        return ['error' => 'Not enough coins to assassinate'];
    }

    // Remove 3 coins from actor
    $actor['coins'] -= 3;

    // Remove one card from target (last card for now)
    if (count($target['cards']) > 0) {
        array_pop($target['cards']);
        if (count($target['cards']) < 1) {
            $target['alive'] = false; // Target is dead if no cards left
            $state['log'][] = "{$target['name']} has been assassinated!";
        } else {
            $state['log'][] = "{$target['name']} lost a card due to assassination!";
        }
        $state['log'][] = "{$actor['name']} assassinated {$target['name']} (3 coins spent)";
    } else {
        $state['log'][] = "{$target['name']} has no cards left to lose!";
    }

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
}

function Exchange() {
    $stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    $input = json_decode(file_get_contents('php://input'), true);
    $playerId = $input['playerId'] ?? null;
    if (!$playerId) {
        return ['error' => 'No playerId provided'];
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
        return ['error' => 'Invalid player'];
    }

    // Draw up to 2 cards from the deck
    $drawn = [];
    for ($i = 0; $i < 2 && count($state['deck']) > 0; $i++) {
        $drawn[] = array_pop($state['deck']);
    }

    // Combine player's cards and drawn cards
    $allCards = array_merge($player['cards'], $drawn);

    // Shuffle and keep 2 (for now, random selection)
    shuffle($allCards);
    $player['cards'] = array_slice($allCards, 0, 2);

    // Return the rest to the deck and shuffle
    $returned = array_slice($allCards, 2);
    $state['deck'] = array_merge($state['deck'], $returned);
    shuffle($state['deck']);

    $state['log'][] = "{$player['name']} exchanged cards";

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
}

function steal(){
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

    // Take up to 2 coins from target, give to actor
    $amount = min(2, $target['coins']);
    $target['coins'] -= $amount;
    $actor['coins'] += $amount;
    $state['log'][] = "{$actor['name']} stole from {$target['name']} for $amount coins";

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
}

// conuteractions
function blockAid() {
    $stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    $input = json_decode(file_get_contents('php://input'), true);
    $blockerId = $input['playerId'] ?? null;

    // Check if there is a pending foreign aid action
    if (
        !$state['pendingAction'] ||
        $state['pendingAction']['type'] !== 'foreignAid' ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        return ['error' => 'No foreign aid to block'];
    }

    // Check if blocker is allowed
    if (!in_array($blockerId, $state['pendingAction']['blockableBy'])) {
        return ['error' => 'You cannot block this action'];
    }

    $state['pendingAction']['status'] = 'blocked';
    $blockerName = null;
foreach ($state['players'] as $p) {
    if ($p['id'] === $blockerId) {
        $blockerName = $p['name'];
        break;
    }
}
$state['log'][] = "Foreign Aid was blocked by $blockerName";

    // Optionally, clear pendingAction if you want to move on
    // $state['pendingAction'] = null;

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
}   

function blocksteal(){
    $stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    $input = json_decode(file_get_contents('php://input'), true);
    $blockerId = $input['playerId'] ?? null;

    // Check if there is a pending steal action
    if (
        !$state['pendingAction'] ||
        $state['pendingAction']['type'] !== 'steal' ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        return ['error' => 'No steal to block'];
    }

    if (!in_array($blockerId, $state['pendingAction']['blockableBy'])) {
        return ['error' => 'You cannot block this action'];
    }

    $state['pendingAction']['status'] = 'blocked';
    $blockerName = null;
foreach ($state['players'] as $p) {
    if ($p['id'] === $blockerId) {
        $blockerName = $p['name'];
        break;
    }
}
$state['log'][] = "Steal was blocked by $blockerName";

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
}
function blockAssassination(){
$stateFile = __DIR__ . '/state.json';
    $state = json_decode(file_get_contents($stateFile), true);

    $input = json_decode(file_get_contents('php://input'), true);
    $blockerId = $input['playerId'] ?? null;

    // Check if there is a pending assasination action
    if (
        !$state['pendingAction'] ||
        $state['pendingAction']['type'] !== 'Assassination' ||
        $state['pendingAction']['status'] !== 'waiting'
    ) {
        return ['error' => 'No assassination to block'];
    }

    // Check if blocker is allowed
    if (!in_array($blockerId, $state['pendingAction']['blockableBy'])) {
        return ['error' => 'You cannot block this action'];
    }

    $state['pendingAction']['status'] = 'blocked';
    $blockerName = null;
foreach ($state['players'] as $p) {
    if ($p['id'] === $blockerId) {
        $blockerName = $p['name'];
        break;
    }
}
$state['log'][] = "Assassination was blocked by $blockerName";

    // Optionally, clear pendingAction if you want to move on
    // $state['pendingAction'] = null;

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
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

        // Challenger loses a card (for now, remove last card)
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
        // Actor loses a card (for now, remove last card)
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

    // Optionally, clear pendingAction if resolved
    // $state['pendingAction'] = null;

    file_put_contents($stateFile, json_encode($state, JSON_PRETTY_PRINT));
    return ['success' => true];
}