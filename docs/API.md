# REST API description

### API base url

`/api/v1/`


### Common errors

401:

Need to refresh token.

```json
{
  "error": 401,
  "message": "string"
}
```

403:

Bad token.

```json
{
  "error": 403,
  "message": "string"
}
```


### Endpoint `/me`

Authorized access

```shell
curl --request GET \
     --url https://localhost:8080/v1/me \
     --header 'Authorization: ' \
     --header 'Content-Type: application/json'
```

200:

Ok!

```json
{
  "id": "number",
  "full_name": {
    "first_name": "string",
    "last_name": "string",
    "author_name": "string"
  },
  "selected_currency": "'RUBLE' | 'DOLLAR' | 'EURO'",
  "email": "string",
  "role": "'USER' | 'ADMIN'"
}
```

### Endpoint `/me/currency` (PUT)

Authorized access

```shell
curl --request PUT \
     --url https://localhost:8080/v1/me/currency \
     --header 'Authorization: ' \
     --header 'Content-Type: application/json' \
     --data '{ "currency": "RUBLE | DOLLAR | EURO" }'
```

200:

Ok!

```json
{
  "id": "number"
}
```


### Endpoint `/products?p={page: 0}&s={count per page: 10}`

Authorized access

```shell
curl --request GET \
     --url https://localhost:8080/v1/products \
     --header 'Authorization: ' \
     --header 'Content-Type: application/json'
```

200:

Ok, but could be empty if no extra data!

```json
{
  "products": [
    {
      "id": "number",
      "title": "string",
      "description": "string",
      "amount": "number",
      "price": "number"
    }
  ]
}
```

### Endpoint `/products/{id}`

Authorized access

```shell
curl --request GET \
     --url https://localhost:8080/v1/products/1 \
     --header 'Authorization: ' \
     --header 'Content-Type: application/json'
```

200:

Ok!

```json
{
  "id": "number",
  "title": "string",
  "description": "string",
  "amount": "number",
  "price": "number"
}
```

404:

No such product

```json
{
  "error": 404,
  "message": "string"
}
```

### Endpoint `/products/` (PUSH)

Authorized access, ADMIN required

```shell
curl --request PUSH \
     --url https://localhost:8080/v1/products \
     --header 'Authorization: ' \
     --header 'Content-Type: application/json' \
     --data '{
  "title": "title",
  "description": "description",
  "amount": 100,
  "price": 100
}'
```

200:

```json
{
  "id": "number"
}
```

400:

Cannot perform adding

```json
{
  "error": 400,
  "message": "string"
}
```

### Endpoint `/products/{id}/amount` (PUT)

Authorized access, ADMIN required

```shell
curl --request PUT \
     --url https://localhost:8080/v1/products/1/amount \
     --header 'Authorization: ' \
     --header 'Content-Type: application/json' \
     --data '{"amount": -10 }'
```

200:

Ok!

```json
{
  "id": "number"
}
```

400:

Cannot perform update

```json
{
  "error": 400,
  "message": "string"
}
```


### Endpoint `/auth/register` (POST)

Public

```shell
curl --request POST \
     --url https://localhost:8080/v1/register \
     --header 'Content-Type: application/json' \
     --data '{
  "full_name": {
    "first_name": "string",
    "last_name": "string",
    "author_name": "string"
  },
  "selected_currency": "'RUBLE' | 'DOLLAR' | 'EURO'",
  "email": "string",
  "password": "string"
}'
```

200:

Ok!

```json
{
  "id": "number"
}
```

400:

Cannot perform registration

```json
{
  "error": 400,
  "message": "string"
}
```

### Endpoint `/auth/token` (POST)

Public

```shell
curl --request POST \
     --url https://localhost:8080/v1/token \
     --header 'Content-Type: application/json' \
     --data '{
  "email": "string",
  "password": "string"
}'
```

200:

Ok!

```json
{
  "token": "string"
}
```


### Endpoint `/users?p={page: 0}&s={count per page: 10}`

Authorized access (ADMIN)

```shell
curl --request GET \
     --url https://localhost:8080/v1/users \
     --header 'Authorization: ' \
     --header 'Content-Type: application/json'
```

200:

Ok!

```json
{
  "users": [
    {
      "id": "number",
      "full_name": {
        "first_name": "string",
        "last_name": "string",
        "author_name": "string"
      },
      "selected_currency": "'RUBLE' | 'DOLLAR' | 'EURO'",
      "email": "string",
      "role": "'USER' | 'ADMIN' | 'OWNER'"
    }
  ]
}
```

### Endpoint `/users/{id}`

Authorized access (ADMIN)

```shell
curl --request GET \
     --url https://localhost:8080/v1/users \
     --header 'Authorization: ' \
     --header 'Content-Type: application/json'
```

200:

Ok!

```json
{
  "id": "number",
  "full_name": {
    "first_name": "string",
    "last_name": "string",
    "author_name": "string"
  },
  "selected_currency": "'RUBLE' | 'DOLLAR' | 'EURO'",
  "email": "string",
  "role": "'USER' | 'ADMIN' | 'OWNER'"
}
```

404:

```json
{
  "error": 404,
  "message": "string"
}
```

### Endpoint `/users/{id}/role` (PUT)

Authorized access (ADMIN)

```shell
curl --request PUT \
     --url https://localhost:8080/v1/users/1/role \
     --header 'Authorization: ' \
     --header 'Content-Type: application/json' \
     --data '{ "role": "ADMIN | USER | OWNER"}'
```

200:

Ok!

```json
{
  "id": "number"
}
```

404:

```json
{
  "error": 404,
  "message": "string"
}
```

