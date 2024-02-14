# Stock Predictor

## Overview
Coding Challenge meant to solve the prediction of the next 3 values of Stock price.

## Features
- Data Extraction: Selects 10 consecutive data points from CSV files starting from a random timestamp to ensure diverse data sampling.
- Predictive Analysis: Predicts the next 3 stock price values using the following logic:
- - n+1: Has the value of the 2nd highest price in the sampled data.
- - n+2: Is equal to n + half the spread between n and n+1.
- - n+3: Is equal to n+1 + a quarter of the spread between n+1 and n+2.

## Getting Started

### Prerequisites
- Maven using jdk-17.0.2

### Installation
1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/BATja20/Stock-Predictor.git
2. Build and compile the .jar file:
   ```bash
   mvn clean install
3. Run the created .jar file or just use the already created one:
   ```bash
   java -jar Stock-Price-Predictor-1.0-SNAPSHOT.jar <path_to_exchange_folder> <max_files_per_exchange>
   ```
   <path_to_exchange_folder>: Path to the directory containing your exchange-specific CSV files.
   <max_files_per_exchange>: Maximum number of CSV files to process per exchange.

## Output
The application generates a new CSV file for each processed file, containing both the sampled data and predicted stock prices.


    