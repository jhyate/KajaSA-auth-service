-- =====================================================
-- KajaSA Auth Service
-- Migration: V1__create_auth_schema.sql
-- Description: Initial authentication database schema
-- =====================================================

-- =====================================================
-- 1. USERS
-- =====================================================

CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(254) NOT NULL UNIQUE,
                       password_hash VARCHAR(512) NOT NULL,
                       status VARCHAR(30) NOT NULL
                           CHECK (status IN ('PENDING_VERIFICATION', 'ACTIVE', 'BLOCKED', 'DISABLED')),
                       email_verified_at TIMESTAMPTZ,
                       created_at TIMESTAMPTZ NOT NULL,
                       updated_at TIMESTAMPTZ NOT NULL
);

-- =====================================================
-- 2. EMAIL_CODES
-- =====================================================

CREATE TABLE email_codes (
                             id UUID PRIMARY KEY,
                             user_id UUID NOT NULL UNIQUE,
                             code_hmac VARCHAR(64) NOT NULL,
                             attempts INTEGER NOT NULL CHECK (attempts >= 0),
                             max_attempts INTEGER NOT NULL CHECK (max_attempts > 0),
                             status VARCHAR(30) NOT NULL
                                 CHECK (status IN ('ACTIVE', 'CONSUMED', 'EXPIRED', 'INVALIDATED')),
                             expires_at TIMESTAMPTZ NOT NULL,
                             created_at TIMESTAMPTZ NOT NULL,
                             updated_at TIMESTAMPTZ NOT NULL,

                             CONSTRAINT fk_email_codes_user
                                 FOREIGN KEY (user_id)
                                     REFERENCES users (id)
                                     ON DELETE RESTRICT
);

-- =====================================================
-- 3. PASSWORD_RESETS
-- =====================================================

CREATE TABLE password_resets (
                                 id UUID PRIMARY KEY,
                                 user_id UUID NOT NULL UNIQUE,
                                 token_hash VARCHAR(64) NOT NULL,
                                 attempts INTEGER NOT NULL CHECK (attempts >= 0),
                                 max_attempts INTEGER NOT NULL CHECK (max_attempts > 0),
                                 status VARCHAR(30) NOT NULL
                                     CHECK (status IN ('ACTIVE', 'USED', 'EXPIRED', 'INVALIDATED')),
                                 expires_at TIMESTAMPTZ NOT NULL,
                                 created_at TIMESTAMPTZ NOT NULL,
                                 updated_at TIMESTAMPTZ NOT NULL,

                                 CONSTRAINT fk_password_resets_user
                                     FOREIGN KEY (user_id)
                                         REFERENCES users (id)
                                         ON DELETE RESTRICT
);


-- =====================================================
-- 4. SESSIONS
-- =====================================================

CREATE TABLE sessions (
                          id UUID PRIMARY KEY,
                          user_id UUID NOT NULL,
                          session_token_hash VARCHAR(64) NOT NULL UNIQUE,
                          status VARCHAR(30) NOT NULL
                              CHECK (status IN ('ACTIVE', 'EXPIRED', 'REVOKED')),
                          expires_at TIMESTAMPTZ NOT NULL,
                          created_at TIMESTAMPTZ NOT NULL,
                          updated_at TIMESTAMPTZ NOT NULL,

                          CONSTRAINT fk_sessions_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users (id)
                                  ON DELETE RESTRICT
);

CREATE INDEX idx_sessions_user_id
    ON sessions (user_id);


-- =====================================================
-- 5. TOKEN_FAMILIES
-- =====================================================

CREATE TABLE token_families (
                                id UUID PRIMARY KEY,
                                session_id UUID NOT NULL UNIQUE,
                                status VARCHAR(30) NOT NULL
                                    CHECK (status IN ('ACTIVE', 'REVOKED', 'COMPROMISED', 'EXPIRED')),
                                expires_at TIMESTAMPTZ NOT NULL,
                                created_at TIMESTAMPTZ NOT NULL,
                                updated_at TIMESTAMPTZ NOT NULL,

                                CONSTRAINT fk_token_families_session
                                    FOREIGN KEY (session_id)
                                        REFERENCES sessions (id)
                                        ON DELETE RESTRICT
);

-- =====================================================
-- 6. REFRESH_TOKENS
-- =====================================================

CREATE TABLE refresh_tokens (
                                id UUID PRIMARY KEY,
                                family_id UUID NOT NULL,
                                token_hash VARCHAR(64) NOT NULL UNIQUE,
                                replaced_by_id UUID UNIQUE,
                                status VARCHAR(30) NOT NULL
                                    CHECK (status IN ('ACTIVE', 'USED', 'REVOKED', 'EXPIRED')),
                                expires_at TIMESTAMPTZ NOT NULL,
                                created_at TIMESTAMPTZ NOT NULL,
                                updated_at TIMESTAMPTZ NOT NULL,

                                CONSTRAINT fk_refresh_tokens_family
                                    FOREIGN KEY (family_id)
                                        REFERENCES token_families (id)
                                        ON DELETE RESTRICT,

                                CONSTRAINT fk_refresh_tokens_replaced_by
                                    FOREIGN KEY (replaced_by_id)
                                        REFERENCES refresh_tokens (id)
                                        ON DELETE RESTRICT
);

CREATE INDEX idx_refresh_tokens_family_id
    ON refresh_tokens (family_id);

-- =====================================================
-- 7. RATE_LIMIT_COUNTERS
-- =====================================================

CREATE TABLE rate_limit_counters (
                                     id UUID PRIMARY KEY,
                                     user_id UUID NOT NULL,
                                     action VARCHAR(40) NOT NULL
                                         CHECK (action IN ('EMAIL_CODE_REQUEST', 'PASSWORD_RESET_REQUEST')),
                                     window_started_at TIMESTAMPTZ NOT NULL,
                                     request_count INTEGER NOT NULL CHECK (request_count >= 0),
                                     last_request_at TIMESTAMPTZ NOT NULL,
                                     created_at TIMESTAMPTZ NOT NULL,
                                     updated_at TIMESTAMPTZ NOT NULL,

                                     CONSTRAINT uq_rate_limit_user_action
                                         UNIQUE (user_id, action),

                                     CONSTRAINT fk_rate_limit_counters_user
                                         FOREIGN KEY (user_id)
                                             REFERENCES users (id)
                                             ON DELETE RESTRICT
);
