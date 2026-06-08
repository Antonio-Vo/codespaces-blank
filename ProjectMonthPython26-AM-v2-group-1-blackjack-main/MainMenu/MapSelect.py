import pygame
import os
import subprocess
import sys

pygame.init()

WINDOWED_WIDTH = 640
WINDOWED_HEIGHT = 360
current_width = WINDOWED_WIDTH
current_height = WINDOWED_HEIGHT

script_dir = os.path.dirname(__file__)

original_map1_image = pygame.image.load(os.path.join(script_dir, "..", "BlackJackImgs", "Areas", "bigromans.png"))
original_map2_image = pygame.image.load(os.path.join(script_dir, "..", "BlackJackImgs", "Areas", "Sewer.png"))
original_map3_image = pygame.image.load(os.path.join(script_dir, "..", "BlackJackImgs", "Areas", "HellsCasino.png"))

screen = pygame.display.set_mode((WINDOWED_WIDTH, WINDOWED_HEIGHT), pygame.RESIZABLE)
pygame.display.set_caption("Select Map")

clock = pygame.time.Clock()
font = pygame.font.SysFont(None, 40)

is_fullscreen = False

map1_image = None
map2_image = None
map3_image = None
map1_rect = pygame.Rect(0, 0, 0, 0)
map2_rect = pygame.Rect(0, 0, 0, 0)
map3_rect = pygame.Rect(0, 0, 0, 0)


def resize_assets(width, height):
    global current_width, current_height
    global map1_image, map2_image, map3_image
    global map1_rect, map2_rect, map3_rect

    current_width = width
    current_height = height

    map1_image = pygame.transform.scale(original_map1_image, (width, height))
    map2_image = pygame.transform.scale(original_map2_image, (width, height))
    map3_image = pygame.transform.scale(original_map3_image, (width, height))

    button_width = min(500, int(width * 0.55))
    button_height = max(60, int(height * 0.14))
    button_x = (width - button_width) // 2
    top_y = int(height * 0.25)
    spacing = int(button_height * 1.25)

    map1_rect = pygame.Rect(button_x, top_y, button_width, button_height)
    map2_rect = pygame.Rect(button_x, top_y + spacing, button_width, button_height)
    map3_rect = pygame.Rect(button_x, top_y + spacing * 2, button_width, button_height)


def draw_centered_text(text_surface, rect, screen_surface):
    text_x = rect.x + (rect.width - text_surface.get_width()) // 2
    text_y = rect.y + (rect.height - text_surface.get_height()) // 2
    screen_surface.blit(text_surface, (text_x, text_y))


game_script = os.path.join(
    script_dir,
    "..",
    "GameScreen",
    "gameScreen.py"
)

resize_assets(current_width, current_height)

while True:
    clock.tick(60)

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            sys.exit()

        if event.type == pygame.KEYDOWN and event.key == pygame.K_F11:
            if is_fullscreen:
                screen = pygame.display.set_mode((WINDOWED_WIDTH, WINDOWED_HEIGHT), pygame.RESIZABLE)
                resize_assets(WINDOWED_WIDTH, WINDOWED_HEIGHT)
                is_fullscreen = False
            else:
                screen = pygame.display.set_mode((0, 0), pygame.FULLSCREEN)
                resize_assets(*screen.get_size())
                is_fullscreen = True

        if event.type == pygame.VIDEORESIZE and not is_fullscreen:
            screen = pygame.display.set_mode((event.w, event.h), pygame.RESIZABLE)
            resize_assets(event.w, event.h)

        if event.type == pygame.MOUSEBUTTONDOWN and event.button == 1:
            if map1_rect.collidepoint(event.pos):
                selected_map = os.path.join(
                    script_dir,
                    "..",
                    "BlackJackImgs",
                    "Areas",
                    "bigRomansEmployeeBehindTable.png"
                )
                selected_map = "map1"
                subprocess.Popen([sys.executable, game_script, selected_map])
                pygame.quit()
                sys.exit()

            elif map2_rect.collidepoint(event.pos):
                selected_map = "map2"
                subprocess.Popen([sys.executable, game_script, selected_map])
                pygame.quit()
                sys.exit()

            elif map3_rect.collidepoint(event.pos):
                selected_map = os.path.join(
                    script_dir,
                    "..",
                    "BlackJackImgs",
                    "Areas",
                    "HellsCasino.png"
                )
                selected_map = "map3"
                subprocess.Popen([sys.executable, game_script, selected_map])
                pygame.quit()
                sys.exit()

    default_border_color1 = (50, 50, 50) #dark gray
    default_border_color2 = (45, 45, 40) #darker gray
    default_border_color3 = (205, 20, 20) #dark red
    hover_border_color1 = (255, 255, 255) #white
    hover_border_color2 = (0) #black
    hover_border_color3 = (255, 0, 0) #red

    screen.fill((20, 20, 20))

    title = font.render("Choose Your Table", True, (255, 255, 255))
    title_x = (current_width - title.get_width()) // 2
    title_y = int(current_height * 0.08)
    screen.blit(title, (title_x, title_y))

    pygame.draw.rect(screen, (225, 225, 0), map1_rect)
    pygame.draw.rect(screen, (45, 45, 40), map2_rect)
    pygame.draw.rect(screen, (205, 20, 20), map3_rect)

    draw_centered_text(font.render("Big Romans", True, (0, 0, 0)), map1_rect, screen)
    draw_centered_text(font.render("Sewer", True, (8, 10, 10)), map2_rect, screen)
    draw_centered_text(font.render("Demon Table", True, (255, 255, 255)), map3_rect, screen)

    if map1_rect.collidepoint(pygame.mouse.get_pos()):
        current_border_color1 = hover_border_color1
    else:
        current_border_color1 = default_border_color1

    if map2_rect.collidepoint(pygame.mouse.get_pos()):
        current_border_color2 = hover_border_color2
    else:
        current_border_color2 = default_border_color2

    if map3_rect.collidepoint(pygame.mouse.get_pos()):
        current_border_color3 = hover_border_color3
    else:
        current_border_color3 = default_border_color3

    pygame.draw.rect(screen, current_border_color1, map1_rect, 3)
    pygame.draw.rect(screen, current_border_color2, map2_rect, 3)
    pygame.draw.rect(screen, current_border_color3, map3_rect, 3)

    pygame.display.flip()
