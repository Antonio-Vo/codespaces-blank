import pygame
from sys import exit
import os
import subprocess
import sys
import Settings

pygame.init()
clock = pygame.time.Clock()
HEIGHT = 360
WIDTH = 640
screen_info = pygame.display.Info()
game_script = os.path.normpath(os.path.join(os.path.dirname(__file__), "..", "GameScreen", "gameScreen.py"))
settings_file = os.path.normpath(os.path.join(os.path.dirname(__file__), "..", "settings.json"))

screen = pygame.display.set_mode((0, 0), pygame.FULLSCREEN)
pygame.display.set_caption("Devilish Dealer")
script_dir = os.path.dirname(__file__)


# current settings state

#put this in settings.
settings_active = False
settings_target = None
settings_scroll = 0
settings_panel_rect = pygame.Rect(80, 60, WIDTH - 160, HEIGHT - 120)

notation_active = False      # ✅ add this
notation_scroll = 0
active_notation_index = 0
notation_boxes = []
controls = Settings.load_menu_controls()

# LOAD BOTH BACKGROUNDS
script_dir = os.path.dirname(__file__) #gets the path to this file
print(script_dir)
bg_windowed = pygame.image.load(os.path.join(script_dir, "..", "BlackJackImgs", "BackGrounds", "MenuBackGround.png")).convert()
bg_fullscreen = pygame.image.load(os.path.join(script_dir, "..", "BlackJackImgs", "BackGrounds", "MenuBackGroundFullscreen.png")).convert()

# START WITH FULLSCREEN
is_fullscreen = True

play_button = pygame.image.load(os.path.join(script_dir, "..", "BlackJackImgs", "ButtonImg", "PlayButton.png")).convert_alpha()
quit_button = pygame.image.load(os.path.join(script_dir, "..", "BlackJackImgs", "ButtonImg", "quit.png")).convert_alpha()
settings_button = pygame.image.load(os.path.join(script_dir, "..", "BlackJackImgs", "ButtonImg", "settingsButton.png")).convert_alpha()
# screen = pygame.display.set_mode((0, 0), pygame.FULLSCREEN)
background = pygame.transform.scale(
    bg_fullscreen,
    (screen_info.current_w, screen_info.current_h)
)
pygame.display.set_caption("Devilish Dealer")

# Allows map selection
map_select_script = os.path.join(
    os.path.dirname(__file__),
    "MapSelect.py"
)


#Class for menubuttons
class Button:
    def __init__(self, xpos, ypos, width = .20, height = 0.2, color = (0, 0, 0), image=None):
        self.screen_w, self.screen_h = screen.get_size()            #gets screen size at startup
        self.x_ratio = xpos 
        self.y_ratio = ypos 
        self.w_ratio = width 
        self.h_ratio = height 
        self.xpos = self.x_ratio * self.screen_w      #the ratio times the screen width = the xpos
        self.ypos = self.y_ratio * self.screen_h
        self.width = self.w_ratio * self.screen_w
        self.height = self.h_ratio * self.screen_h
        self.color = color
        self.image = image
        self.original_image = image

        if self.image:                                                                               #if self.image exists, change to the entered size and width
            self.image = pygame.transform.scale(self.image, (self.width, self.height))               #makes a rectangle of the image, and sets the position of the image and rectangle
            self.rect = self.image.get_rect(topleft = (self.xpos, self.ypos))
            self.surface = self.image
        else:
            self.rect = pygame.Rect((self.xpos, self.ypos), (self.width, self.height))                 #if self.image does NOT exist make a rectangle with position and size
            self.surface = pygame.Surface((self.rect.width, self.rect.height))                         #set the surface to be the surface width and rectangle
            self.surface.fill(self.color)                                                              #fill it with color, the screen has no set posistion, because that is done with the draw method.
        
        self.original_rect = self.rect.copy()

    def resize(self, screen):
        new_width, new_height = screen.get_size()

        width = self.w_ratio * new_width
        height = self.h_ratio * new_height
        x_pos = self.x_ratio * new_width
        y_pos = self.y_ratio * new_height

        self.rect.update((x_pos, y_pos, width, height)) #updates the rectangle to the new size and pos

        self.image = pygame.transform.scale(self.original_image, (width, height)) #scale image to the new width and
        self.surface = self.image   #makes a cope of image and sets it equal to the rects surface

        self.rect.update((x_pos, y_pos, width, height))
        self.original_rect = self.rect.copy()
        

    def set_pos(self, x, y):
        self.rect.center = (x, y)
    

    def draw(self, screen):
        screen.blit(self.surface, (self.rect))         #this is where surface gets its position, setting self.rect to self.original_rect would cause the hovering effect, to come from the topright
                                                        #because of what the code does in hover.

    def is_clicked(self, event, pos):
         if self.rect.collidepoint(pos) and event.type == pygame.MOUSEBUTTONDOWN: #I need THIS method to be used more, should be updated
            print("AAAAAAH")

    def is_hovered(self, pos):
        if self.original_rect.collidepoint(pos):                            #checks if cursor is withe th original_rect. both rect and original rect are the same at this point
            self.surface = pygame.transform.scale_by(self.image, 1.1)           #saves the surface as the scaled image, if self.surface was made self.image, the button will keep growing.
            self.rect = self.surface.get_rect(center=self.original_rect.center) #the makes self.rect the same size as surface from the top left , and moves it center back to its original position.
        else:
            self.surface = self.image                           #once cursor is off sets surface back to normal image
            self.rect = self.original_rect.copy()               #sets rect back to its original self.


PlayButton = Button(xpos = .5 - 0.165/2, ypos = .65, height = .044, width = .165, image = play_button)                         # enter the size and position in ratio
SettingsButton = Button(xpos = 0.5 - .11/2, ypos = 0.75, height = .044, width = .11, image = settings_button)
QuitButton = Button(xpos = 0.5 - 0.055/2, ypos = .85, height = .0825, width = .055, image = quit_button)

# settings UI helpers are provided by Settings.py

while True:
    pos = pygame.mouse.get_pos()
    if settings_active:
        settings_panel_rect, settings_boxes, settings_max_scroll = Settings.get_settings_layout(screen, controls, settings_scroll)
    else:
        settings_boxes = []

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            exit()

        if event.type == pygame.KEYDOWN:
            if settings_active and event.key == pygame.K_ESCAPE:
                settings_active = False
                settings_target = None
                settings_scroll = 0
                continue

            for box_rect, box_index in settings_boxes:
                if box_rect.collidepoint(pos):
                    if box_index == "notation":     # ✅ notation button was clicked
                        notation_active = True      # open notation panel
                        settings_active = False     # close settings panel
                    else:
                        settings_target = box_index # normal keybind box

            if event.key == pygame.K_F11:

                if is_fullscreen:
                    screen = pygame.display.set_mode((WIDTH, HEIGHT), pygame.RESIZABLE)

                    background = pygame.transform.scale(
                        bg_windowed,
                        (WIDTH, HEIGHT)
                    )

                    PlayButton.resize(screen)
                    QuitButton.resize(screen)
                    SettingsButton.resize(screen)

                    is_fullscreen = False

                else:
                    screen = pygame.display.set_mode((0, 0), pygame.FULLSCREEN)

                    background = pygame.transform.scale(
                        bg_fullscreen,
                        (screen_info.current_w, screen_info.current_h)
                    )

                    PlayButton.resize(screen)
                    QuitButton.resize(screen)


                    SettingsButton.resize(screen)

                    is_fullscreen = True

        if event.type == pygame.MOUSEWHEEL and settings_active:
            settings_scroll -= event.y * 30
            settings_scroll = max(0, min(settings_scroll, settings_max_scroll))

        if event.type == pygame.VIDEORESIZE:
            screen = pygame.display.set_mode((event.w, event.h), pygame.RESIZABLE)
            PlayButton.resize(screen)
            QuitButton.resize(screen)
            SettingsButton.resize(screen)

            # Use correct background depending on mode
            if is_fullscreen:
                background = pygame.transform.scale(bg_fullscreen, (event.w, event.h))
            else:
                background = pygame.transform.scale(bg_windowed, (event.w, event.h))

           

        if event.type == pygame.MOUSEBUTTONDOWN and event.button == 1:
            if settings_active:
                clicked_settings_box = False
                
                notation_rect = pygame.Rect(
                    settings_panel_rect.x + 24,
                    settings_panel_rect.y + 140,
                    settings_panel_rect.width - 48,
                    52,
                )
                
                # Check if notation box was clicked
                if notation_rect.collidepoint(pos):
                    current = Settings.load_notation_preference()
                    options = Settings.NOTATION_OPTIONS
                    current_index = options.index(current)
                    next_index = (current_index + 1) % len(options)
                    Settings.save_notation_preference(options[next_index])
                    clicked_settings_box = True
                else:
                    # Check if a keybind control box was clicked
                    for box_rect, box_label in settings_boxes:
                        if box_rect.collidepoint(pos):
                            settings_target = box_label
                            clicked_settings_box = True
                            break

                if not clicked_settings_box and not settings_panel_rect.collidepoint(pos):
                    settings_active = False
                    settings_target = None

            else:
                if PlayButton.rect.collidepoint(pos):
                    subprocess.Popen(
                        [sys.executable, map_select_script],

                    )
                    pygame.quit()
                    exit()

                if QuitButton.rect.collidepoint(pos):
                    pygame.quit()
                    exit()

                if SettingsButton.rect.collidepoint(pos):
                    settings_active = True
                    settings_target = None
                    settings_scroll = 0
                    settings_panel_rect, settings_boxes, settings_max_scroll = Settings.get_settings_layout(screen, controls, settings_scroll)

        if settings_active:
            settings_panel_rect, settings_boxes, settings_max_scroll = Settings.draw_settings_panel(screen, controls, settings_target, settings_scroll)
        elif notation_active:                   # ✅ draw notation panel instead
            panel_rect, notation_boxes, max_scroll = Settings.draw_notation_panel(screen, active_notation_index, notation_scroll)

            # handle notation scroll
           
        if event.type == pygame.MOUSEWHEEL:
                    notation_scroll -= event.y * 30

        if event.type == pygame.MOUSEBUTTONDOWN:
            for box_rect, index in notation_boxes:
                if box_rect.collidepoint(pos):
                    active_notation_index = index   # ✅ save selected notation
                    notation_active = False          # close panel
        if event.type == pygame.KEYDOWN and event.key == pygame.K_ESCAPE:
            if settings_active:
                settings_active = False
            elif notation_active:               # ✅ close notation panel too
                notation_active = False
                notation_scroll = 0
        if event.type == pygame.KEYDOWN and settings_active and settings_target is not None:
            new_key_name = pygame.key.name(event.key)
            for item in controls:
                if item[0] == settings_target:
                    item[1] = new_key_name
                    break
            Settings.save_menu_controls(controls)
            settings_target = None
    
    if settings_active:
        settings_panel_rect, settings_boxes, settings_max_scroll = Settings.get_settings_layout(screen, controls, settings_scroll)
        settings_scroll = min(settings_scroll, settings_max_scroll)
    
    screen.blit(background, (0, 0))


    PlayButton.is_hovered(pos)
    QuitButton.is_hovered(pos)
    SettingsButton.is_hovered(pos)


    PlayButton.draw(screen)
    QuitButton.draw(screen)
    SettingsButton.draw(screen)
    if settings_active:
        settings_panel_rect, settings_boxes, settings_max_scroll = Settings.draw_settings_panel(screen, controls, settings_target, settings_scroll)
    elif notation_active:
        panel_rect, notation_boxes, notation_max_scroll = Settings.draw_notation_panel(screen, active_notation_index, notation_scroll)
    else:
        settings_boxes = []
        notation_boxes = []

    pygame.display.flip()
    clock.tick(60)
