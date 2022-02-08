# Arquitectura
Arquitectura hexagonal (puertos y adaptadores) implementada siguiendo un paradigma Domain Driven Design con una base de datos relacional en memoria.

#Testing
Implementadas pruebas unitarias y de integración con base de datos.

# Puntos de api implementados
GET /wallets/{walletId}
- Obtiene toda la información relativa a un monedero

POST wallet/{walletId}/recharge
- LLeva a cabo una recarga monetaria en el monedero asociado. 
- Ejemplo de json de entrada para la llamada rest de recarga de monedero:
``` 
{
  "creditCardNumber": "1",
  "amount": {
    "value": 15,
    "currency": "EUR"
  }
}


