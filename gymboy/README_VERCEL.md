Vercel Deployment Notes

This project is a Spring Boot application and is not a native fit for Vercel's serverless platform. The repository includes a Dockerfile and vercel.json that attempt to deploy the app as a container using the community Docker builder.

Steps to deploy on Vercel (may require a paid plan):
1. Push this branch to GitHub.
2. In Vercel, "Import Project" from GitHub and select this repo.
3. In Vercel project settings -> Environment Variables, add:
   - `PORT` (optional — Vercel provides one)
   - `SPRING_PROFILES_ACTIVE=prod`
   - `DATABASE_URL` (Postgres) and related DB vars
   - `ADMIN_USERNAME`, `ADMIN_PASSWORD`
4. Vercel will use the `Dockerfile` via `vercel.json` using `@vercel/docker` builder.

Limitations & Notes:
- Vercel is optimized for serverless functions and static sites. Containers may not be supported on free plans or may have lifecycle restrictions.
- Uploaded files stored in `uploads/` inside the container are ephemeral — use external storage for persistence.
- If Vercel rejects the Docker builder, use Railway, Render, Fly, or Heroku instead.
