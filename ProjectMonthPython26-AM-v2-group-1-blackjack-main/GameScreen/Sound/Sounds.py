import random
import pygame
import os




class Sounds:
    pygame.mixer.init()
    sound_folder = os.path.join(os.path.dirname(__file__), "..", "..", "BlackJackImgs", "Sounds")


    #WINNNIING SOUNDS
    win = pygame.mixer.Sound(os.path.join(sound_folder, "Win.mp3"))
    win2 = pygame.mixer.Sound(os.path.join(sound_folder, "Win2.mp3"))
    win3 = pygame.mixer.Sound(os.path.join(sound_folder, "Win3.mp3"))
    win4 = pygame.mixer.Sound(os.path.join(sound_folder, "Win4.mp3"))

    #crying sound for when player goes all in
    cry = pygame.mixer.Sound(os.path.join(sound_folder, "cry.mp3"))

    #LOSING SOUNDS
    lose = pygame.mixer.Sound(os.path.join(sound_folder, "Lose.wav"))
    lose2 = pygame.mixer.Sound(os.path.join(sound_folder, "Lose2.mp3"))
    lose3 = pygame.mixer.Sound(os.path.join(sound_folder, "Lose3.mp3"))

    #Tie sound
    tie = pygame.mixer.Sound(os.path.join(sound_folder, "tie.mp3"))
    #sound for when player MAKES a bet
    bet = pygame.mixer.Sound(os.path.join(sound_folder, "bet.mp3"))
    bet2 = pygame.mixer.Sound(os.path.join(sound_folder, "bet2.mp3"))
    bet3 = pygame.mixer.Sound(os.path.join(sound_folder, "bet3.mp3"))

    #for hitting and taking a card
    taking_card = pygame.mixer.Sound(os.path.join(sound_folder, "takingplaycard.mp3"))

    #sound for when player tries to do an invalid action, such as betting more than they have or entering a non-integer bet amount
    error = pygame.mixer.Sound(os.path.join(sound_folder, "Error.wav"))
    #at the start of ever turn, shuffle the deck and play the sound of shuffling cards  
    card_shuffle = pygame.mixer.Sound(os.path.join(sound_folder, "cardshuffle.mp3"))

    
    def winner_sound(self):
        win_sounds = [self.win, self.win2, self.win3, self.win4]
        sound = random.choice(win_sounds)
        sound.set_volume(0.2)  # Adjust the volume as needed (0.
        sound.play()
    
    def bet_sound(self):
        bet_sounds = [self.bet, self.bet2, self.bet3]
        sound = random.choice(bet_sounds)
        sound.set_volume(0.5)  # Adjust the volume as needed (0.0 to 1.0)
        sound.play()

    def lose_sound(self):
        lose_sounds = [self.lose, self.lose2, self.lose3]
        sound = random.choice(lose_sounds)
        sound.set_volume(0.5)  # Adjust the volume as needed (0.0 to 1.0)
        sound.play()
    
    def shuffle_sound(self):
        sound = self.card_shuffle
        sound.set_volume(0.5)
        sound.play()
    
    def error_sound(self):
        sound = self.error
        sound.set_volume(0.5)
        sound.play()
    
    def take_card_sound(self):
        sound = self.taking_card
        sound.set_volume(0.5)
        sound.play()
    
    def cry_sound(self):
        sound = self.cry
        sound.set_volume(0.5)
        sound.play()
    
    def tie_sound(self):
        sound = self.tie
        sound.set_volume(0.5)
        sound.play()

print(Sounds.sound_folder)