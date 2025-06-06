# Din Tai Fung Cash Register System

The Din Tai Fung Cash Register System is a Java-based application designed to streamline restaurant transactions, including order processing, payment handling, and receipt generation. The system features a secure authentication system where users can log in with a username and password (limited to three attempts) or register as new users with validated credentials—usernames must be 5-15 alphanumeric characters, while passwords require 8-20 characters with at least one uppercase letter and one number. A default admin account (`admin`/`Admin123`) is provided for initial access.  

Once logged in, users can manage orders through an intuitive cash register interface. The system displays a menu of available products with prices, allowing staff to add, modify, or remove items from the cart, adjust quantities, and apply discounts for senior citizens (20%), PWD customers (20%), or employees (15%). During checkout, multiple payment methods are supported, including cash, credit/debit cards (Visa, Mastercard, JCB), QR code payments (GCash, Maya, ShopeePay), and online banking (BDO, BPI, Metrobank, Unionbank). Detailed receipts are generated, listing items, subtotals, discounts, VAT (12%), payment methods, and change due.  

Administrators can manage the restaurant menu by adding new products, editing existing items (name or price), or removing products (with confirmation). All transactions are logged in `transactions.txt`, recording order numbers, timestamps, cashier details, purchased items, and total amounts for accountability and reporting.  

The application uses ArrayLists to store user credentials, product menus, and shopping cart data, with robust input validation for numeric entries, usernames, passwords, and payment details. Error handling ensures smooth operation even with unexpected inputs.  

To use the system, launch the Java application, authenticate as an existing user or register a new account, and navigate the main menu to process orders. The checkout flow guides users through discount applications, payment processing, and receipt printing.
