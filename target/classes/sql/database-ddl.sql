-- DDL H2 (MEMORY) PARA BASE RECUPERATORIO

-- =========================================================
-- SECUENCIAS
-- =========================================================

-- Crear secuencias
CREATE SEQUENCE IF NOT EXISTS SEQ_ARTIST_ID INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_ALBUM_ID INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_GENRE_ID INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_MEDIA_TYPE_ID INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_TRACK_ID INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_CUSTOMER_ID INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_EMPLOYEE_ID INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_INVOICE_ID INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_INVOICE_LINE_ID INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_PLAYLIST_ID INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SEQ_PLAYLIST_TRACK_ID INCREMENT BY 1 START WITH 1;

-- =========================================================
-- TABLA ARTISTS
-- =========================================================

-- Crear tabla ARTISTS
CREATE TABLE IF NOT EXISTS artists (ARTIST_ID INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_ARTIST_ID, name VARCHAR(120), PRIMARY KEY (ARTIST_ID));

-- =========================================================
-- TABLA GENRES
-- =========================================================

-- Crear tabla GENRES
CREATE TABLE IF NOT EXISTS genres (GENRE_ID INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_GENRE_ID, name VARCHAR(25), PRIMARY KEY (GENRE_ID));

-- =========================================================
-- TABLA MEDIA_TYPES
-- =========================================================

-- Crear tabla MEDIA_TYPES
CREATE TABLE IF NOT EXISTS media_types (MEDIA_TYPE_ID INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_MEDIA_TYPE_ID, name VARCHAR(120), PRIMARY KEY (MEDIA_TYPE_ID));

-- =========================================================
-- TABLA ALBUMS
-- =========================================================

-- Crear tabla ALBUMS
CREATE TABLE IF NOT EXISTS albums (ALBUM_ID INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_ALBUM_ID, artist_id INTEGER, title VARCHAR(160), PRIMARY KEY (ALBUM_ID), FOREIGN KEY (artist_id) REFERENCES artists (ARTIST_ID));

-- =========================================================
-- TABLA TRACKS
-- =========================================================

-- Crear tabla TRACKS
CREATE TABLE IF NOT EXISTS tracks (track_id INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_TRACK_ID, album_id INTEGER, bytes BIGINT, composer VARCHAR(220), genre_id INTEGER, media_type_id INTEGER, milliseconds INTEGER, name VARCHAR(200), unit_price DOUBLE PRECISION, PRIMARY KEY (track_id), FOREIGN KEY (album_id) REFERENCES albums (ALBUM_ID), FOREIGN KEY (genre_id) REFERENCES genres (GENRE_ID), FOREIGN KEY (media_type_id) REFERENCES media_types (MEDIA_TYPE_ID));

-- =========================================================
-- TABLA EMPLOYEES
-- =========================================================

-- Crear tabla EMPLOYEES
CREATE TABLE IF NOT EXISTS employees (EMPLOYEE_ID INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_EMPLOYEE_ID, address VARCHAR(70), birth_date DATE, city VARCHAR(40), country VARCHAR(40), email VARCHAR(60), fax VARCHAR(24), first_name VARCHAR(40), hire_date DATE, last_name VARCHAR(20), phone VARCHAR(24), postal_code VARCHAR(10), reports_to INTEGER, state VARCHAR(40), title VARCHAR(30), PRIMARY KEY (EMPLOYEE_ID), FOREIGN KEY (reports_to) REFERENCES employees (EMPLOYEE_ID));

-- =========================================================
-- TABLA CUSTOMERS
-- =========================================================

-- Crear tabla CUSTOMERS
CREATE TABLE IF NOT EXISTS customers (CUSTOMER_ID INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_CUSTOMER_ID, address VARCHAR(70), city VARCHAR(40), company VARCHAR(80), country VARCHAR(40), email VARCHAR(60), fax VARCHAR(24), first_name VARCHAR(40), last_name VARCHAR(20), phone VARCHAR(24), postal_code VARCHAR(10), state VARCHAR(40), support_rep_id INTEGER, PRIMARY KEY (CUSTOMER_ID), FOREIGN KEY (support_rep_id) REFERENCES employees (EMPLOYEE_ID));

-- =========================================================
-- TABLA INVOICES
-- =========================================================

-- Crear tabla INVOICES
CREATE TABLE IF NOT EXISTS invoices (INVOICE_ID INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_INVOICE_ID, billing_address VARCHAR(70), billing_city VARCHAR(40), billing_country VARCHAR(40), billing_postal_code VARCHAR(10), billing_state VARCHAR(40), customer_id INTEGER, invoice_date DATE, total DOUBLE PRECISION, PRIMARY KEY (INVOICE_ID), FOREIGN KEY (customer_id) REFERENCES customers (CUSTOMER_ID));

-- =========================================================
-- TABLA INVOICE_LINES
-- =========================================================

-- Crear tabla INVOICE_LINES
CREATE TABLE IF NOT EXISTS invoice_lines (INVOICE_LINE_ID INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_INVOICE_LINE_ID, invoice_id INTEGER, quantity INTEGER, track_id INTEGER, unit_price DOUBLE PRECISION, PRIMARY KEY (INVOICE_LINE_ID), FOREIGN KEY (invoice_id) REFERENCES invoices (INVOICE_ID), FOREIGN KEY (track_id) REFERENCES tracks (track_id));

-- =========================================================
-- TABLA PLAYLISTS
-- =========================================================

-- Crear tabla PLAYLISTS
CREATE TABLE IF NOT EXISTS playlists (PLAYLIST_ID INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_PLAYLIST_ID, name VARCHAR(120), PRIMARY KEY (PLAYLIST_ID));

-- =========================================================
-- TABLA PLAYLIST_TRACK
-- =========================================================

-- Crear tabla PLAYLIST_TRACK
CREATE TABLE IF NOT EXISTS playlist_track (PLAYLIST_TRACK_ID INTEGER NOT NULL DEFAULT NEXT VALUE FOR SEQ_PLAYLIST_TRACK_ID, playlist_id INTEGER, track_id INTEGER, PRIMARY KEY (PLAYLIST_TRACK_ID), FOREIGN KEY (playlist_id) REFERENCES playlists (PLAYLIST_ID), FOREIGN KEY (track_id) REFERENCES tracks (track_id));

-- =========================================================
-- ÍNDICES
-- =========================================================

-- Crear índices
CREATE INDEX IF NOT EXISTS IFK_PLAYLIST_TRACK_PLAYLIST_ID ON playlist_track (playlist_id);
CREATE INDEX IF NOT EXISTS IFK_PLAYLIST_TRACK_TRACK_ID ON playlist_track (track_id);
CREATE INDEX IF NOT EXISTS IFK_TRACKS_ALBUM_ID ON tracks (album_id);
CREATE INDEX IF NOT EXISTS IFK_TRACKS_GENRE_ID ON tracks (genre_id);
CREATE INDEX IF NOT EXISTS IFK_TRACKS_MEDIA_TYPE_ID ON tracks (media_type_id);
CREATE INDEX IF NOT EXISTS IFK_ALBUMS_ARTIST_ID ON albums (artist_id);
CREATE INDEX IF NOT EXISTS IFK_INVOICES_CUSTOMER_ID ON invoices (customer_id);
CREATE INDEX IF NOT EXISTS IFK_INVOICE_LINES_INVOICE_ID ON invoice_lines (invoice_id);
CREATE INDEX IF NOT EXISTS IFK_INVOICE_LINES_TRACK_ID ON invoice_lines (track_id);
CREATE INDEX IF NOT EXISTS IFK_CUSTOMERS_EMPLOYEE_ID ON customers (support_rep_id);
CREATE INDEX IF NOT EXISTS IFK_EMPLOYEES_REPORTS_TO ON employees (reports_to);