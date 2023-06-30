#Banking Transaction API
The Banking Transaction API is an API used for banking transactions such as inter-customer transfers, transaction searches, and more. This API allows users to access banking services over HTTP.

##Features
This API provides several key features:

- Inter-customer transfers
- Transaction search by date
- Customer search by name, address, email, and status
- Pagination for search results
- Input validation to ensure valid and accurate data
##Technology Used
The technologies used in the development of this API include:

- Java as the main programming language
- Spring Boot Framework for building and managing the API
- Spring Data JPA for database access
- MySQL as the database
- Maven as the project management and dependency tool
- JSON as the data exchange format
- Docker and Docker Compose for containerization and deployment
##Running the Application with Docker
To run the application using Docker, follow these steps:

- Make sure you have Docker and Docker Compose installed on your computer.
- Open the docker-compose.yml file and configure the database settings under the mysql service. Set the DB_NAME, DB_USERNAME, and DB_PASSWORD according to your preferences.
- In the same docker-compose.yml file, configure the initial admin user under the myapp service. Set the FIRST_USER_NAME, FIRST_USER_EMAIL, and FIRST_USER_PASSWORD according to your preferences.
- Open a terminal or command prompt and navigate to the project's root directory.
- Run the command docker-compose up to start the application and the MySQL database container.
- Wait for the application to start successfully. You should see log messages indicating that the application has started.
- Once the application is running, you can access it at http://localhost:8080.


##API Endpoints
The Banking Transaction API provides various endpoints for banking transactions and user management. Here is a list of available APIs:

###1. Login

- Path: /api/v1/auth/login
- Method: POST
- Description: Used for user login by ADMIN, EMPLOYEE, and CUSTOMER.
###2. Get User By Id

- Path: /api/v1/admin/user/{userId}
- Method: GET
- Description: Retrieves user details by user ID. Only accessible to users with ADMIN or EMPLOYEE roles.
###3. Create User

- Path: /api/v1/admin/user
- Method: POST
- Description: Creates a new user with ADMIN or EMPLOYEE role. Only accessible to users with ADMIN role.
###4. Change Password

- Path: /api/v1/account/change-password
- Method: POST
- Description: Changes the default password for a user. All users are required to change their initial password.
###5. Get My Profile

- Path: /api/v1/account/my-profile
- Method: GET
- Description: Retrieves account details such as name, email, and role for users with ADMIN or EMPLOYEE roles.
###6. Create Customer

- Path: /api/v1/account/customer-register
- Method: POST
- Description: Creates a new customer account. Accessible to users with ADMIN or EMPLOYEE roles.
###7. Update Customer

- Path: /api/v1/account/customer-update/{accountNo}
- Method: PUT
- Description: Updates customer data such as name, address, and email. Accessible to users with ADMIN or EMPLOYEE roles.
###8. Delete Account

- Path: /api/v1/account/delete/{userId}
- Method: DELETE
- Description: Deactivates a user account. Only accessible to users with ADMIN role.
###9. Search User

- Path: /api/v1/admin/users
- Method: GET
- Description: Retrieves a list of users based on search parameters such as email, name, page, size, and sort. Accessible to users with ADMIN role.
###10. Search Customer

- Path: /api/v1/account/customer-search
- Method: GET
- Description: Searches for customer data based on parameters such as account number, address, email, name, page, size, and sort. Accessible to all users.
###11. Transaction Transfer

- Path: /api/v1/transaction/transfer
- Method: POST
- Description: Allows CUSTOMER to transfer funds from their account to another CUSTOMER account.
###12. Transaction History

- Path: /api/v1/transaction/history
- Method: GET
- Description: Retrieves transaction history for a CUSTOMER within a specified date range using the startDate and endDate query parameters.

For sample requests and responses, please refer to the provided Postman collection in the "Postman Collection" folder.

Note: Ensure that you have configured the necessary environment variables, such as database settings, before running the API.