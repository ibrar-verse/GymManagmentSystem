Java Spring Boot Gym Management System

## Deployment on Railway.app

### Prerequisites
- Java 17+
- PostgreSQL database (Railway will provide via $DATABASE_URL)

### Environment Variables Required
```
DATABASE_URL=postgresql://user:password@host:port/dbname
DB_USER=postgres
DB_PASSWORD=your_password
PORT=8080 (set automatically by Railway)
SPRING_PROFILES_ACTIVE=prod
```

### Build Command
```bash
mvn clean package
```

### Runtime
Railway will automatically detect the `Procfile` and deploy using the configuration there.

### Features
- Staff management with photo uploads
- Member check-in system
- Facility and class management  
- Admin dashboard
- Member dashboard

### Database
- Uses PostgreSQL in production
- Migrations handled automatically via Hibernate (ddl-auto=update)
- H2 used for local development only

### Deployment Steps
1. Push code to GitHub
2. Connect repository to Railway.app
3. Set environment variables in Railway dashboard
4. Railway will automatically build and deploy
