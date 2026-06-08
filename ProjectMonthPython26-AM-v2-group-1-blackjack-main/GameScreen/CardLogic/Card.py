import pygame
import os

class Images:

    file_path = os.path.dirname(__file__)
    assets = os.path.join(file_path, "..", "..", "BlackJackImgs")
    spades = os.path.join(assets, "Spades")
    clubs = os.path.join(assets, "Clubs")
    diamonds = os.path.join(assets, "Diamonds")
    hearts = os.path.join(assets, "Hearts")

    suit_folders = {
        "Spades":   os.path.join(assets, "Spades"),
        "Clubs":    os.path.join(assets, "Clubs"),
        "Diamonds": os.path.join(assets, "Diamonds"),
        "Hearts":   os.path.join(assets, "Hearts"),
    }

    _cache = {}  # Avoid reloading images every time

    @classmethod
    def load_all(cls):
        if cls._cache:
            return cls._cache  # Already loaded, reuse
        for suit, folder in cls.suit_folders.items():
            cls._cache[suit] = {}
            for filename in os.listdir(folder):
                if filename.lower().endswith((".png", ".jpg", ".jpeg")):
                    name = os.path.splitext(filename)[0].lower().split("_")[0].split("of")[0]   # e.g. "ace", "10", "king"
                    full_path = os.path.join(folder, filename)
                    cls._cache[suit][name] = full_path
        return cls._cache


    @classmethod
    def get(cls, suit, rank):
        """Look up a surface by suit and rank. Loads all images if not yet cached."""
        all_images = cls.load_all()
        suit_images = all_images.get(suit, {})
        return suit_images.get(str(rank).lower())  # Returns None if not found

        


class Cards(pygame.Surface):

    rank_values = {
            'Ace': 11,
            '2': 2, '3': 3, '4': 4,
            '5': 5, '6': 6, '7': 7,
            '8': 8, '9': 9, '10': 10,
            'Jack': 10, 'Queen': 10, 'King': 10
        }
    def __init__(self, suit, rank):
        self.suit = suit
        self.rank = rank
        self.value = self.find_value()  # returns the value of the above input if 'Ace' then self.value = 11
      

        
        image_path = Images.get(suit, rank)
        if image_path:
            try:
                self.file = pygame.image.load(image_path)
                self.image = pygame.transform.scale_by(self.file, 0.05)
            except Exception:
                self.file = None
                self.image = pygame.Surface((100, 150))
                self.image.fill((100, 100, 100))
        else:
            self.file = None
            self.image = pygame.Surface((100, 150))
            self.image.fill((100, 100, 100))

        self.rect = self.image.get_rect()
        self.surface = self.image
        self.original_rect = self.rect.copy()
        super().__init__((self.rect.w, self.rect.h))


    
    def __repr__(self):
        return f"{self.rank} of {self.suit}"
    
    def resize(self, width, height):
        self.image = pygame.transform.scale(self.file, (int(width), int(height)))
        self.surface = self.image
        self.rect = self.image.get_rect()
        self.original_rect = self.rect.copy()
  
    def find_value(self):
        return self.rank_values.get(self.rank)
    
    def set_suit(self, newSuit):
        self.suit = newSuit

    def set_rank(self, newRank):
        self.rank = newRank

    def set_image(self, newImage):
        self.image = newImage

    def get_rank(self):
        return self.rank
    
    def get_suit(self):
        return self.suit
    
    def get_height(self):
        return self.rect.height
    
    def get_width(self):
        return self.rect.width
    
    def is_hovered(self, pos):
        if self.original_rect.collidepoint(pos):   
            print("original: ", self.original_rect)   
                                 
            self.surface = pygame.transform.scale_by(self.image, 1.1)           
            self.rect = self.surface.get_rect(center=self.original_rect.center) 
            print("new", self.rect)
        else:
            self.surface = self.image                           
            self.rect = self.original_rect.copy()               
    
    def is_clicked(self, event, pos):
        if self.rect.collidepoint(pos) and event.type == pygame.MOUSEBUTTONDOWN: 
            print("AAAAAAH")
    
    def draw(self, screen: pygame.Surface, pos: tuple[int, int]):
        self.original_rect.topleft = pos       
        screen.blit(self.surface, pos)  

class SpecialCards(Cards):
    pass

 
