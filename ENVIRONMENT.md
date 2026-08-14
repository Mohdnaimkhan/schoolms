# Production configuration

Required environment variables:

- `DB_URL` (example: `jdbc:mysql://localhost:3306/school_db`)
- `DB_USERNAME` (example: `school_app`)
- `DB_PASSWORD`
- `INITIAL_ADMIN_PASSWORD` (recommended; otherwise a temporary random password is generated on first startup)

Optional:
- `SERVER_ADDRESS`
- `SERVER_PORT`
- `DDL_AUTO` (use `validate` in production)
- `JPA_SHOW_SQL`
- `THYMELEAF_CACHE`
- `MYSQLDUMP_COMMAND`
- `BACKUP_DIRECTORY`

The backup feature now expects `mysqldump` to be installed on the server.
