# Stocks Manager System

A Java-based stock management system designed for efficient real-time stock tracking and price updates.

## Overview

This project implements a custom stock management data structure that supports real-time stock initialization, updates, deletions, and range queries while maintaining efficient time complexities.

The system was developed as part of a data structures and algorithms programming assignment focused on designing optimized dynamic data structures without using Java built-in collection libraries.

## Features

- Add and initialize stocks
- Remove stocks and their historical data
- Real-time stock price updates
- Remove invalid timestamp updates
- Retrieve current stock prices
- Query stocks within a specific price range
- Return sorted stock IDs by price
- Efficient logarithmic-time operations

## Data Structures Used

- Binary Search Trees
- AVL Trees / Balanced Trees
- Doubly Linked Lists
- Custom Nodes and Keys
- Timestamp-based indexing

## Time Complexity

| Operation | Complexity |
|---|---|
| Add Stock | O(log N) |
| Remove Stock | O(log N) |
| Update Stock | O(log N + log M) |
| Get Stock Price | O(log N) |
| Remove Timestamp Update | O(log N + log M) |
| Price Range Queries | O(log N + K) |

Where:
- N = number of stocks
- M = number of updates for a specific stock
- K = number of returned stocks in range

## Technologies

- Java
- Object-Oriented Programming (OOP)
- Data Structures & Algorithms

## Project Structure

```text
src/
├── DoublyLinkedList.java
├── PriceKey.java
├── StockManager.java
├── StockNode.java
├── StockTree.java
├── StocksByPriceTree.java
├── TimeStampTree.java
└── TimeStampsNode.java
```

## Author

Sham Alem
