if (!localStorage.getItem('playerId')) {
  localStorage.setItem('playerId', crypto.randomUUID());
}
const playerId = localStorage.getItem('playerId');

function updateUI(state) {
  const slots = document.getElementById('slots');
  slots.innerHTML = '';
  state.players.forEach((player, index) => {
    const div = document.createElement('div');
    div.innerText = player.name || `Slot ${index + 1} (Click to Join)`;
    if (!player.name) {
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
            return res.json().then(data => { throw new Error(data.error); });
          }
          return res.json();
        })
        .then(() => loadState())
        .catch(err => alert(err.message));
      };
    }
    slots.appendChild(div);
  });

  const log = document.getElementById('log');
  log.innerHTML = state.log.map(entry => `<div>${entry}</div>`).join('');

  // Handle pending foreign aid
  if (state.pendingAction && state.pendingAction.type === 'foreign_aid') {
    if (state.pendingAction.blockableBy && state.pendingAction.blockableBy.includes(playerId)) {
      // Show block button
      if (!document.getElementById('block-foreign-aid')) {
        const btn = document.createElement('button');
        btn.id = 'block-foreign-aid';
        btn.innerText = 'Block Foreign Aid';
        btn.onclick = () => {
          fetch('api/actions.php', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: 'block_foreign_aid', playerId })
          })
          .then(res => res.json())
          .then(() => loadState());
        };
        document.body.appendChild(btn);
      }
    } else if (state.pendingAction.actor === playerId) {
      // Show resolve button after a timeout (e.g., 10 seconds)
      if (!document.getElementById('resolve-foreign-aid')) {
        setTimeout(() => {
          if (!document.getElementById('resolve-foreign-aid')) {
            const btn = document.createElement('button');
            btn.id = 'resolve-foreign-aid';
            btn.innerText = 'Resolve Foreign Aid';
            btn.onclick = () => {
              fetch('api/actions.php', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ action: 'resolve_foreign_aid', playerId })
              })
              .then(res => res.json())
              .then(() => loadState());
            };
            document.body.appendChild(btn);
          }
        }, 10000); // 10 seconds
      }
    }
  } else {
    // Remove block/resolve buttons if not needed
    const blockBtn = document.getElementById('block-foreign-aid');
    if (blockBtn) blockBtn.remove();
    const resolveBtn = document.getElementById('resolve-foreign-aid');
    if (resolveBtn) resolveBtn.remove();
  }

  // Handle challenge action
  if (state.pendingAction && state.pendingAction.blockableBy && state.pendingAction.blockableBy.includes(playerId)) {
    if (!document.getElementById('challenge-btn')) {
      const btn = document.createElement('button');
      btn.id = 'challenge-btn';
      btn.innerText = 'Challenge';
      btn.onclick = challenge;
      document.body.appendChild(btn);
    }
  } else {
    const challengeBtn = document.getElementById('challenge-btn');
    if (challengeBtn) challengeBtn.remove();
  }

  // Show  cards
  const myCardsDiv = document.getElementById('my-cards');
  const me = state.players.find(p => p.id === playerId);
  if (me && me.cards && me.cards.length > 0) {
    myCardsDiv.innerHTML = `<h3>Your Cards:</h3>` +
      me.cards.map(card => `<span class="card">${card}</span>`).join(' ');
  } else {
    myCardsDiv.innerHTML = `<h3>Your Cards:</h3> <span>No cards</span>`;
  }
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
    .then(res => {
      if (!res.ok) {
        return res.json()
          .then(data => { throw new Error(data.error); })
          .catch(() => { throw new Error("at least two players are needed"); });
      }
      return res.json();
    })
    .then(() => loadState())
    .catch(err => alert(err.message));
}

function steal() {
  // Prompt for a target player slot 
  const target = prompt("Enter the slot number of the player you want to steal from (1-4):");
  const targetIndex = parseInt(target, 10) - 1;
  if (isNaN(targetIndex) || targetIndex < 0 || targetIndex > 3) {
    alert("Invalid target.");
    return;
  }

  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'steal',
      playerId,      
      target: targetIndex
    })
  })
  .then(res => {
    if (!res.ok) {
      return res.json().then(data => { throw new Error(data.error); });
    }
    return res.json();
  })
  .then(() => loadState())
  .catch(err => alert(err.message));
}
// give coin. can't block
function income() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'income',
      playerId 
    })
  })
  .then(res => {
    if (!res.ok) {
      return res.json().then(data => { throw new Error(data.error); });
    }
    return res.json();
  })
  .then(() => loadState())
  .catch(err => alert(err.message));
}
// take two coins. can block with duke
function foreignAid() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'foreign_aid',
      playerId 
    })
  })
  .then(res => {
    if (!res.ok) {
      return res.json().then(data => { throw new Error(data.error); });
    }
    return res.json();
  })
  .then(() => loadState())
  .catch(err => alert(err.message));
}

function challenge() {
  // Only allow if there is a pending action to challenge
  if (!window.lastState || !window.lastState.pendingAction) {
    alert("No action to challenge.");
    return;
  }

  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'challenge',
      playerId 
    })
  })
  .then(res => {
    if (!res.ok) {
      return res.json().then(data => { throw new Error(data.error); });
    }
    return res.json();
  })
  .then(() => loadState())
  .catch(err => alert(err.message));
}

function resolveAssassinate() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'resolve_assassinate',
      playerId
    })
  })
  .then(res => res.json())
  .then(() => loadState());
}

function resolveSteal() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'resolve_steal',
      playerId
    })
  })
  .then(res => res.json())
  .then(() => loadState());
}

function resolveExchange() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'resolve_exchange',
      playerId
    })
  })
  .then(res => res.json())
  .then(() => loadState());
}
// take 3 coins. can't be blocked
function tax() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'tax',
      playerId
    })
  })
  .then(res => res.json())
  .then(() => loadState());
}

function exchange() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'exchange',
      playerId
    })
  })
  .then(res => res.json())
  .then(() => loadState());
}

function coup() {
  const target = prompt("Enter the slot number of the player you want to coup (1-4):");
  const targetIndex = parseInt(target, 10) - 1;
  if (isNaN(targetIndex) || targetIndex < 0 || targetIndex > 3) {
    alert("Invalid target.");
    return;
  }
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'coup',
      playerId,
      target: targetIndex
    })
  })
  .then(res => res.json())
  .then(() => loadState());
}

function blockAssassinate() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'block_assassinate',
      playerId
    })
  })
  .then(res => res.json())
  .then(() => loadState());
}

function blockSteal() {
  fetch('api/actions.php', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      action: 'block_steal',
      playerId
    })
  })
  .then(res => res.json())
  .then(() => loadState());
}

setInterval(loadState, 1000);
loadState();

