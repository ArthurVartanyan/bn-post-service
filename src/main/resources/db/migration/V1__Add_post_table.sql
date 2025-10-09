CREATE TABLE posts
(
    id         BIGSERIAL PRIMARY KEY,
    company_id BIGINT       NOT NULL,
    content    TEXT,
    image_path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_deleted BOOLEAN
);
