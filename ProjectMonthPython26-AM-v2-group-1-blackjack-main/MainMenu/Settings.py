
import pygame
import os
import json
import sys



# Add parent directory to path to import NumberFormatter
ROOT_DIR = os.path.normpath(os.path.join(os.path.dirname(__file__), ".."))
if ROOT_DIR not in sys.path:
    sys.path.insert(0, ROOT_DIR)

from GameScreen.NumberFormatter import format_number, NOTATION_TYPES
SETTINGS_FILE = os.path.normpath(os.path.join(ROOT_DIR, "settings.json"))
FONT = None
DEFAULT_CONTROLS_MENU = [
    ["Hit", "h"],
    ["Stand", "s"],
    ["Double Down", "d"],
    ["Bet", "b"],
    ["Menu", "m"],
    ["All In", "a"],
]

DEFAULT_CONTROLS_GAME = {
    "Hit": "h",
    "Stand": "s",
    "Double Down": "d",
    "Bet": "b",
    "Menu": "m",
    "All In": "a",
}

font = None

def get_font():
    global FONT
    if FONT is None:
        FONT = pygame.font.SysFont("Arial", 24)
    return FONT #returns font


def load_settings_data():
    try:
        with open(SETTINGS_FILE, "r", encoding="utf-8") as json_file:
            return json.load(json_file)
    except Exception:
        return {}


def load_menu_controls():
    saved = load_settings_data()
    result = []
    for label, default_key in DEFAULT_CONTROLS_MENU:
        result.append([label, saved.get(label, default_key)])
    return result


def save_menu_controls(controls):
    try:
        settings = load_settings_data()
        settings.update({label: key for label, key in controls})
        with open(SETTINGS_FILE, "w", encoding="utf-8") as json_file:
            json.dump(settings, json_file, indent=2)
    except Exception:
        pass


def load_game_controls():
    saved = load_settings_data()
    controls = {}
    for action, default_key in DEFAULT_CONTROLS_GAME.items():
        key_name = saved.get(action, default_key)
        try:
            controls[action] = pygame.key.key_code(key_name)
        except Exception:
            controls[action] = pygame.key.key_code(default_key)
    return controls


def save_game_controls(controls):
    try:
        with open(SETTINGS_FILE, "w", encoding="utf-8") as json_file:
            json.dump({action: pygame.key.name(key_code) for action, key_code in controls.items()}, json_file, indent=2)
    except Exception:
        pass


def format_key(key_value):
    if isinstance(key_value, int):
        return pygame.key.name(key_value).upper()
    return str(key_value).upper()


def load_notation_preference():
    """Load the number notation preference from settings."""
    saved = load_settings_data()
    return saved.get("notation", "standard")


def save_notation_preference(notation):
    """Save the number notation preference to settings."""
    try:
        settings = load_settings_data()
        settings["notation"] = notation
        with open(SETTINGS_FILE, "w", encoding="utf-8") as json_file:
            json.dump(settings, json_file, indent=2)
    except Exception:
        pass


def draw_settings_panel(screen, controls, active_label, scroll_offset):
    FONT = get_font()
    panel_margin = 70
    box_height = 52
    box_spacing = 18
    content_height = (len(controls) + 1) * (box_height + box_spacing) - box_spacing  # +1 for notation button
    natural_height = 140 + content_height + 24
    window_height = screen.get_height()
    panel_height = min(window_height - panel_margin * 2, natural_height)
    panel_top = max(panel_margin, (window_height - panel_height) // 2)
    panel_rect = pygame.Rect(
        panel_margin,
        panel_top,
        screen.get_width() - panel_margin * 2,
        panel_height,
    )

    overlay = pygame.Surface(screen.get_size(), pygame.SRCALPHA)
    overlay.fill((0, 0, 0, 190))
    screen.blit(overlay, (0, 0))

    pygame.draw.rect(screen, (32, 32, 32), panel_rect, border_radius=20)
    pygame.draw.rect(screen, (90, 90, 90), panel_rect, 2, border_radius=20)

    header_rect = pygame.Rect(panel_rect.x, panel_rect.y, panel_rect.width, 64)
    pygame.draw.rect(screen, (24, 24, 24), header_rect, border_radius=20)
    pygame.draw.rect(screen, (70, 70, 70), header_rect, 1, border_radius=20)

    title = FONT.render("Settings", True, (245, 245, 245))
    screen.blit(title, (panel_rect.x + 22, panel_rect.y + 18))

    close_text = FONT.render("ESC to close", True, (160, 160, 160))
    screen.blit(close_text, (panel_rect.right - close_text.get_width() - 22, panel_rect.y + 20))

    divider_y = panel_rect.y + 70
    pygame.draw.line(screen, (70, 70, 70), (panel_rect.x + 20, divider_y), (panel_rect.right - 20, divider_y), 2)

    instructions = FONT.render("Click a control box and press a key to change that binding.", True, (200, 200, 200))
    screen.blit(instructions, (panel_rect.x + 22, panel_rect.y + 80))

    section_label = FONT.render("CONTROLS", True, (180, 180, 180))
    screen.blit(section_label, (panel_rect.x + 22, panel_rect.y + 110))
    
    notation = load_notation_preference()

    notation_rect = pygame.Rect(
        panel_rect.x + 24,
        panel_rect.y + 140,
        panel_rect.width - 48,
        52,
    )

    pygame.draw.rect(screen, (48, 48, 48), notation_rect, border_radius=14)
    pygame.draw.rect(screen, (92, 92, 92), notation_rect, 2, border_radius=14)

    notation_label = FONT.render("Number Format", True, (235, 235, 235))
    screen.blit(notation_label, (notation_rect.x + 16, notation_rect.y + 15))

    notation_value = FONT.render(notation.upper(), True, (210, 210, 210))
    screen.blit(
        notation_value,
        (notation_rect.right - notation_value.get_width() - 16, notation_rect.y + 15)
    )

    content_top = panel_rect.y + 210
    visible_height = panel_rect.height - (content_top - panel_rect.y) - 20
    max_scroll = max(0, content_height - visible_height)
    scroll_offset = max(0, min(scroll_offset, max_scroll))

    content_clip = pygame.Rect(
        panel_rect.x + 20,
        content_top,
        panel_rect.width - 40,
        max(0, visible_height),
    )

    clip_rect = screen.get_clip()
    screen.set_clip(content_clip)
    settings_boxes = []

    for index, (label, key_value) in enumerate(controls):
        box_y = content_top + index * (box_height + box_spacing) - scroll_offset
        box_rect = pygame.Rect(
            panel_rect.x + 24,
            box_y,
            panel_rect.width - 48,
            box_height,
        )

        pygame.draw.rect(screen, (48, 48, 48), box_rect, border_radius=14)
        pygame.draw.rect(screen, (92, 92, 92), box_rect, 2, border_radius=14)

        is_active = label == active_label
        if is_active:
            pygame.draw.rect(screen, (75, 135, 220), box_rect, 3, border_radius=14)

        label_surface = FONT.render(label, True, (235, 235, 235))
        screen.blit(label_surface, (box_rect.x + 16, box_rect.y + 15))

        if is_active:
            prompt_surface = FONT.render("Press a key to rebind", True, (160, 160, 160))
            screen.blit(prompt_surface, (box_rect.right - prompt_surface.get_width() - 16, box_rect.y + 15))
        else:
            key_surface = FONT.render(format_key(key_value), True, (210, 210, 210))
            screen.blit(key_surface, (box_rect.right - key_surface.get_width() - 16, box_rect.y + 15))

        settings_boxes.append((box_rect, label))

    # ✅ notation button added here, same as draw_game_settings_panel
    notation_button_y = content_top + len(controls) * (box_height + box_spacing) - scroll_offset
    notation_button_rect = pygame.Rect(
        panel_rect.x + 24,
        notation_button_y,
        panel_rect.width - 48,
        box_height,
    )
    pygame.draw.rect(screen, (48, 48, 48), notation_button_rect, border_radius=14)
    pygame.draw.rect(screen, (92, 92, 92), notation_button_rect, 2, border_radius=14)

    notation_label = FONT.render("Number Notation", True, (235, 235, 235))
    screen.blit(notation_label, (notation_button_rect.x + 16, notation_button_rect.y + 15))

    settings_boxes.append((notation_button_rect, "notation"))

    screen.set_clip(clip_rect)
    return panel_rect, settings_boxes, max_scroll


def get_settings_layout(screen, controls, scroll_offset):
    panel_margin = 70
    box_height = 52
    box_spacing = 18

    content_height = len(controls) * (box_height + box_spacing) - box_spacing
    natural_height = 140 + content_height + 24
    window_height = screen.get_height()
    panel_height = min(window_height - panel_margin * 2, natural_height)
    panel_top = max(panel_margin, (window_height - panel_height) // 2)

    panel_rect = pygame.Rect(
        panel_margin,
        panel_top,
        screen.get_width() - panel_margin * 2,
        panel_height,
    )

    content_top = panel_rect.y + 210
    visible_height = panel_rect.height - (content_top - panel_rect.y) - 20
    max_scroll = max(0, content_height - visible_height)
    scroll_offset = max(0, min(scroll_offset, max_scroll))
    settings_boxes = []

    for index, (label, key_value) in enumerate(controls):
        box_y = content_top + index * (box_height + box_spacing) - scroll_offset
        box_rect = pygame.Rect(
            panel_rect.x + 24,
            box_y,
            panel_rect.width - 48,
            box_height,
        )
        settings_boxes.append((box_rect, label))

    return panel_rect, settings_boxes, max_scroll


def draw_game_settings_panel(screen, controls, active_index, scroll_offset):
    FONT = get_font()
    panel_margin = 50
    box_height = 44
    box_spacing = 14
    content_height = len(controls) * (box_height + box_spacing) - box_spacing
    natural_height = 100 + content_height + 24
    window_height = screen.get_height()
    panel_height = min(window_height - panel_margin * 2, natural_height)
    panel_top = max(panel_margin, (window_height - panel_height) // 2)
    panel_rect = pygame.Rect(
        panel_margin,
        panel_top,
        screen.get_width() - panel_margin * 2,
        panel_height,
    )
    
    overlay = pygame.Surface(screen.get_size(), pygame.SRCALPHA)
    overlay.fill((0, 0, 0, 190))
    screen.blit(overlay, (0, 0))

    pygame.draw.rect(screen, (32, 32, 32), panel_rect, border_radius=20)
    pygame.draw.rect(screen, (90, 90, 90), panel_rect, 2, border_radius=20)

    header_rect = pygame.Rect(panel_rect.x, panel_rect.y, panel_rect.width, 50)
    pygame.draw.rect(screen, (24, 24, 24), header_rect, border_radius=20)
    pygame.draw.rect(screen, (70, 70, 70), header_rect, 1, border_radius=20)

    title = FONT.render("Settings", True, (245, 245, 245))
    screen.blit(title, (panel_rect.x + 18, panel_rect.y + 14))

    close_text = pygame.font.SysFont(None, 20).render("ESC", True, (160, 160, 160))
    screen.blit(close_text, (panel_rect.right - close_text.get_width() - 18, panel_rect.y + 16))

    content_top = panel_rect.y + 60
    visible_height = panel_rect.height - (content_top - panel_rect.y) - 20
    max_scroll = max(0, content_height - visible_height)
    scroll_offset = max(0, min(scroll_offset, max_scroll))

    content_clip = pygame.Rect(
        panel_rect.x + 20,
        content_top,
        panel_rect.width - 40,
        max(0, visible_height),
    )
    
    clip_rect = screen.get_clip()
    screen.set_clip(content_clip)
    settings_boxes = []

    for index, (action, key_code) in enumerate(list(controls.items())):
        box_y = content_top + index * (box_height + box_spacing) - scroll_offset
        box_rect = pygame.Rect(
            panel_rect.x + 20,
            box_y,
            panel_rect.width - 40,
            box_height,

        )
        pygame.draw.rect(screen, (48, 48, 48), box_rect, border_radius=12)
        pygame.draw.rect(screen, (92, 92, 92), box_rect, 2, border_radius=12)
        if index == active_index:
            pygame.draw.rect(screen, (75, 135, 220), box_rect, 3, border_radius=12)

        label_surface = FONT.render(action, True, (235, 235, 235))
        screen.blit(label_surface, (box_rect.x + 12, box_rect.y + 10))

        if index == active_index:
            prompt_surface = pygame.font.SysFont(None, 18).render("Press a key", True, (160, 160, 160))
            screen.blit(prompt_surface, (box_rect.right - prompt_surface.get_width() - 12, box_rect.y + 10))
        else:
            key_surface = FONT.render(format_key(key_code), True, (210, 210, 210))
            screen.blit(key_surface, (box_rect.right - key_surface.get_width() - 12, box_rect.y + 10))

        settings_boxes.append((box_rect, index))

    notation_button_y = content_top + len(controls) * (box_height + box_spacing) - scroll_offset
    notation_button_rect = pygame.Rect(
        panel_rect.x + 20,
        notation_button_y,
        panel_rect.width - 40,
        box_height,
    )
    pygame.draw.rect(screen, (48, 48, 48), notation_button_rect, border_radius=12)
    pygame.draw.rect(screen, (92, 92, 92), notation_button_rect, 2, border_radius=12)

    label = FONT.render("Number Notation", True, (235, 235, 235))
    screen.blit(label, (notation_button_rect.x + 12, notation_button_rect.y + 10))

    settings_boxes.append((notation_button_rect, "notation"))
    screen.set_clip(clip_rect)
    return panel_rect, settings_boxes, max_scroll


def get_game_settings_layout(screen, controls, scroll_offset):
    panel_margin = 50
    box_height = 44
    box_spacing = 14
    content_height = len(controls) * (box_height + box_spacing) - box_spacing
    natural_height = 100 + content_height + 24
    window_height = screen.get_height()
    panel_height = min(window_height - panel_margin * 2, natural_height)
    panel_top = max(panel_margin, (window_height - panel_height) // 2)

    panel_rect = pygame.Rect(
        panel_margin,
        panel_top,
        screen.get_width() - panel_margin * 2,
        panel_height,
    )

    content_top = panel_rect.y + 60
    visible_height = panel_rect.height - (content_top - panel_rect.y) - 20
    max_scroll = max(0, content_height - visible_height)
    scroll_offset = max(0, min(scroll_offset, max_scroll))

    settings_boxes = []
    for index, (action, key_code) in enumerate(list(controls.items())):
        box_y = content_top + index * (box_height + box_spacing) - scroll_offset
        box_rect = pygame.Rect(
            panel_rect.x + 20,
            box_y,
            panel_rect.width - 40,
            box_height,
        )

        settings_boxes.append((box_rect, action))

    return panel_rect, settings_boxes, max_scroll


# --------- NOTATION SETTINGS ---------

NOTATION_OPTIONS = ["standard", "comma", "abbreviated", "written"]


def draw_notation_panel(screen, active_index, scroll_offset):
    """Draw the notation selection panel"""
    FONT = get_font()
    panel_margin = 50
    box_height = 70
    box_spacing = 14
    content_height = len(NOTATION_OPTIONS) * (box_height + box_spacing) - box_spacing
    natural_height = 120 + content_height + 24
    window_height = screen.get_height()
    panel_height = min(window_height - panel_margin * 2, natural_height)
    panel_top = max(panel_margin, (window_height - panel_height) // 2)

    panel_rect = pygame.Rect(
        panel_margin,
        panel_top,
        screen.get_width() - panel_margin * 2,
        panel_height,
    )
    
    overlay = pygame.Surface(screen.get_size(), pygame.SRCALPHA)
    overlay.fill((0, 0, 0, 190))
    screen.blit(overlay, (0, 0))

    pygame.draw.rect(screen, (32, 32, 32), panel_rect, border_radius=20)
    pygame.draw.rect(screen, (90, 90, 90), panel_rect, 2, border_radius=20)

    header_rect = pygame.Rect(panel_rect.x, panel_rect.y, panel_rect.width, 50)
    pygame.draw.rect(screen, (24, 24, 24), header_rect, border_radius=20)
    pygame.draw.rect(screen, (70, 70, 70), header_rect, 1, border_radius=20)

    title = FONT.render("Number Notation Format", True, (245, 245, 245))
    screen.blit(title, (panel_rect.x + 18, panel_rect.y + 14))

    close_text = pygame.font.SysFont(None, 20).render("ESC", True, (160, 160, 160))
    screen.blit(close_text, (panel_rect.right - close_text.get_width() - 18, panel_rect.y + 16))

    content_top = panel_rect.y + 60
    visible_height = panel_rect.height - (content_top - panel_rect.y) - 20
    max_scroll = max(0, content_height - visible_height)
    scroll_offset = max(0, min(scroll_offset, max_scroll))

    content_clip = pygame.Rect(
        panel_rect.x + 20,
        content_top,
        panel_rect.width - 40,
        max(0, visible_height),
    )
    
    clip_rect = screen.get_clip()
    screen.set_clip(content_clip)
    notation_boxes = []

    for index, notation in enumerate(NOTATION_OPTIONS):
        box_y = content_top + index * (box_height + box_spacing) - scroll_offset
        box_rect = pygame.Rect(
            panel_rect.x + 20,
            box_y,
            panel_rect.width - 40,
            box_height,
        )
        
        pygame.draw.rect(screen, (48, 48, 48), box_rect, border_radius=12)
        pygame.draw.rect(screen, (92, 92, 92), box_rect, 2, border_radius=12)
        
        if index == active_index:
            pygame.draw.rect(screen, (75, 135, 220), box_rect, 3, border_radius=12)

        notation_info = NOTATION_TYPES[notation]
        name_surface = FONT.render(notation_info["name"], True, (235, 235, 235))
        screen.blit(name_surface, (box_rect.x + 12, box_rect.y + 5))
        
        example_surface = pygame.font.SysFont(None, 18).render(f"Example: {notation_info['example']}", True, (180, 180, 180))
        screen.blit(example_surface, (box_rect.x + 12, box_rect.y + 30))

        notation_boxes.append((box_rect, index))

    screen.set_clip(clip_rect)
    return panel_rect, notation_boxes, max_scroll


def get_notation_layout(screen, scroll_offset):
    """Get the layout for notation selection without drawing"""
    panel_margin = 50
    box_height = 70
    box_spacing = 14
    content_height = len(NOTATION_OPTIONS) * (box_height + box_spacing) - box_spacing
    natural_height = 120 + content_height + 24
    window_height = screen.get_height()
    panel_height = min(window_height - panel_margin * 2, natural_height)
    panel_top = max(panel_margin, (window_height - panel_height) // 2)

    panel_rect = pygame.Rect(
        panel_margin,
        panel_top,
        screen.get_width() - panel_margin * 2,
        panel_height,
    )

    content_top = panel_rect.y + 60
    visible_height = panel_rect.height - (content_top - panel_rect.y) - 20
    max_scroll = max(0, content_height - visible_height)
    scroll_offset = max(0, min(scroll_offset, max_scroll))

    notation_boxes = []
    for index, notation in enumerate(NOTATION_OPTIONS):
        box_y = content_top + index * (box_height + box_spacing) - scroll_offset
        box_rect = pygame.Rect(
            panel_rect.x + 20,
            box_y,
            panel_rect.width - 40,
            box_height,
        )
        notation_boxes.append((box_rect, index))

    return panel_rect, notation_boxes, max_scroll