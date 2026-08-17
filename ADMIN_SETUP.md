# First Administrator Setup

The application no longer creates a hard-coded `admin/admin123` account.

## Fresh installation

1. Configure the database using `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.
2. Start the application.
3. Open `/setup`.
4. Create the first administrator username and password (minimum 8 characters).
5. After creation, `/setup` redirects to `/login`.
6. `/setup` is automatically unavailable once any ADMIN account exists.

If users exist but there is no ADMIN yet, the setup page remains available so an administrator can be created.

Do not expose `/setup` to the public internet beyond the initial installation window; once an ADMIN exists it redirects to login.
