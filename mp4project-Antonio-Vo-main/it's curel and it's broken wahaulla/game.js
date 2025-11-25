if (!localStorage.getItem('playerId')) {
  localStorage.setItem('playerId', crypto.randomUUID());
}
const playerId = localStorage.getItem('playerId');

function updateUI(state) {
  const slots = document.getElementById('slots');
  slots.innerHTML = '';

  // Check if this playerId is already in the game
  const alreadyJoined = state.players.some(player => player.id === playerId);

  state.players.forEach((player, index) => {
    const div = document.createElement('div');
    div.innerText = player.name || `Slot ${index + 1} (Click to Join)`;
    if (!player.name && !alreadyJoined) {
      div.onclick = () => {
        const name = prompt('Enter your name');
        if (!name) return;
        fetch('api/join.php', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ name, slot: index, playerId })
        })
        .then(res => {
          if (!res.ok) {
            return res.json().then(data => { 
              alert(data.error); 
              loadState();
              throw new Error(data.error); 
            });
          }
          return res.json();
        })
        .then(() => loadState())
        .catch(err => {
          // Always reload state and show error if not already shown
          if (err && err.message) {
            alert(err.message);
          }
          loadState();
        });
    }
    slots.appendChild(div);
  });

  const log = document.getElementById('log');
  log.innerHTML = state.log.map(entry => `<div>${entry}</div>`).join('');

  const blockBtn = document.getElementById('block');
  blockBtn.style.display = 'none';
  if (state.pendingAction && state.pendingAction.blockableBy && state.pendingAction.blockableBy.includes(playerId)) {
    blockBtn.style.display = '';
  }

  // Show current player's cards
  const myCardsDiv = document.getElementById('my-cards');
  const me = state.players.find(p => p.id === playerId);
  if (me && me.cards) {
    myCardsDiv.innerHTML = `<b>Your Cards:</b> ${me.cards.map(card => card.revealed ? '🂠' : card.name).join(', ')}`;
  } else {
    myCardsDiv.innerHTML = '';
  }

  window.lastState = state; // Save the last state for action functions
}

function loadState() {
  fetch('api/state.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ playerId })
  })
    .then(res => res.json())
    .then(updateUI);
}

function startGame() {
  fetch('api/start.php', { method: 'POST' })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        loadState(); // Refresh UI after starting
      } else if (data.error) {
        alert(data.error);
      }
    });
}

function income() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ action: 'income', playerId })
  }).then(() => loadState());
}

function foreignAid() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ action: 'foreignAid', playerId })
  }).then(() => loadState());
}

function coup() {
  const target = prompt('Enter the name or slot number of the player to coup:');
  // You should map this to the correct playerId
  // For now, let's assume you prompt for the slot index (0-based)
  const state = window.lastState; // Save the last state in updateUI for easy access
  if (!state) return alert('Game state not loaded!');
  const targetPlayer = state.players[target];
  if (!targetPlayer || !targetPlayer.id) return alert('Invalid target!');
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ action: 'coup', playerId, target: targetPlayer.id })
  }).then(() => loadState());
}

function taxAction() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ action: 'tax', playerId })
  }).then(() => loadState());
}

function assassinate() {
  const target = prompt('Enter the slot number of the player to assassinate:');
  const state = window.lastState;
  if (!state) return alert('Game state not loaded!');
  const targetPlayer = state.players[target];
  if (!targetPlayer || !targetPlayer.id) return alert('Invalid target!');
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ action: 'assassinate', playerId, target: targetPlayer.id })
  }).then(() => loadState());
}

function exchange() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ action: 'exchange', playerId })
  }).then(() => loadState());
}

function steal() {
  const target = prompt('Enter the slot number of the player to steal from:');
  const state = window.lastState;
  if (!state) return alert('Game state not loaded!');
  const targetPlayer = state.players[target];
  if (!targetPlayer || !targetPlayer.id) return alert('Invalid target!');
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ action: 'steal', playerId, target: targetPlayer.id })
  }).then(() => loadState());
}

function blockAction() {
  const state = window.lastState;
  if (!state || !state.pendingAction) return alert('No action to block!');
  let endpoint = '';
  switch (state.pendingAction.type) {
    case 'foreignAid':
      endpoint = 'blockAid';
      break;
    case 'steal':
      endpoint = 'blocksteal';
      break;
    case 'assassinate':
    case 'assassination':
      endpoint = 'blockAssassination';
      break;
    default:
      return alert('This action cannot be blocked.');
  }
  fetch(`api/actions.php?action=${endpoint}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ playerId })
  }).then(() => loadState());
}

function challenge() {
  const state = window.lastState;
  if (!state || !state.pendingAction) return alert('No action to challenge!');
  fetch('api/actions.php?action=challenge', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ playerId })
  }).then(() => loadState());
}

setInterval(loadState, 1000);
loadState();

