import json
import os

SAVE_FILE = os.path.join(os.path.dirname(__file__), "save.json")

def save_game(game, notation="standard"):

    save_data = {
        "coins": game.coins,
        "bet_amount": game.bet_amount,
        "notation": notation
    }

    with open(SAVE_FILE, "w") as file:
        json.dump(save_data, file, indent=4)

def load_game(game):

    if not os.path.exists(SAVE_FILE):
        return "standard"

    try:
        with open(SAVE_FILE, "r") as file:
            save_data = json.load(file)
    except (json.JSONDecodeError, OSError) as e:
        print(f"Warning: could not load save file: {e}. Resetting save.")
        save_data = {
            "coins": 100,
            "bet_amount": 0,
            "notation": "standard"
        }
        with open(SAVE_FILE, "w") as file:
            json.dump(save_data, file, indent=4)
        return "standard"

    game.coins = save_data.get("coins", 100)
    game.bet_amount = save_data.get("bet_amount", 0)
    return save_data.get("notation", "standard")