from email.mime import image
import sys
import os
import pygame
from sys import exit

# Ensure the project root is on sys.path so sibling packages like Saves can be imported
project_root = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
if project_root not in sys.path:
    sys.path.insert(0, project_root)

# Add MainMenu to path to import Settings
sys.path.insert(0, os.path.join(project_root, "MainMenu"))

from CardLogic.deck import GameLogic
from GUI.CardArea import CardArea
from Saves.Save_Manager import save_game, load_game
from NumberFormatter import format_number, parse_bet_input
from MainMenu.Settings import load_notation_preference


pygame.init()

script_dir = os.path.dirname(__file__)
print(script_dir)

font_path = os.path.join(script_dir, "../BlackJackImgs/Text/PressStart2P.ttf")
my_font = pygame.font.Font(font_path, 24)

WIDTH = 640
HEIGHT = 360
caption = "Devilish Dealer"

pygame.display.set_caption(caption)
clock = pygame.time.Clock()

screen = pygame.display.set_mode((WIDTH, HEIGHT), pygame.RESIZABLE)

game = GameLogic()

# Load game state from save file, then load notation preference from settings
load_game(game)
notation = load_notation_preference()  # Load notation from settings file

# ----------------------------
# MAP / BACKGROUND SETUP
# ----------------------------

script_dir = os.path.dirname(__file__)

background_paths = {
    "map1": os.path.join(script_dir, "..", "BlackJackImgs", "Areas", "BigRomans.png"),
    "map2": os.path.join(script_dir, "..", "BlackJackImgs", "Areas", "Sewer.png"),
    "map3": os.path.join(script_dir, "..", "BlackJackImgs", "Areas", "HellsCasino.png")
}

selected_map_arg = sys.argv[1] if len(sys.argv) > 1 else None

background_original = None
background_image = None

background_path = None
if selected_map_arg:
    if selected_map_arg in background_paths:
        background_path = background_paths[selected_map_arg]
    elif os.path.isfile(selected_map_arg):
        background_path = selected_map_arg
    else:
        print(f"Warning: map selection '{selected_map_arg}' not recognized. Falling back to default map.")

if background_path is None:
    background_path = background_paths["map1"]

try:
    background_original = pygame.image.load(background_path).convert()
    background_image = pygame.transform.scale(background_original, (WIDTH, HEIGHT))
except Exception as e:
    print(f"Failed to load background image '{background_path}': {e}")
    background_original = None
    background_image = None

# ----------------------------
# UI / GAME OBJECTS
# ----------------------------

rectangle = pygame.Rect((10, 9, 100, 100))

class button:
    def __init__(self, xpos, ypos, width, height, image=None, color=None):

        self.screen_w, self.screen_h = screen.get_size()
        self.x_ratio = xpos
        self.y_ratio = ypos
        self.width = width
        self.height = height
        self.image = image
        self.color = color

        if self.image:
            self.image = pygame.transform.scale(self.image, (self.width, self.height))
            self.rect = self.image.get_rect(topleft=(self.x_ratio, self.y_ratio))
            self.surface = self.image
        else:
            self.rect = pygame.Rect((self.x_ratio, self.y_ratio), (self.width, self.height))
            self.surface = pygame.Surface((self.rect.width, self.rect.height))
            self.surface.fill(self.color)

        self.original_image = self.image
        self.original_rect = self.rect.copy()
        self.original_surface = self.surface

    def draw(self, screen, pos):
        screen.blit(self.surface, self.rect)

    def is_clickedBet(self, event, pos):
        return self.rect.collidepoint(pos) and event.type == pygame.MOUSEBUTTONDOWN

    def is_clickedDeal(self, event, pos):
        if self.rect.collidepoint(pos) and event.type == pygame.MOUSEBUTTONDOWN:
            game.deal()

    def is_clickedHit(self, event, pos):
        if self.rect.collidepoint(pos) and event.type == pygame.MOUSEBUTTONDOWN:
            if game.can_click and game.has_placedBet():
                game.hit_p()
                if game.is_bust():
                    game.winnerCheck()
            else:
                print("You need to place a bet")

    def is_clickedStand(self, event, pos):
        if self.rect.collidepoint(pos) and event.type == pygame.MOUSEBUTTONDOWN:
            if game.can_click and game.has_placedBet():
                game.stand_p()
            else:
                print("You need to place a bet")

            if game.is_bust():
                game.winnerCheck()

    def is_clickedDD(self, event, pos):
        if self.rect.collidepoint(pos) and event.type == pygame.MOUSEBUTTONDOWN:
            if game.can_click:
                game.dd_p()
                if game.is_bust():
                    game.winnerCheck()
                        
    def is_clickedMenu(self, event, pos):
        if self.rect.collidepoint(pos) and event.type == pygame.MOUSEBUTTONDOWN:
            map_select_script = os.path.join(
                script_dir,
                "..",
                "MainMenu",
                "Main.py"
            )
            import subprocess
            subprocess.Popen([sys.executable, map_select_script])
            pygame.quit()
            exit()
            
    def is_clickedAllIn(self, event, pos):
        if self.rect.collidepoint(pos) and event.type == pygame.MOUSEBUTTONDOWN:
            if game.can_click:
                game.all_in_p()
                if game.is_bust():
                    game.winnerCheck()

    def is_hovered(self, pos):
        if self.rect.collidepoint(pos):
            if self.image:
                self.surface = pygame.transform.scale_by(self.image, 1.1)
            else:
                self.surface = pygame.transform.scale_by(self.original_surface, 1.1)

            self.rect = self.surface.get_rect(center=self.original_rect.center)
        else:
            if self.image:
                self.surface = self.image
            else:
                self.surface = pygame.Surface((self.original_rect.w, self.original_rect.h))
                self.surface.fill(self.color)

            self.rect = self.original_rect.copy()

    def resize(self, screen):
        new_width, new_height = screen.get_size()

        width = self.width * new_width
        height = self.height * new_height
        x_pos = self.x_ratio * new_width
        y_pos = self.y_ratio * new_height

        self.rect.update((x_pos, y_pos, width, height))

        if self.image:
            self.image = pygame.transform.scale(self.original_image, (width, height))
            self.surface = self.image
        else:
            self.surface = pygame.Surface((width, height))
            self.surface.fill(self.color)
            self.original_surface = self.surface.copy()

        self.original_rect = self.rect.copy()
class FloatingText:
    def __init__(self, x, y, text, color, font, duration=1.0):
        self.x = x
        self.y = y
        self.text = text
        self.color = color
        self.font = font
        self.duration = duration  # in seconds
        self.elapsed = 0.0
        
    def update(self, dt):
        """Update the floating text (dt in seconds)"""
        self.elapsed += dt
        
    def is_alive(self):
        """Check if the text should still be displayed"""
        return self.elapsed < self.duration
    
    def get_alpha(self):
        """Get the alpha value (0-255) based on elapsed time"""
        # Start at full opacity, fade out
        progress = self.elapsed / self.duration
        return int(255 * (1 - progress))
    
    def draw(self, screen):
        """Draw the floating text"""
        if not self.is_alive():
            return
        
        # Calculate vertical offset (float upward)
        progress = self.elapsed / self.duration
        offset_y = self.y - (progress * 50)  # Move up 50 pixels over the duration
        
        # Render text
        text_surface = self.font.render(self.text, True, self.color)
        
        # Apply alpha (opacity)
        alpha = self.get_alpha()
        text_surface.set_alpha(alpha)
        
        # Draw
        screen.blit(text_surface, (self.x - text_surface.get_width() // 2, offset_y))

# ----------------------------
# SIDE PANEL
# ----------------------------

class SidePanel:
    def __init__(self, x, y, width, height):
        self.image = os.path.abspath(os.path.join(script_dir, "..", "BlackJackImgs", "BackGrounds", "sidepanel.png"))

        self.rect = pygame.Rect(x, y, width, height)
        self.surface = pygame.Surface((width, height))


    def draw(self, screen):
        self.surface.blit(pygame.image.load(self.image), (0, 0))
        screen.blit(self.surface, self.rect)

# ----------------------------
# INITIAL UI LAYOUT
# ----------------------------

screen_width = screen.get_width()
screen_height = screen.get_height()

side_panel = SidePanel(
    0,
    0,
    int(screen_width * 0.15),
    int(screen_height * 1)
)

PlayButton = button(1, 0.2, 0.2, 0.2, color=(90, 90, 90))
betButton = button(1, 0.4, 0.2, 0.2, color=(12, 111, 90))
hitButton = button(1, 0.8, 0.2, 0.2, color=(1, 1, 90))
standButton = button(1, 0.1, 0.2, 0.2, color=(90, 90, 90))
doubleDownButton = button(0.12, 0.5, 0.2, 0.2, color=(90, 90, 90))
menuButton = button(0, 0, 0.2, 0.2, color=(90, 90, 90))
allInButton = button(0.1, 0.7, 0.2, 0.2, color=(90, 90, 90))
dealer_card_area = CardArea(screen, side_panel.rect.w / screen_width, 0, game)
player_card_area = CardArea(screen, side_panel.rect.w / screen_width, 0.5, game)

# game.deal()
font_size = int(screen.get_height() * 0.033)
try:
    font = pygame.font.Font("PressStart2P.ttf", font_size)
except:
    font = pygame.font.SysFont("Arial", font_size, bold=True)

result_font_size = int(screen.get_height() * 0.15)
try:
    result_font = pygame.font.Font("PressStart2P.ttf", result_font_size)
except:
    result_font = pygame.font.SysFont("Arial", result_font_size, bold=True)

bet_active = False
bet_input = ""
bet_message = "Click Bet to start"

floating_texts = []
previous_coins = game.coins
coins_display_x = 70  # Approximate x position for coin display
last_result_shown = None  # Track if we've already shown the result

# ----------------------------
# MAIN LOOP
# ----------------------------

while True:

    # Reload notation preference in case it was changed in settings
    notation = load_notation_preference()

    dt = clock.get_time() / 1000.0  # already calculated later, move it up top
    if not game.player_turn:
        # If player busted, skip dealer entirely
        if game.playerTotal > 21:
            game.winnerCheck()
        else:
            game.show_facedown = False
            game.dealer_reveal_timer -= dt

            if game.dealer_reveal_timer <= 0:
                dealer_total = game.hand_value(game.dealerHand)
                has_ace = any(c.get_rank() == 'Ace' for c in game.dealerHand)

                if dealer_total < 17 or (dealer_total == 17 and has_ace):
                    game.dealerPlay()
                else:
                    game.winnerCheck()

                game.dealer_reveal_timer = 1.5
    pos = pygame.mouse.get_pos()

    for event in pygame.event.get():
            
        PressStart2P = os.path.join(script_dir, "../BlackJackImgs/Text/PressStart2P.ttf")
        font_size = int(screen.get_height() * 0.03)
        font = pygame.font.Font(PressStart2P, font_size)

        PressStart2P = os.path.join(project_root, "BlackJackImgs", "Text", "PressStart2P.ttf")
        font_size = int(screen.get_height() * 0.03)
        font = pygame.font.Font(PressStart2P, font_size)

        if event.type == pygame.QUIT:
            save_game(game, notation)
            pygame.quit()
            exit()

        elif event.type == pygame.VIDEORESIZE:

            WIDTH, HEIGHT = event.w, event.h

            screen = pygame.display.set_mode((event.w, event.h), pygame.RESIZABLE)

            if background_original:
                background_image = pygame.transform.scale(background_original, (WIDTH, HEIGHT))

            side_panel = SidePanel(
                0,
                0,
                int(event.w * 0.30),
                event.h
            )
            dealer_card_area = CardArea(
                screen, 
                0.15,
                0,
                game
            )
            player_card_area = CardArea(
                screen,
                0.15,
                0.5,
                game

            )
            
            print(project_root)

            
            result_font_size = int(screen.get_height() * 0.15)
            result_font = pygame.font.Font(PressStart2P, result_font_size)

        if event.type == pygame.MOUSEBUTTONDOWN and event.button == 1 and not bet_active:
            if betButton.is_clickedBet(event, pos):
                bet_active = True
                bet_input = ""
                bet_message = "Type your bet and press Enter"

            hitButton.is_clickedHit(event, pos)
            standButton.is_clickedStand(event, pos)
            doubleDownButton.is_clickedDD(event, pos)
            allInButton.is_clickedAllIn(event, pos)
        if event.type == pygame.KEYDOWN:

            if not bet_active:

                if event.key == pygame.K_h:
                    if game.can_click and game.has_placedBet():
                        game.hit_p()

                elif event.key == pygame.K_s:
                    if game.can_click and game.has_placedBet():
                        game.stand_p()

                elif event.key == pygame.K_d:
                    if game.can_click and game.has_placedBet():
                        game.dd_p()
                elif event.key == pygame.K_a:
                    if game.can_click:
                        game.all_in_p()
                        if game.is_bust():
                            game.winnerCheck()

                elif event.key == pygame.K_m:
                    map_select_script = os.path.join(
                        script_dir,
                        "..",
                        "MainMenu",
                        "Main.py"
                    )
                    import subprocess
                    subprocess.Popen([sys.executable, map_select_script])
                    pygame.quit()
                    exit()

                elif event.key == pygame.K_b:
                    bet_active = True
                    bet_input = ""
                    bet_message = "Type your bet and press Enter"

            else:

                if event.key == pygame.K_BACKSPACE:
                    bet_input = bet_input[:-1]

                elif event.key in (pygame.K_RETURN, pygame.K_KP_ENTER):

                    if bet_input:
                        # Try parsing the input (supports "50k", "50 thousand", etc)
                        amount = parse_bet_input(bet_input)

                        if amount and game.bet(amount):
                            bet_message = f"Bet placed: {amount}"
                            save_game(game, notation)
                            bet_active = False
                            game.deal()
                            # Create floating text for bet
                            floating_texts.append(FloatingText(
                                coins_display_x, 
                                int(screen.get_height() * 0.45) + font_height,
                                f"-{amount}",
                                (255, 0, 0),  # Red color
                                font,
                                duration=1.0
                            ))
                            previous_coins = game.coins
                        else:
                            bet_message = "Invalid bet"
                            bet_input = ""

                elif event.unicode.isdigit():
                    bet_input += event.unicode


    # menuButton removed
    
    side_panel.draw(screen)
    player_card_area.draw_cards(game.playerHand)
    player_card_area.draw()

    dealer_card_area.draw()
    dealer_card_area.draw_cards(game.dealerHand)

    if background_image:
        screen.blit(background_image, (0, 0))
    else:
        screen.fill((0, 0, 0))

    side_panel.draw(screen)

    player_card_area.draw()
    player_card_area.draw_cards(game.playerHand)

    dealer_card_area.draw()
    dealer_card_area.draw_cards(game.dealerHand)

    pygame.draw.rect(screen, (50, 50, 50), (0, 0, WIDTH, 40))
    pygame.draw.rect(screen, (50, 50, 50), (0, HEIGHT - 40, WIDTH, 40))

    betButton.is_hovered(pos)
    betButton.draw(screen, pos)
    betButton.resize(screen)

    hitButton.is_hovered(pos)
    hitButton.draw(screen, pos)
    hitButton.resize(screen)

    standButton.is_hovered(pos)
    standButton.draw(screen, pos)
    standButton.resize(screen)

    # doubleDownButton removed

    # allInButton removed

    font_height = font.get_height()

    ui_y = int(screen.get_height() * 0.45)
    formatted_coins = format_number(game.coins, notation)
    formatted_bet = format_number(game.bet_amount, notation)
    status_text = font.render(
        f"Coins: {game.coins}   Bet: {game.bet_amount}",
        True,
        (255, 255, 255)
    )

    cardValue_text = font.render(
        f"Hand Value: {game.playerTotal}",
        True,
        (255,255,255)
    )
    

    screen.blit(status_text, (10, ui_y))
    screen.blit(cardValue_text, (10, ui_y + font_height + 10))
    keybind_text = font.render(
        "H = Hit    S = Stand    D = Double Down    B = Bet    M = Menu",
        True,
        (255, 255, 255)
    )

    screen.blit(
    keybind_text,
    (
        screen.get_width() * 0.5 - keybind_text.get_width() / 2,
        screen.get_height() * 0.0
    )
)
    # screen.blit(keybind_text,(screen.get_width() * 0.68, screen.get_height() - HEIGHT))
    
    # Clears the old text area first
    # pygame.draw.rect(screen, (0, 0, 0), (0, 30, 400, 40))
       # Update and draw floating texts
    dt = clock.get_time() / 1000.0  # Convert milliseconds to seconds
    for floating_text in floating_texts[:]:
        floating_text.update(dt)
        floating_text.draw(screen)
        if not floating_text.is_alive():
            floating_texts.remove(floating_text)
    
    # Detect win and create floating text
    if game.coins > previous_coins:
        win_amount = game.coins - previous_coins
        floating_texts.append(FloatingText(
            coins_display_x,
            int(screen.get_height() * 0.45) + font_height,
            f"+{win_amount}",
            (0, 255, 0),  # Green color
            font,
            duration=1.0
        ))
        previous_coins = game.coins
    
    # Display WIN/LOSE/TIE message
    if game.last_result and game.last_result != last_result_shown:
        result_text = game.last_result.upper()
        
        if game.last_result == "win":
            result_color = (0, 255, 0)  # Green
        elif game.last_result == "lose":
            result_color = (255, 0, 0)  # Red
        else:  # tie
            result_color = (255, 255, 255)  # White
        
        center_x = screen.get_width() / 2
        center_y = screen.get_height() / 2
        
        floating_texts.append(FloatingText(
            center_x,
            center_y,
            result_text,
            result_color,
            result_font,
            duration=1.0
        ))
        last_result_shown = game.last_result


    if bet_active:
        prompt_text = font.render(f"Enter bet: {bet_input}", True, (255, 255, 255))
    else:
        prompt_text = font.render(bet_message, True, (255, 255, 255))

    screen.blit(prompt_text, (0, HEIGHT / 1.5))

    pygame.display.update()
    clock.tick(60)