-- create WALLET table
CREATE TABLE WALLET(
    WALLET_ID VARCHAR(254) NOT NULL COMMENT 'Unique wallet identifier',
    BALANCE_VALUE NUMBER(10,2) NOT NULL COMMENT 'Account balance value',
    BALANCE_CURRENCY VARCHAR(3) NOT NULL COMMENT 'Account balance currency',
    CREATED_AT TIMESTAMP NOT NULL COMMENT 'Created date',
    UPDATED_AT TIMESTAMP NOT NULL COMMENT 'Last updated date',
    PRIMARY KEY(WALLET_ID)
);