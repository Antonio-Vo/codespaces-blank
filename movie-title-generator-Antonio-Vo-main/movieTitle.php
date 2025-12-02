<?php
/*
  Name: Antonio Thomas
  Date: 5/9/2025
*/

/*
Ridiculous Movie Title & Summary Generator (Starter Code)
- Use user input, random values, arrays, conditionals, and logic
- Build each part of the generator in mini-challenges
- Add comments as you go. Comments count for 10% of your grade!!
*/

$movie = [
    "hero" => "Unknown",
    "tone" => "unspecified",
    "traits" => [],
    "genre" => "Undefined",
    "setting" => "Nowhere",
    "twist" => "Nothing happens",
    "sidekick" => "no one",
    "title" => "Untitled"
];

// 🧠 Get User Input: ask the user to enter their hero's name and assign their reply to the "hero" variable
    // For testing purposes, output the users selection
/************************ ADD YOUR CODE BELOW  *****************************/
$hero = readline("Hero's name: ");

/************************ ADD YOUR CODE ABOVE  *****************************/
// 🧠 Get User Input: ask the user to enter a tone for the movie. Provide three possible options. I used "epic, silly, and dark".
    // Other options could be "quirky", "inspirational", "mysterious", "melodramatic", "retro", or make up your own. Just keep in mind you 
    // will also need to write a description of the film based on the tone.
    // For testing purposes, output the users selection
/************************ ADD YOUR CODE BELOW  *****************************/
// This asks they user to input one of three tones: funny, gloomy, or calm. If the user input is invalid, it defaults to "Mysterious". I tried to make it loop for invaid inputs but I couldn't get it to work.
$tone = readline("Pick a tone:\n 1.funny\n 2.gloomy\n 3.calm\n ");
    if($tone == 1 || $tone == "funny" || $tone == "Funny" ){
        $tone = "Funny";
    } elseif($tone == 2 || $tone == "gloomy" || $tone == "Gloomy" ) {
        $tone = "Gloomy";
    } elseif ($tone == 3 || $tone == "calm" || $tone == "Calm" ){
        $tone = "Calm";
    } else {
        echo "your input was invailded. selcting secrect option: Mysterious";
        $tone = "Mysterious";
    }
echo "$tone .....\n";


/************************ ADD YOUR CODE ABOVE  *****************************/
// 🧪 Mini-Challenge 1: Roll for Traits
/* 
    Basic: Ask user to hit enter to trigger the rollTraits() function for each trait.
    The function should determine a random value for the level of Action, Romance, and Weirdness the film includes
    Return the values in an associative array with the trait as the key/
*/
/*
    Challenge: Use themed ranges. Base your trait ranges on numbers that relate to the trait. For example, my output asking the user to roll for Action says:
        echo "\nThe action movie genre spans wildly different subgenres — from espionage thrillers to sci-fi blockbusters.";
        echo "\nThere were 6 James Bond films starring the best Bond (Sean Connery, of course).";
        echo "\nMeanwhile, a DeLorean had to hit 88 mph to send Marty McFly *Back to the Future*.";
        readline("\nHit Enter to roll your movie's Action Level (between 6 and 88): ");
*/
// For testing purposes, output the traits and the selected values in a descriptive string
function rollTraits() {
/************************ ADD YOUR CODE BELOW  *****************************/
readline("Hit enter to select trait levels for action, romance, and weirdness...\n");

    $traits = [
        "Action" => rand(1, 100), 
        "Romance" => rand(1, 100), 
        "Weirdness" => rand(1, 100) 
    ];

    foreach ($traits as $key => $num) {
        echo "{$key}: {$num}\n";
    }
        return $traits;
    }

/************************ ADD YOUR CODE ABOVE  *****************************/

$movie["traits"] = rollTraits();

// 🧪 Mini-Challenge 2: Assign Genre Based on Traits
/*  
    Fill in the elseif conditions with logic based on Action, Romance, Weirdness values
    The if and the else are provided. Fill in at least 3 elseif options that return genre based on the traits
    I used "Rom-Com", "Absurdist Horror", and "Slice of Life Documentary". You are free to select other genres.
*/
// For testing purposes, output the selected genre in a descriptive string
function assignGenre($traits) {
    $action = $traits["Action"];
    $romance = $traits["Romance"];
    $weirdness = $traits["Weirdness"];

    if ($action > 80) {
        return "Action";
    }
/************************ ADD YOUR CODE BELOW  *****************************/
elseif ($action > 70 && $weirdness > 70) {
    return "buddy cop movie";
    echo "111111";
} elseif ($weirdness > 90){
    return "comedy";
    echo "222222";
} elseif ($action > 50 && $romance > 70 && $weirdness > 70) {
return "romantic comedy";
echo "333333";
/************************ ADD YOUR CODE ABOVE  *****************************/
} else {
        return "Experimental Indie Film";
    }
};
$movie["genre"] = assignGenre($movie["traits"]);
// 🧪 Mini-Challenge 3: Choose a Setting
/*
    Create an indexed array with at least three settings for your movie
    Display the options using a loop and ask the user to select one
    I used "Space Diner", "Underwater Castle", "Mini Golf Course" but you are free to select your own settings
    Besure to provide a default in case the user selects a value outside the ones provided
*/
// For testing purposes, output the selected setting in a descriptive string
function chooseSetting() {
/************************ ADD YOUR CODE BELOW  *****************************/
$settings = {"space station", "water city", "Void Creek"}

/************************ ADD YOUR CODE ABOVE  *****************************/
}
$movie["setting"] = chooseSetting();

// 🧪 Mini-Challenge 4: Add a Plot Twist
/*
    Select a twist from a list of at least 5 options
    This can be random or based on some condition
    I used "Aliens crash the party", "Everyone is a ghost", "The villain is a chicken","A musical number saves the day", 
        and "Everything was just a dream... or was it?" but you are free to create your own plot twist 
*/
// For testing purposes, output the selected plot twist in a descriptive string
function getPlotTwist() {
/************************ ADD YOUR CODE BELOW  *****************************/


/************************ ADD YOUR CODE ABOVE  *****************************/
}
$movie["twist"] = getPlotTwist();

// 🧪 Mini-Challenge 5: Choose a Sidekick
/*
    Match a sidekick to the movie's genre using a switch statement
    Include at least 5 specific pairings and a default
    I used "a former rival turned ally" for "Action", "best friend from 3rd grade" for "Rom-Com", 
        "a talking taxidermy owl" for "Absurdist Horror", 
        "their overly helpful neighbor who always carries snacks" for "Slice of Life Documentary", 
        and "a silent mime who communicates only through interpretive dance" for "Experimental Indie Film".
        (eventhough I used AI for these, you may not!)
*/
// For testing purposes, output the selected sidekick in a descriptive string
function getSidekick($genre) {
/************************ ADD YOUR CODE BELOW  *****************************/


/************************ ADD YOUR CODE ABOVE  *****************************/
}
$movie["sidekick"] = getSidekick($movie["genre"]);

// 🧪 Mini-Challenge 6: Generate a Title
/*
    Generate a ridiculous movie title by combining two random words with genre and setting
    Create two arrays: one with at least 5 adjectives and one with at least 5 nouns. 
        I used "Unbelievable", "Mysterious", "Explosive", "Romantic", and "Weird" for adjectives and 
        "Revenge", "Adventure", "Love Story", "Conspiracy", and "Saga" for nouns but you are free to select your own words
    The variable names for the arrays should be "adjectives" and "nouns".
    Randomly select one adjective and one noun and assign them to variables named adjective and noun.
*/
// For testing purposes, output has been provided for you
function generateTitle($genre, $setting, $traits) {
/************************ ADD YOUR CODE BELOW  *****************************/


/************************ ADD YOUR CODE ABOVE  *****************************/
echo "The {$adj} {$noun} of {$genre} in the {$setting}";
return "The {$adj} {$noun} of {$genre} in the {$setting}";
}
$movie["title"] = generateTitle($movie["genre"], $movie["setting"], $movie["traits"]);

// 🧪 Mini-Challenge 7: Print Full Summary
/*
    Display the final movie summary based on tone
    Include details like title, hero, traits, genre, setting, twist, and sidekick
    Use if/elseif or switch to display based on tone (epic, silly, dark, or default fallback)

    My summary for an epic movie looks like this but your are free to be as creative as you would like:
        echo "🔥 In a tale of grand destiny, {$movie['hero']} must brave the {$movie['setting']} with {$movie['sidekick']} by their side.";
        echo "\nThis {$movie['genre']} features Action: {$movie['traits']['Action']}, Romance: {$movie['traits']['Romance']}, Weirdness: {$movie['traits']['Weirdness']}.";
        echo "\nBut beware... {$movie['twist']}\n"; 
    You can also take a more straight forward aproach simply putting all of the collected information into a logical, readable string.
*/
function printSummary($movie) {
/************************ ADD YOUR CODE BELOW  *****************************/ 
    

/************************ ADD YOUR CODE ABOVE  *****************************/
}
printSummary($movie);


?>
