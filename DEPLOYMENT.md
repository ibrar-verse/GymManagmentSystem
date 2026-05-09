Java Spring Boot Gym Management System - Railway.app Deployment Guide

## Critical Fixes Applied ✅

1. **Port Configuration**: Now uses `${PORT:8080}` - Railway sets the PORT env var dynamically
2. **Database Crash Prevention**: DataInitializer now has error handling - app won't crash if DB isn't ready on startup
3. **Admin Credentials**: Now uses environment variables instead of hardcoded values

## Prerequisites
- Java 17 LTS or higher  
- PostgreSQL database (Railway provides via `$DATABASE_URL`)

## Environment Variables REQUIRED in Railway Dashboard

| Variable | Value | Example |
|----------|-------|---------|
| `SPRING_PROFILES_ACTIVE` | `prod` | prod |
| `DATABASE_URL` | PostgreSQL connection string | `postgresql://user:pass@localhost:5432/gymboy` |
| `ADMIN_USERNAME` | Admin login username | `ibiverse` |
| `ADMIN_PASSWORD` | Admin login password | `SecurePassword123!` |

**Optional**:
- `DB_USER`: Postgres username (default: postgres)
- `DB_PASSWORD`: Postgres password (if separate from DATABASE_URL)
- `PORT`: Set automatically by Railway (default: 8080)

## Critical Features & What Works

✅ Staff management with photo uploads (filesystem-based in production)
✅ Member check-in system with facility tracking
✅ Admin dashboard with authentication  
✅ Member dashboard
✅ Graceful startup - app won't crash if database takes time to initialize

## Database

- **Production**: PostgreSQL (automatic schema creation/updates via Hibernate)
- **Development**: H2 in-memory (seed data on startup)
- **Schema Management**: `spring.jpa.hibernate.ddl-auto=update` in production - automatically creates/updates tables

## Deployment Steps (Railway.app)

1. **Push to GitHub**: Ensure all code is committed and pushed
2. **Create Railway Project**: New Project → GitHub → Select this repository
3. **Add PostgreSQL Service**: 
   - In Railway, click "+ New" → Database → PostgreSQL
   - This automatically sets `DATABASE_URL` environment variable
4. **Set Required Environment Variables** in Railway dashboard:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `ADMIN_USERNAME=<your-admin-username>`
   - `ADMIN_PASSWORD=<your-admin-password>`
5. **Deploy**: Railway automatically detects Procfile and deploys

## Build Command

```bash
mvn clean package
```

## Procfile Entry Point

```
web: java -Dspring.profiles.active=prod -jar target/*.jar
```

## Troubleshooting Railway Deployments

### "Request ID" errors / "Application failed to respond"
- Check Railway logs for `BeanCreationException` or `Failed to execute CommandLineRunner`
- This usually means database connection failed during startup
- **Solution**: Ensure `DATABASE_URL` is set in Railway dashboard and PostgreSQL service is running

### Build fails with "Failed to delete test-classes"
- This is a Windows file lock issue in local testing, not a deployment issue
- Clean up: Delete `target/` folder locally before rebuilding

### Photos don't show after upload
- Production uses `{user.dir}/uploads/staff/` for storage
- **Note**: In container environments, uploaded files will be lost on restart (temporary storage)
- For persistent storage, consider: S3, MinIO, or volume mounts

### Logs show "Failed to seed database"
- This is normal on first startup - app continues anyway (DataInitializer has error handling)
- Seed data will be added once tables are created
