# Architecture
Hexagonal architecture (ports and adaptors) implemented following domain driven design paradigm using a relational in-memory database

# Testing
Unit and integration testing development

# Implemented endpoints
GET /wallets/{walletId}
- It gets all specific wallet information related.

POST /wallets/{walletId}/recharge
- It executes a monetary recharge in the specified wallet
- Json data input example:
```
{
  "creditCardNumber": "1",
  "amount": {
    "value": 15,
    "currency": "EUR"
  }
}