CREATE TABLE files_storage
(
    id                BIGSERIAL PRIMARY KEY,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename   UUID         NOT NULL UNIQUE,
    content_type      VARCHAR(255) NOT NULL,
    file_type         VARCHAR(100) NOT NULL,
    file_path         TEXT         NOT NULL
);