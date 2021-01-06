### Basic Auth

#### Public path
```shell
 curl -i -X GET http://localhost:8080/

 Home

 curl -i -X GET http://localhost:8080/login

 Login
```

#### Basic authentication path
- Request without Basic Auth

```shell
curl -i -X GET http://localhost:8080/api/health
```

```shell
HTTP/1.1 401
Set-Cookie: JSESSIONID=22B55C72BDFF852E986FEED1F411CF72; Path=/; HttpOnly
WWW-Authenticate: Basic realm="Realm"
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 06 Jan 2021 17:56:06 GMT

{"timestamp":"2021-01-06T17:56:06.695+00:00","status":401,"error":"Unauthorized","message":"Unauthorized","path":"/api/health"}
```

- Request with Basic Auth
```shell
curl -i --user admin:password -X GET http://localhost:8080/api/health
```

```shell
{"status":"Alive"}
```
