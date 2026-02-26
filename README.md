# Expense Tracker - Vaadin Frontend

A modern Vaadin-based frontend for the Daily Expense Tracker application. This is a Java-based web UI that communicates with the Spring Boot backend REST API.

## Project Structure

```
frontend/
├── pom.xml                                      # Maven configuration
├── src/main/
│   ├── java/com/expensetracker/
│   │   ├── ExpenseTrackerFrontendApplication.java  # Spring Boot entry point
│   │   ├── service/
│   │   │   └── ApiClient.java                  # HTTP client for backend API
│   │   ├── util/
│   │   │   └── Logger.java                     # Structured logging utility
│   │   ├── dto/                                # Data Transfer Objects
│   │   │   ├── ExpenseResponse.java
│   │   │   ├── CreateExpenseRequest.java
│   │   │   ├── UpdateExpenseRequest.java
│   │   │   ├── ListExpensesResponse.java
│   │   │   ├── BatchCreateExpensesRequest.java
│   │   │   └── BatchCreateResponse.java
│   │   └── views/                              # Vaadin UI components
│   │       ├── MainView.java                   # Main application layout
│   │       └── ExpensesView.java               # Expenses management view
│   └── resources/
│       └── application.properties              # Application configuration
└── README.md
```

## Features

- **Calendar View**: Interactive calendar showing expenses for each day of the month
- **List View**: Tabular view of all expenses with sorting and filtering
- **Month/Year Picker**: Easy navigation between different months and years
- **Add Expenses**: Create new expenses with amount, category, and optional description
- **Edit Expenses**: Modify existing expense details
- **Delete Expenses**: Remove expenses with confirmation
- **Monthly Summary**: Display total spending for the selected month
- **REST API Integration**: Seamless communication with Spring Boot backend

## Technology Stack

- **Framework**: Vaadin 24.2.0
- **Backend Integration**: Spring WebClient
- **Build Tool**: Maven
- **Java Version**: 17+
- **Server Port**: 8081

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Spring Boot backend running on port 8080
- MySQL database (configured in backend)

## Installation & Setup

### 1. Build the Project

```bash
cd frontend
mvn clean install
```

### 2. Configure Backend URL

Edit `src/main/resources/application.properties`:

```properties
backend.api.url=http://localhost:8080/api
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The Vaadin frontend will start on **http://localhost:8081**

## API Integration

The frontend communicates with the backend via REST API endpoints:

| Operation | Endpoint | Method |
|-----------|----------|--------|
| Create Expense | `/api/expenses` | POST |
| Update Expense | `/api/expenses/{id}` | PUT |
| Delete Expense | `/api/expenses/{id}` | DELETE |
| List Expenses | `/api/expenses?year=2025&month=1` | GET |
| Batch Create | `/api/expenses/batch` | POST |

## Components

### MainView
The main application layout with sidebar navigation and header. Provides the overall structure for the application.

### ExpensesView
The primary view for managing expenses. Features:
- Month/Year picker for navigation
- Calendar and List tabs
- Expense grid with edit/delete actions
- Monthly total display
- Add Expense button

### ApiClient
Service class that handles all HTTP communication with the backend. Includes error handling and logging.

### Logger
Utility class for structured logging with different severity levels (info, warn, error, debug).

## Customization

### Styling
Vaadin uses the Lumo theme by default. Customize colors and styles in:
- `frontend/themes/my-theme/styles.css`
- Component-level styling with `addClassNames()`

### Adding New Views
1. Create a new class extending `VerticalLayout` or `HorizontalLayout`
2. Annotate with `@Route(value = "path", layout = MainView.class)`
3. Add navigation item in `MainView.createDrawer()`

### Extending API Client
Add new methods to `ApiClient` class for additional backend operations:

```java
public MyResponse myOperation(String param) {
    return webClient.get()
        .uri("/endpoint/{param}", param)
        .retrieve()
        .bodyToMono(MyResponse.class)
        .block();
}
```

## Troubleshooting

### Backend Connection Error
- Ensure backend is running on port 8080
- Check `backend.api.url` in application.properties
- Verify network connectivity

### Port Already in Use
Change the server port in `application.properties`:
```properties
server.port=8082
```

### Build Errors
Clear Maven cache and rebuild:
```bash
mvn clean install -U
```

## Development Workflow

1. **Start Backend**: `cd backend && mvn spring-boot:run`
2. **Start Frontend**: `cd frontend && mvn spring-boot:run`
3. **Access Application**: Open http://localhost:8081 in browser
4. **Make Changes**: Edit Java files or Vaadin components
5. **Reload**: Changes are automatically reloaded in development mode

## Performance Tips

- Use lazy loading for large expense lists
- Implement pagination in the grid
- Cache frequently accessed data
- Minimize API calls with batch operations

## Security Considerations

- Validate all user inputs on the frontend
- Use HTTPS in production
- Implement authentication/authorization
- Never expose sensitive data in logs
- Use parameterized queries for database operations

## License

MIT License - See LICENSE file for details

## Support

For issues or questions, refer to the main project README or contact the development team.
# Expense-Tracker-Vaadin-Backend
