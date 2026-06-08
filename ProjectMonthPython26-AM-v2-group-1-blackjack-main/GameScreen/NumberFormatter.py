"""
Number formatting and parsing utilities for displaying and parsing coin amounts.
"""

def format_number(value, notation="standard"):
    """
    Format a number based on the notation style.
    
    Args:
        value: The integer value to format
        notation: One of "standard", "comma", "abbreviated", "written"
                 - "standard": 100000
                 - "comma": 100,000
                 - "abbreviated": 100K
                 - "written": One Hundred Thousand
    
    Returns:
        The formatted string
"""
    if not isinstance(value, int) or value < 0:
        return str(value)
    
    if notation == "standard":
        return str(value)
    
    elif notation == "comma":
        return f"{value:,}"
    
    elif notation == "abbreviated":
        if value >= 1_000_000:
            return f"{value / 1_000_000:.0f}M"
        elif value >= 1_000:
            return f"{value / 1_000:.0f}K"
        else:
            return str(value)
    
    elif notation == "written":
        return _number_to_words(value)
    
    return str(value)


def _number_to_words(n):
    """Convert a number to words (e.g., 100000 -> 'One Hundred Thousand')"""
    ones = ["", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"]
    teens = ["Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
             "Sixteen", "Seventeen", "Eighteen", "Nineteen"]
    tens = ["", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"]
    scales = ["", "Thousand", "Million", "Billion", "Trillion"]
    
    if n == 0:
        return "Zero"
    
    def convert_hundreds(num):
        result = ""
        
        # Hundreds place
        if num >= 100:
            result += ones[num // 100] + " Hundred"
            num %= 100
            if num > 0:
                result += " "
        
        # Tens and ones
        if num >= 20:
            result += tens[num // 10]
            if num % 10 > 0:
                result += " " + ones[num % 10]
        elif num > 0:
            result += teens[num - 10] if num >= 10 else ones[num]
        
        return result.strip()
    
    # Break number into groups of three
    groups = []
    while n > 0:
        groups.append(n % 1000)
        n //= 1000
    
    # Convert each group
    result_parts = []
    for i, group in enumerate(reversed(groups)):
        if group > 0:
            part = convert_hundreds(group)
            if i > 0:
                part += " " + scales[i]
            result_parts.append(part)
    
    return " ".join(result_parts)


def parse_bet_input(user_input):
    """
    Parse user input for bet amounts.
    Supports formats like: "50000", "50k", "50K", "50 thousand", "fifty thousand"
    
    Args:
        user_input: The string input from the user
    
    Returns:
        The parsed integer amount, or None if parsing fails
    """
    user_input = user_input.strip().lower()
    
    if not user_input:
        return None
    
    # Try parsing as a simple number first
    try:
        return int(user_input)
    except ValueError:
        pass
    
    # Handle "K" suffix (e.g., "50k" -> 50000)
    if user_input.endswith("k"):
        try:
            return int(float(user_input[:-1]) * 1000)
        except ValueError:
            pass
    
    # Handle "M" suffix (e.g., "2m" -> 2000000)
    if user_input.endswith("m"):
        try:
            return int(float(user_input[:-1]) * 1_000_000)
        except ValueError:
            pass
    
    # Handle written numbers (e.g., "fifty thousand", "fifty k")
    word_map = {
        "zero": 0, "one": 1, "two": 2, "three": 3, "four": 4,
        "five": 5, "six": 6, "seven": 7, "eight": 8, "nine": 9,
        "ten": 10, "eleven": 11, "twelve": 12, "thirteen": 13,
        "fourteen": 14, "fifteen": 15, "sixteen": 16, "seventeen": 17,
        "eighteen": 18, "nineteen": 19, "twenty": 20, "thirty": 30,
        "forty": 40, "fifty": 50, "sixty": 60, "seventy": 70,
        "eighty": 80, "ninety": 90, "hundred": 100, "thousand": 1000,
        "million": 1_000_000, "billion": 1_000_000_000
    }
    
    words = user_input.split()
    current = 0
    result = 0
    
    for word in words:
        if word in word_map:
            num = word_map[word]
            if num >= 1000:
                current *= num
                result += current
                current = 0
            else:
                current += num
        else:
            return None
    
    result += current
    
    if result > 0:
        return result
    
    return None


# Display notation info for settings menu
NOTATION_TYPES = {
    "standard": {
        "name": "Standard",
        "description": "Standard Format",
        "example": "100,000"
    },
    "comma": {
        "name": "Standard with Commas",
        "description": "Standard with Commas",
        "example": "100,000"
    },
    "abbreviated": {
        "name": "Abbreviated",
        "description": "Abbreviated Format",
        "example": "100K"
    },
    "written": {
        "name": "Written",
        "description": "Written Out",
        "example": "One Hundred Thousand"
    }
}
