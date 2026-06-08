import random
import pygame
import os
from CardLogic.Card import Cards
from Sound.Sounds import Sounds



class GameLogic:
    Suits = ['Clubs', 'Diamonds', 'Hearts', 'Spades']
    Ranks = ['Ace', '2', '3', '4',
            '5', '6', '7', '8',
            '9', '10', 'Jack', 'Queen', 'King']
    turn = 0
    deck = []
    playerHand = []
    dealerHand = []


    player_turn = True
    playerTotal = 0
    dealerTotal = 0
    bet_amount = 0
    coins = 100
    turn = 0

    sounds = Sounds()
    last_result = None  # Track win/lose/tie for UI display

    def __init__(self):
        self.deck = []
        self.build_deck()
        # whether to render the dealer's first card facedown
        self.show_facedown = False
        self.dealer_reveal_timer = 0  # seconds remaining before dealer acts
        self.dealer_waiting = False

    # -------- Deck -------- #
    def build_deck(self):
        for rank in self.Ranks:
            for suit in self.Suits:
                self.deck.append(Cards(suit, rank))
                print("deck built")

        random.shuffle(self.deck)
        print("deck shuffled")

    # -------- Hand Value -------- #
    def hand_value(self, hand):
        total = 0
        aces = 0

        for card in hand:
            card : Cards
            total += card.value
            if card.get_rank().lower() == 'ace':
                aces += 1

        while total > 21 and aces > 0:
            total -= 10
            aces -= 1
        if total == 21:
            print("BLACKJACK")
            if hand == self.playerHand:
                self.sounds.winner_sound()
        return total
    
    # -------- Deal -------- #
    def deal(self):
        #removes two cards from deck, removes another two from deck, BUT playerhand and dealer hand becomes the cards removed
        self.playerHand = [self.deck.pop(), self.deck.pop()]
        self.dealerHand = [self.deck.pop(), self.deck.pop()]

        #iterates through each cards and add its total.
        self.playerTotal = self.hand_value(self.playerHand)
        self.dealerTotal = self.hand_value(self.dealerHand)
        self.sounds.shuffle_sound()
        # when a new hand is dealt, show the dealer's facedown card
        self.show_facedown = True
        #card_shuffle.play()
        print(self.playerHand,self.playerTotal)
          
    def next_turn(self):
        
        #resets bet amount to 0
        self.bet_amount = 0
        self.turn += 1

        #addes all the card from player and dealer hadn to deck
        self.deck.extend(self.playerHand)
        self.deck.extend(self.dealerHand)
        
        # Clear the hands for the next round
        self.playerHand = []
        self.dealerHand = []
        self.playerTotal = 0
        self.dealerTotal = 0
        
        random.shuffle(self.deck)
        print("turn: ",self.turn)
        #when it is players turn, deal hand, print coins
        self.player_turn = True
        # hide facedown until next deal (new bet/start)
        self.show_facedown = False
        print("coins: ",self.coins)
        return
    # -------- Player -------- #
    def hit_p(self):
        #adds first card in deck to hand and removes it from library.
        card = self.deck.pop()
        self.playerHand.append(card)
        #finds new hand value, with added card
        
        self.playerTotal = self.hand_value(self.playerHand)
        print(self.playerHand, self.playerTotal)
        self.sounds.take_card_sound()
        self.is_bust()

    def stand_p(self):
        self.player_turn = False

    def dd_p(self):
        self.hit_p()
        self.player_turn = False

    def all_in_p(self):
        # Bet all remaining coins
        remaining_coins = self.coins
        if remaining_coins > 0:
            self.bet(remaining_coins)
        # Hit one card
        self.hit_p()
        self.player_turn = False

    def can_hit(self):
        if not self.player_turn:
            return False

        if self.is_bust():
            return False

        if not self.can_click():
            return False

        return True
    # -------- Dealer -------- #
    def hit_d(self):
        self.dealerHand.append(self.deck.pop())
        self.dealerTotal = self.hand_value(self.dealerHand)
        self.sounds.take_card_sound()
        print(self.dealerHand, self.dealerTotal)

    def dealerPlay(self):
        #if it's players turn, quit
        if self.player_turn == True:
            return
        #finds dealers hand value
        self.dealerTotal = self.hand_value(self.dealerHand)

        if self.dealerTotal > 21:
            return

        # has_ace should fix the issue of the dealer not hitting to 17 if the dealer had an ace with the vaule of 1.
        has_ace = any(card.get_rank() == 'Ace' for card in self.dealerHand)
        if self.dealerTotal < 17 or (self.dealerTotal == 17 and has_ace):
            print("DEALER HITS")
            self.hit_d()
            print(self.dealerTotal)
        else:
            print("STOP")
            print(self.dealerTotal)


    def is_bust(self):
        if self.playerTotal > 21:

            self.stand_p()
            return True
        return False

    def bet(self, amount):

        if not isinstance(amount, int):
            self.sounds.error_sound()
            return False

        if amount <= 0:
            self.sounds.error_sound()
            return False

        if amount > self.coins:
            self.sounds.error_sound()
            return False

        self.bet_amount += amount
        self.coins -= amount
        self.sounds.bet_sound()

        return True

    def has_placedBet(self):
        if self.bet_amount > 0:
            return True
        
        return False

    # -------- Winner -------- #

    def winnerCheck(self):
        bet_gain = 0
        #checks if player scores more than 32
        if self.playerTotal > 21:
            print("dealer wins(via bust)")
            self.last_result = "lose"
            self.next_turn()
            self.sounds.lose_sound()
            return
        if self.dealerTotal > 21:
            print("player wins(via bust)")
            bet_gain += self.bet_amount * 2
            print("won:", bet_gain)
            self.coins += bet_gain
            bet_gain = 0
            self.sounds.winner_sound()
            self.last_result = "win"
            self.next_turn()
            return
        if self.dealerTotal > self.playerTotal:
            print("DEALER WINS")
            self.last_result = "lose"
            self.next_turn()
            self.sounds.lose_sound()
            return
        elif self.dealerTotal < self.playerTotal:
            print("PLAYER WINS ")
            bet_gain += self.bet_amount * 2
            print("won:", bet_gain)
            self.coins += bet_gain
            self.sounds.winner_sound()
            bet_gain = 0
            self.last_result = "win"
            self.next_turn()
            return
        else:
            print('TIE')
            self.coins += self.bet_amount
            self.last_result = "tie"
            self.next_turn()
            return
        
    def can_click(self):
        return True
