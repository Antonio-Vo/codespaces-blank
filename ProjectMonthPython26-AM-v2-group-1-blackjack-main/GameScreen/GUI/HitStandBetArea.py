import pygame
class HitStandBetArea(pygame.Surface):
    def __init__(self, x, y, width, height):
        super().__init__((width, height))
        self.x = x
        self.y = y
    
    def draw(self, screen):
