# Bank-Application-project

Bank Management System Project Description
This Java project implements a comprehensive bank management system that allows for multiple banks, account management, and banking operations.

1. Account Class
Core Entity: Represents a bank account with essential attributes:
Balance
Account holder name
Account number
Username
4-digit PIN

Functionality:
Deposit money (with validation for positive amounts)
Withdraw money (with balance check)
Transfer money to other accounts
Getter methods for all attributes

2. Bank Class
Core Management: Handles all operations for a specific bank
Key Features:
Maintains a collection of all accounts (using HashMap)
Static collection of all banks in the system
Account number generation (8-digit random numbers)
PIN validation (must be exactly 4 digits)

Bank Operations:
Account Registration:
Collects user details (name, username)
Sets a 4-digit PIN with validation
Generates unique account number
Creates and stores new account

Account Deletion:
Verifies account number and PIN
Removes account from the system

Display All Accounts:
Shows complete details of all accounts in the bank

Banking Operations:
Deposit/withdraw money
Check balance
Transfer money (both intra-bank and inter-bank)
Logout functionality

Money Transfer System:
Supports transfers within the same bank
Supports transfers to other registered banks
Validates recipient accounts
Ensures sufficient balance before transfer

3. TestBank Class (Main Class)
System Initialization:
Creates two banks ("My Bank" and "Friend Bank")
Provides menu to select which bank to operate on
Main loop for continuous operation until exit
Key Technical Aspects
Data Structures: Uses HashMap for efficient account lookup by account number
Input Validation: Robust handling of user input with try-catch blocks
Object-Oriented Design: Proper encapsulation with private fields and public methods
Error Handling: Comprehensive exception handling for input mismatches
Static Bank Registry: Maintains list of all banks for inter-bank transfers

Security Features
PIN protection for sensitive operations
Account number verification
Balance checks before withdrawals/transfers
User Experience
Clear menu-driven interface
Informative prompts and error messages
Consistent formatting for displayed information
