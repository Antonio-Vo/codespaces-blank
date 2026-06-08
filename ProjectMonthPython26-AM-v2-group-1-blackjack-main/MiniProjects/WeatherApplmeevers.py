import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import csv

class WeatherApp:
    csv_file = "WeatherStationData.csv"
    def __init__(self, csv_file):
        self.csv_file = csv_file
    #print csv file data

    def __ask_for_time(self) -> str:
        print("H:MM AM/PM")
        hour = input("Enter Hour: ")
        print(f"{hour}:MM AM/PM")
        minute = input("Enter Minute: ")
        print(f"{hour}:{minute} AM/PM")
        am_pm = input("Enter before midday or after midday: ")
        print(f"{hour}:{minute} {am_pm}")

        return f"{hour}:{minute} {am_pm}"

    def __ask_for_date(self):
        print("MM/DD/YYYY")
        month = input("Enter Month: ")
        print(f"{month}/DD/YYYY")
        day = input("Enter Day: ")
        print(f"{month}/{day}/YYYY")
        year = input("Enter Year: ")
        print(f"{month}/{day}/{year}")

        return f"{month}/{day}/{year}"
    def load_data(self):
        with open(self.csv_file, "r") as csv_file:
            for line in csv_file:
                station, location, date, time_start, time_end, highest_temp, lowest_temp, humidity, mph, weather = line.split(",")
                print(station, "\t", location, "\t", date, "\t", time_start, "\t", time_end, "\t", highest_temp, "\t", lowest_temp, "\t", humidity, "\t", mph, "\t", weather)

    #show data
    def visualize_data(self):
        pass

    #add a weather station with complete data
    #must validate user input
    #append data
    def add_weather_station(self):
        """Adds a new weather entry with validation."""
        print("\n--- Add New Weather Station Entry ---")
        station = input("Enter Station Name: ")
        location = input("Enter Location: ")
        weather = input("Enter Weather: ")
        date = self.__ask_for_date()
        time_start = self.__ask_for_time()
        time_end = self.__ask_for_time()

        # Validating numerical inputs
        try:
            highest_temp = float(input("Enter Highest Temperature: "))
            lowest_temp = float(input("Enter Lowest Temperature: "))
            humidity = float(input("Enter Humidity (%): "))
            mph = float(input("Enter Wind Speed (MPH): "))
        except ValueError as e:
            print(f"Invalid Numerical Input: {e}. Row not added.")
            return

        # Prepare new row
        new_row = [station, location, date, time_start, time_end,
                   highest_temp, lowest_temp, humidity, mph, weather]

        # Append data to CSV
        try:
            with open(self.csv_file, 'a', newline='') as file:
                file.seek(0, 2)  # go to end of file
                if file.tell() > 0:
                    file.write('\n')  # ensure we're on a new line
                writer = csv.writer(file)
                writer.writerow(new_row)
        except Exception as e:
            print(f"Error writing to file: {e}")


    #select a weather station to change
    #must validate input
    def update_weather_station(self):
        pass


app = WeatherApp("WeatherStationData.csv")
app.add_weather_station()
app.load_data()