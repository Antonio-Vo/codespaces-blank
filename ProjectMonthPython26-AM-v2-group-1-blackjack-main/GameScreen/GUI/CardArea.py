import pygame
import os
from CardLogic.deck import GameLogic
from CardLogic.Card import Cards

FACEDOWN_FILENAME = os.path.join(os.path.dirname(__file__), "..", "..", "BlackJackImgs", "Cardbacks", "FacedownCard1.png")



class CardArea(pygame.Surface):

    def __init__(self, screen: pygame.display, xpos : int, ypos : int, game : GameLogic):
        screen_w, screen_h = screen.get_size()
        self.screen = screen
        self.screen_w, self.screen_h = screen.get_size() 
        self.area_width = screen_w * 0.85
        self.game = game
        self.area_height = screen_h // 2
        self.xpos =  xpos
        self.ypos = ypos
       
        super().__init__((self.area_width, self.area_height), pygame.SRCALPHA)
    
 
    
    def __find_pos(self, cards : set[Cards]):
        #get length of list
        count = len(cards)
        if count == 0:
            return

        card_width = cards[0].get_width() 
        card_height = cards[0].get_height()
        total_width = card_width * count  #width of surface

        if total_width > self.area_width: 
            card_width = self.area_width // count #the card with is the surface width // amount of cards
            total_width = card_width * count #becomes new width

        start_x = (self.area_width - total_width) // 2   # pixel center the start is the surface width - the cardwith *count //2
        center_y = (self.area_height - card_height) // 2  # pixel center

        for i in range(count):
            yield (start_x + i * card_width, center_y)   # xpos is start_x + i *card width
    

    def draw_cards(self, cards: list[Cards]):
        self.fill((0, 0, 0, 0))                         # clear first
        count = len(cards)
        if count == 0:
            return

        # calculate target card size based on area
        card_h = int(self.area_height * 0.7)            # card height is 70 of ear height
        card_w = card_h                  #  card width is 100 ofgit push  card_h

        if card_w * count > self.area_width:            # squeeze if too many
            card_w = int(self.area_width // count) #card width is the are width // count, all cards width smaller than the surface
            card_h = card_w
          
           

        total_width = card_w * count
        start_x = int((self.area_width - total_width) // 2)
        center_y = int((self.area_height - card_h) // 2)

        positions = list(self.__find_pos(cards))
        for i, (card, pos) in enumerate(zip(cards, positions)):
            card: Cards
            card.resize(card_w, card_h)                 # resize before drawing

            # Dealer's first card facedown behavior tied to game.show_facedown
            if self.ypos == 0 and i == 0 and getattr(self.game, "show_facedown", False):
                try:
                    if not hasattr(self, "_facedown_orig"):
                        self._facedown_orig = pygame.image.load(FACEDOWN_FILENAME).convert_alpha()
                    facedown_img = pygame.transform.smoothscale(self._facedown_orig, (card_w, card_h))
                except Exception:
                    facedown_img = pygame.Surface((card_w, card_h))
                    facedown_img.fill((80, 80, 80))

                # save original surface once
                if not hasattr(card, "_saved_surface_for_facedown"):
                    card._saved_surface_for_facedown = card.surface

                # set facedown surface and draw
                card.surface = facedown_img
                card.original_rect.topleft = pos
                card.rect = card.original_rect.copy()
                card.draw(self, pos)
                # mark as currently facedown
                card._is_facedown = True
                continue

            # restore original surface if it was facedown previously
            if hasattr(card, "_is_facedown") and card._is_facedown:
                try:
                    if hasattr(card, "_saved_surface_for_facedown"):
                        card.surface = card._saved_surface_for_facedown
                        del card._saved_surface_for_facedown
                except Exception:
                    pass
                try:
                    del card._is_facedown
                except Exception:
                    pass

            card.draw(self, pos)
    

        

    def draw(self):
        self.screen.blit(self, (self.xpos * self.screen_w, self.ypos * self.screen_h))
        
    def reveal(self, card: Cards):
        card

