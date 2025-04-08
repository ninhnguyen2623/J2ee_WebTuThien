# SpringDonateWeb  

**SpringDonateWeb** is a web application built with Java Spring Boot to support donation programs. It allows users to view, donate, and manage programs effectively while ensuring secure transactions and user-friendly navigation.  

## Features  
- User authentication and Google OAuth2 login integration.  
- Program donation functionality with automatic updates to donation goals.  
- Role-based access control for managing programs and donations.  
- Dynamic "Donate Now" button that transforms into a dropdown for logged-in users.  
- Error handling with a custom error controller that serves HTML pages.  

## Tech Stack  
- **Backend**: Java Spring Boot 3  
- **Frontend**: Thymeleaf  
- **Database**: H2 (development), MySQL (production-ready)  
- **Authentication**: Google OAuth2  

## Requirements  
- Java 21  
- Maven 3.x  
- MySQL (or other preferred database for production)  

## Getting Started  

### Clone the Repository  
```bash  
git clone https://github.com/thanhtrongvo/SpringDonateWeb.git  
cd SpringDonateWeb  
